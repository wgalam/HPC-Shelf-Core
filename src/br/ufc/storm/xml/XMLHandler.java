package br.ufc.storm.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
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
import br.ufc.storm.exception.XMLException;
import br.ufc.storm.io.FileHandler;
import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.AbstractUnitType;
import br.ufc.storm.jaxb.CandidateListType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.ContractList;
import br.ufc.storm.jaxb.ObjectFactory;
import br.ufc.storm.jaxb.QualityFunctionType;
import br.ufc.storm.jaxb.UnitFileType;
import br.ufc.storm.model.ResolutionNode;
import br.ufc.storm.sql.AbstractComponentHandler;
import br.ufc.storm.sql.AbstractUnitHandler;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.sql.ContextParameterHandler;
import br.ufc.storm.sql.DBHandler;
import br.ufc.storm.sql.QualityHandler;
import br.ufc.storm.sql.ResolutionHandler;
import br.ufc.storm.sql.ConcreteUnitHandler;

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


	public static String resolve(String file) throws DBHandlerException{
		ContextContract cc = new ContextContract();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<ContextContract> element = (JAXBElement<ContextContract>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(file)));
			cc=element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		//		CandidateListType list = new CandidateListType();
		//		list.getCandidate().addAll(Resolution.resolve(cc, null));

		java.io.StringWriter sw = new StringWriter();
		context = null;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			JAXBElement<CandidateListType> element1 = new ObjectFactory().createCandidateList(Resolution.resolve(cc, null,null));
			marshaller.marshal(element1, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		String str = sw.toString();
		try {
			sw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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


	/**
	 * This method adds an abstract component from a xml file
	 * @param file XML file
	 * @return True if non exception occurs
	 * @throws DBHandlerException 
	 */
	public static boolean addAbstractComponentFromXML(String file) throws DBHandlerException{



		AbstractComponentType ac = new AbstractComponentType();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<AbstractComponentType> element = (JAXBElement<AbstractComponentType>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(file)));
			ac = element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
			return false;
		}
		if(ac.getSupertype().getIdAc()==null){
			try {
				ac.getSupertype().setIdAc(AbstractComponentHandler.getAbstractComponentID(ac.getSupertype().getName()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			AbstractComponentHandler.addAbstractComponent(ac);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public static boolean addContextContract(String cmp, Connection con) throws XMLException, DBHandlerException{
		ContextContract cc = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<ContextContract> element = (JAXBElement<ContextContract>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(cmp)));
			cc = element.getValue();
			ContextContractHandler.addContextContract(cc);
			return true;
		} catch (JAXBException e) {
			throw new XMLException("Can not add context contract, problem JAXB translator");
		} catch (SQLException e) {
			throw new DBHandlerException("Can not add context contract, problem in database");
		}
		
	}

	/**
	 * This method generates a XML file with an abstract component
	 * @param ac_name Abstract component name
	 * @return XML file content with abstract component values
	 */	

	public static String getAbstractComponent(String ac_name){
		AbstractComponentType abscom = null;
		try {
			abscom = AbstractComponentHandler.getAbstractComponent(AbstractComponentHandler.getAbstractComponentID(ac_name));
		} catch (DBHandlerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(abscom.toString());
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
	 */

	public static boolean addContextParameterFromXML(String cmp, Connection con){
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
		ContextParameterType cp = ac.getContextParameter().get(0);
		ContextParameterHandler.addContextParameter(cp.getName(), cp.getBound().getAbstractComponent().getName(), ac.getName(), cp.getContextVariable().getCcName(), con);
		return true;
	}


	/**
	 * This method generates a XML file with all context parameters owned by a given abstract component name 
	 * @param ac_name Abstract component name
	 * @return XML file content with context parameters values
	 */

	public static String getContextParameters(String ac_name){
		AbstractComponentType ac = new AbstractComponentType();
		try {
			ac.setIdAc(AbstractComponentHandler.getAbstractComponentID(ac_name));
		} catch (SQLException e2) {
			
		}
		Connection con;
		List<ContextParameterType> cp = null;
		try {
			con = DBHandler.getConnection();
			cp = ContextParameterHandler.getAllContextParameterFromAbstractComponent(ac.getIdAc(), con);
		
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		List<ContextParameterType> aux = ac.getContextParameter();
		aux = cp;
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
	 * Method for add an instantiation type
	 * @param cmp XML file 
	 * @return Returns True if instantiation type was correctly added.
	 */

	public static int addInstantiationTypeFromXML(String cmp){
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
			return -1;
		}
		ContextContract it = ac.getContextParameter().get(0).getContextVariable();
		return ContextContractHandler.addContextContract(it.getCcName(), ac.getName());		
	}

	/**
	 * This method adds a platform profile to components library
	 * @param cmp XML file content 
	 * @return True if the addiction was well successfully
	 */

	public static boolean addProfileFromXML(String cmp) {
	 	return false;
	}

	/**
	 * This method adds a context argument to an abstract components 
	 * @param file XML file content 
	 * @return True if the addiction was well successfully
	 */


	/**
	 * This method adds an inner component
	 * @param cmp XML file content
	 * @return True if the addiction was well successfully
	 */

	public static boolean addInnerComponentFromXML(String cmp) {

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
		ContextContractHandler.addInnerComponent(ac.getInnerComponents().get(0).getName(), ac.getInnerComponents().get(0).getName(), ac.getName());
		return true;
	}

	/**
	 * This method add all abstract units of an abstract component	
	 * @param file
	 */

	public static void addAbstractUnitsFromXML(File file) {
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
			AbstractUnitHandler.addAbstractUnit(aut.getAcId(), aut.getAuName());
		}
	}

	/**
	 * This method adds an concrete unit
	 * @param cmp XML file content
	 * @return True if the addiction was well successfully
	 */

	public static int addUnitFromXML(String cmp) {
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
			return -1;
		}

		//return DBHandler.addConcreteUnit(, DBHandler.getAbstractUnitID(name)+"");

		return -1;
		/*
		String name;
		String concrete_component_id;
		Document doc = XMLHandler.openXMLFileNew(file);
		Element raiz = doc.getDocumentElement();
		NodeList listUnits = raiz.getElementsByTagName("concrete_unit");
		Element component = (Element) listUnits.item(0);
		name = component.getAttribute("au_name");		
		concrete_component_id = component.getAttribute("concrete_component_id");
		DBHandler.addConcreteUnit(concrete_component_id, DBHandler.getAbstractUnitID(name)+"");//Corrigir
		return DBHandler.getConcreteUnitID(name); //Corrigir
		 */	}

	public static boolean addUnitFile(String xml, byte [] data){
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
				if(FileHandler.addFile(data, uft.getPath()+uft.getFilename()+"."+uft.getExtension())){
					return true;
				}
			}
		} catch (DBHandlerException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;

	}

	/*public static boolean addUnitFile(String xml, byte [] data){
		String filename;
		String extension;
		String folder;
		String build_cfg;
		String version;
		String filetype;
		String uid;
		Document doc = XMLHandler.openXMLFile(xml);
		Element raiz = doc.getDocumentElement();
		NodeList listUnits = raiz.getElementsByTagName("unit_file");
		Element component = (Element) listUnits.item(0);
		filename = component.getAttribute("name");		
		uid = component.getAttribute("u_id");
		extension = component.getAttribute("extension");
		folder = component.getAttribute("folder");
		build_cfg = component.getAttribute("build_cfg");
		version = component.getAttribute("version");
		filetype = component.getAttribute("file_type");

		if(DBHandler.addConcreteUnitFile(filename, uid, extension, folder, build_cfg, version, filetype)){
			if(FileHandler.addFile(data, uid+"."+filename+"."+extension)){
				return true;
			}else{
				//REMOVER
				return false;
			}
		}
		return false;

	}*/


	/**
	 * This method gets a list of all abstract components
	 * @return XML file content
	 */


	public static String listComponent(){
		ResolutionNode tree  = ResolutionHandler.generateResolutionTree();
		List<AbstractComponentType> abscom = null;
		abscom = AbstractComponentHandler.listAbstractComponents();
		org.jdom2.Element cp = new org.jdom2.Element("root");
		org.jdom2.Document doc = new org.jdom2.Document(cp);
		for(int i = 0; i < abscom.size(); i++){ 
			org.jdom2.Element contextParameter = new org.jdom2.Element("abstract_component");
			contextParameter.setAttribute(new Attribute("ac_id", abscom.get(i).getIdAc()+""));
			contextParameter.setAttribute(new Attribute("name", abscom.get(i).getName()));
			contextParameter.setAttribute(new Attribute("path", tree.findNode(abscom.get(i).getIdAc()).getPath()));
			contextParameter.setAttribute(new Attribute("supertype", abscom.get(i).getSupertype().getName()));
			//contextParameter.setAttribute(new Attribute("kind", abscom.get(i).getKind()+""));
			doc.getRootElement().addContent(contextParameter);
		}
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		return xmlOutput.outputString((org.jdom2.Document) doc);
	}

	public static AbstractComponentType generateAbstractComponentType(int ac_id, String name, int supertype, int parent, int kind){
		AbstractComponentType ac = new AbstractComponentType();
		ac.setIdAc(ac_id);
		ac.setName(name);
		ac.setSupertype(new AbstractComponentType());
		ac.getSupertype().setIdAc(supertype);
		ac.setKind(kind);
		return ac;		
	}

	public static AbstractComponentType generateAbstractComponentType(String name, int supertype, int parent, int kind){
		AbstractComponentType ac = new AbstractComponentType();
		ac.setName(name);
		ac.setSupertype(new AbstractComponentType());
		ac.getSupertype().setIdAc(supertype);
		ac.setKind(kind);
		return ac;
	}

	public static int addQualityFunction(String file){
		QualityFunctionType qf = new QualityFunctionType();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(br.ufc.storm.jaxb.ObjectFactory.class.getPackage().getName(),
					br.ufc.storm.jaxb.ObjectFactory.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<QualityFunctionType> element = (JAXBElement<QualityFunctionType>) unmarshaller.unmarshal(new InputSource(new java.io.StringReader(file)));
			qf = element.getValue();
		} catch (JAXBException e) {
			e.printStackTrace();
			return -1;
		}
		return QualityHandler.addQualityFunction(qf);
	}

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

	public static String listContract(int ac_id) throws DBHandlerException{
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
			throw new DBHandlerException(e1.getMessage());
		}
		
	}

	public static boolean addContextContractFromXML(String file) {
		// TODO Auto-generated method stub
		return false;
	}

	public static int addAbstractUnitFromXML(String cmp) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static String getInnerComponent(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public static int addConcreteUnit(String cmp) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static CandidateListType getList(File file) {
		// TODO Auto-generated method stub
		return null;
	}







}