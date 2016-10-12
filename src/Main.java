

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.udojava.evalex.Expression;

import br.ufc.storm.backend.BackendHandler;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.ResolveException;
import br.ufc.storm.exception.XMLException;
import br.ufc.storm.export.FormalFormat;
import br.ufc.storm.io.FileHandler;
import br.ufc.storm.io.LogHandler;
import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.ConcreteUnitType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextArgumentValueType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.PlatformProfileType;
import br.ufc.storm.jaxb.UnitFileType;
import br.ufc.storm.sql.AbstractComponentHandler;
import br.ufc.storm.sql.ConcreteUnitHandler;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.sql.DBHandler;
import br.ufc.storm.webservices.CoreServices;
import br.ufc.storm.xml.XMLHandler;

public class Main {
	public static void main(String[] args) throws IOException {
	
		try {
			int ac = 1;
			int cc = 229;
			int cc2 = 126;
//			System.out.println(FormalFormat.exportContextContractWithIDs(ContextContractHandler.getContextContract(cc), null));
//			System.out.println();
//			System.out.println(FormalFormat.exportContextContractWithIDs(ContextContractHandler.getContextContract(cc2), null));
//			System.out.println();
//			System.out.println(FormalFormat.exportComponentSignatureWithIDs(AbstractComponentHandler.getAbstractComponent(ac), null));
			
			System.out.println(XMLHandler.getContextContract(ContextContractHandler.getContextContract(cc))+"\n");
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		try {
//			System.out.println(XMLHandler.listComponent());
//		} catch (XMLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

	}
}
