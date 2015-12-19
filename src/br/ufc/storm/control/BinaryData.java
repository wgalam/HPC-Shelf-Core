package br.ufc.storm.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlType;

import org.apache.axiom.attachments.ByteArrayDataSource;

import br.ufc.storm.properties.PropertiesHandler;

/*
@XmlType
public class BinaryData {
	

	 public void doBinaryWithDataHandler(DataHandler data,String fileName) {
         try {
               System.out.println("Content Type :  "+data.getContentType());
               System.out.println("DataSourceName  :  "+data.getName());
              
               File outFile = new File(PropertiesHandler.getProperty("core.library.path")+fileName);
               FileOutputStream fileOutputStream = new FileOutputStream(outFile);
               data.writeTo(fileOutputStream);
               fileOutputStream.flush();
               fileOutputStream.close();
         } catch (IOException e) {
               e.printStackTrace();
         }
   }

   public boolean doBinaryWithByteArray(byte[] data,String fileName) {
         try {
               File outFile = new File(PropertiesHandler.getProperty("core.library.path")+fileName);
               ByteArrayDataSource dataSource = new ByteArrayDataSource(data);
               DataHandler dh = new DataHandler(dataSource);
               FileOutputStream fileOutputStream = new FileOutputStream(outFile);
               dh.writeTo(fileOutputStream);
               fileOutputStream.flush();
               fileOutputStream.close();
         } catch (IOException e) {
               e.printStackTrace();
         }
         return true;
   }
}
*/