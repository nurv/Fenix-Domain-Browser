package fenixDomainBrowser.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;

public class CustomButton extends Button {
    private String text;
    private Image imageDown;
    private Image imageUp;
    private Image placed;
    private boolean down = false;

    public CustomButton() {
	super();
    }

    public CustomButton(String up, String down) {
	super();
	setImageUp(up);
	setImageDown(down);
	addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		setDown(!isDown());
	    }
	});
	setDown(false);
    }

    public boolean isDown() {
	return down;
    }

    public void setDown(boolean value) {
	this.down = value;
	update();
    }

    private void update() {
	if (down && imageDown != null) {
	    setImage(getImageDown());
	    placed = getImageDown();
	} else {
	    if (imageUp != null) {
		setImage(getImageUp());
		placed = getImageUp();
	    }
	}
    }

    private void setImage(Image img) {
	String definedStyles = img.getElement().getAttribute("style");
	img.getElement().setAttribute("style", definedStyles + "; vertical-align:middle;");
	if (placed != null) {
	    DOM.removeChild(getElement(), placed.getElement());
	}
	DOM.insertBefore(getElement(), img.getElement(), DOM.getFirstChild(getElement()));
    }

    @Override
    public void setText(String text) {
	this.text = text;
	Element span = DOM.createElement("span");
	span.setInnerText(text);
	span.setAttribute("style", "padding-left:3px; vertical-align:middle;");
	DOM.insertChild(getElement(), span, 0);
    }

    @Override
    public String getText() {
	return this.text;
    }

    public void setImageDown(String imageDown) {
	this.imageDown = new Image(imageDown);
	update();
    }

    public Image getImageDown() {
	return imageDown;
    }

    public void setImageUp(String imageUp) {
	this.imageUp = new Image(imageUp);
	update();
    }

    public Image getImageUp() {
	return imageUp;
    }
}
