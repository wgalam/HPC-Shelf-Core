package br.ufc.storm.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Attribute;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.ufc.storm.control.Resolution;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.ResolveException;
import br.ufc.storm.exception.XMLException;
import br.ufc.storm.export.FormalFormat;
import br.ufc.storm.io.FileHandler;
import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.AbstractUnitType;
import br.ufc.storm.jaxb.CalculatedFunctionType;
import br.ufc.storm.jaxb.CandidateListType;
import br.ufc.storm.jaxb.ComputationalSystemType;
import br.ufc.storm.jaxb.ConcreteUnitType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.ContractList;
import br.ufc.storm.jaxb.ObjectFactory;
import br.ufc.storm.jaxb.UnitFileType;
import br.ufc.storm.model.ResolutionNode;
import br.ufc.storm.sql.AbstractComponentHandler;
import br.ufc.storm.sql.AbstractUnitHandler;
import br.ufc.storm.sql.CalculatedArgumentHandler;
import br.ufc.storm.sql.ConcreteUnitHandler;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.sql.ContextParameterHandler;
import br.ufc.storm.sql.DBHandler;
import br.ufc.storm.sql.PlatformHandler;
import br.ufc.storm.sql.ResolutionHandler;


public class XMLHandler {
	static DocumentBuilderFactory dbf;
	static DocumentBuilder db = null;
	static Document doc = null;
	static URL url;

