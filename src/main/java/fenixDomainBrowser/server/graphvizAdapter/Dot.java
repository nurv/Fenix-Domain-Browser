package fenixDomainBrowser.server.graphvizAdapter;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.google.web.bindery.autobean.vm.Configuration;

import fenixDomainBrowser.server.ConfigurationProperties;

public class Dot {
    public static String slurp(String path) {
	File file = new File(path);
	FileInputStream fis = null;
	BufferedInputStream bis = null;
	DataInputStream dis = null;
	String s = "";

	try {
	    fis = new FileInputStream(file);

	    // Here BufferedInputStream is added for fast reading.
	    bis = new BufferedInputStream(fis);
	    dis = new DataInputStream(bis);

	    // dis.available() returns 0 if the file does not have more lines.
	    while (dis.available() != 0) {

		// this statement reads the line from the file and print it to
		// the console.
		s += dis.readLine() + "\n";
	    }

	    // dispose all the resources after using them.
	    fis.close();
	    bis.close();
	    dis.close();

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return s;
    }

    public static String generate(Graph graph) {
	return new String(generate(graph, "svg"));
    }

    public static byte[] generate(Graph graph, String type) {
	try {
	    File temp = File.createTempFile("diagraph", ".dot");
	    BufferedWriter out = new BufferedWriter(new FileWriter(temp));
	    out.write(graph.toDot());
	    out.close();
	    byte[] b = new byte[4096];

	    int c;
	    Process p = Runtime.getRuntime().exec(
		    ConfigurationProperties.getDot() +  " -T" + type + " -o" + temp.getAbsolutePath() + "." + type + " " + temp.getAbsolutePath());
	    p.getInputStream().read();
	    File f = new File(temp.getAbsolutePath() + "." + type);
	    byte[] ba = new byte[(int) f.length()];
	    FileInputStream fis = new FileInputStream(f);
	    fis.read(ba);
	    return ba;
	} catch (IOException e) {
	    throw new RuntimeException("graphviz failed", e);
	}
    }

}
