

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.udojava.evalex.Expression;

import br.ufc.storm.backend.BackendHandler;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.XMLException;
import br.ufc.storm.export.FormalFormat;
import br.ufc.storm.io.FileHandler;
import br.ufc.storm.io.LogHandler;
import br.ufc.storm.jaxb.ConcreteUnitType;
import br.ufc.storm.jaxb.UnitFileType;
import br.ufc.storm.sql.AbstractComponentHandler;
import br.ufc.storm.sql.ConcreteUnitHandler;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.sql.DBHandler;
import br.ufc.storm.webservices.CoreServices;
import br.ufc.storm.xml.XMLHandler;

public class Main {
	public static void main(String[] args) throws IOException {
	
//		try {
//			List<ConcreteUnitType> cut = ConcreteUnitHandler.getConcreteUnits(50);
//			System.out.println(cut.get(0).getUId());
//		} catch (DBHandlerException e) {
//			// TODO Auto-generated block
//			e.printStackTrace();
//		}
		
		Expression e = new Expression("v0-v1");
		int i = 0;
			e.with("v"+i, new BigDecimal(5));
			i++;
			e.with("v"+i, new BigDecimal(2));
			//System.out.println(qft.getFunctionValue()+" ... "+qft.getFunctionArguments().get(0).getValue().getValue()+" ... "+qft.getFunctionArguments().get(1).getValue().getValue());
	
		System.out.println(e.eval());
		
		//try {
//			System.out.println(XMLHandler.getContextContract(126));
			//System.out.println(FormalFormat.exportContextContract(ContextContractHandler.getContextContract(126), null));
//			System.out.println(FormalFormat.exportComponentSignature(AbstractComponentHandler.getAbstractComponent(19), null));
//			System.out.println(XMLHandler.getAbstractComponent("MatrixMultiplication"));
		//} catch (Exception e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
	//	}
//		System.out.println(CoreServices.getContextContract(136));
		
		
		
		
		
//		List<UnitFileType> list;
//		try {
//			list = ConcreteUnitHandler.getUnitFiles(17);
//			System.out.println(list.size());
////			for(UnitFileType uf : list){
////				System.out.println(new String(FileHandler.getUnitFile(uf.getFileId())));
////			}
//		} catch (DBHandlerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			System.out.println(ContextContractHandler.listContract(1));
//		} catch (DBHandlerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
}
