package com.hp.sdf.ngp.ui.common;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class CustomizePagingNavigator extends PagingNavigator {

	private static final long serialVersionUID = -5890095039718692947L;

	private DataView<?> dataView;

	private Label beginLabel, endLabel, sumLabel;

	public CustomizePagingNavigator(String id, DataView<?> dataView) {
		super(id, dataView);
		this.dataView = dataView;

		beginLabel = new Label("begin", "0");
		endLabel = new Label("end", "0");
		add(beginLabel);
		add(endLabel);

		sumLabel = new Label("sum", String.valueOf(dataView.getItemCount()));
		add(sumLabel);

		Form<Void> form = new InputForm("goForm");
		add(form);

		// add(new FeedbackPanel("feedback", new ContainerFeedbackMessageFilter(form)));
	}

	@Override
	protected void onBeforeRender() {

		int currentPage = dataView.getCurrentPage();
		int countAll = dataView.getItemCount();
		int countPerPage = dataView.getItemsPerPage();

		sumLabel.setDefaultModel(new Model<Integer>(dataView.getItemCount()));

		if (countAll > 0) {
			beginLabel.setDefaultModel(new Model<Integer>(countPerPage * currentPage + 1));

			if (currentPage == dataView.getPageCount() - 1)
				endLabel.setDefaultModel(new Model<Integer>(countAll));
			else
				endLabel.setDefaultModel(new Model<Integer>(countPerPage * (currentPage + 1)));
		} else {
			beginLabel.setDefaultModel(new Model<Integer>(0));
			endLabel.setDefaultModel(new Model<Integer>(0));
		}

		super.onBeforeRender();
	}

	public class InputForm extends Form<Void> {

		private static final long serialVersionUID = 3667010044309302235L;

		private String go;

		public InputForm(String id) {
			super(id);
			TextField<String> textField = new TextField<String>("go", new PropertyModel<String>(this, "go"));
			add(textField);
			// textField.setRequired(true);
			// textField.add(new IValidator<Integer>() {
			// private static final long serialVersionUID = 4731588081843615292L;
			//
			// public void validate(IValidatable<Integer> validatable) {
			// final Integer value = validatable.getValue();
			// if (value <= 0 || value > dataView.getPageCount()) {
			// error("Please input the right page number");
			// }
			// }
			// });

			Button button = new Button("submit") {
				private static final long serialVersionUID = 3597083851646909560L;

				@Override
				public void onSubmit() {
					if (StringUtils.isNotEmpty(go)) {
						try {
							int goPage = Integer.getInteger(go);
							if (goPage > 1 && goPage <= dataView.getPageCount())
								dataView.setCurrentPage(goPage - 1);
						} catch (Exception ex) {
							return;
						} finally {
							go = null;
						}
					}
					// else {
					// error("Please input the right page number");
					// }
				}
			};
			add(button);

		}

		@Override
		protected void onError() {
			clearInput();
			super.onError();
		}
	}
}