package fenixDomainBrowser.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fenixDomainBrowser.server.graphvizAdapter.Dot;
import fenixDomainBrowser.shared.FDBState;
import flexjson.JSONDeserializer;

public class ImageServlet extends javax.servlet.http.HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String file = Dot.slurp(new String(new sun.misc.BASE64Decoder().decodeBuffer(req.getParameter("filename"))));
	FDBState state = new JSONDeserializer<FDBState>().deserialize(file);
	UMLGraph g = new UMLGraph(state);
	byte[] s = g.generateImage();
	resp.setContentType( "image/png" );
	ServletOutputStream op = resp.getOutputStream();
	op.write(s);
    }
}
