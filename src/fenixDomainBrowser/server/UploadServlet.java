package fenixDomainBrowser.server;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

public class UploadServlet extends UploadAction {

    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
	String response = "";
	int cont = 0;
	for (FileItem item : sessionFiles) {
	    if (false == item.isFormField()) {
		cont++;
		try {
		    File file = File.createTempFile("upload-", ".fdbTemp");
		    item.write(file);
		    response += file.getAbsolutePath();

		} catch (Exception e) {
		    throw new UploadActionException(e);
		}
	    }
	}

	removeSessionFileItems(request);
	return response;
    }

}
