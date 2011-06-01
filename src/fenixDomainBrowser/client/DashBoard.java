package fenixDomainBrowser.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class DashBoard extends Composite {

    private static DashBoardUiBinder uiBinder = GWT.create(DashBoardUiBinder.class);

    interface DashBoardUiBinder extends UiBinder<Widget, DashBoard> {
    }

    @UiField
    ClassSelected selected;

    @UiField(provided = true)
    ClassSearch search = new ClassSearch();
    
    public DashBoard() {
	initWidget(uiBinder.createAndBindUi(this));
    }

}
