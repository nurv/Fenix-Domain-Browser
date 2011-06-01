package fenixDomainBrowser.shared;

import java.io.Serializable;

public class SlotBean implements Serializable {
    private String typePackage;
    private String type;
    private String name;

    public SlotBean() {
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setTypePackage(String typePackage) {
	this.typePackage = typePackage;
    }

    public String getTypePackage() {
	return typePackage;
    }
}
