package fenixDomainBrowser.server.graphvizAdapter;

import java.util.HashMap;
import java.util.Iterator;

public class Node {
    private final String name;
    private final HashMap<String, String> properties = new HashMap<String, String>();

    public Node(String name) {
	this.name = name;
    }

    public Node add(String key, String value) {
	properties.put(key, value);
	return this;
    }

    public String getName() {
	return name;
    }

    public String toDot() {
	String v = "\"" + name + "\"";

	if (properties.size() > 0) {
	    v += " [";
	    for (Iterator<String> iterator = properties.keySet().iterator(); iterator.hasNext();) {
		String key = iterator.next();
		v += key + "=\"" + properties.get(key) + "\"";
		if (iterator.hasNext()) {
		    v += ", ";
		}
	    }
	    v += "]";
	}

	return v + ";";
    }

}