	/**
	 * This method open a xml file
	 * @param file XML file URL or content
	 * @return XML document opened
	 */
	public static Document openXMLFile(String file){

		dbf = DocumentBuilderFactory.newInstance();
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		InputSource inStream = new InputSource(); 
		try {
			url = new URL(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder out = new StringBuilder();
			String newLine = System.getProperty("line.separator");
			String line;
			while ((line = in.readLine()) != null) {
				out.append(line);
				out.append(newLine);
			}
			file = out.toString();
			in.close();
		} catch (MalformedURLException e) {
			//Not an error
			//This exception is purposely left in blank, because always when enter in this method, 
			//the file name can be a url or a file content as string
		} catch (IOException e) {
			e.printStackTrace();
		}
		inStream.setCharacterStream(new StringReader(file)); 
		try {
			doc = db.parse(inStream);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	public static ContextContract importContextContract(String file) throws XMLException{
		String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(file)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ContextContract cc = new ContextContract();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			@SuppressWarnings("unchecked")
			JAXBElement<ContextContract> element = (JAXBElement<ContextContract>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(file)));
			cc=element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return cc;
	}

	/**
	 * This method resolves a XML Context Contract
	 * @param file in XML
	 * @return Candidate list XML
	 * @throws XMLException
	 */
	public static String resolve(String file) throws XMLException{
		ContextContract cc = new ContextContract();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			@SuppressWarnings("unchecked")
			JAXBElement<ContextContract> element = (JAXBElement<ContextContract>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(file)));
			cc=element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		
		
		java.io.StringWriter sw = new StringWriter();
		context = null;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			JAXBElement<CandidateListType> element1;
			try {
				element1 = new ObjectFactory().createCandidateList(Resolution.resolve(cc));
			} catch (ResolveException e) {
				throw new XMLException(e);
			}
			marshaller.marshal(element1, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		String str = sw.toString();
		try {
			sw.close();
		} catch (IOException e) {
			throw new XMLException(e);
		}
		return str;

	}
	
	public static String exportContextContractFromContractXML(String file) throws XMLException{
		ContextContract cc = new ContextContract();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			@SuppressWarnings("unchecked")
			JAXBElement<ContextContract> element = (JAXBElement<ContextContract>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(file)));
			cc=element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return FormalFormat.exportContextContract(cc, null);

	}
	
	public static String sortCandidateList(String file, int rank) throws XMLException{
		CandidateListType cl = new CandidateListType();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			@SuppressWarnings("unchecked")
			JAXBElement<CandidateListType> element = (JAXBElement<CandidateListType>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(file)));
			cl=element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		java.io.StringWriter sw = new StringWriter();
		context = null;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			JAXBElement<CandidateListType> element1;
			element1 = new ObjectFactory().createCandidateList(Resolution.sortCandidateList(cl, rank));
			marshaller.marshal(element1, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		String str = sw.toString();
		try {
			sw.close();
		} catch (IOException e) {
			throw new XMLException(e);
		}
		return str;

	}

	/**
	 * Resolve a contract
	 * @param file
	 * @return
	 */
	public static String resolveInverse(AbstractComponentType abs){
		JAXBContext context;
		try {
			context = JAXBContext.newInstance("br.ufc.lia.storm.jaxb");
			Marshaller marshaller = context.createMarshaller();
			JAXBElement<AbstractComponentType> element = new ObjectFactory().createAbstractComponent(abs);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(element, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static AbstractComponentType XMLtoJava(String file){

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("br.ufc.lia.storm.jaxb");
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			JAXBElement<AbstractComponentType> customer = (JAXBElement<AbstractComponentType>) jaxbUnmarshaller.unmarshal(new File(file));
			return customer.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*@SuppressWarnings("unchecked")
	public static AbstractComponentType generateContextParameter(File file){
		AbstractComponentType ac = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<AbstractComponentType> element = (JAXBElement<AbstractComponentType>) unmarshaller.unmarshal(file);
			ac = element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return ac;
	}
*/

	/**
	 * This method adds an abstract component from a xml file
	 * @param file XML file
	 * @return True if non exception occurs
	 * @throws DBHandlerException 
	 * @throws XMLException 
	 * @throws ResolveException 
	 */
	
	@SuppressWarnings("unchecked")
	public static boolean addAbstractComponentFromXML(String file) throws XMLException, ResolveException{

		AbstractComponentType ac = new AbstractComponentType();
		try {
			JAXBContext context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<AbstractComponentType> element = (JAXBElement<AbstractComponentType>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(file)));
			ac = element.getValue();
		} catch (JAXBException e) {
			throw new XMLException("Can not unmarshler XML", e);
		}

		if(ac.getSupertype().getIdAc()==null){
			try {
				ac.getSupertype().setIdAc(AbstractComponentHandler.getAbstractComponentID(ac.getSupertype().getName()));
			} catch (DBHandlerException e) {
				throw new XMLException(e);
			}
		}

		try {
			DBHandler.getConnection().setAutoCommit(false);
			AbstractComponentHandler.addAbstractComponent(ac, null);
			DBHandler.getConnection().commit();
			DBHandler.getConnection().setAutoCommit(true);
		} catch (SQLException e) {
			throw new XMLException("Error commiting changes ",e);
		} catch (DBHandlerException e) {
			throw new XMLException("Error commiting changes ",e);
		}


		//			Adding context parameter this abstract component has

		return true;
	}

	/**
	 * This method generates a XML file with an abstract component
	 * @param ac_name Abstract component name
	 * @return XML file content with abstract component values
	 * @throws DBHandlerException 
	 */	

	public static String getContextContract(int cc_id) throws DBHandlerException{
		ContextContract cc = null;
		cc = ContextContractHandler.getContextContract(cc_id);
		ContextContractHandler.completeContextContract(cc);
		JAXBContext context;
		java.io.StringWriter sw = new StringWriter();
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			JAXBElement<ContextContract> element = new ObjectFactory().createContextContract(cc);
			marshaller.marshal(element, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	/**
	 * This method generates a XML file with an abstract component
	 * @param ac_name Abstract component name
	 * @return XML file content with abstract component values
	 */	

	public static String getContextContract(ContextContract cc){
		JAXBContext context;
		java.io.StringWriter sw = new StringWriter();
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			JAXBElement<ContextContract> element = new ObjectFactory().createContextContract(cc);
			marshaller.marshal(element, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	/**
	 * 
	 * @param cmp
	 * @param con
	 * @return
	 * @throws XMLException 
	 * @throws DBHandlerException 
	 */
	@SuppressWarnings("unchecked")
	public static boolean addPlatformContextContract(String cmp) throws XMLException, DBHandlerException{
		ContextContract cc = null;
		JAXBContext context;
		try {
			DBHandler.getConnection().setAutoCommit(false);
		} catch (SQLException e) {
			throw new XMLException("Error commiting changes ",e);
		}
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<ContextContract> element = (JAXBElement<ContextContract>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(cmp)));
			cc = element.getValue();
			ContextContractHandler.addPlatformContextContract(cc);
			try {
				DBHandler.getConnection().commit();
				DBHandler.getConnection().setAutoCommit(true);
			} catch (SQLException e) {
				throw new XMLException("Error commiting changes ",e);
			}
			return true;
		} catch (JAXBException e) {
			throw new XMLException("Can not add context contract, problem JAXB translator", e);
		}

	}
	
	/**
	 * 
	 * @param cmp
	 * @param con
	 * @return
	 * @throws XMLException 
	 * @throws DBHandlerException 
	 */
	@SuppressWarnings("unchecked")
	public static boolean addContextContract(String cmp) throws XMLException, DBHandlerException{
		ContextContract cc = null;
		JAXBContext context;
		try {
			DBHandler.getConnection().setAutoCommit(false);
		} catch (SQLException e) {
			throw new XMLException("Error commiting changes ",e);
		}
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<ContextContract> element = (JAXBElement<ContextContract>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(cmp)));
			cc = element.getValue();
			ContextContractHandler.addContextContract(cc);
			try {
				DBHandler.getConnection().commit();
				DBHandler.getConnection().setAutoCommit(true);
			} catch (SQLException e) {
				throw new XMLException("Error commiting changes ",e);
			}
			return true;
		} catch (JAXBException e) {
			throw new XMLException("Can not add context contract, problem JAXB translator", e);
		}

	}

	/**
	 * This method generates a XML file with an abstract component
	 * @param ac_name Abstract component name
	 * @return XML file content with abstract component values
	 * @throws XMLException 
	 */	

	public static String getAbstractComponent(String ac_name) throws XMLException{
		AbstractComponentType abscom = null;
		try {
			abscom = AbstractComponentHandler.getAbstractComponent(AbstractComponentHandler.getAbstractComponentID(ac_name), true);
		} catch (DBHandlerException e1) {
			throw new XMLException("Can not get component in database", e1);
		}
		JAXBContext context;
		java.io.StringWriter sw = new StringWriter();
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			JAXBElement<AbstractComponentType> element = new ObjectFactory().createAbstractComponent(abscom);
			marshaller.marshal(element, sw);
		} catch (JAXBException e) {
			throw new XMLException("Can not convert component to XML", e);
		}
		return sw.toString();
	}

	/**
	 * This method generates a XML file with an abstract component
	 * @param ac_name Abstract component name
	 * @return XML file content with abstract component values
	 */	

	public static String getAbstractComponent(AbstractComponentType abscom){
		JAXBContext context;
		java.io.StringWriter sw = new StringWriter();
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			JAXBElement<AbstractComponentType> element = new ObjectFactory().createAbstractComponent(abscom);
			marshaller.marshal(element, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}
	/**
	 * This method adds a context parameter from a XML file
	 * @param cmp XML file
	 * @return True if non exception occurs
	 * @throws XMLException 
	 */

	@SuppressWarnings("unchecked")
	public static boolean addContextParameterFromXML(String cmp, Connection con) throws XMLException{
		AbstractComponentType ac = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<AbstractComponentType> element = (JAXBElement<AbstractComponentType>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(cmp)));
			ac = element.getValue();
		} catch (JAXBException e) {
			throw new XMLException("Error while unmarshaling context parameter: ", e);
		}
		ContextParameterType cp = ac.getContextParameter().get(0);
		try {
			ContextParameterHandler.addContextParameter(cp.getName(), cp.getBound().getAbstractComponent().getName(), ac.getName(), cp.getContextVariableProvided(), cp.getBoundValue(), null , null, cp.getKind(),cp.getVariance());
		} catch (DBHandlerException | ResolveException e) {
			throw new XMLException(e);
		}
		return true;
	}


	/**
	 * This method generates a XML file with all context parameters owned by a given abstract component name 
	 * @param ac_name Abstract component name
	 * @return XML file content with context parameters values
	 * @throws XMLException 
	 */

	public static String getContextParameters(String ac_name) throws XMLException{
		AbstractComponentType ac = new AbstractComponentType();
		try {
			ac.setIdAc(AbstractComponentHandler.getAbstractComponentID(ac_name));
		} catch (DBHandlerException e2) {
			throw new XMLException(e2);
		}
			try {
				ac.getContextParameter().addAll(ContextParameterHandler.getAllContextParameterFromAbstractComponent(ac.getIdAc(),null));
			} catch (DBHandlerException e1) {
				throw new XMLException(e1);
			}
		JAXBContext context;
		java.io.StringWriter sw = new StringWriter();
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			//marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JAXBElement<AbstractComponentType> element = new ObjectFactory().createAbstractComponent(ac);
			marshaller.marshal(element, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}


	/**
	 * This method adds an inner component
	 * @param cmp XML file content
	 * @return True if the addiction was well successfully
	 * @throws XMLException 
	 */

	@SuppressWarnings("unchecked")
	public static boolean addInnerComponentFromXML(String cmp) throws XMLException {

		AbstractComponentType ac = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<AbstractComponentType> element = (JAXBElement<AbstractComponentType>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(cmp)));
			ac = element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
			return false;
		}
		try {
			AbstractComponentHandler.addInnerComponnet(ac.getIdAc(), ac.getInnerComponents().get(0).getIdAc());
		} catch (DBHandlerException e) {
			throw new XMLException(e);
		} catch (ResolveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * This method add all abstract units of an abstract component	
	 * @param file
	 * @throws XMLException 
	 */

	@SuppressWarnings("unchecked")
	public static void addAbstractUnitsFromXML(File file) throws XMLException {
		AbstractComponentType ac = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<AbstractComponentType> element = (JAXBElement<AbstractComponentType>) unmarshaller.unmarshal(file);
			ac = element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		for(AbstractUnitType aut : ac.getAbstractUnit()){
			try {
				AbstractUnitHandler.addAbstractUnit(ac.getName(), aut.getAuName());
			} catch (DBHandlerException e) {
				throw new XMLException(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean addUnitFile(String xml, byte [] data) throws XMLException{
		UnitFileType uft = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<UnitFileType> element = (JAXBElement<UnitFileType>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(xml)));
			uft = element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		//TODO: Verificar se existe unidade concreta, se existir, atualizar
		String path;
		try {
			path = br.ufc.storm.sql.ConcreteUnitHandler.addConcreteUnitFile(uft);
			System.out.println(path);
			if(uft.getPath()!= null){
				uft.setPath(path.replace('.', '/')+"/");
				String filename = uft.getPath()+uft.getFilename();
				if(!uft.getExtension().equals("")){
					filename+="."+uft.getExtension();
				}
				if(FileHandler.addFile(data, filename)){
					return true;
				}
			}
		} catch (DBHandlerException e) {
			throw new XMLException(e);
		}

		return false;

	}

	public static int addQualityFunction(String file) throws XMLException{
		return XMLHandler.addCalculatedFunction(file, 1);
	}
	
	public static int addCostFunction(String file) throws XMLException{
		return XMLHandler.addCalculatedFunction(file, 2);
	}
	
	public static int addRankFunction(String file) throws XMLException{
		return XMLHandler.addCalculatedFunction(file, 3);
	}
	
	@SuppressWarnings("unchecked")
	public static int addCalculatedFunction(String file, int type) throws XMLException{
		CalculatedFunctionType qf = new CalculatedFunctionType();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<CalculatedFunctionType> element = (JAXBElement<CalculatedFunctionType>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(file)));
			qf = element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
			return -1;
		}
		try {
			return CalculatedArgumentHandler.addCalculatedFunction(qf, type);
					//QualityHandler.addQualityFunction(qf);
		} catch (DBHandlerException e) {
			throw new XMLException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static ContextContract cloneContextContract(ContextContract componentToClone){
		JAXBContext context;
		ContextContract newComponent = new ContextContract();
		java.io.StringWriter sw = new StringWriter();
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			JAXBElement<ContextContract> element = new ObjectFactory().createContextContract(componentToClone);
			marshaller.marshal(element, sw);
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			element = (JAXBElement<ContextContract>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(sw.toString())));
			newComponent = element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return newComponent;
	}

	@SuppressWarnings("unchecked")
	public static Integer addAbstractUnitFromXML(String cmp) throws XMLException {
		AbstractUnitType aut = new AbstractUnitType();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<AbstractUnitType> element = (JAXBElement<AbstractUnitType>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(cmp)));
			aut = element.getValue();
		} catch (JAXBException e) {
			throw new XMLException("An error occurred while converting XML", e);
		}
		try {
			return AbstractUnitHandler.addAbstractUnit(AbstractComponentHandler.getAbstractComponentName(aut.getAcId()), aut.getAuName());
		} catch (DBHandlerException e) {
			throw new XMLException("An error occurred while registering abstract unit", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static int addConcreteUnit(String cmp) throws XMLException {
		ConcreteUnitType cut = new ConcreteUnitType();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<ConcreteUnitType> element = (JAXBElement<ConcreteUnitType>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(cmp)));
			cut = element.getValue();
		} catch (JAXBException e) {
			throw new XMLException("An error occurred while converting XML", e);
		}
		try {
			return ConcreteUnitHandler.addConcreteUnit(cut.getCcId(), cut.getAuId());
		} catch (DBHandlerException e) {
			throw new XMLException("An error occurred while registering concrete unit", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static CandidateListType getCandidateList(String file) throws XMLException {
		CandidateListType list = new CandidateListType();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<CandidateListType> element = (JAXBElement<CandidateListType>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(file)));
			list = element.getValue();
			return list;
		} catch (JAXBException e) {
			throw new XMLException("An error occurred while converting XML", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ComputationalSystemType getComputationalSystemType(String cst) throws XMLException {
		ComputationalSystemType comp = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<ComputationalSystemType> element = (JAXBElement<ComputationalSystemType>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(cst)));
			comp = element.getValue();
			return comp;
		} catch (JAXBException e) {
			throw new XMLException("An error occurred while converting XML", e);
		}
	}


	/**
	 * This method gets a list of all abstract components
	 * @return XML file content
	 * @throws XMLException 
	 */
	
	
	public static String listComponent() throws XMLException{
		List<AbstractComponentType> abscom = null;
		try {
			ResolutionNode.setup();
			abscom = AbstractComponentHandler.listAbstractComponents();
		} catch (Exception e) {
			throw new XMLException(e);
		}
		org.jdom2.Element cp = new org.jdom2.Element("root");
		org.jdom2.Document doc = new org.jdom2.Document(cp);
		for(int i = 0; i < abscom.size(); i++){ 
			org.jdom2.Element contextParameter = new org.jdom2.Element("abstract_component");
			contextParameter.setAttribute(new Attribute("ac_id", abscom.get(i).getIdAc()+""));
			contextParameter.setAttribute(new Attribute("name", abscom.get(i).getName()));
			contextParameter.setAttribute(new Attribute("path", ResolutionNode.resolutionTree.findNode(abscom.get(i).getIdAc()).getPath()));
			contextParameter.setAttribute(new Attribute("supertype", abscom.get(i).getSupertype().getName()));
			doc.getRootElement().addContent(contextParameter);
		}
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		return xmlOutput.outputString((org.jdom2.Document) doc);
	}


	public static String listContextContract(int ac_id) throws DBHandlerException{
		ContractList list;
		try {
			list = ContextContractHandler.listContract(ac_id);
			JAXBContext context;
			java.io.StringWriter sw = new StringWriter();
			try {
				context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
						br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
				Marshaller marshaller = context.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				JAXBElement<ContractList> element = new ObjectFactory().createContractList(list);
				marshaller.marshal(element, sw);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			return sw.toString();
		} catch (DBHandlerException e1) {
			throw new DBHandlerException(e1);
		}
	
	}


	public static String getComputationalSystem(ComputationalSystemType cst) {
		JAXBContext context;
		java.io.StringWriter sw = new StringWriter();
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			JAXBElement<ComputationalSystemType> element = new ObjectFactory().createComputationalSystem(cst);
			marshaller.marshal(element, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

}