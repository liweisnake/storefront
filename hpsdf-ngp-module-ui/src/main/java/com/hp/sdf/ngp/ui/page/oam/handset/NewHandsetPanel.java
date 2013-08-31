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
package com.hp.sdf.ngp.ui.page.oam.handset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.custom.sbm.api.impl.model.HandSetDeviceImpl;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.exception.HandsetServiceException;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.HandSetDevice;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.HandsetDeviceService;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.page.oam.account.SearchAccountTabelPanel;

public class NewHandsetPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8920226863398352415L;

	private static final Log log = LogFactory.getLog(NewHandsetPanel.class);

	private PromptPanel promptPanel;

	private static final int BIG_DISPLAY_SIZE = 6;

	private static final int MIDDLE_DISPLAY_SIZE = 5;

	private static final int SMALL_DISPLAY_SIZE = 4;

	public NewHandsetPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		this.add(new NewHandsetForm("newHandsetForm"));

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("promptTitle", this), getLocalizer().getString("successMsg", this), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public final class NewHandsetForm extends Form<Void> {

		private static final long serialVersionUID = 1L;

		@SpringBean
		private HandsetDeviceService handsetDeviceService;

		private String displayName;

		private String deviceName;

		private String newMIMEType;

		private ListMultipleChoice<String> originals;

		// The Selected options in the list choices.
		private List<String> selectedOriginals;

		private boolean accSensorCheck = false;

		private boolean cameraCheck = false;

		private boolean gpsCheck = false;

		private RadioChoice<Integer> displayChoice;

		public NewHandsetForm(String id) {
			super(id);
			this.add(new FeedbackPanel("feedBack"));
			
			TextField<String> displayNameField = new TextField<String>("displayName", new PropertyModel<String>(this, "displayName"));
			add(displayNameField);

			TextField<String> deviceNameField = new TextField<String>("deviceName", new PropertyModel<String>(this, "deviceName"));
			add(deviceNameField);

			TextField<String> newMIMETypeField = new TextField<String>("newMIMEType", new PropertyModel<String>(this, "newMIMEType"));
			add(newMIMETypeField);

			AjaxButton add = new AjaxButton("add") {
				private static final long serialVersionUID = 1124467472760934679L;

				@SuppressWarnings("unchecked")
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					log.debug("newMIMEType :" + newMIMEType);

					if (StringUtils.isNotEmpty(newMIMEType)) {
						List<String> originalChoices = getChoices(originals);
						if (!originalChoices.contains(newMIMEType)) {
							originalChoices.add(newMIMEType);
						}
						originals.setChoices(originalChoices);
					}

					target.addComponent(originals);
				}

			};

			add(add);

			originals = new ListMultipleChoice<String>("originals", new PropertyModel<Collection<String>>(this, "selectedOriginals"), new LoadableDetachableModel<List<String>>() {
				private static final long serialVersionUID = 3246034607367521265L;

				protected List<String> load() {
					ArrayList<String> mimeList = new ArrayList<String>();
					return mimeList;
				}
			});
			originals.setOutputMarkupId(true);
			add(originals);

			AjaxButton delete = new AjaxButton("delete") {
				private static final long serialVersionUID = -2943734077697127565L;

				@SuppressWarnings("unchecked")
				protected void onSubmit(AjaxRequestTarget target, Form form) {

					List<String> originalChoices = getChoices(originals);
					originalChoices.removeAll(selectedOriginals);

					originals.setChoices(originalChoices);
					target.addComponent(originals);
				}
			};
			add(delete);

			add(new CheckBox("accSensor", new IModel<Boolean>() {

				private static final long serialVersionUID = 7865238665776348241L;

				public Boolean getObject() {
					return accSensorCheck;
				}

				public void setObject(Boolean object) {
					accSensorCheck = object;
				}

				public void detach() {
				}
			}));

			add(new CheckBox("camera", new IModel<Boolean>() {

				private static final long serialVersionUID = 7865238665776348241L;

				public Boolean getObject() {
					return cameraCheck;
				}

				public void setObject(Boolean object) {
					cameraCheck = object;
				}

				public void detach() {
				}
			}));

			add(new CheckBox("gps", new IModel<Boolean>() {

				private static final long serialVersionUID = 7865238665776348241L;

				public Boolean getObject() {
					return gpsCheck;
				}

				public void setObject(Boolean object) {
					gpsCheck = object;
				}

				public void detach() {
				}
			}));

			List<Integer> operators = new ArrayList<Integer>();
			operators.add(BIG_DISPLAY_SIZE);
			operators.add(MIDDLE_DISPLAY_SIZE);
			operators.add(SMALL_DISPLAY_SIZE);

			final HashMap<Integer, String> names = new HashMap<Integer, String>();
			names.put(BIG_DISPLAY_SIZE, getLocalizer().getString("big", NewHandsetPanel.this));
			names.put(MIDDLE_DISPLAY_SIZE, getLocalizer().getString("middle", NewHandsetPanel.this));
			names.put(SMALL_DISPLAY_SIZE, getLocalizer().getString("small", NewHandsetPanel.this));

			IChoiceRenderer<Integer> renderer = new ChoiceRenderer<Integer>() {
				private static final long serialVersionUID = -7548268556525735726L;

				public String getDisplayValue(Integer object) {
					return names.get(object);
				}
			};

			displayChoice = new RadioChoice<Integer>("displaySizeChoices", new Model<Integer>(), operators, renderer);
			add(displayChoice);

			
			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", NewHandsetPanel.this), getLocalizer().getString("confirmMsg", NewHandsetPanel.this)) {
				private static final long serialVersionUID = 7465424124110025236L;
				@Override
				public void howDo() {
					log.debug("displayName :" + displayName);
					log.debug("deviceName :" + deviceName);
					log.debug("accSensorCheck :" + accSensorCheck);
					log.debug("cameraCheck :" + cameraCheck);
					log.debug("gpsCheck :" + gpsCheck);

					if (StringUtils.isEmpty(displayName)) {
						this.error(getLocalizer().getString("handsetNameIsNull", NewHandsetPanel.this));
						return;
					}

					if (StringUtils.isEmpty(deviceName)) {
						this.error(getLocalizer().getString("deviceNameIsNull", NewHandsetPanel.this));
						return;
					}

					log.debug("Getting mime type list.");
					List<String> mimeTypes = getChoices(originals);
					if (null==mimeTypes || mimeTypes.size()==0) {
						this.error(getLocalizer().getString("mimeTypeIsNull", NewHandsetPanel.this));
						return;
					}
					
					/**
					if (!accSensorCheck && !cameraCheck && !gpsCheck) {
						this.error(getLocalizer().getString("selectDeviceFunction", NewHandsetPanel.this));
						return;
					}
					**/

					Integer radioIndex = (Integer) displayChoice.getModelObject();
					log.debug("radioIndex :" + radioIndex);
					if (null == radioIndex) {
						this.error(getLocalizer().getString("selectDisplaySize", NewHandsetPanel.this));
						return;
					}

					HandSetDevice handSetDevice = new HandSetDeviceImpl();
					handSetDevice.setDeviceName(deviceName);
					handSetDevice.setDisplayName(displayName);

					handSetDevice.setResolutionFilter(NumberUtils.toLong(radioIndex + ""));

					StringBuilder functionFilter = new StringBuilder();
					functionFilter.append(accSensorCheck ? "1" : "0");
					functionFilter.append(cameraCheck ? "1" : "0");
					functionFilter.append(gpsCheck ? "1" : "0");
					log.debug("functionFilter :" + functionFilter);
					handSetDevice.setFunctionFilter(functionFilter.toString());

					
					if (mimeTypes != null && mimeTypes.size() > 0) {
						for (String mimeType : mimeTypes) {
							log.debug("add mimeType :" + mimeType);
							handSetDevice.addMimeType(mimeType);
						}
					}

					try {
						handsetDeviceService.saveHandSetDevice(handSetDevice);
					} catch (HandsetServiceException exception) {
						log.error("HandsetServiceException :" + exception);
						exception.printStackTrace();
					}

					promptPanel.show();

				}
			};
			add(checkPanel);
			
			Button save = new Button("save") {
				private static final long serialVersionUID = 1510836418186350793L;

				public void onSubmit() {
					checkPanel.show();
					
				}
			};
//			save.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", NewHandsetPanel.this)));
			add(save);

			Button backBtn = new Button("back") {

				private static final long serialVersionUID = 8116120281966867260L;

				public void onSubmit() {
					PageParameters parameters = new PageParameters();
					HandsetSearchPage page = new HandsetSearchPage(parameters);
					setResponsePage(page);
				}
			};
			backBtn.setDefaultFormProcessing(false);
			add(backBtn);
		}

		private List<String> getChoices(ListMultipleChoice<String> choice) {
			List<String> choices = new ArrayList<String>();
			choices.addAll(choice.getChoices());
			return choices;
		}

	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "New Handset Model");
	}

}
