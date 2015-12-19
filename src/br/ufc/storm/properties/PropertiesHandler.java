package br.ufc.storm.properties;

import java.io.FileInputStream; 
import java.io.IOException; 
import java.io.InputStream;
import java.util.Properties;

import br.ufc.storm.webservices.CoreServices;

public class PropertiesHandler {

	public PropertiesHandler() {
		// TODO Auto-generated constructor stub
	}

   
 public static String getProperty(String p) throws IOException { 
	 Properties props = new Properties(); 
	 //FileInputStream file = new FileInputStream( "./properties/config.properties"); //Use this line insted bellow line if want to test with local machine
	 InputStream file =  CoreServices.class.getResourceAsStream("/br/ufc/storm/properties/config.properties");
	 props.load(file); 
	 return props.getProperty(p);
	 } 
 }
