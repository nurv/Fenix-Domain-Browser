package fenixDomainBrowser.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.FDBState;

public class Interface extends Composite {

    public final static FenixDomainBrowserRelayAsync RELAY = GWT.create(FenixDomainBrowserRelay.class);

    private static InterfaceUiBinder uiBinder = GWT.create(InterfaceUiBinder.class);

    interface InterfaceUiBinder extends UiBinder<Widget, Interface> {
    }

    public static FDBState currentState;
    public static Interface currentInterface;

    @UiField
    CustomButton relationExplorationButton;

    @UiField
    CustomButton singleLabelRelationButton;

    @UiField
    CustomButton hsallslotsButton;

    @UiField
    CustomButton newDomainModel;

    @UiField
    CustomButton save;

    @UiField
    CustomButton open;

    @UiField
    DockLayoutPanel overall;

    @UiField
    Widget content;

    @UiField
    Anchor modelName;

    @UiField
    CustomButton print;

    @UiField
    CustomButton image;
    
    DashBoard dashboard;
    

    public Interface() {
	Interface.currentInterface = this;
	initWidget(uiBinder.createAndBindUi(this));
	initInjectFunctionForDispatch();

	relationExplorationButton.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (currentState != null) {
		    boolean relExpValue = !Interface.currentState.getRelationExploration();
		    Interface.currentState.setRelationExploration(relExpValue);
		    relationExplorationButton.setDown(relExpValue);
		    if (!relExpValue) {
			ClassBean cl = Interface.currentState.getClassesToSee().get(
				Interface.currentState.getClassesToSee().size() - 1);
			Interface.currentState.addClassesToSee(cl);
			Interface.refresh();
		    } else {
			Interface.refresh(Interface.currentState);
		    }
		}
	    }
	});

	singleLabelRelationButton.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if (currentState != null) {
		    boolean singleLblValue = !Interface.currentState.getSingleLabelRelations();
		    Interface.currentState.setSingleLabelRelations(singleLblValue);
		    singleLabelRelationButton.setDown(singleLblValue);
		    Interface.refresh();
		}
	    }
	});

	hsallslotsButton.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (currentState != null) {
		    boolean hsAllSlots = !Interface.currentState.getHideAllSlots();
		    Interface.currentState.setHideAllSlots(hsAllSlots);
		    hsallslotsButton.setDown(hsAllSlots);
		    Interface.refresh();
		}
	    }
	});

	newDomainModel.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		new NewFilesPopup().show();
	    }
	});

	save.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		RELAY.generateSaveFile(Interface.currentState, new AsyncCallback<String>() {

		    @Override
		    public void onFailure(Throwable caught) {
			new ErrorPopup(caught).show();
		    }

		    @Override
		    public void onSuccess(String result) {
			Window.open(GWT.getHostPageBaseURL() + "saveFile?filename=" + result, "", "");
		    }
		});
	    }
	});

	open.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		new OpenFilePopup().show();
	    }
	});

	print.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		final Object win = openWindow();
		if (currentState != null && currentState.getClassesInGraph().size() > 0) {
		    RELAY.saveStateForOtherOps(Interface.currentState, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
			    new ErrorPopup(caught).show();

			}

			@Override
			public void onSuccess(String result) {
			 changeUrl(GWT.getHostPageBaseURL() + "printFile?filename=" + result,win);
			}
		    });
		}
	    }
	});
	
	image.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		final Object win = openWindow();
		if (currentState != null && currentState.getClassesInGraph().size() > 0) {
		    RELAY.saveStateForOtherOps(Interface.currentState, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
			    new ErrorPopup(caught).show();
			}

			@Override
			public void onSuccess(String result) {
			 changeUrl(GWT.getHostPageBaseURL() + "imageFile?filename=" + result,win);
			}
		    });
		}
	    }
	});
	modelName.addClickHandler(new ClickHandler() {
	    
	    @Override
	    public void onClick(ClickEvent event) {
		new InformationPopup().show();
	    }
	});
    }

    public native Object openWindow()/*-{
	return $wnd.open("about:blank", "_blank", "");
    }-*/;
    
    public native void changeUrl(String where, Object win)/*-{
        win.window.location = where;
    }-*/;
    
    public static void injectSVG(String s) {
	String pattern = s.replaceAll("xlink:title=\"([^(\"]*)\"", "xlink:title=\"$1\" style=\"cursor:pointer;\" onclick=\"dispatchClick('$1')\"");
	DOM.getElementById("loc").setInnerHTML(pattern);
    }

    public static void disptach(String type) {
	Interface.currentState.addClassesToSee(Interface.currentInterface.dashboard.search.getClassBeanForName(type));
	refresh();
    }

    private native void initInjectFunctionForDispatch()/*-{
		$wnd.dispatchClick = function(type) {
			@fenixDomainBrowser.client.Interface::disptach(Ljava/lang/String;)(type);
		}
    }-*/;

    public static void refresh(FDBState state) {
	currentState = state;
	if (state.getCurrentGraph() != null && state.getCurrentGraph().length() != 0) {
	    Interface.injectSVG(state.getCurrentGraph());
	    Interface.currentInterface.dashboard.selected.setClasses();
	}

	if (state.getClassesToSee().size() > 0) {
	    ClassBean element = state.getClassesToSee().get(state.getClassesToSee().size() - 1);
	    Interface.currentInterface.dashboard.search.selectionModel.setSelected(element, true);
	    Interface.currentInterface.dashboard.search.list.getRowElement(
		    Interface.currentInterface.dashboard.search.list.getDisplayedItems().indexOf(
			    Interface.currentInterface.dashboard.search.selectionModel.getSelectedObject())).scrollIntoView();
	    Interface.currentInterface.dashboard.search.scrollPanel.setHorizontalScrollPosition(0);
	}
    }

    public static void refresh() {
	DOM.getElementById("loc").setInnerHTML("<img src='wait.gif' />");
	RELAY.generateGraph(currentState, new AsyncCallback<FDBState>() {

	    @Override
	    public void onFailure(Throwable caught) {
		new ErrorPopup(caught).show();
	    }

	    @Override
	    public void onSuccess(FDBState result) {
		refresh(result);
	    }
	});
    }

    public void setModelName(String name) {
	modelName.setText(name);
    }
}
