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

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.CommentsSensorWord;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.PromptPanel;

public class CensoredWordNewPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8920226863398352415L;

	private static final Log log = LogFactory.getLog(CensoredWordNewPanel.class);

	private PromptPanel promptPanel;
	private CheckPanel checkPanel;

	public CensoredWordNewPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		add(new CensoredWordNewForm("censoredWordNewForm"));

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("promptTitle", this), getLocalizer().getString("successMsg", this), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public final class CensoredWordNewForm extends Form<Void> {

		private static final long serialVersionUID = 2121043705949859826L;

		private String newCensoredWord;

		@SpringBean
		private ApplicationService applicationService;

		public CensoredWordNewForm(String id) {
			super(id);

			this.add(new FeedbackPanel("feedBack"));
			TextField<String> newCensoredWordText = new TextField<String>("newCensoredWord", new PropertyModel<String>(this, "newCensoredWord"));
			add(newCensoredWordText);

			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", CensoredWordNewPanel.this), getLocalizer().getString("checkPanel.confirmMsg", CensoredWordNewPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					CommentsSensorWord censoredWord = new CommentsSensorWord();
					censoredWord.setSensorWord(newCensoredWord);
					censoredWord.setOperatorId(WicketSession.get().getId());
					censoredWord.setUpdateDate(new Date());
					applicationService.saveOrUpdateCommentsSensorWord(censoredWord);

					promptPanel.show();
				}
			};
			add(checkPanel);

			


			final CheckPanel checkPanel2 = new CheckPanel("checkPanel2", getLocalizer().getString("promptTitle", CensoredWordNewPanel.this), getLocalizer().getString("confirmMsg",
					CensoredWordNewPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					log.debug("newCensoredWord :" + newCensoredWord);

					if (StringUtils.isNotEmpty(newCensoredWord)) {
						if (applicationService.checkCommentsSensorWordByName(newCensoredWord)) {
							// error(getLocalizer().getString("error.dpword", CensoredWordNewPanel.this));
							checkPanel.show();
							return;
						} else {
							CommentsSensorWord censoredWord = new CommentsSensorWord();
							censoredWord.setSensorWord(newCensoredWord);
							censoredWord.setUpdateDate(new Date());

							applicationService.saveOrUpdateCommentsSensorWord(censoredWord);

							promptPanel.show();
						}
					} else {
						error(getLocalizer().getString("error.emptyword", CensoredWordNewPanel.this));
						return;
					}

				}
			};
			add(checkPanel2);
			
			Button save = new Button("save") {

				private static final long serialVersionUID = 1510836418186350793L;

				public void onSubmit() {checkPanel2.show();}
			};
			//save.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", CensoredWordNewPanel.this)));
			add(save);

			Button backBtn = new Button("back") {

				private static final long serialVersionUID = 8116120281966867260L;

				public void onSubmit() {
					log.debug("back");

					PageParameters parameters = new PageParameters();
					CensoredWordListPage page = new CensoredWordListPage(parameters);
					setResponsePage(page);
				}
			};
			backBtn.setDefaultFormProcessing(false);
			add(backBtn);
		}

	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "New Censored Word");
	}

}
