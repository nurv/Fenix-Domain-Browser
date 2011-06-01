package fenixDomainBrowser.client;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fenixDomainBrowser.shared.ClassBean;

public class ClassSelected extends Composite implements RequiresResize {

    private static ClassSelectedUiBinder uiBinder = GWT.create(ClassSelectedUiBinder.class);

    @UiField
    ScrollPanel scrollPanel;
    
    @UiField
    VerticalPanel panel;

    interface ClassSelectedUiBinder extends UiBinder<Widget, ClassSelected> {
    }

    public ClassSelected() {
	initWidget(uiBinder.createAndBindUi(this));
    }

    public void setClasses() {
	panel.clear();
	ArrayList<ClassBean> cl = new ArrayList<ClassBean>(Interface.currentState.getClassesInGraph());
	Collections.sort(cl);
	for (final ClassBean element : cl) {

	    HorizontalPanel line = new HorizontalPanel();
	    line.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
	    if (!Interface.currentState.getClassesToSee().contains(element)) {
		final ToggleButton hide = new ToggleButton(new Image("icons/eye--minus.png"),
			new Image("icons/eye--plus.png"));
		if (Interface.currentState.getHideClasses().contains(element)) {
		    hide.setDown(true);
		}
		hide.setStyleName("icon");
		hide.addClickHandler(new ClickHandler() {

		    @Override
		    public void onClick(ClickEvent event) {
			if (hide.isDown()) {
			    Interface.currentState.addHideClasses(element);
			} else {
			    Interface.currentState.removeHideClasses(element);
			}
			Interface.refresh();
		    }
		});

		line.add(hide);
	    }else{
		final ToggleButton hide = new ToggleButton(new Image("icons/minus-button.png"),
			new Image("icons/plus-button.png"));
		if (Interface.currentState.getHideClasses().contains(element)) {
		    hide.setDown(true);
		}
		hide.setStyleName("icon");
		hide.addClickHandler(new ClickHandler() {

		    @Override
		    public void onClick(ClickEvent event) {
			Interface.currentState.removeClassesToSee(element);
			Interface.refresh();
		    }
		});

		line.add(hide);
	    }

	    if (!Interface.currentState.getHideAllSlots()) {
		final ToggleButton slots = new ToggleButton(new Image("icons/document-attribute-e.png"), new Image(
			"icons/document-list.png"));
		if (Interface.currentState.getHideSlots().contains(element)) {
		    slots.setDown(true);
		}
		slots.addClickHandler(new ClickHandler() {

		    @Override
		    public void onClick(ClickEvent event) {
			if (slots.isDown()) {
			    Interface.currentState.addHideSlots(element);
			} else {
			    Interface.currentState.removeHideSlots(element);
			}
			Interface.refresh();
		    }

		});
		slots.setStyleName("icon");
		line.add(slots);
	    }

	    if (Interface.currentState.getRelationExploration() && Interface.currentState.getClassesToSee().contains(element)) {
		final ToggleButton other = new ToggleButton(new Image("icons/leaf.png"), new Image("icons/leaf-plant.png"));
		if (Interface.currentState.getExclusiveSelection().contains(element)) {
		    other.setDown(true);
		}
		other.setStyleName("icon");
		other.addClickHandler(new ClickHandler() {

		    @Override
		    public void onClick(ClickEvent event) {
			if (other.isDown()) {
			    Interface.currentState.addExclusiveSelection(element);
			} else {
			    Interface.currentState.removeExclusiveSelection(element);
			}
			Interface.refresh();
		    }
		});
		line.add(other);
	    }

	    Anchor a = new Anchor(element.getQualifiedName());
	    a.addClickHandler(new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
		    Interface.currentState.addClassesToSee(element);
		    Interface.refresh();
		}
	    });
	    line.add(a);
	    panel.add(line);
	}

	for (final ClassBean classBean : Interface.currentState.getHideClasses()) {
	    HorizontalPanel line = new HorizontalPanel();
	    line.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

	    final ToggleButton hide = new ToggleButton(new Image("icons/eye--minus.png"),
			new Image("icons/eye--plus.png"));
	    if (Interface.currentState.getHideClasses().contains(classBean)) {
		hide.setDown(true);
	    }
	    hide.setStyleName("icon");

	    hide.setDown(true);
	    hide.addClickHandler(new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
		    if (hide.isDown()) {
			Interface.currentState.addHideClasses(classBean);
		    } else {
			Interface.currentState.removeHideClasses(classBean);
		    }
		    Interface.refresh();
		}
	    });

	    line.add(hide);
	    Label a = new Label(classBean.getQualifiedName());

	    line.add(a);
	    panel.add(line);
	}
    }

    @Override
    public void onResize() {
	scrollPanel.onResize();
    }
}
