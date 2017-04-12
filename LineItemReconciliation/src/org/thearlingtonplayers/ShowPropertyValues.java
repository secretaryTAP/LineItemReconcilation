package org.thearlingtonplayers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ShowPropertyValues {

	private static Properties prop = new Properties();
	private static ShowPropertyValues showPropertyValues = new ShowPropertyValues();

	private ShowPropertyValues() {
		String propFileName = "config.properties";

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		try {
			if (inputStream != null) {

				prop.load(inputStream);

			} else {
				throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String lookupProperty(String propName) {
		return prop.getProperty(propName);
	}
	
	public static ShowPropertyValues getInstance() {
		return showPropertyValues; 
	}

}
