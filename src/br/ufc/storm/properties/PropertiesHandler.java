package br.ufc.storm.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import br.ufc.storm.webservices.CoreServices;

public class PropertiesHandler {

	/**
	 * This method read properties files
	 * @param p Name of property wanted
	 * @return Property required value
	 * @throws IOException 
	 */
	public static String getProperty(String p) throws IOException { 
		Properties props = new Properties(); 
		InputStream file =  CoreServices.class.getResourceAsStream("/br/ufc/storm/properties/config.properties");
		props.load(file); 
		return props.getProperty(p);
	} 
}
