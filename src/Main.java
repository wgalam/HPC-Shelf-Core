

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.AbstractUnitType;
import br.ufc.storm.jaxb.ConcreteUnitType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.QualityFunctionTermType;
import br.ufc.storm.jaxb.QualityFunctionType;
import br.ufc.storm.jaxb.QualityParameterType;
import br.ufc.storm.jaxb.SliceType;
import br.ufc.storm.jaxb.UnitFileType;
import br.ufc.storm.model.ResolutionNode;
import br.ufc.storm.sql.DBHandler;
import br.ufc.storm.sql.SliceHandler;
import br.ufc.storm.webservices.CoreServices;
import br.ufc.storm.xml.XMLHandler;
import br.ufc.storm.sql.ConcreteUnitHandler;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.sql.ContextParameterHandler;
import br.ufc.storm.control.FunctionHandler;
import br.ufc.storm.control.Resolution;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.StormException;
import br.ufc.storm.io.FileHandler;
import br.ufc.storm.io.LogHandler;

public class Main {
	public static void main(String[] args) throws IOException {
		
		LogHandler.doLog("Testando Log1");

		LogHandler.doLog("Testando Log2");
		
		LogHandler.doLog("Testando Log3");
		
		
		//		System.out.println(CoreServices.listContract(19));
		/*AbstractUnitType cut = UnitHandler.getAbstractUnit(25);
		System.out.println(cut.getAuId()+" Abstract component id: "+cut.getAcId()+" Abstract unit name: "+cut.getAuName());
		*/
		/*
		try {
			XMLHandler.addUnitFile(FileHandler.readFileAsString("unidade.xml").toString(), "TEste de conteudo de arquivo".getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/		
		
		
//		List<UnitFileType> list = DBHandler.getUnitFiles(16);
//		for(UnitFileType uf : list){
//			System.out.println(new String(UnitHandler.getUnitFile(uf.getFileId())));
//		}
//		
		//System.out.println(DBHandler.generateResolutionTree().findNode("root.Software.Solver").getAc_id());
//		System.out.println(XMLHandler.listComponent());
		
//		int sessionID = DBHandler.createSession(2);
//		try {
//			Thread.sleep(10002);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		DBHandler.getSessionTime(sessionID);
//		System.out.println(DBHandler.destroySession(sessionID));
		
		//DBHandler.getAbstractComponent(119);
		
//		AbstractComponentType ac = new AbstractComponentType();
//		ac.setName("Testando");
//		ac.setKind(1);
//		
//		ac.setSupertype(DBHandler.getAbstractComponent(95));
//		System.out.println(DBHandler.addAbstractComponent(ac));
//		
//		System.out.println(DBHandler.addContextParameter("teste", "storage", "Testanto", "A"));;
//	System.out.println(DBHandler.addContextContract("teste", "Testanto"));	

		
		
		
		

		
		
		
//		System.out.println(XMLHandler.getAbstractComponent("Cluster"));
		
		
		
		
		//System.out.println(DBHandler.getAbstractComponentID("root"));
		//System.out.println(DBHandler.getAbstractComponent(19).getQualityParameters().get(0).getName());
		
//		System.out.println(XMLHandler.getContextContract(cc));
//		System.out.println(cc.getContextArguments().size());
//			System.out.println(Resolution.getArgumentRecursive(cc, 33).getValue());
	
			
			
			
			
			//	System.out.println(root.toString());
//		System.out.println(XMLHandler.getAbstractComponent("TesteTrigiagonal"));
		
//		System.out.println(Resolution.toString(cc));
//		System.out.println(cc.getContextArguments().size());
//		for(ContextArgumentType cat: cc.getContextArguments()){
//			System.out.println(cat.getValue());
//		}
		
		
		
//		System.out.println(cc.getQualityArguments().get(0).getValue());
//		
		
		
		//ArrayList<QualityFunctionTermType> cps= DBHandler.getQualityParameter(1);
		//System.out.println(cps.get(0).getCpId()+"->"+cps.get(0).getOrder()+"###"+cps.get(1).getCpId()+"->"+cps.get(1).getOrder());
		
		//QualityFunctionType qf = DBHandler.getQualityFunction(1, 33);
		//System.out.println(qf.getFunctionName());
		
//		ArrayList<QualityParameterType> qp = DBHandler.getQualityParameters(19);
//		System.out.println(qp.get(1).getName()+qp.get(1).getQpId());
		
		
		//XMLHandler xml = new XMLHandler();
		//System.out.println(xml.getAbstractComponent("XEON").toString());
		//System.out.println(XMLHandler.getQualityFunction(6));
		
//		//List tst = DBHandler.getContextArgumentValue(cp_id, cc_id);
//		Object o  = DBHandler.getContextArgumentValue(15, 32);
//		System.out.println(o);
//		
	//	System.out.println(XMLHandler.getContextContract(33));
////		System.out.println(o.getClass().getName());
//		if(o instanceof ContextContract){
//			System.out.println(((ContextContract) o).getCcName());;
//		}else{
//			System.out.println((String)o);
//		}
		
//		Teste da importação de contrato contextual
//		ContextContract cc = DBHandler.getContextContract(21);
//		System.out.println(cc.getCcName());
//		System.out.println("Context Parameters: "+cc.getAbstractComponent().getContextParameter().size());
//		for(ContextParameterType cp : cc.getAbstractComponent().getContextParameter()){
//			System.out.println(cp.getName());
//		}
//		System.out.println("Context Arguments: "+cc.getContextArguments().size());
//		for(ContextArgumentType ca : cc.getContextArguments()){
//			System.out.println(ca.getContextContract().getCcName());
//		}
		
		//ResolutionNode tree = DBHandler.generateResolutionTree();
		//System.out.println(tree.toString());
		
		
		
//		List<ContextArgumentType> cas = DBHandler.getContextArguments("SparseLinearSystemSolver");
//		System.out.println(cas.size());
//		for(ContextArgumentType cat:cas){
//			System.out.println(cat.getContextContract().getCcName());
//			if(cat.getContextContract().getContextArguments().size()>0){
//				System.out.println("XXXXXXXXXXXXXXXXXXXX "+cat.getContextContract().getContextArguments().size());
//			}
//		}
		
		
		/*AbstractComponentType act = DBHandler.getAbstractComponent(38);
		System.out.println(act.getName()+act.getKind()+act.getIdAc()+act.getSupertype().getIdAc());
		System.out.println("///////////////////////////////////////////////////////");*/
//		System.out.println(DBHandler.getBoundID(cp_id));
		
		/*
		List<ContextParameterType> cp = DBHandler.getAllContextParameterFromAbstractComponent(42);
		for(ContextParameterType c: cp){
			System.out.println("Name: "+c.getName());
			System.out.println("ID: "+c.getCpId());
			System.out.println("Bound: "+c.getBound().getName());
		}*/
		
		
		
		
//		List<ContextContract> abs = XMLHandler.resolve(new File("context_contract_example.xml"));
//		System.out.println(abs.size());
		
		/*List<ContextParameterType> l = DBHandler.getContextParameter(1);
		for(ContextParameterType p: l){f
			System.out.println(p.getName());
		}*/
		
	/*	AbstractComponentTyp
		
		
//		List<ContextParameterType> t = DBHandler.getContextParameter(14);
//		for(ContextParameterType x: t){
//			System.out.println("AAAAA:"+x.getName());
//		}
		
//		x0  //limite superior
//		
//		    x1
//		    
//		         x3  
//		         
//		    x4          x5 //limite pedido
//		
	e ac = DBHandler.getAbstractComponent(1);
		System.out.println(ac.getName());
		for(ContextParameterType p: ac.getContextParameter()){
			System.out.println(p.getName());
			System.out.println(p.getBound().getContextParameter().size());
			if(p.getBound().getContextParameter().size()>0){
				System.out.println("Size kkk...: "+p.getBound().getContextParameter().size());
			}
		}*/

	
		
		
		
		/*AbstractComponentType x = new AbstractComponentType();
		x.setName("Wagner");
		x.setKind("0");
		x.setIdAc(15);
		
		XMLHandler.JavaToXML(x);*/
		//System.out.println(XMLHandler.listComponent());
//		System.out.println(XMLHandler.getContextParameters("OPTERON"));
		//System.out.println(DBHandler.getAbstractComponentName(23));
		
		//System.out.println(DBHandler.getContextContract(15).getCcName());
		/*List<ContextContract> list  = DBHandler.generateCandidates(1, 31);
		for(ContextContract i: list){
			System.out.println("Ahhhh: "+i.getCcName());
		}*/
		
/*		ArrayList<String> vec = new ArrayList<String>();
		vec.add("5");
		vec.add("2");
		vec.add("20");
		String f = "?^2+2*?+?";
		System.out.println(QualityParameter.evaluateQualityParameter(null,vec,f));
*/		
		
		/*AbstractComponentType act = new AbstractComponentType();
		act.setName("Joao");
		act.setKind("1");
		
		XMLHandler.JavaToXML(act);*/
		
		//XMLHandler.addAbstractComponentFromXML(new File ("blablabla2.xml"));
		
		//System.out.println(XMLHandler.getAbstractComponent("I7"));
		
		//####################################################################
		
		//System.out.println(DBHandler.getBoundValue(11));
		
		
		
//		Teste do resolve
//		ContextContract cc = new ContextContract();
//		AbstractComponentType ac = new AbstractComponentType();
//		ac.setIdAc(35);
//		cc.setAbstractComponent(ac);
//		AbstractComponentType ad = new AbstractComponentType();
//		ad.setIdAc(35);
//		cc.getAbstractComponent().setSupertype(ad);
//		List<ContextContract> resolve = DBHandler.resolve(cc);
//		System.out.println(resolve.size());
//		for(ContextContract a:resolve){
//			System.out.println("Candidato: "+a.getAbstractComponent().getName());
//		}
//					
		
		
		
		
		
		
//		List<ContextParameterType> t = DBHandler.getContextParameter(14);
//		for(ContextParameterType x: t){
//			System.out.println("AAAAA:"+x.getName());
//		}
		
//		x0  //limite superior
//		
//		    x1
//		    
//		         x3  
//		         
//		    x4          x5 //limite pedido
//		
	
		
	}
}
