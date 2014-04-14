package fenixDomainBrowser.server.graphvizAdapter;

import fenixDomainBrowser.shared.ClassBean;

public class InheritanceEdge extends Edge{

    public InheritanceEdge(ClassBean from, ClassBean to) {
	super(from.getId(), to.getId());
	add("color", "blue").add("arrowhead", "empty");
    }

}
