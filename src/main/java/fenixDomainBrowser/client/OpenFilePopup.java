package fenixDomainBrowser.client;

import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.FDBState;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.SingleUploader;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OpenFilePopup extends PopupPanel {

    private static OpenFilePopupUiBinder uiBinder = GWT.create(OpenFilePopupUiBinder.class);

    interface OpenFilePopupUiBinder extends UiBinder<Widget, OpenFilePopup> {
    }

    @UiField
    SimplePanel uploader;

    @UiField
    VerticalPanel content;

    boolean loading;

    public OpenFilePopup() {
	super(true);
	add(uiBinder.createAndBindUi(this));
	setGlassEnabled(true);
	setWidth("500px");

	SingleUploader uploader = new SingleUploader();
	this.uploader.add(uploader);
	uploader.addOnFinishUploadHandler(new OnFinishUploaderHandler() {

	    @Override
	    public void onFinish(IUploader uploader) {
		if (uploader.getStatus() == Status.SUCCESS) {
		    if (!loading) {
			loading = true;
			content.clear();

			final Label label = new Label("Loading...");
			content.add(label);
			center();
			Interface.RELAY.loadFile(uploader.getServerInfo().message, new AsyncCallback<FDBState>() {

			    @Override
			    public void onSuccess(final FDBState state) {

				label.setText("Loading classes...");
				Interface.currentState = state;
				Interface.currentInterface.setModelName(state.getModelName());
				Interface.currentInterface.overall.remove(Interface.currentInterface.content);
				Interface.currentInterface.dashboard = new DashBoard();
				Interface.currentInterface.content = Interface.currentInterface.dashboard;
				Interface.currentInterface.overall.add(Interface.currentInterface.dashboard);
				Interface.currentInterface.relationExplorationButton.setDown(state.getRelationExploration());
				Interface.currentInterface.singleLabelRelationButton.setDown(state.getSingleLabelRelations());
				Interface.currentInterface.hsallslotsButton.setDown(state.getHideAllSlots());

				Interface.RELAY.getDomainClasses(state, new AsyncCallback<ClassBean[]>() {

				    @Override
				    public void onSuccess(ClassBean[] result) {
					Interface.currentInterface.dashboard.search.allClasses = Arrays.asList(result);
					Interface.currentInterface.dashboard.search
						.setData(Interface.currentInterface.dashboard.search.allClasses);
					Interface.refresh(state);
					hide();
				    }

				    @Override
				    public void onFailure(Throwable caught) {
					new ErrorPopup(caught).show();
				    }

				});

			    }

			    @Override
			    public void onFailure(Throwable caught) {
				new ErrorPopup(caught).show();

			    }
			});
		    } else {
			return;
		    }
		}
	    }
	});
	center();
    }
}
