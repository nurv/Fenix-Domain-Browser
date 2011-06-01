package fenixDomainBrowser.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class FDBState implements Serializable {

    private boolean relationExploration;
    private boolean seeDomainObject;
    private boolean seeRootDomainObject;
    private boolean singleLabelRelations;
    private String currentGraph;
    private boolean hideAllSlots;
    private boolean seeSlots;
    private String modelName;
    private String modelPackageHeader;
    private ArrayList<ClassBean> classesToSee = new ArrayList<ClassBean>();
    private ArrayList<ClassBean> classesInGraph = new ArrayList<ClassBean>();
    private ArrayList<ClassBean> hideSlots = new ArrayList<ClassBean>();
    private ArrayList<ClassBean> hideClasses = new ArrayList<ClassBean>();
    private ArrayList<ClassBean> exclusiveSelection = new ArrayList<ClassBean>();
    private DomainModelSignatures signature;
    private int classes;
    private int relations;
    private int valueTypes;
    public FDBState() {
    }
    
    public FDBState(DomainModelSignatures signature) {
	this.setSignature(signature); 
    }
    
    public Boolean getRelationExploration() {
	return relationExploration;
    }

    public void setRelationExploration(Boolean relationExploration) {
	this.relationExploration = relationExploration;
    }

    public Boolean getSeeDomainObject() {
	return seeDomainObject;
    }

    public void setSeeDomainObject(Boolean seeDomainObject) {
	this.seeDomainObject = seeDomainObject;
    }

    public Boolean getSeeRootDomainObject() {
	return seeRootDomainObject;
    }

    public void setSeeRootDomainObject(Boolean seeRootDomainObject) {
	this.seeRootDomainObject = seeRootDomainObject;
    }

    public Boolean getSingleLabelRelations() {
	return singleLabelRelations;
    }

    public void setSingleLabelRelations(Boolean singleLabelRelations) {
	this.singleLabelRelations = singleLabelRelations;
    }

    public Boolean getSeeSlots() {
	return seeSlots;
    }

    public void setSeeSlots(Boolean seeSlots) {
	this.seeSlots = seeSlots;
    }

    public ArrayList<ClassBean> getClassesToSee() {
	return classesToSee;
    }

    public void addClassesToSee(ClassBean cl) {
	if (!relationExploration) {
		classesToSee.clear();
	    }
	if (!classesToSee.contains(cl)) {
	    classesToSee.add(cl);
	}else{
	    classesToSee.remove(cl);
	    classesToSee.add(cl);
	}
    }

    public void removeClassesToSee(ClassBean cl) {
	classesToSee.remove(cl);
    }

    public void setCurrentGraph(String currentGraph) {
	this.currentGraph = currentGraph;
    }

    public String getCurrentGraph() {
	return currentGraph;
    }

    public ArrayList<ClassBean> getClassesInGraph() {
	return classesInGraph;
    }

    public void setNewGraph(String graph, Collection<ClassBean> classes) {
	this.currentGraph = graph;
	this.classesInGraph = new ArrayList<ClassBean>(classes);
    }

    public void addHideSlots(ClassBean cl) {
	this.hideSlots.add(cl);
    }

    public void clearHideSlots() {
	this.hideSlots.clear();
    }

    public ArrayList<ClassBean> getHideSlots() {
	return hideSlots;
    }

    public void removeHideSlots(ClassBean element) {
	this.hideSlots.remove(element);
    }

    public void setHideAllSlots(boolean hideAllSlots) {
	this.hideAllSlots = hideAllSlots;
    }

    public boolean getHideAllSlots() {
	return hideAllSlots;
    }

    public void addHideClasses(ClassBean cl) {
	this.hideClasses.add(cl);
    }

    public void removeHideClasses(ClassBean cl) {
	this.hideClasses.remove(cl);
    }

    public ArrayList<ClassBean> getHideClasses() {
	return hideClasses;
    }

    public void addExclusiveSelection(ClassBean classBean) {
	this.exclusiveSelection.add(classBean);
    }

    public void removeExclusiveSelection(ClassBean classBean) {
	this.exclusiveSelection.remove(classBean);
    }

    public ArrayList<ClassBean> getExclusiveSelection() {
	return exclusiveSelection;
    }

    public DomainModelSignatures getSignature() {
	return signature;
    }

    public void setModelName(String modelName) {
	this.modelName = modelName;
    }

    public String getModelName() {
	return modelName;
    }

    public void setModelPackageHeader(String modelPackageHeader) {
	this.modelPackageHeader = modelPackageHeader;
    }

    public String getModelPackageHeader() {
	return modelPackageHeader;
    }

    public void setSignature(DomainModelSignatures signature) {
	this.signature = signature;
    }

    public void setClasses(int classes) {
	this.classes = classes;
    }

    public int getClasses() {
	return classes;
    }

    public void setRelations(int relations) {
	this.relations = relations;
    }

    public int getRelations() {
	return relations;
    }

    public void setValueTypes(int valueTypes) {
	this.valueTypes = valueTypes;
    }

    public int getValueTypes() {
	return valueTypes;
    }

}
