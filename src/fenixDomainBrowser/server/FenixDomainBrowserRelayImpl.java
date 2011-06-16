package fenixDomainBrowser.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import pt.ist.fenixframework.pstm.dml.FenixDomainModel;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import dml.DomainClass;
import fenixDomainBrowser.client.FenixDomainBrowserRelay;
import fenixDomainBrowser.client.NewFilesPopup.OpenFilesState;
import fenixDomainBrowser.server.DomainModelProvider.DomainModelDefinitions;
import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.DomainModelSignatures;
import fenixDomainBrowser.shared.FDBState;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@SuppressWarnings("serial")
public class FenixDomainBrowserRelayImpl extends RemoteServiceServlet implements FenixDomainBrowserRelay {
    public static final DomainModelProvider DOMAIN_PROVIDER = new DomainModelProvider();

    public FenixDomainBrowserRelayImpl() {
    }

    @Override
    public ClassBean[] getDomainClasses(FDBState bean) {
	FenixDomainModel fdm = DOMAIN_PROVIDER.getDomainModel(bean.getSignature());
	ClassBean[] cb = new ClassBean[fdm.getDomainClasses().size()];
	int i = 0;
	for (DomainClass domainClass : fdm.getDomainClasses()) {
	    cb[i++] = ReflexFactory.fromClass(domainClass, bean);
	}
	Arrays.sort(cb);
	return cb;
    }

    @Override
    public FDBState generateGraph(FDBState bean) {
	UMLGraph g = new UMLGraph(bean);
	FDBState result = g.generate();
	return result;
    }

    @Override
    public ClassBean findClass(String name, FDBState bean) {
	return ReflexFactory.fromClass(DOMAIN_PROVIDER.getDomainModel(bean.getSignature()).findClass(name), bean);
    }

    private void dumpFiles(OpenFilesState files) {
	try {
	    List<String> result = new ArrayList<String>();
	    for (String s : files.files) {
		ZipFile zipFile = new ZipFile(s);
		for (Enumeration list = zipFile.entries(); list.hasMoreElements();) {
		    ZipEntry entry = (ZipEntry) list.nextElement();
		    InputStream eis = zipFile.getInputStream(entry);
		    byte[] buffer = new byte[1024];
		    int bytesRead = 0;

		    ByteArrayOutputStream baos = new ByteArrayOutputStream();

		    while ((bytesRead = eis.read(buffer)) != -1) {
			baos.write(buffer, 0, bytesRead);
		    }
		    File f = File.createTempFile("dmlFromZip", ".dml");
		    FileOutputStream fos = new FileOutputStream(f.getAbsoluteFile());
		    fos.write(baos.toByteArray());
		    fos.close();
		    result.add(f.getAbsoluteFile().getAbsolutePath());
		}
	    }
	    files.files = result;
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public FDBState loadFiles(OpenFilesState files) {
	if (files.zip) {
	    
	    dumpFiles(files);
	}
	
	DomainModelSignatures dms = DOMAIN_PROVIDER.loadFiles(files);
	FDBState state = new FDBState(dms);
	FenixDomainModel domainModel = DOMAIN_PROVIDER.getDomainModel(dms);
	state.setClasses(domainModel.getDomainClasses().size());
	state.setRelations(domainModel.getDomainRelations().size());
	state.setValueTypes(domainModel.getAllValueTypes().size());
	return state;
    }

    @Override
    public String generateSaveFile(FDBState state) {
	DomainModelDefinitions dmd = DOMAIN_PROVIDER.getLoadedDomainModel(state).getDomainModelDefinitions();
	File tempFile;
	try {
	    tempFile = File.createTempFile("save", ".fdb3");

	    JSONSerializer serializerState = new JSONSerializer().include("classesToSee", "classesInGraph", "hideSlots",
		    "hideClasses", "exclusiveSelection");
	    String serialState = serializerState.serialize(state);

	    JSONSerializer serializerDomainModel = new JSONSerializer().include("fileNames", "contents", "signatures",
		    "signatures.signatures");
	    String serialDomainModel = serializerDomainModel.serialize(dmd);

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();

	    baos.write((serialState.length() + "\n").getBytes());
	    baos.write(serialState.getBytes());
	    baos.write(serialDomainModel.getBytes());
	    baos.close();

	    FileOutputStream fos = new FileOutputStream(tempFile.getAbsoluteFile());
	    fos.write(DomainModelProvider.zipBytes("save.fdb3", baos.toByteArray()));

	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
	return new sun.misc.BASE64Encoder().encode(tempFile.getAbsolutePath().getBytes());
    }

    @Override
    public FDBState loadFile(String file) {
	try {
	    ZipFile zipFile = new ZipFile(file);
	    ZipEntry entry = (ZipEntry) zipFile.entries().nextElement();

	    InputStream eis = zipFile.getInputStream(entry);
	    byte[] buffer = new byte[1024];
	    int bytesRead = 0;

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();

	    while ((bytesRead = eis.read(buffer)) != -1) {
		baos.write(buffer, 0, bytesRead);
	    }
	    String result = new String(baos.toByteArray());
	    int split = result.indexOf('\n');
	    int size = Integer.valueOf(result.substring(0, split));

	    String state = result.substring(split + 1, split + 1 + size);
	    String dmd = result.substring(split + 1 + size);
	    FDBState fdbState = new JSONDeserializer<FDBState>().deserialize(state);
	    DomainModelDefinitions domainModelDefinitions = new JSONDeserializer<DomainModelDefinitions>().deserialize(dmd);

	    DOMAIN_PROVIDER.loadFromDomainModelDomainModelDefinitions(domainModelDefinitions);
	    fdbState.setSignature(domainModelDefinitions.signatures);
	    return fdbState;
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public String saveStateForOtherOps(FDBState state) {
	JSONSerializer serializerState = new JSONSerializer().include("classesToSee", "classesInGraph", "hideSlots",
		"hideClasses", "exclusiveSelection", "signature", "signature.signatures");
	try {
	    String serialState = serializerState.serialize(state);
	    File tempFile = File.createTempFile("otherOps", ".state");
	    FileOutputStream fos = new FileOutputStream(tempFile);
	    fos.write(serialState.getBytes());
	    return new sun.misc.BASE64Encoder().encode(tempFile.getAbsolutePath().getBytes());
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }
}
