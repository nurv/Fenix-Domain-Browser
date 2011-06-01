package fenixDomainBrowser.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.fenixframework.pstm.dml.FenixDomainModel;

import dml.DomainClass;
import dml.DomainRelation;
import dml.Role;
import fenixDomainBrowser.server.graphvizAdapter.ClassNode;
import fenixDomainBrowser.server.graphvizAdapter.Dot;
import fenixDomainBrowser.server.graphvizAdapter.Graph;
import fenixDomainBrowser.server.graphvizAdapter.InheritanceEdge;
import fenixDomainBrowser.server.graphvizAdapter.RelationEdge;
import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.FDBState;

public class UMLGraph {

    public class Pair<A, B> {
	private A first;
	private B second;

	public Pair(A first, B second) {
	    super();
	    this.first = first;
	    this.second = second;
	}

	public int hashCode() {
	    int hashFirst = first != null ? first.hashCode() : 0;
	    int hashSecond = second != null ? second.hashCode() : 0;

	    return (hashFirst + hashSecond) * hashSecond + hashFirst;
	}

	public boolean equals(Object other) {
	    if (other instanceof Pair) {
		Pair otherPair = (Pair) other;
		return ((this.first == otherPair.first || (this.first != null && otherPair.first != null && this.first
			.equals(otherPair.first))) && (this.second == otherPair.second || (this.second != null
			&& otherPair.second != null && this.second.equals(otherPair.second))));
	    }

	    return false;
	}

	public String toString() {
	    return "(" + first + ", " + second + ")";
	}

	public A getFirst() {
	    return first;
	}

	public void setFirst(A first) {
	    this.first = first;
	}

	public B getSecond() {
	    return second;
	}

	public void setSecond(B second) {
	    this.second = second;
	}
    }

    Graph graph = new Graph();
    Set<ClassBean> usedClasses = new HashSet<ClassBean>();
    Set<DomainRelation> usedRelations = new HashSet<DomainRelation>();
    Set<Pair<ClassBean, ClassBean>> usedInheritance = new HashSet<Pair<ClassBean, ClassBean>>();
    HashMap<ClassBean, DomainRelation> exclusionSet = new HashMap<ClassBean, DomainRelation>();
    FDBState bean;

    public UMLGraph(FDBState bean) {
	this.bean = bean;
    }

    public FDBState generate() {
	for (ClassBean classBean : bean.getClassesToSee()) {
	    addAllClasssesAndRelations(classBean);
	}

	addToPendingClassesToGraph();
	bean.setNewGraph(Dot.generate(graph), usedClasses);

	return bean;
    }
    
    public byte[] generateImage() {
	for (ClassBean classBean : bean.getClassesToSee()) {
	    addAllClasssesAndRelations(classBean);
	}

	addToPendingClassesToGraph();
	return Dot.generate(graph,"png");
    }

    private void addAllClasssesAndRelations(ClassBean classBean) {
	boolean isExclusive = bean.getExclusiveSelection().contains(classBean);

	FenixDomainModel domainModel = FenixDomainBrowserRelayImpl.DOMAIN_PROVIDER.getDomainModel(bean);
	DomainClass domainClass = domainModel.findClass(classBean.getId());
	if (!bean.getHideClasses().contains(classBean)) {
	    usedClasses.add(classBean);
	}
	String superClass = classBean.getExtends();
	ClassBean son = classBean;
	ClassBean parent = null;

	while (superClass != null) {
	    parent = ReflexFactory.fromClass(domainModel.findClass(superClass),bean);

	    if (!(bean.getHideClasses().contains(parent) || bean.getHideClasses().contains(son))) {
		usedClasses.add(parent);
		usedInheritance.add(new Pair<ClassBean, ClassBean>(son, parent));
	    }

	    superClass = parent.getExtends();
	    son = parent;
	}

	for (String subClasses : FenixDomainBrowserRelayImpl.DOMAIN_PROVIDER.getSubclasses(classBean.getId(),bean)) {
	    ClassBean cl = ReflexFactory.fromClass(domainModel.findClass(
		    subClasses),bean);
	    if (!bean.getHideClasses().contains(cl)) {
		usedClasses.add(cl);
		usedInheritance.add(new Pair<ClassBean, ClassBean>(cl, classBean));
	    }
	}

	for (Role rl : domainClass.getRoleSlotsList()) {
	    ClassBean otherClass = ReflexFactory.fromClass(rl.getType(),bean);

	    if (!bean.getHideClasses().contains(otherClass)) {
		if (isExclusive && !usedClasses.contains(otherClass) && !exclusionSet.containsKey(otherClass)) {
		    exclusionSet.put(otherClass, rl.getRelation());
		} else {
		    usedClasses.add(otherClass);
		    usedRelations.add(rl.getRelation());
		    if (exclusionSet.containsKey(otherClass)) {
			usedRelations.add(exclusionSet.get(otherClass));
			exclusionSet.remove(otherClass);
		    }
		}
	    }
	}
    }

    private void addToPendingClassesToGraph() {
	for (ClassBean cl : usedClasses) {

	    ClassNode node;

	    if (!bean.getHideAllSlots()) {
		node = new ClassNode(cl, bean.getHideSlots().contains(cl),bean);
	    } else {
		node = new ClassNode(cl, true,bean);
	    }

	    if (bean.getClassesToSee().contains(cl)) {
		node.setSelected();
	    }

	    graph.addNode(node);
	}

	for (Pair<ClassBean, ClassBean> pair : usedInheritance){
	    graph.addEdge(new InheritanceEdge(pair.getFirst(), pair.getSecond()));
	}
	
	for (DomainRelation r : usedRelations) {
	    graph.addEdge(new RelationEdge(r, bean.getSingleLabelRelations()));
	}

    }
}
