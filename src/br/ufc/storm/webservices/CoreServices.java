package br.ufc.storm.webservices;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.CandidateListType;
import br.ufc.storm.sql.AbstractComponentHandler;
import br.ufc.storm.sql.SessionHandler;
import br.ufc.storm.xml.XMLHandler;
import br.ufc.storm.control.ComputationalSystemHandler;
import br.ufc.storm.control.Deployer;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.XMLException;

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
		} catch (XMLException e) {
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
		return XMLHandler.addContextContractFromXML(cmp);
	}

	/**
	 * This method gets an abstract component and returns its description.
	 * @param name Abstract component name
	 * @return Abstract component data as XML string
	 */

	public String getAbstractComponent(String name){
		return XMLHandler.getAbstractComponent(name);
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
		} catch (DBHandlerException e) {
			return null;
		}

	}

	/**
	 * This method adds an profile into database.
	 * @param cmp XML file as string
	 * @return True if no exception occurs
	 */

	public boolean addProfile(String cmp) throws ParserConfigurationException, SAXException, IOException{
		return XMLHandler.addProfileFromXML(cmp);
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
	 * This method gets an inner component.
	 * @param Inner component name
	 * @return XML file with an inner component description
	 */
	public String getInnerComponent(String name){
		return XMLHandler.getInnerComponent(name);
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
	public int addAbstractUnit(String cmp){
		return XMLHandler.addAbstractUnitFromXML(cmp);
	}

	/**
	 * This method adds an unit to an concrete component
	 * @param cmp XML file as string
	 * @return True if no exception occurs
	 */
	public int addConcreteUnit(String cmp){
		return XMLHandler.addConcreteUnit(cmp);
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

		return XMLHandler.listContract(ac_id);
	}

	/**
	 * This method resolve a context contract
	 * @param cmp Context contract
	 * @return List of components
	 */
	public String resolve(String cmp){
		try {
			return XMLHandler.resolve(cmp);
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
		return ComputationalSystemHandler.releasePlatform(uri);
	}

	/**
	 * This method deploy the application in selected backend infrastructure.
	 * @param cmp - component in xml as string
	 * @return session URI 
	 */
	public String deploy(int sessionID, String cmp){
		CandidateListType clist = XMLHandler.getList(new File(cmp));
		try {
			SessionHandler.createSession(sessionID);
		} catch (DBHandlerException e) {
			return null;
		}
		return(Deployer.deploy(clist));
	}

	/**
	 * Returns a component address
	 * @param uri
	 * @return
	 */
	public String instantiate(String uri){
		return ComputationalSystemHandler.instantiate(uri);
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
