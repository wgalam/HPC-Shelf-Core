package br.ufc.storm.backend;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

import org.apache.axis2.AxisFault;

import br.ufc.storm.backend.FakeEndServicesStub.AddFileResponse;
import br.ufc.storm.backend.FakeEndServicesStub.GetStatusResponse;
import br.ufc.storm.backend.FakeEndServicesStub.RunFileResponse;
import br.ufc.storm.exception.ShelfRuntimeException;
import br.ufc.storm.io.LogHandler;
import br.ufc.storm.jaxb.CandidateListType;
import br.ufc.storm.jaxb.ComputationalSystemType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.PlatformProfileType;
import br.ufc.storm.jaxb.UnitFileType;
import br.ufc.storm.sql.SessionHandler;
import br.ufc.storm.xml.XMLHandler;

public class BackendHandler {

	CandidateListType list; //List of candidates
	int candidate; //Position of actual candidate in array list
	ComputationalSystemType computationalSystem;
	int session; //Session id
	
	public BackendHandler(CandidateListType list) {
		this.list = list;
		candidate = 0;
	}
	
	/**
	 * This method tries to instantiate an platform in respective backend
	 * 
	 * @return ComputationalSystem instantiated
	 * @throws ShelfRuntimeException 
	 */
	public static boolean instantiatePlatftorm(PlatformProfileType platform) throws ShelfRuntimeException{
		try{
			if (getStatus("http://"+platform.getNetworkIpAddress()+"/axis2/services/FakeEndServices.FakeEndServicesHttpSoap12Endpoint/") != null) {
				return true;
			} else {
				throw new ShelfRuntimeException("Can not instantiate platform");
			}
		}catch(Exception e){
			throw new ShelfRuntimeException("Can not instantiate platform at this moment", e);
		}
	}
	
	public static List<UnitFileType> getFiles(ContextContract cc){
//		for () {
//			
//		}
		
		
		return null;
		
	}
	
	public static ComputationalSystemType deployComponent(CandidateListType clist) throws ShelfRuntimeException{
		try {
			for(ContextContract cc : clist.getCandidate()){
					if (instantiatePlatftorm(cc.getPlatform())){
						ComputationalSystemType cst = new ComputationalSystemType();
						cst.setContextContract(cc);
						cst.setNetworkAddress(cc.getPlatform().getNetworkIpAddress());
						cst.setSession(SessionHandler.createSession(clist.getUserId()));
						return cst;
					}
			}
			return null;
		} catch (Exception e) {
			throw new ShelfRuntimeException("Can not instantiate component files", e);
		}
	}
	
	public void instantiateComponent(ContextContract cc) throws ShelfRuntimeException{
		try{
			//Call backend stub to instantiate component 
			//if not possible, throw an exception
		}catch(Exception e){
			throw new ShelfRuntimeException("Can not instantiate component files", e);
		}
	}

	public static boolean releasePlatform(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	public static String instantiate(ContextContract cc) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		String server = "http://localhost:8080/axis2/services/FakeEndServices.FakeEndServicesHttpSoap12Endpoint/";
		String sourceFile = "/home/wagner/dir";
		String targetFile = "/home/wagner/storm/di";
		System.out.println(sendFile(sourceFile, null, targetFile));
		System.out.println(sendCommand(server, "chmod +x "+targetFile));
		System.out.println(sendCommand(server, targetFile));
	}

	private static String sendCommand(String target, String command) {
		
		FakeEndServicesStub stub = null;
		try {
			stub = new FakeEndServicesStub(target);
//			stub = new FakeEndServicesStub("http://localhost:8080/HPC-Shelf-FakeEnd/services/FakeEndServices.FakeEndServicesHttpSoap12Endpoint/");
		} catch (AxisFault e1) {
			e1.printStackTrace();
		}
		//Cria a requisicao para o servico
		FakeEndServicesStub.RunFile request;
		request = new FakeEndServicesStub.RunFile();
		request.setScript(command);
		RunFileResponse response = null;
		try {
			response = stub.runFile(request);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return response.get_return();
		
	}
	
private static String getStatus(String targetEndPoint) {
		
		FakeEndServicesStub stub = null;
		try {
			stub = new FakeEndServicesStub(targetEndPoint);
//			stub = new FakeEndServicesStub("http://localhost:8080/HPC-Shelf-FakeEnd/services/FakeEndServices.FakeEndServicesHttpSoap12Endpoint/");
		} catch (AxisFault e1) {
			e1.printStackTrace();
		}
		//Cria a requisicao para o servico
		FakeEndServicesStub.GetStatus request;
		request = new FakeEndServicesStub.GetStatus();
		GetStatusResponse response = null;
		try {
			response = stub.getStatus(request);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return response.get_return();
	}
	
	
	private static boolean sendFile(String sourcePath, String target, String toPath) {
		
		FakeEndServicesStub stub = null;
		try {
			stub = new FakeEndServicesStub();
//			stub = new FakeEndServicesStub("http://localhost:8080/HPC-Shelf-FakeEnd/services/FakeEndServices.FakeEndServicesHttpSoap12Endpoint/");
		} catch (AxisFault e1) {
			e1.printStackTrace();
		}
		//Cria a requisicao para o servico
		FakeEndServicesStub.AddFile request;
		request = new FakeEndServicesStub.AddFile();
		request.setFullPath(toPath);
		javax.activation.FileDataSource fileDataSource = new javax.activation.FileDataSource(sourcePath);
		javax.activation.DataHandler dh = new javax.activation.DataHandler(fileDataSource);
	
		request.setFile(dh);
		AddFileResponse response = null;
		try {
			response = stub.addFile(request);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return response.get_return();
	}
	
	private static String readFile(String pathname) throws IOException {

		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int)file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");
		try {
			while(scanner.hasNextLine()) {        
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}
	
}
