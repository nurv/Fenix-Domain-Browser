package fenixDomainBrowser.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fenixDomainBrowser.client.NewFilesPopup.OpenFilesState;
import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.FDBState;
import fenixDomainBrowser.shared.MethodBean;
import fenixDomainBrowser.shared.SlotBean;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface FenixDomainBrowserRelayAsync {

    void generateGraph(FDBState bean, AsyncCallback<FDBState> callback);

    void getDomainClasses(FDBState bean,AsyncCallback<ClassBean[]> callback);

    void findClass(String name, FDBState bean,AsyncCallback<ClassBean> callback);

    void loadFiles(OpenFilesState files, AsyncCallback<FDBState> callback);

    void generateSaveFile(FDBState state, AsyncCallback<String> callback);

    void loadFile(String file, AsyncCallback<FDBState> callback);

    void saveStateForOtherOps(FDBState state, AsyncCallback<String> callback);
}
