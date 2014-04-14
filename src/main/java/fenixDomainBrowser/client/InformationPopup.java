package fenixDomainBrowser.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fenixDomainBrowser.shared.FDBState;

public class InformationPopup extends PopupPanel {

    private static InformationPopupUiBinder uiBinder = GWT.create(InformationPopupUiBinder.class);

    interface InformationPopupUiBinder extends UiBinder<Widget, InformationPopup> {
    }
    
    @UiField
    TextBox modelName;
    
    @UiField
    Label valueTypes;
    
    @UiField
    Label entities;
    
    @UiField
    Label relations;
    

    public InformationPopup() {
	add(uiBinder.createAndBindUi(this));
	setGlassEnabled(true);
	FDBState currentState = Interface.currentState;
	modelName.setText(currentState.getModelName());
	valueTypes.setText("" + currentState.getValueTypes());
	entities.setText("" + currentState.getClasses());
	relations.setText("" + currentState.getRelations());
	center();
    }

    @UiHandler("cancel")
    void onCancelClick(ClickEvent event) {
	this.hide();
    }

    @UiHandler("save")
    void onSaveClick(ClickEvent event) {
	Interface.currentState.setModelName(modelName.getText());
	Interface.currentInterface.setModelName(modelName.getText());
	this.hide();
    }
}
