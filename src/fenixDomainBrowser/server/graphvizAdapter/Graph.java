package fenixDomainBrowser.server.graphvizAdapter;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    List<Node> nodes = new ArrayList<Node>();
    List<Edge> edges = new ArrayList<Edge>();

    public void addEdge(Edge node) {
	edges.add(node);
    }
    
    public void addNode(Node node) {
	nodes.add(node);
    }

    public String toDot() {
	String v = "digraph UML {\n" + "\tfontsize=8\n" + "\tfontname=Helvetica\n"
		+ "\tnode [fillcolor=white, fontname=Helvetica, fontsize = 9];\n"
		+ "\tedge [fontname=Helvetica, fontsize = 9];\n";
        for(Node node : nodes){
            v += "\t" + node.toDot();
        }
	for (Edge edge : edges) {
	    v += "\t" + edge.toDot() + "\n";
	}
	
	v += "}";
	return v;
    }
}
