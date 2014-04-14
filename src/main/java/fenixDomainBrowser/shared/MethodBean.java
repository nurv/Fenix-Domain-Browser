package fenixDomainBrowser.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class MethodBean implements Serializable {

    public String returnType;
    public String name;
    public boolean isStatic;
    public ArrayList<String> args = new ArrayList<String>();
    
}
