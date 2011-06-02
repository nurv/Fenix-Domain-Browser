package fenixDomainBrowser.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import pt.ist.fenixframework.pstm.DML;
import pt.ist.fenixframework.pstm.dml.FenixDomainModel;
import pt.ist.fenixframework.pstm.dml.FenixDomainModelWithOCC;
import sun.misc.BASE64Decoder;
import dml.DomainClass;
import dml.Slot;
import fenixDomainBrowser.client.NewFilesPopup.OpenFilesState;
import fenixDomainBrowser.server.graphvizAdapter.Dot;
import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.DomainModelSignatures;
import fenixDomainBrowser.shared.FDBState;
import fenixDomainBrowser.shared.SlotBean;

public class DomainModelProvider {

    public static class DomainModelDefinitions {
	public int files;
	public String[] fileNames;
	public String[] contents;
	public DomainModelSignatures signatures;

	public DomainModelDefinitions(){
	}
	
	public DomainModelDefinitions(List<String> data) {
	    fileNames = new String[data.size()];
	    contents = new String[data.size()];
	    files = data.size();
	    String[] signatures = new String[data.size()];

	    MessageDigest md;
	    try {
		md = MessageDigest.getInstance("SHA-256");
	    } catch (NoSuchAlgorithmException e) {
		throw new RuntimeException(e);
	    }
	    sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();

	    int i = 0;
	    for (String content : data) {
		fileNames[i] = content;
		byte[] zipFile = zipBytes("domain_model_" + (i + 1) + ".dml", Dot.slurp(content).getBytes());
		contents[i] = encoder.encode(zipFile);
		signatures[i] = encoder.encode(md.digest(zipFile));
		i++;
	    }
	    this.signatures = new DomainModelSignatures(signatures);
	}
    }

    public class LoadedDomainModel {

	private FenixDomainModel domainModel;
	private DomainModelDefinitions domainModelDefinitions;
	private HashMap<String, List<String>> subclasses = new HashMap<String, List<String>>();

	public LoadedDomainModel(DomainModelDefinitions dmd) {
	    List<URL> domainModelURLs = new ArrayList<URL>();
	    try {
		for (String string : dmd.fileNames) {
		    domainModelURLs.add(new File(string).toURL());
		}
		domainModel = DML.getDomainModelForURLs(FenixDomainModelWithOCC.class, domainModelURLs, true);
	    } catch (Exception e) {
		throw new RuntimeException("error loading DML",e);
	    }
	    setDomainModelDefinitions(dmd);

	    for (Iterator<DomainClass> iterator = domainModel.getClasses(); iterator.hasNext();) {
		DomainClass cl = iterator.next();
		if (cl.getSuperclass() != null) {
		    String superClass = cl.getSuperclass().getFullName();

		    if (subclasses.get(superClass) == null) {
			subclasses.put(superClass, new ArrayList<String>());
		    }

		    subclasses.get(superClass).add(cl.getFullName());
		}
	    }
	}

	public void setDomainModelDefinitions(DomainModelDefinitions domainModelDefinitions) {
	    this.domainModelDefinitions = domainModelDefinitions;
	}

	public DomainModelDefinitions getDomainModelDefinitions() {
	    return domainModelDefinitions;
	}
    }

    private HashMap<DomainModelSignatures, LoadedDomainModel> domainModels = new HashMap<DomainModelSignatures, DomainModelProvider.LoadedDomainModel>();

    public DomainModelProvider() {
    }

    public DomainModelSignatures loadFiles(OpenFilesState bean) {
	DomainModelDefinitions dmd = new DomainModelDefinitions(bean.files);
	if (domainModels.containsKey(dmd.signatures)) {
	    return dmd.signatures;
	}

	LoadedDomainModel ldm = new LoadedDomainModel(dmd);
	domainModels.put(dmd.signatures, ldm);
	return dmd.signatures;
    }

    public LoadedDomainModel getLoadedDomainModel(DomainModelSignatures signature) {
	return domainModels.get(signature);
    }

    public LoadedDomainModel getLoadedDomainModel(FDBState state) {
	return domainModels.get(state.getSignature());
    }

    public FenixDomainModel getDomainModel(DomainModelSignatures signature) {
	return getLoadedDomainModel(signature).domainModel;
    }

    public FenixDomainModel getDomainModel(FDBState signature) {
	return getDomainModel(signature.getSignature());
    }

    public SlotBean[] getSlots(ClassBean cl, FDBState state) {
	List<Slot> l = getDomainModel(state).findClass(cl.getId()).getSlotsList();
	SlotBean[] array = new SlotBean[l.size()];
	int i = 0;
	for (Slot slot : l) {
	    array[i++] = ReflexFactory.fromSlot(slot);
	}
	return array;
    }

    public List<String> getSubclasses(String s, FDBState state) {
	List<String> result = getLoadedDomainModel(state.getSignature()).subclasses.get(s);
	if (result == null) {
	    result = new ArrayList<String>();
	}
	return result;
    }

    public static byte[] zipBytes(String string, byte[] input) {
	try {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(baos);
	    ZipEntry entry = new ZipEntry(string);
	    entry.setSize(input.length);
	    zos.putNextEntry(entry);
	    zos.write(input);
	    zos.closeEntry();
	    zos.close();
	    return baos.toByteArray();
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    public void loadFromDomainModelDomainModelDefinitions(DomainModelDefinitions dmd) {
	if (domainModels.containsKey(dmd.signatures)) {
	    return;
	}
	BASE64Decoder decoder = new BASE64Decoder();
	int i = 0;
	for (String filename : dmd.fileNames) {
	    File f = new File(filename);
	    try {
		FileOutputStream s = new FileOutputStream(f);
		byte[] b = decoder.decodeBuffer(dmd.contents[i]);
		File tempZipFile = File.createTempFile("zip", ".zip");
		FileOutputStream fos = new FileOutputStream(tempZipFile);
		fos.write(b);
		fos.close();

		ZipFile zipFile = new ZipFile(tempZipFile);
		ZipEntry entry = (ZipEntry) zipFile.entries().nextElement();

		InputStream eis = zipFile.getInputStream(entry);
		byte[] buffer = new byte[1024];
		int bytesRead = 0;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		while ((bytesRead = eis.read(buffer)) != -1) {
		    baos.write(buffer, 0, bytesRead);
		}
		
		fos = new FileOutputStream(f);
		fos.write(baos.toByteArray());
		fos.close();
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
	}
	LoadedDomainModel ldm = new LoadedDomainModel(dmd);
	domainModels.put(dmd.signatures, ldm);
    }

}
