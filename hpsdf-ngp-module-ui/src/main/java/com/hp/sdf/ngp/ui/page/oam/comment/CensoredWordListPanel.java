/*
 * Copyright (c) 2009 Hewlett-Packard Company, All Rights Reserved.
 *
 * RESTRICTED RIGHTS LEGEND Use, duplication, or disclosure by the U.S.
 * Government is subject to restrictions as set forth in sub-paragraph
 * (c)(1)(ii) of the Rights in Technical Data and Computer Software
 * clause in DFARS 252.227-7013.
 *
 * Hewlett-Packard Company
 * 3000 Hanover Street
 * Palo Alto, CA 94304 U.S.A.
 * Rights for non-DOD U.S. Government Departments and Agencies are as
 * set forth in FAR 52.227-19(c)(1,2).
 */
package com.hp.sdf.ngp.ui.page.oam.comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.CommentsSensorWord;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.provider.CensoredWordProvider;
import com.hp.sdf.ngp.workflow.Privilege;

public class CensoredWordListPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 2847203327312187476L;

	private static final Log log = LogFactory.getLog(CensoredWordListPanel.class);

	public CensoredWordListPanel(String id, final IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		CensoredWordListForm censoredWordListForm = new CensoredWordListForm("censoredWordListForm", breadCrumbModel);
		MetaDataRoleAuthorizationStrategy.authorize(censoredWordListForm, Component.RENDER, Privilege.VIEWCENSOREDWORDLIST);
		this.add(censoredWordListForm);
	}

	public final class CensoredWordListForm extends Form<Void> {

		private static final long serialVersionUID = -4951597258253103907L;

		private Map<Long, String> censoredWordMap = new HashMap<Long, String>();

		@SpringBean
		private ApplicationService applicationService;

		private int itemsPerPage = 50;

		private boolean groupSelected;

		public boolean isGroupSelected() {
			return groupSelected;
		}

		public void setGroupSelected(boolean groupSelected) {
			this.groupSelected = groupSelected;
		}

		public CensoredWordListForm(String id, IBreadCrumbModel breadCrumbModel) {
			super(id);

			this.add(new FeedbackPanel("feedBack"));
			CheckBox groupselector = new CheckBox("groupselector", new PropertyModel<Boolean>(this, "groupSelected"));
			groupselector.setMarkupId("groupselector");
			add(groupselector);

			CensoredWordProvider censoredWordProvider = new CensoredWordProvider(applicationService);
			log.debug("censoredWordProvider.size() :" + censoredWordProvider.size());

			CensoredWordDataView listView = new CensoredWordDataView("listview", breadCrumbModel, censoredWordProvider, itemsPerPage);
			add(listView);
			add(new CustomizePagingNavigator("navigator", listView));

			Button newEntry = new Button("newEntry") {
				private static final long serialVersionUID = 8179675420318996436L;

				public void onSubmit() {

					activate(new IBreadCrumbPanelFactory() {
						private static final long serialVersionUID = 3840521373650730173L;

						public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
							return new CensoredWordNewPanel(componentId, breadCrumbModel);
						}
					});
				}
			};
			MetaDataRoleAuthorizationStrategy.authorize(newEntry, Component.RENDER, Privilege.NEWCENSOREDWORD);
			add(newEntry);

			

			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", CensoredWordListPanel.this), getLocalizer().getString("confirmMsg", CensoredWordListPanel.this)) {
				private static final long serialVersionUID = 7465424124110025236L;
				@Override
				public void howDo() {

					if (censoredWordMap != null && censoredWordMap.size() > 0) {
						for (Entry<Long, String> entry : censoredWordMap.entrySet()) {
							log.debug("The Censored word need to be deleted: " + entry.getValue());
							if (!applicationService.checkCommentsSensorWordByName((String) entry.getValue())) {
								log.error("Can not delete the censored word because this word is not exists.");
								error(getLocalizer().getString("msg.error.del.nocensoredword",  CensoredWordListPanel.this ));
								log.debug("Set SelectAll checkbox to not checked.");
								setGroupSelected(false);
								log.debug("Clear the subscribeMap.");
								censoredWordMap.clear();
								return;
							}
						}

						List<Long> commentsSensorWordId = new ArrayList<Long>();
						commentsSensorWordId.addAll(censoredWordMap.keySet());

						applicationService.batchDeleteCommentsSensorWord(commentsSensorWordId);
					} else {
						error(getLocalizer().getString("msg.error.select.noselect",  CensoredWordListPanel.this ));
						return;
					}

					log.debug("Set SelectAll checkbox to not checked.");
					setGroupSelected(false);
					log.debug("Clear the subscribeMap.");
					censoredWordMap.clear();
				}
			};
			add(checkPanel);
			
			Button delete = new Button("delete") {

				private static final long serialVersionUID = 2189543498800077396L;

				public void onSubmit() {
					checkPanel.show();
				}
			};
			//delete.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", CensoredWordListPanel.this)));
			MetaDataRoleAuthorizationStrategy.authorize(delete, Component.RENDER, Privilege.DELETECENSOREDWORD);
			add(delete);
		}

		class CensoredWordDataView extends DataView<CommentsSensorWord> {

			private static final long serialVersionUID = -6514262177740324330L;

			private IBreadCrumbModel breadCrumbModel;

			protected CensoredWordDataView(String id, IBreadCrumbModel breadCrumbModel, IDataProvider<CommentsSensorWord> dataProvider, int itemsPerPage) {
				super(id, dataProvider, itemsPerPage);
				this.breadCrumbModel = breadCrumbModel;
			}

			protected void populateItem(Item<CommentsSensorWord> item) {
				final CommentsSensorWord censoredWord = (CommentsSensorWord) item.getModelObject();

				item.add(new CheckBox("select", new IModel<Boolean>() {

					private static final long serialVersionUID = 7865238665776348241L;

					public Boolean getObject() {

						if (censoredWordMap != null && censoredWordMap.size() != 0) {
							String o = censoredWordMap.get(censoredWord.getId());
							return null == o ? false : true;
						}

						return false;
					}

					public void setObject(Boolean object) {
						if (object) {
							censoredWordMap.put(censoredWord.getId(), censoredWord.getSensorWord());
						}
					}

					public void detach() {
					}
				}));

				Label labelCensoredWord = new Label("censoredWord", censoredWord.getSensorWord());
				item.add(labelCensoredWord);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				Label labelUpdateDate = new Label("updateDate", censoredWord.getUpdateDate() != null ? sdf.format(censoredWord.getUpdateDate()) : "");
				item.add(labelUpdateDate);

				BreadCrumbPanelLink edit = new BreadCrumbPanelLink("detail", breadCrumbModel, new IBreadCrumbPanelFactory() {

					private static final long serialVersionUID = 1L;

					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						return new CensoredWordEditPanel(componentId, breadCrumbModel, censoredWord);
					}
				});
				MetaDataRoleAuthorizationStrategy.authorize(edit, Component.RENDER, Privilege.EDITCENSOREDWORD);
				item.add(edit);

			}
		}

	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Censored Word List");
	}

}
