package br.ufc.storm.webservices;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.CandidateListType;
import br.ufc.storm.sql.AbstractComponentHandler;
import br.ufc.storm.sql.SessionHandler;
import br.ufc.storm.xml.XMLHandler;
import br.ufc.storm.control.BackendHandler;
import br.ufc.storm.control.Deployer;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.ResolveException;
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

	public String getContextContract(int id){
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
	public boolean addConcreteUnit(String cmp){
		LogHandler.getLogger().info("Starting to add a concrete unit...");
		try {
			XMLHandler.addConcreteUnit(cmp);
			LogHandler.close();
			return true;
		} catch (XMLException e) {
			LogHandler.close();
			return false;
		}
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

	public boolean releasePlatform(String uri){
		LogHandler.getLogger().info("Starting to reease a platform...");
		boolean b = BackendHandler.releasePlatform(uri);
		LogHandler.close();
		return b;
	}

	/**
	 * This method deploy the application in selected backend infrastructure.
	 * @param cmp - component in xml as string
	 * @return session URI 
	 */
	public String deploy(int sessionID, String cmp){
		LogHandler.getLogger().info("Starting to deploy an application...");
		try {
			CandidateListType clist = XMLHandler.getCandidateList(cmp);
			SessionHandler.createSession(sessionID);
			String str = (Deployer.deploy(clist));
			LogHandler.close();
			return str;
		} catch (Exception e) {
			LogHandler.close();
			return null;
		}
	}

	/**
	 * Returns a component address
	 * @param uri
	 * @return
	 */
	public String instantiate(String uri){
		LogHandler.getLogger().info("Starting to instantiate an application...");
		String s = BackendHandler.instantiate(uri);
		LogHandler.close();
		return s;
	}

	public boolean cancelSession(int sessionID){
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
