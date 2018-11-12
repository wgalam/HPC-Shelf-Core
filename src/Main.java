

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.ws.axis2.FakeEndServicesStub;
import org.apache.ws.axis2.FakeEndServicesStub.DeploycallBack;
import org.apache.ws.axis2.FakeEndServicesStub.DeploycallBackResponse;

import com.udojava.evalex.Expression;

import br.ufc.storm.backend.BackendHandler;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.ResolveException;
import br.ufc.storm.exception.ShelfRuntimeException;
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
import br.ufc.storm.sql.PlatformHandler;
import br.ufc.storm.webservices.CoreServices;
import br.ufc.storm.xml.XMLHandler;

public class Main {
//	static final String server = "http://http://52.88.89.46/:8080/axis2/services/CoreServices.CoreServicesHttpSoap12Endpoint/";
	public static void main(String[] args) throws IOException {
		

		try {
			int ac = 1;
			int cc = 315;
			int cc2 = 126;
			//System.out.println(FormalFormat.exportContextContractWithIDs(ContextContractHandler.getContextContract(cc), null));
			Expression expression = new Expression("((((100.0*100.0*100.0)/2.0)*4.0)/(320.4*10.0^9.0))+(((100.0*100.0*100.0)/2.0)*8.0*(10.0*10.0^-9.0))+0.000009588");
			expression.setPrecision(20);
			System.out.println(expression.eval().doubleValue());
			
			
			
			
			
			
			//			System.out.println();
//			System.out.println(FormalFormat.exportContextContractWithIDs(ContextContractHandler.getContextContract(cc2), null));
//			System.out.println();
//			System.out.println(FormalFormat.exportComponentSignatureWithIDs(AbstractComponentHandler.getAbstractComponent(ac), null));
			//System.out.println(FormalFormat.exportCandyComponentSignature(AbstractComponentHandler.getAbstractComponent(19), ""));;
//			System.out.println(XMLHandler.getContextContract(ContextContractHandler.getContextContract(cc))+"\n");
			
			//200.19.177.89

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
	public static boolean testaFake(int session, String uri) throws ShelfRuntimeException{
		FakeEndServicesStub stub = null;
		try {
			stub = new FakeEndServicesStub();
		} catch (AxisFault e1) {
			e1.printStackTrace();
		}
		//Cria a requisicao para o servico
		DeploycallBack request;
		request = new DeploycallBack();
		request.setProfile_id(uri); 
		request.setSessionID(session);
		//Invoca o servico
		DeploycallBackResponse response = null;
		try {
			response = stub.deploycallBack(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.get_return();
	}
}
