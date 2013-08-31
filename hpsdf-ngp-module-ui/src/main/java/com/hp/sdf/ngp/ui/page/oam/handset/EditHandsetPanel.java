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
import java.util.Set;

import org.apache.commons.collections.ListUtils;
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
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.HandsetDeviceService;
import com.hp.sdf.ngp.custom.sbm.storeclient.model.MimeType;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.PromptPanel;

public class EditHandsetPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8920226863398352415L;

	private static final Log log = LogFactory.getLog(EditHandsetPanel.class);

	private PromptPanel promptPanel;

	private static final int BIG_DISPLAY_SIZE = 6;

	private static final int MIDDLE_DISPLAY_SIZE = 5;

	private static final int SMALL_DISPLAY_SIZE = 4;

	public EditHandsetPanel(String id, IBreadCrumbModel breadCrumbModel, HandSetDeviceImpl handSetDevice) {
		super(id, breadCrumbModel);

		this.add(new EditHandsetForm("editHandsetForm", handSetDevice));

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("promptTitle", this), getLocalizer().getString("successMsg", this), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public final class EditHandsetForm extends Form<Void> {

		private static final long serialVersionUID = 1L;

		@SpringBean
		private HandsetDeviceService handsetDeviceService;

		private String deviceName;

		private String newMIMEType;

		private ListMultipleChoice<String> originals;

		// The Selected options in the list choices.
		private List<String> selectedOriginals;

		private boolean accSensorCheck = false;

		private boolean cameraCheck = false;

		private boolean gpsCheck = false;

		private RadioChoice<Integer> displayChoice;

		// handSetDevice function filter
		private String filter;

		public EditHandsetForm(String id, final HandSetDeviceImpl handSetDevice) {
			super(id);

			this.add(new FeedbackPanel("feedBack"));
			TextField<String> displayNameField = new TextField<String>("displayName", new Model<String>());
			displayNameField.setDefaultModelObject(handSetDevice.getDisplayName());
			displayNameField.setEnabled(false);
			add(displayNameField);

			TextField<String> deviceNameField = new TextField<String>("deviceName", new PropertyModel<String>(this, "deviceName"));
			deviceNameField.setDefaultModelObject(handSetDevice.getDeviceName());
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

			final List<String> originalMimeTypes = handSetDevice.getMimeType();
			log.debug("handSetDevice.getMimeType().size() :" + originalMimeTypes.size());

			originals = new ListMultipleChoice<String>("originals", new PropertyModel<Collection<String>>(this, "selectedOriginals"), new LoadableDetachableModel<List<String>>() {
				private static final long serialVersionUID = 3246034607367521265L;

				protected List<String> load() {
					return originalMimeTypes;
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

			filter = handSetDevice.getFunctionFilter();
			if (StringUtils.isEmpty(filter) || 3 != filter.length()) {
				// means no checkbox is selected
				filter = "000";
			}
			log.debug("filter of handSetDevice :" + filter);
			accSensorCheck = "1".equals(filter.substring(0, 1));
			cameraCheck = "1".equals(filter.substring(1, 2));
			gpsCheck = "1".equals(filter.substring(2, 3));

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
			names.put(BIG_DISPLAY_SIZE, getLocalizer().getString("big", EditHandsetPanel.this));
			names.put(MIDDLE_DISPLAY_SIZE, getLocalizer().getString("middle", EditHandsetPanel.this));
			names.put(SMALL_DISPLAY_SIZE, getLocalizer().getString("small", EditHandsetPanel.this));

			IChoiceRenderer<Integer> renderer = new ChoiceRenderer<Integer>() {
				private static final long serialVersionUID = -7548268556525735726L;

				public String getDisplayValue(Integer object) {
					return names.get(object);
				}
			};

			// default radio select value
			int resolutionFilter = handSetDevice.getResolutionFilter() != null ? handSetDevice.getResolutionFilter().intValue() : -1;
			log.debug("resolutionFilter :" + resolutionFilter);

			displayChoice = new RadioChoice<Integer>("displaySizeChoices", new Model<Integer>(), operators, renderer);
			displayChoice.setDefaultModelObject(resolutionFilter);
			add(displayChoice);
			
			
			
			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", EditHandsetPanel.this), getLocalizer().getString("confirmMsg", EditHandsetPanel.this)) {
				private static final long serialVersionUID = 7465424124110025236L;
				@Override
				public void howDo() {
					log.debug("accSensorCheck :" + accSensorCheck);
					log.debug("cameraCheck :" + cameraCheck);
					log.debug("gpsCheck :" + gpsCheck);
					log.debug("deviceName :" + deviceName);

					if (StringUtils.isEmpty(deviceName)) {
						this.error(getLocalizer().getString("deviceNameIsNull", EditHandsetPanel.this));
						return;
					}

					log.debug("Getting mime type list in the select list.");
					List<String> mimeTypes = getChoices(originals);
					List<String> intersection = ListUtils.intersection(originalMimeTypes, mimeTypes);
					log.debug("intersection.size() :" + intersection.size());
					List<String> deletedMimeTypeList = ListUtils.subtract(originalMimeTypes, intersection);
					log.debug("deletedMimeTypeList.size() :" + deletedMimeTypeList.size());
					List<String> addMimeTypeList = ListUtils.subtract(mimeTypes, intersection);
					log.debug("addMimeTypeList.size() :" + addMimeTypeList.size());

					if ((addMimeTypeList == null || addMimeTypeList.size() == 0)
							&& (deletedMimeTypeList != null && deletedMimeTypeList.size() == handSetDevice.getHandSetDevice().getMimeTypes().size())) {
						this.error(getLocalizer().getString("mimeTypeIsNull", EditHandsetPanel.this));
						return;
					}
					
					/**
					if (!accSensorCheck && !cameraCheck && !gpsCheck) {
						this.error(getLocalizer().getString("selectDeviceFunction", EditHandsetPanel.this));
						return;
					}
					**/

					Integer radioIndex = (Integer) displayChoice.getModelObject();
					log.debug("radioIndex :" + radioIndex);
					if (null == radioIndex) {
						this.error(getLocalizer().getString("selectDisplaySize", EditHandsetPanel.this));
						return;
					}

					log.debug("radioIndex :" + radioIndex);
					handSetDevice.setResolutionFilter(NumberUtils.toLong(radioIndex + ""));

					StringBuilder functionFilter = new StringBuilder();
					functionFilter.append(accSensorCheck ? "1" : "0");
					functionFilter.append(cameraCheck ? "1" : "0");
					functionFilter.append(gpsCheck ? "1" : "0");
					log.debug("functionFilter :" + functionFilter);

					handSetDevice.setDeviceName(deviceName);
					handSetDevice.setFunctionFilter(functionFilter.toString());

					if (addMimeTypeList != null && addMimeTypeList.size() > 0) {
						for (String mimeType : addMimeTypeList) {
							log.debug("add mimeType :" + mimeType);
							handSetDevice.addMimeType(mimeType);
						}
					}

					if (deletedMimeTypeList != null && deletedMimeTypeList.size() > 0) {
						Set<MimeType> mimeTypeSet = handSetDevice.getHandSetDevice().getMimeTypes();

						for (String mimeType : deletedMimeTypeList) {
							log.debug("Deleteing mimeType :" + mimeType);

							for (MimeType mime : mimeTypeSet) {
								if (mimeType.equals(mime.getType())) {
									log.debug("Removing mimeType from mimeTypeSet.");
									mimeTypeSet.remove(mime);
									break;
								}
							}

							handSetDevice.getHandSetDevice().setMimeTypes(mimeTypeSet);
						}
					}

					log.debug("After adding new mime types and delete mime types, update the handSetDevice.");
					try {
						handsetDeviceService.updateHandSetDevice(handSetDevice);
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

				@SuppressWarnings("unchecked")
				public void onSubmit() {
					checkPanel.show();
				}
			};
			//save.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", EditHandsetPanel.this)));
			add(save);

			
			
			final CheckPanel checkPanel2 = new CheckPanel("checkPanel2", getLocalizer().getString("promptTitle", EditHandsetPanel.this), getLocalizer().getString("confirmDeleteMsg", EditHandsetPanel.this)) {
				private static final long serialVersionUID = 7465424124110025236L;
				@Override
				public void howDo() {
					if (handSetDevice.getHandSetDevice() != null) {
						log.debug("Delete current handset device. id is :" + handSetDevice.getHandSetDevice().getId());
						try {
							if (handsetDeviceService.getHandSetDeviceById(handSetDevice.getHandSetDevice().getId()) != null) {
								handsetDeviceService.deleteHandSetDeviceById(handSetDevice.getHandSetDevice().getId());
							} else {
								error(getLocalizer().getString("msg.error.save.nohandset", this, "Handset does not exist!"));
								return;
							}

							PageParameters parameters = new PageParameters();
							HandsetSearchPage page = new HandsetSearchPage(parameters);
							setResponsePage(page);
						} catch (HandsetServiceException exception) {
							log.error("HandsetServiceException :" + exception);
							exception.printStackTrace();
						}
					}
				}
			};
			add(checkPanel2);
			
			Button deleteBtn = new Button("deleteBtn") {

				private static final long serialVersionUID = 8116120281966867260L;

				public void onSubmit() {
					checkPanel2.show();
				}
			};
//			deleteBtn.add(Tools.addConfirmJs(getLocalizer().getString("confirmDeleteMsg", EditHandsetPanel.this)));
			add(deleteBtn);

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
		return this.getLocalizer().getString("title", this, "Edit Handset Model");
	}

}
