package fenixDomainBrowser.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
		Map<String,URL> urls = new HashMap<String,URL>();
		Integer max = Integer.parseInt((String) props.get("dml.urls.max"));
		for(int i=1; i <= max; i++) {
			try {
				String url = (String) props.get("dml.urls." + i);
				urls.put(String.valueOf(i), new URL(url));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return urls;
	}
}
