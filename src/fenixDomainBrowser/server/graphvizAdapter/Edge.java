package fenixDomainBrowser.server.graphvizAdapter;

import java.util.HashMap;
import java.util.Iterator;

public class Edge {
	private final String from;
	private final String to;
	private final HashMap<String, String> properties = new HashMap<String, String>();

	public Edge(String from, String to) {
	    this.from = from;
	    this.to = to;
	}
	
	public Edge add(String key,String value){
	    properties.put(key,value);
	    return this;
	}

	public String toDot() {
	    String v = "\"" + from + "\" -> \"" + to + "\"";

	    if (properties.size() > 0) {
		v += "[";
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
