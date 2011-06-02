package fenixDomainBrowser.server.graphvizAdapter;

import fenixDomainBrowser.server.FenixDomainBrowserRelayImpl;
import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.FDBState;
import fenixDomainBrowser.shared.SlotBean;

public class ClassNode extends Node {
    private final ClassBean classBean;

    public ClassNode(ClassBean provided, FDBState state) {
	this(provided, false, state);
    }

    public ClassNode(ClassBean provided, boolean noSlots, FDBState state) {
	super(provided.getId());
	classBean = provided;
	if (noSlots) {
	    add("shape", "rectangle").add("label", classBean.getQualifiedName());
	} else {
	    add("shape", "record").add("label", genSlotString(state));
	}

	add("tooltip", classBean.getId()).add("style", "filled");
    }

    public void setSelected() {
	add("fillcolor", "bisque");
    }

    private String genSlotString(FDBState state) {
	String v = "{" + classBean.getQualifiedName() + "|";
	for (SlotBean s : FenixDomainBrowserRelayImpl.DOMAIN_PROVIDER.getSlots(classBean, state)) {
	    v += "+ " +  s.getName() + " : " + s.getType() + "\\l";
	}
	v += "}";
	return v;
    }
}
