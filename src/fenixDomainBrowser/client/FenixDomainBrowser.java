package fenixDomainBrowser.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class FenixDomainBrowser implements EntryPoint {

    public void onModuleLoad() {
	Interface i = new Interface();
	RootLayoutPanel.get().add(i);
    }
    
    public static String getContext(){
	String path = Window.Location.getPath();
	String context = path.substring(0, path.substring(1).indexOf("/")+1);
	return context;
    }
}
