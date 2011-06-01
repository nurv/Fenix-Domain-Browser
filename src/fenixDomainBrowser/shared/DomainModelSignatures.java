package fenixDomainBrowser.shared;

import java.io.Serializable;

public class DomainModelSignatures implements Serializable{
	public String[] signatures;

	public DomainModelSignatures() {
	}
	
	public DomainModelSignatures(String[] signatures) {
	    this.signatures = signatures;
	}

	@Override
	public int hashCode() {
	    String result = "";
	    for (String element : signatures) {
		result += element;
	    }
	    return result.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj instanceof DomainModelSignatures) {
		DomainModelSignatures dms = (DomainModelSignatures) obj;
		if (dms.signatures.length == this.signatures.length) {
		    int i = 0;
		    for (String s : signatures) {
			if (!s.equals(dms.signatures[i++])) {
			    return false;
			}
		    }
		    return true;
		} else {
		    return false;
		}
	    } else {
		return false;
	    }
	}
}