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
		
		try {
			return XMLHandler.addAbstractComponentFromXML(cmp);
		} catch (XMLException | ResolveException e) {
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
		try {
			return XMLHandler.addContextContract(cmp);
		} catch (XMLException | DBHandlerException e) {
			return false;
		}
	}

	/**
	 * This method gets an abstract component and returns its description.
	 * @param name Abstract component name
	 * @return Abstract component data as XML string
	 */

	public String getAbstractComponent(String name){
		System.out.println("AHHHHHHHHHHHH");
		LogHandler.getLogger().info("Starting to get Abstract component");
		try {
			return XMLHandler.getAbstractComponent(name);
		} catch (XMLException e) {
			return null;
		}
	}

	/**
	 * This method gets an abstract component and returns its description.
	 * @param id Abstract component id
	 * @return Abstract component data as string
	 */

	public String getAbstractComponentByID(int id){
		try {
			AbstractComponentType ac = AbstractComponentHandler.getAbstractComponentPartial(id);
			return XMLHandler.getAbstractComponent(ac.getName());
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * Returns a profile description
	 * @param id
	 * @return A XML with profile data
	 */

	public String getProfile(int id){
		try {
			return XMLHandler.getContextContract(id);
		} catch (DBHandlerException e) {
			return null;
		}
	}

	/**
	 * This method adds an inner component into database.
	 * @param cmp XML file as string
	 * @return True if no exception occurs
	 */

	public boolean addInnerComponent(String cmp){
		try {
			return  XMLHandler.addInnerComponentFromXML(cmp);
		} catch (XMLException e) {
			return false;
		}
	}



	/**
	 * This method adds a context parameter into database.
	 * @param cmp XML file as string
	 * @return True if no exception occurs
	 */
	public boolean addContextParameter(String cmp){
		try {
			return XMLHandler.addContextParameterFromXML(cmp, null);
		} catch (XMLException e) {
			return false;
		}
	}

	/**
	 * This method gets a list of context parameters.
	 * @param name Abstract component name
	 * @return XML file with a list of context parameters belonging to given abstract component
	 */
	public String getContextParameter(String name){
		try {
			return XMLHandler.getContextParameters(name);
		} catch (XMLException e) {
			return null;
		}
	}

	/**
	 * This method gets a context contract.
	 * @param id Context contract id
	 * @return XML file with a context contract
	 */

	public String getContextContract(int id){
		try {
			return XMLHandler.getContextContract(id);
		} catch (DBHandlerException e) {
			return null;
		}
	}


	//#################################################################
	/**
	 * This method adds a concrete component into core.
	 * @param abstractcomponent Abstract component name.
	 * @param url Source code file URL
	 * @return True if no exception occurs
	 */
	public boolean addConcreteComponent(String abstractcomponent, String url){
		//TODO: A fazer
		return false;
	}

	//#################################################################
	/**
	 * This method adds an abstract unit to an abstract component
	 * @param cmp XML file as string
	 * @return True if no exception occurs
	 */
	public Integer addAbstractUnit(String cmp){
		try {
			return XMLHandler.addAbstractUnitFromXML(cmp);
		} catch (XMLException e) {
			return null;
		}
	}

	/**
	 * This method adds an unit to an concrete component
	 * @param cmp XML file as string
	 * @return True if no exception occurs
	 */
	public boolean addConcreteUnit(String cmp){
		try {
			XMLHandler.addConcreteUnit(cmp);
			return true;
		} catch (XMLException e) {
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
		try {
			XMLHandler.addUnitFile(xml, data);
			return true;
		} catch (XMLException e) {
			return false;
		}
	}

	/**
	 * Set a component as obsolete, disabling it for new components, but preserving compatibility with old components
	 * @param cmp Abstract component name
	 * @return True if operation is well successful
	 */
	public boolean setObsolete(String cmp){
		try {
			AbstractComponentHandler.setObsolete(cmp);
			return true;
		} catch (DBHandlerException e) {
			return false;
		}
	}

	/**
	 * This method lists all components registered on database
	 * @return - XML file with a list of all components
	 */
	public String list(){
		try {
			return XMLHandler.listComponent();
		} catch (XMLException e) {
			return null;
		}
	}

	public static String listContract(int ac_id) throws DBHandlerException{

		return XMLHandler.listContextContract(ac_id);
	}

	/**
	 * This method resolve a context contract
	 * @param cmp Context contract
	 * @return List of components
	 */
	public String resolve(String cmp){
		try {
			LogHandler.getLogger().info("STARTING RESOLUTION");
			String str = XMLHandler.resolve(cmp);
			//LogHandler.close();
			return str;
		} catch (XMLException e) {
			return null;
		}
		
	}

	/**
	 * This method release a platform, closing its current session
	 * @param uri of session
	 * @return True if no exception occurs
	 */

	public boolean releasePlatform(String uri){
		return BackendHandler.releasePlatform(uri);
	}

	/**
	 * This method deploy the application in selected backend infrastructure.
	 * @param cmp - component in xml as string
	 * @return session URI 
	 */
	public String deploy(int sessionID, String cmp){
		try {
			CandidateListType clist = XMLHandler.getCandidateList(cmp);
			SessionHandler.createSession(sessionID);
			return(Deployer.deploy(clist));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns a component address
	 * @param uri
	 * @return
	 */
	public String instantiate(String uri){
		return BackendHandler.instantiate(uri);
	}

	public boolean cancelSession(int sessionID){
		try {
			SessionHandler.destroySession(sessionID);
			return true;
		} catch (DBHandlerException e) {
			return false;
		}
	}

	public String instantiateACK(String address){
		return SAFeServices.sendAck(address);
	}


	/**
	 * This method adds an unit to an concrete component
	 * @return True if no exception occurs
	 */
	public int addQualityFunction(String func){
		try {
			return XMLHandler.addQualityFunction(func);
		} catch (XMLException e) {
			return -1;
		}
	}
}
