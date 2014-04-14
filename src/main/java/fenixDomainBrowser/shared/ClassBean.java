package fenixDomainBrowser.shared;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ProvidesKey;

public class ClassBean implements Serializable, Comparable<ClassBean> {

    public static class ClassBeanCell extends AbstractCell<ClassBean> {
	@Override
	public void render(Context context, ClassBean value, SafeHtmlBuilder sb) {
	    sb.appendHtmlConstant("<div style='padding-left:10px'>" + value.getQualifiedName()+ "</div>");
	}
    }

    public static ProvidesKey<ClassBean> KEY_PROVIDER = new ProvidesKey<ClassBean>() {
	public Object getKey(ClassBean item) {
	    // Always do a null check.
	    return (item == null) ? null : item.getId();
	}
    };
    
    public ClassBean() {
    }

    private String pacakge;
    private String name;
    private String qualifiedName;
    private String xtds;

    public void getSlots(AsyncCallback<List<SlotBean>> callback) {
	callback.onSuccess(null);
    }
    
    public void getMethods(AsyncCallback<List<MethodBean>> callback) {
	callback.onSuccess(null);
    }

    public void setPacakge(String pacakge) {
	this.pacakge = pacakge;
    }

    public String getPacakge() {
	return pacakge;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }
    
    public String getQualifiedName(){
	return qualifiedName;
    }

    public void setExtends(String xtds) {
	this.xtds = xtds;
    }

    public String getExtends() {
	return xtds;
    }

    public String getId() {
	return getPacakge() + "." + getName();
    }

    @Override
    public int compareTo(ClassBean o) {
	return this.getQualifiedName().compareTo(o.getQualifiedName());
    }
    
    @Override
    public boolean equals(Object obj) {
	if(obj instanceof ClassBean){
	    return this.getId().equals(((ClassBean)obj).getId());
	}else{
	    return false;
	}
    }
    
    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    public void setQualifiedName(String qualifiedName) {
	this.qualifiedName = qualifiedName;
    }

}
