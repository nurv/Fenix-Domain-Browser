package fenixDomainBrowser.client;

import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.FDBState;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.MultiUploader;

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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NewFilesPopup extends PopupPanel{
    
    private static NewFilesPopupUiBinder uiBinder = GWT.create(NewFilesPopupUiBinder.class);
    
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
    
    public static class OpenFilesState implements Serializable{
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
		Interface.RELAY.loadFiles(files, new AsyncCallback<FDBState>() {
		    
		    @Override
		    public void onSuccess(FDBState result) {
			label.setText("Loading classes...");
			Interface.currentState = result;
			result.setModelName(modelName.getText());
			result.setModelPackageHeader(modelPackageHeader.getText());
			Interface.currentInterface.setModelName(modelName.getText());
			Interface.currentInterface.overall.remove(Interface.currentInterface.content);
			Interface.currentInterface.dashboard = new DashBoard();
			Interface.currentInterface.content = Interface.currentInterface.dashboard; 
			Interface.currentInterface.overall.add(Interface.currentInterface.dashboard);
			
			Interface.RELAY.getDomainClasses(result, new AsyncCallback<ClassBean[]>() {
			    
			    @Override
			    public void onSuccess(ClassBean[] result) {
				Interface.currentInterface.dashboard.search.allClasses = Arrays.asList(result);
				Interface.currentInterface.dashboard.search.setData(Interface.currentInterface.dashboard.search.allClasses);
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
