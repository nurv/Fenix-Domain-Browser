package fenixDomainBrowser.server.graphvizAdapter;

import dml.DomainRelation;
import dml.Role;

public class RelationEdge extends Edge {
    private final DomainRelation relation;

    public RelationEdge(DomainRelation relation) {
	this(relation, false);
    }

    public RelationEdge(DomainRelation relation, boolean singleRelation) {
	super(relation.getFirstRole().getType().getFullName(), relation.getSecondRole().getType().getFullName());
	this.relation = relation;

	Role first = relation.getFirstRole();
	Role second = relation.getSecondRole();

	String tooltip = relation.getName() + "(" + first.getName() + "," + second.getName() + ")";
	if (singleRelation) {
	    add("label", "[" + getMultiplicity(first) + "," + getMultiplicity(second) + "]");
	} else {
	    add("arrowhead", "none").add("headlabel", getMultiplicity(second)).add("taillabel", getMultiplicity(first));
	}
	add("color", "red").add("labeltooltip", tooltip).add("style", "bold");
    }

    private String getMultiplicity(Role role) {
	String result;
	if (role.getMultiplicityUpper() == Role.MULTIPLICITY_MANY) {
	    result = "*";
	} else if (role.getMultiplicityLower() == 0 && role.getMultiplicityUpper() == 1) {
	    result = "1";
	} else {
	    result = role.getMultiplicityLower() + ".." + role.getMultiplicityUpper();
	}
	return result;
    }
}
