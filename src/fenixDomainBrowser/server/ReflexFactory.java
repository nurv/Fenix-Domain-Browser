package fenixDomainBrowser.server;

import dml.DomainClass;
import dml.DomainEntity;
import dml.Slot;
import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.FDBState;
import fenixDomainBrowser.shared.SlotBean;

public class ReflexFactory {
    public static ClassBean fromClass(DomainClass domainClass, FDBState bean) {
	ClassBean c = new ClassBean();
	c.setName(domainClass.getName());
	if (!bean.getModelPackageHeader().equals("") && domainClass.getFullName().startsWith(bean.getModelPackageHeader())) {
	    c.setQualifiedName(domainClass.getFullName().substring(bean.getModelPackageHeader().length() + 1));
	} else {
	    c.setQualifiedName(domainClass.getFullName());
	}
	c.setPacakge(domainClass.getPackageName());
	if (domainClass.getSuperclass() != null) {
	    c.setExtends(domainClass.getSuperclass().getFullName());
	}
	return c;
    }

    public static ClassBean fromClass(DomainEntity domainClass, FDBState bean) {
	return fromClass(FenixDomainBrowserRelayImpl.DOMAIN_PROVIDER.getDomainModel(bean).findClass(domainClass.getFullName()),
		bean);
    }

    public static SlotBean fromSlot(Slot domainSlot) {
	SlotBean sb = new SlotBean();
	sb.setType(domainSlot.getTypeName());
	String f = domainSlot.getTypeName();
	String cl;
	String pk;
	if (f.contains(".")) {
	    cl = f.substring(f.lastIndexOf('.') + 1);
	    pk = f.substring(0, f.lastIndexOf('.'));
	} else {
	    cl = f;
	    pk = "";
	}
	sb.setTypePackage(pk);
	sb.setType(cl);
	sb.setName(domainSlot.getName());
	return sb;

    }
}
