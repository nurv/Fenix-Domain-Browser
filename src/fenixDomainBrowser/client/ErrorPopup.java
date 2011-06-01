package fenixDomainBrowser.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ErrorPopup  extends PopupPanel {

    private static ErrorPopupUiBinder uiBinder = GWT.create(ErrorPopupUiBinder.class);

    interface ErrorPopupUiBinder extends UiBinder<Widget, ErrorPopup> {
    }
    
    Throwable t;
    
    @UiField
    Label message;
    
    
    public ErrorPopup(Throwable e) {
	add(uiBinder.createAndBindUi(this));
	setGlassEnabled(true);
	setWidth("500px");
	t = e;
	if (e.getCause() != null){ 
	    message.setText(e.getCause().getMessage());
	}else{
	    message.setText(e.getMessage());
	}
	center();
    }
}
