package fenixDomainBrowser.server;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class ConfigurationProperties {
    private static Properties props;

    static {
        props = new Properties();
        try {
            props.load(ConfigurationProperties.class.getResourceAsStream("/configuration.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, URL> getDMLUrls() {
        Map<String, URL> urls = new HashMap<String, URL>();
        String dmlPath = (String) props.get("dml.path");

        if (StringUtils.isEmpty(dmlPath)) {
            dmlPath = "/tmp/dmls/";
        }

        File[] listFiles = new File(dmlPath).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".dml");
            }
        });

        for (File file : listFiles) {
            try {
                urls.put(file.getName(), file.toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public static String getDot() {
        return (String) props.get("dot");
    }
}