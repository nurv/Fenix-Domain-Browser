package fenixDomainBrowser.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import fenixDomainBrowser.client.NewFilesPopup.OpenFilesState;
import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.FDBState;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface FenixDomainBrowserRelay extends RemoteService {
    ClassBean[] getDomainClasses(FDBState bean);
    ClassBean findClass(String name,FDBState bean);
    FDBState generateGraph(FDBState bean);
    FDBState loadFiles(OpenFilesState files);
    String generateSaveFile(FDBState state);
    FDBState loadFile(String file);
    String saveStateForOtherOps(FDBState state);
}
