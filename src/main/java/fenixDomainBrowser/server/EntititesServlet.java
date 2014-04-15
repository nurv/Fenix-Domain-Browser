package fenixDomainBrowser.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import fenixDomainBrowser.server.graphvizAdapter.Dot;
import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.FDBState;
import flexjson.JSONDeserializer;

public class EntititesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String file = Dot.slurp(new String(Base64.decodeBase64(req.getParameter("filename").getBytes())));

        FDBState state = new JSONDeserializer<FDBState>().deserialize(file);
        String result = "";
        for (ClassBean bean : state.getClassesToSee()) {
            result += bean.getId() + "\n";
        }
        ServletOutputStream op = resp.getOutputStream();
        op.write(result.getBytes());
    }
}
