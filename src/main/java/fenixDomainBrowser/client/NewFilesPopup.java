package fenixDomainBrowser.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.FDBState;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.MultiUploader;

public class NewFilesPopup extends PopupPanel {

    private static NewFilesPopupUiBinder uiBinder = GWT.create(NewFilesPopupUiBinder.class);

    public static final class LoadClassesCallback implements AsyncCallback<FDBState> {
        private final Label label;

        private final String modelName;

        private final String modelPackageHeader;

        private final PopupPanel popup;

        public LoadClassesCallback(String modelName, String modelPackageHeader) {
            this(null, "Loading...", modelName, modelPackageHeader);
        }

        public LoadClassesCallback(PopupPanel popup, String lblText, String modelName, String modelPackageHeader) {
            this.label = new Label(lblText);
            this.modelName = modelName;
            this.modelPackageHeader = modelPackageHeader;
            this.popup = popup;
        }

        @Override
        public void onSuccess(FDBState result) {
            label.setText("Loading classes...");
            Interface.currentState = result;
            result.setModelName(modelName);
            result.setModelPackageHeader(modelPackageHeader);
            Interface.currentInterface.setModelName(modelName);
            Interface.currentInterface.overall.remove(Interface.currentInterface.content);
            Interface.currentInterface.dashboard = new DashBoard();
            Interface.currentInterface.content = Interface.currentInterface.dashboard;
            Interface.currentInterface.overall.add(Interface.currentInterface.dashboard);

            Interface.RELAY.getDomainClasses(result, new AsyncCallback<ClassBean[]>() {

                @Override
                public void onSuccess(ClassBean[] result) {
                    Interface.currentInterface.dashboard.search.allClasses = Arrays.asList(result);
                    Interface.currentInterface.dashboard.search.setData(Interface.currentInterface.dashboard.search.allClasses);
                    if (popup != null) {
                        popup.hide();
                    }
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
    }

    interface NewFilesPopupUiBinder extends UiBinder<Widget, NewFilesPopup> {
    }

    @UiField
    TextBox modelName;

    @UiField
    TextBox modelPackageHeader;

    @UiField
    SimplePanel uploader;

    @UiField
    Button load;

    @UiField
    Button cancel;

    @UiField
    VerticalPanel content;

    @UiField
    CheckBox zip;

    public static class OpenFilesState implements Serializable {
        public boolean zip;
        public List<String> files = new ArrayList<String>();
    }

    OpenFilesState files = new OpenFilesState();

    public NewFilesPopup() {
        add(uiBinder.createAndBindUi(this));
        setGlassEnabled(true);
        setWidth("500px");
        MultiUploader uploader = new MultiUploader();
        this.uploader.add(uploader);
        uploader.addOnFinishUploadHandler(new OnFinishUploaderHandler() {

            @Override
            public void onFinish(IUploader uploader) {

                if (uploader.getStatus() == Status.SUCCESS) {
                    if (uploader.getServerInfo().message == null) {
                        Window.alert("got null");
                    }
                    files.files.add(uploader.getServerInfo().message);
                }
            }
        });

        uploader.addStyleName("uploader");

        load.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                content.clear();

                final Label label = new Label("Loading...");
                content.add(label);
                center();
                files.zip = zip.getValue();
                Interface.RELAY.loadFiles(files, new LoadClassesCallback(NewFilesPopup.this, "Loading...", modelName.getText(),
                        modelPackageHeader.getText()));
            }
        });

        cancel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });

        center();
    }

}
