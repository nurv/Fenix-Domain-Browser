package fenixDomainBrowser.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class FenixDomainBrowser implements EntryPoint {

    public void onModuleLoad() {
	Interface i = new Interface();
	RootLayoutPanel.get().add(i);
    }
}
