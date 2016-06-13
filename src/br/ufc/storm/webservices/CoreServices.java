package br.ufc.storm.webservices;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.CandidateListType;
import br.ufc.storm.jaxb.ComputationalSystemType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.PlatformProfileType;
import br.ufc.storm.jaxb.UnitFileType;
import br.ufc.storm.sql.AbstractComponentHandler;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.sql.SessionHandler;
import br.ufc.storm.xml.XMLHandler;
import export.FormalFormat;
import br.ufc.storm.backend.BackendHandler;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.ResolveException;
import br.ufc.storm.exception.ShelfRuntimeException;
import br.ufc.storm.exception.XMLException;
import br.ufc.storm.io.LogHandler;

public class CoreServices {

	int i = 0;
	/**
	 * 	 * This method adds an abstract component into database.
	 * @param cmp(xml file as string)
	 * @return True if no exception occurs
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws DBHandlerException 
	 */

	public boolean addAbstractComponent(String cmp) throws ParserConfigurationException, SAXException, IOException, DBHandlerException{
		LogHandler.getLogger().info("Starting to add an abstract component...");
		try {
			Boolean b = XMLHandler.addAbstractComponentFromXML(cmp);
			LogHandler.close();
			return b;
		} catch (XMLException | ResolveException e) {
			LogHandler.close();
			return false;
		}
	}

	/**
	 * 	 * This method adds an context contract into database.
	 * @param cmp(xml file as string)
	 * @return True if no exception occurs
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */

	public boolean addContextContract(String cmp) throws ParserConfigurationException, SAXException, IOException{
		LogHandler.getLogger().info("Starting to add a context component...");
		try {
			Boolean b = XMLHandler.addContextContract(cmp);
			LogHandler.close();
			return b;
		} catch (XMLException | DBHandlerException e) {
			LogHandler.close();
			return false;
		}
	}

	/**
	 * This method gets an abstract component and returns its description.
	 * @param name Abstract component name
	 * @return Abstract component data as XML string
	 */

	public String getAbstractComponent(String name){
		LogHandler.getLogger().info("Starting to get an abstract component...");
		try {
			String str = XMLHandler.getAbstractComponent(name);
			LogHandler.close();
			return str;
		} catch (XMLException e) {
			LogHandler.close();
			return null;
		}
	}

	/**
	 * This method gets an abstract component and returns its description.
	 * @param id Abstract component id
	 * @return Abstract component data as string
	 */

	public String getAbstractComponentByID(int id){
		LogHandler.getLogger().info("Starting to get an abstract component id...");
		try {
			AbstractComponentType ac = AbstractComponentHandler.getAbstractComponentPartial(id);
			String str = XMLHandler.getAbstractComponent(ac.getName());
			LogHandler.close();
			return str;
		} catch (Exception e) {
			LogHandler.close();
			return null;
		}

	}

	/**
	 * Returns a profile description
	 * @param id
	 * @return A XML with profile data
	 */

	public String getProfile(int id){
		LogHandler.getLogger().info("Starting to get a profile...");
		try {
			String str = XMLHandler.getContextContract(id);
			LogHandler.close();
			return str;
		} catch (DBHandlerException e) {
			LogHandler.close();
			return null;
		}
	}

	/**
	 * This method adds an inner component into database.
	 * @param cmp XML file as string
	 * @return True if no exception occurs
	 */

	public boolean addInnerComponent(String cmp){
		LogHandler.getLogger().info("Starting to add an inner component...");
		try {
			boolean b = XMLHandler.addInnerComponentFromXML(cmp);
			LogHandler.close();
			return b;
		} catch (XMLException e) {
			LogHandler.close();
			return false;
		}
	}



	/**
	 * This method adds a context parameter into database.
	 * @param cmp XML file as string
	 * @return True if no exception occurs
	 */
	public boolean addContextParameter(String cmp){
		LogHandler.getLogger().info("Starting to add a context parameter...");
		try {
			boolean b = XMLHandler.addContextParameterFromXML(cmp, null);
			LogHandler.close();
			return b;
		} catch (XMLException e) {
			LogHandler.close();
			return false;
		}
	}

	/**
	 * This method gets a list of context parameters.
	 * @param name Abstract component name
	 * @return XML file with a list of context parameters belonging to given abstract component
	 */
	public String getContextParameter(String name){
		LogHandler.getLogger().info("Starting to get a context parameter...");
		try {
			String s = XMLHandler.getContextParameters(name);
			LogHandler.close();
			return s;
		} catch (XMLException e) {
			LogHandler.close();
			return null;
		}
	}

	/**
	 * This method gets a context contract.
	 * @param id Context contract id
	 * @return XML file with a context contract
	 */

	public static String getContextContract(int id){
		LogHandler.getLogger().info("Starting to get a context contract...");
		try {
			String s = XMLHandler.getContextContract(id);
			LogHandler.close();
			return s;
		} catch (DBHandlerException e) {
			LogHandler.close();
			return null;
		}
	}


	/**
	 * This method adds an abstract unit to an abstract component
	 * @param cmp XML file as string
	 * @return True if no exception occurs
	 */
	public Integer addAbstractUnit(String cmp){
		LogHandler.getLogger().info("Starting to add an abstract unit...");
		try {
			int i = XMLHandler.addAbstractUnitFromXML(cmp);
			LogHandler.close();
			return i;
		} catch (XMLException e) {
			LogHandler.close();
			return null;
		}
	}

	/**
	 * This method adds an unit to an concrete component
	 * @param cmp XML file as string
	 * @return True if no exception occurs
	 */
	public Integer addConcreteUnit(String cmp){
		LogHandler.getLogger().info("Starting to add a concrete unit...");
		try {
			int cuid = XMLHandler.addConcreteUnit(cmp);
			LogHandler.close();
			return cuid;
		} catch (XMLException e) {
			LogHandler.close();
		}
		return null;
	}

