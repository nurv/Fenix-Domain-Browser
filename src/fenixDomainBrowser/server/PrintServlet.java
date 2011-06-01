package fenixDomainBrowser.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dml.DomainClass;
import fenixDomainBrowser.server.graphvizAdapter.Dot;
import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.FDBState;
import flexjson.JSONDeserializer;

public class PrintServlet extends HttpServlet {
    private static final String DOMAIN_OBJECT_CLASSNAME = "net.sourceforge.fenixedu.domain.DomainObject";

    private static final String FRAMEWORK_PACKAGE = "pt.ist.fenixframework.pstm";
    
    private static String getTableName(final String name) {
	final StringBuilder stringBuilder = new StringBuilder();
	boolean isFirst = true;
	for (final char c : name.toCharArray()) {
	    if (isFirst) {
		isFirst = false;
		stringBuilder.append(Character.toUpperCase(c));
	    } else {
		if (Character.isUpperCase(c)) {
		    stringBuilder.append('_');
		    stringBuilder.append(c);
		} else {
		    stringBuilder.append(Character.toUpperCase(c));
		}
	    }
	}
	return stringBuilder.toString();
    }

    protected static String getExpectedTableName(final DomainClass domainClass) {
	// Shameless hack to make OJB map to the special framework tables
	if (domainClass.getFullName().startsWith(FRAMEWORK_PACKAGE)) {
	    return "FF$" + getTableName(domainClass.getName());
	}
	if (domainClass.getFullName().equals(DOMAIN_OBJECT_CLASSNAME)) {
	    return null;
	}
	if (domainClass.getSuperclass() == null
		|| (domainClass.getSuperclass() instanceof DomainClass && domainClass.getSuperclass().getFullName()
			.equals(DOMAIN_OBJECT_CLASSNAME))) {
	    return getTableName(domainClass.getName());
	}
	return domainClass.getSuperclass() instanceof DomainClass ? getExpectedTableName((DomainClass) domainClass
		.getSuperclass()) : null;
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String file = Dot.slurp(new String(new sun.misc.BASE64Decoder().decodeBuffer(req.getParameter("filename"))));
	FDBState state = new JSONDeserializer<FDBState>().deserialize(file);
	UMLGraph g = new UMLGraph(state);
	FDBState result = g.generate();
	String svg = result.getCurrentGraph();
	String mappingTable ="";
	for (ClassBean classBean : state.getClassesInGraph()){
	    if (classBean.getId().equals(DOMAIN_OBJECT_CLASSNAME)){
		continue;
	    }
	    String mapping = getExpectedTableName(FenixDomainBrowserRelayImpl.DOMAIN_PROVIDER.getDomainModel(result).findClass(classBean.getId()));
	    mappingTable +="<tr><td>" + classBean.getQualifiedName() + "</td><td><pre>" + mapping + "</pre></td></tr>";
	}
	
	String template = Dot.slurp(getServletContext().getRealPath("/template.html"));
	
	resp.setContentType( "text/html" );
	ServletOutputStream op = resp.getOutputStream();
	op.write(template.replace("{{ svg }}", svg).replace("{{ mapping }}", mappingTable).getBytes());
    }
    
}