	/**
	 * This method register a source code of unit file
	 * @param data File as byte array
	 * @param xml XML file as string with metadata
	 * @return True if no exception occurs
	 */
	public boolean addUnitFile(byte[] data, String xml) {
		LogHandler.getLogger().info("Starting to add an unit file...");
		try {
			XMLHandler.addUnitFile(xml, data);
			LogHandler.close();
			return true;
		} catch (XMLException e) {
			LogHandler.close();
			return false;
		}
	}

	/**
	 * Set a component as obsolete, disabling it for new components, but preserving compatibility with old components
	 * @param cmp Abstract component name
	 * @return True if operation is well successful
	 */
	public boolean setObsolete(String cmp){
		LogHandler.getLogger().info("Starting to set a component obsolete...");
		try {
			AbstractComponentHandler.setObsolete(cmp);
			LogHandler.close();
			return true;
		} catch (DBHandlerException e){
			LogHandler.close();
			return false;
		}
	}

	/**
	 * This method lists all components registered on database
	 * @return - XML file with a list of all components
	 */
	public String list(){
		LogHandler.getLogger().info("Starting to create a list of abstract components...");
		try {
			String s = XMLHandler.listComponent();
			LogHandler.close();
			return s;
		} catch (XMLException e) {
			LogHandler.close();
			return null;
		}
	}

	public static String listContract(int ac_id) throws DBHandlerException{
		LogHandler.getLogger().info("Starting to create a list of context contracts...");
		String s = XMLHandler.listContextContract(ac_id);
		LogHandler.close();
		return s;
	}

	/**
	 * This method return an abstract component in usual Shelf notation
	 * @param cc_id
	 * @return
	 */
	public static String exportContextContract(int cc_id){
		try {
			ContextContract cc = ContextContractHandler.getContextContract(cc_id);
			return FormalFormat.exportContextContract(cc, null);
		} catch (DBHandlerException e) {
			return null;
		}
	}
	
	/**
	 * This method return a context contract in usual Shelf notation
	 * @param ac_id
	 * @return
	 */
	public static String exportComponentSignature(int ac_id){
		try {
			AbstractComponentType ac = AbstractComponentHandler.getAbstractComponent(ac_id);
			return FormalFormat.exportComponentSignature(ac);
		} catch (DBHandlerException e) {
			return null;
		}
	}
	/**
	 * This method resolve a context contract
	 * @param cmp Context contract
	 * @return List of components
	 */
	public String resolve(String cmp){
		try {
			LogHandler.getLogger().info("Starting to resolve a context contract...");
			String str = XMLHandler.resolve(cmp);
			LogHandler.close();
			return str;
		} catch (XMLException e) {
			LogHandler.close();
			return null;
		}

	}

	/**
	 * This method release a platform, closing its current session
	 * @param uri of session
	 * @return True if no exception occurs
	 */

	public boolean releasePlatform(String cst){
		LogHandler.getLogger().info("Starting to reease a platform...");
		ComputationalSystemType clt = null;
		try {
			clt = XMLHandler.getComputationalSystemType(cst);
			boolean b = BackendHandler.releasePlatform(clt);
			LogHandler.close();
			return b;
		} catch (XMLException e) {
			cancelSession(clt.getSession());
			LogHandler.close();
			return false;
		}
	}

	/**
	 * This method deploy the application in selected backend infrastructure.
	 * @param candidateList - component in xml as string
	 * @return session URI 
	 */
	public static String deploy(String candidateList){
		CandidateListType clt;
		try {
			clt = XMLHandler.getCandidateList(candidateList);
			LogHandler.getLogger().info("Starting to deploy an application...");
			ComputationalSystemType ppt = BackendHandler.deployComponent(clt);
			LogHandler.close();
			return XMLHandler.getComputationalSystem(ppt);
		} catch (XMLException | ShelfRuntimeException e) {
			return "An error occurred while deploying application";
		}
	}

	public static String instantiate(String cst){
		ComputationalSystemType clt = null;
		try {
			clt = XMLHandler.getComputationalSystemType(cst);
			LogHandler.getLogger().info("Starting to instantiate application...");
			String str = BackendHandler.instantiateComponent(clt);
			LogHandler.close();
			return str;
		} catch (XMLException | ShelfRuntimeException e) {
			cancelSession(clt.getSession());
			return null;
		}
	}

	//TODO: Fazer
	public static String getStatus(String cst){
		ComputationalSystemType clt = null;
		try {
			clt = XMLHandler.getComputationalSystemType(cst);
			return BackendHandler.getStatus(clt.getNetworkAddress());
		} catch (XMLException e) {
			return null;
		}
	}

	public static boolean cancelSession(int sessionID){
		LogHandler.getLogger().info("Starting to cancel a session...");
		try {
			SessionHandler.destroySession(sessionID);
			LogHandler.close();
			return true;
		} catch (DBHandlerException e) {
			LogHandler.close();
			return false;
		}
	}

	public String instantiateACK(String address){
		LogHandler.getLogger().info("Starting to ack a session...");
		String s = SAFeServices.sendAck(address);
		LogHandler.close();
		return s;
	}


	/**
	 * This method adds an unit to an concrete component
	 * @return True if no exception occurs
	 */
	public int addQualityFunction(String func){
		LogHandler.getLogger().info("Starting to add a quality function...");
		try {
			int i = XMLHandler.addQualityFunction(func);
			LogHandler.close();
			return i;
		} catch (XMLException e) {
			LogHandler.close();
			return -1;
		}
	}
}
