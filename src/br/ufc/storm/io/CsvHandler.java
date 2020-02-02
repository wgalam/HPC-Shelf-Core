package br.ufc.storm.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.export.FormalFormat;
import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextArgumentValueType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.sql.AbstractComponentHandler;
import br.ufc.storm.sql.ContextParameterHandler;
import br.ufc.storm.sql.DBHandler;
import br.ufc.storm.xml.XMLHandler;

public class CsvHandler {

	public static void main(String [] args){
		importCSVNodes("teste.csv");
	}
	
	public static void importCSVNodes(String filename){
		Reader in;
		ArrayList<ContextContract> list = new ArrayList<>();
		try {
			in = new FileReader(filename);
			Iterable<CSVRecord> records = CSVFormat.EXCEL.withDelimiter(';').withHeader().parse(in);
			for (CSVRecord record : records) {
				ContextContract cc = new ContextContract();
				cc.setCcName(record.get("cc_name"));
				try {
					cc.setAbstractComponent(AbstractComponentHandler.getAbstractComponent(25, false));
				} catch (DBHandlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				addArgument(cc, "available_cores", record.get("available_cores"));
				addArgument(cc, "num_of_processors", record.get("num_of_processors"));
				//###############################
//				addArgument(cc, "node_processor", record.get("node_processor"));
				ContextContract node_processor = new ContextContract();
				node_processor.setAbstractComponent(AbstractComponentHandler.getAbstractComponent(3, false));
				node_processor.setCcName(record.get("node_processor"));
				addArgument(node_processor, "processor_L3_cache_size", record.get("processor_L3_cache_size"));
				addArgument(node_processor, "processor_L2_cache_size", record.get("processor_L2_cache_size"));
				addArgument(node_processor, "processor_L1_cache_size", record.get("processor_L1_cache_size"));
				addArgument(node_processor, "processor_core_clock", record.get("processor_core_clock"));
				addArgument(node_processor, "processor_family", record.get("processor_family"));
				addArgument(node_processor, "processor_manufacturer", record.get("processor_manufacturer"));
				addArgument(node_processor, " processor_architecture", record.get(" processor_architecture"));
				addArgument(node_processor, "processor_cores", record.get("processor_cores"));
				addArgument(node_processor, "processor_performance", record.get("processor_performance"));
				addArgument(node_processor, "processor_threads", record.get("processor_threads"));
				addArgument(node_processor, "cost_per_core", record.get("cost_per_core"));
				addArgument(node_processor, "energy_consumption_per_processor", record.get("energy_consumption_per_processor"));
				addArgument(cc, "node_processor", node_processor);
				//###############################
				addArgument(cc, "num_of_acelerator", record.get("num_of_acelerator"));
				//###############################
				ContextContract node_accelerator = new ContextContract();
				node_accelerator.setAbstractComponent(AbstractComponentHandler.getAbstractComponent(7, false));
				node_accelerator.setCcName(record.get("node_accelerator"));
				addArgument(node_accelerator, "accelerator_model", record.get("accelerator_model"));
				addArgument(node_accelerator, "accelerator_memory", record.get("accelerator_memory"));
				addArgument(node_accelerator, "accelerator_manufacturer", record.get("accelerator_manufacturer"));
				addArgument(cc, "node_accelerator", node_accelerator);
				//###############################
				
				
				addArgument(cc, "memory_size", record.get("memory_size"));
				addArgument(cc, "memory_throughput", record.get("memory_throughput"));
				addArgument(cc, "memory_cost", record.get("memory_cost"));
				//###############################
				ContextContract node_storage = new ContextContract();
				node_storage.setAbstractComponent(AbstractComponentHandler.getAbstractComponent(329, false));
				node_storage.setCcName(record.get("node_storage"));
				addArgument(node_storage, "local_disk_size", record.get("local_disk_size"));
				addArgument(node_storage, "local_disk_size_max", record.get("local_disk_size_max"));
				addArgument(node_storage, "local_disk_throughput", record.get("local_disk_throughput"));
				addArgument(cc, "node_storage", node_storage);
				//###############################

				
				addArgument(cc, "operating_system", record.get("operating_system"));
				addArgument(cc, "node_cost", record.get("node_cost"));
				addArgument(cc, "node_network_throughput", record.get("node_network_throughput"));
				System.out.println(FormalFormat.exportContextContract(cc, null));
//				System.out.println(XMLHandler.getContextContract(cc));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addArgument(ContextContract cc, String cp_name, String cp_value){
		try{
			ContextArgumentType t = new ContextArgumentType();
			Integer i = ContextParameterHandler.findCpId(cc.getAbstractComponent(), cp_name);
			if(i==null){
				System.out.println("Context Parameter "+cp_name+" missing in abstract component");
			}
			t.setCpId(i);
			ContextArgumentValueType cavt = new ContextArgumentValueType();
			cavt.setValue(cp_value);
			t.setValue(cavt);
			cc.getContextArguments().add(t);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addArgument(ContextContract cc, String cp_name, ContextContract cc_parameter){
		try{
			ContextArgumentType t = new ContextArgumentType();
			Integer i = ContextParameterHandler.findCpId(cc.getAbstractComponent(), cp_name);
			if(i==null){
				System.out.println("Context Parameter "+cp_name+" missing in abstract component");
			}
			t.setCpId(i);
			t.setContextContract(cc_parameter);
			cc.getContextArguments().add(t);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean addPlatform(ContextContract cc){
		//BEGIN PROCESSOR
		ContextContract processor = new ContextContract();
		processor.setCcName("E5-2650v3");
		processor.setAbstractComponent(new AbstractComponentType());
		processor.getAbstractComponent().setName("XEON");
		processor.getContextArguments().add(new ContextArgumentType());
		processor.getContextArguments().get(0).setCpId(54);
		processor.getContextArguments().get(0).setValue(new ContextArgumentValueType());
		processor.getContextArguments().get(0).getValue().setValue("20");
		processor.getContextArguments().get(0).getValue().setDataType("Integer");
		processor.getContextArguments().add(new ContextArgumentType());
		processor.getContextArguments().get(1).setCpId(33);
		processor.getContextArguments().get(1).setValue(new ContextArgumentValueType());
		processor.getContextArguments().get(1).getValue().setValue("10");
		processor.getContextArguments().get(1).getValue().setDataType("Integer");
		processor.getContextArguments().add(new ContextArgumentType());
		processor.getContextArguments().get(2).setCpId(42);
		processor.getContextArguments().get(2).setValue(new ContextArgumentValueType());
		processor.getContextArguments().get(2).getValue().setValue("2300");
		processor.getContextArguments().get(2).getValue().setDataType("Integer");
		processor.getContextArguments().add(new ContextArgumentType());
		processor.getContextArguments().get(3).setCpId(43);
		processor.getContextArguments().get(3).setValue(new ContextArgumentValueType());
		processor.getContextArguments().get(3).getValue().setValue("640");
		processor.getContextArguments().get(3).getValue().setDataType("Integer");
		processor.getContextArguments().add(new ContextArgumentType());
		processor.getContextArguments().get(4).setCpId(44);
		processor.getContextArguments().get(4).setValue(new ContextArgumentValueType());
		processor.getContextArguments().get(4).getValue().setValue("2560");
		processor.getContextArguments().get(4).getValue().setDataType("Integer");
		processor.getContextArguments().add(new ContextArgumentType());
		processor.getContextArguments().get(5).setCpId(45);
		processor.getContextArguments().get(5).setValue(new ContextArgumentValueType());
		processor.getContextArguments().get(5).getValue().setValue("25600");
		processor.getContextArguments().get(5).getValue().setDataType("Integer");
		processor.getContextArguments().add(new ContextArgumentType());
		processor.getContextArguments().get(6).setCpId(28);
		processor.getContextArguments().get(6).setContextContract(new ContextContract());
		processor.getContextArguments().get(6).getContextContract().setCcName("Intel");
		//END PROCESSOR

		//BEGIN NODE
		ContextContract node = new ContextContract();
		node.setCcName("MDCC-CPU-Node");
		node.setAbstractComponent(new AbstractComponentType());
		node.getAbstractComponent().setName("Node");
		node.getContextArguments().add(new ContextArgumentType());
		node.getContextArguments().get(0).setCpId(28);
		node.getContextArguments().get(0).setContextContract(processor);
		node.getContextArguments().add(new ContextArgumentType());
		node.getContextArguments().get(1).setCpId(49);
		node.getContextArguments().get(1).setContextContract(new ContextContract());
		node.getContextArguments().get(1).getContextContract().setCcId(234);
		node.getContextArguments().add(new ContextArgumentType());
		node.getContextArguments().get(2).setCpId(32);
		node.getContextArguments().get(2).setValue(new ContextArgumentValueType());
		node.getContextArguments().get(2).getValue().setValue("2");
		node.getContextArguments().get(2).getValue().setDataType("Integer");
		node.getContextArguments().add(new ContextArgumentType());
		node.getContextArguments().get(3).setCpId(38);
		node.getContextArguments().get(3).setValue(new ContextArgumentValueType());
		node.getContextArguments().get(3).getValue().setValue("2048");
		node.getContextArguments().get(3).getValue().setDataType("Integer");
		node.getContextArguments().add(new ContextArgumentType());
		node.getContextArguments().get(4).setCpId(36);
		node.getContextArguments().get(4).setValue(new ContextArgumentValueType());
		node.getContextArguments().get(4).getValue().setValue("64");
		node.getContextArguments().get(4).getValue().setDataType("Integer");
		//					node.getContextArguments().add(new ContextArgumentType());
		//					node.getContextArguments().get(5).setCpId(101);
		//					node.getContextArguments().get(5).setContextContract(new ContextContract());
		//					node.getContextArguments().get(5).getContextContract().setCcId(185);
		//END NODE


		//BEGIN INTERCONNECT
		ContextContract interconnect = new ContextContract();
		interconnect.setCcName("MDCC-Interconnect");
		interconnect.setAbstractComponent(new AbstractComponentType());
		interconnect.getAbstractComponent().setName("GigabitEthernet");
		interconnect.getContextArguments().add(new ContextArgumentType());
		interconnect.getContextArguments().get(0).setCpId(98);
		interconnect.getContextArguments().get(0).setValue(new ContextArgumentValueType());
		interconnect.getContextArguments().get(0).getValue().setValue("5");
		interconnect.getContextArguments().get(0).getValue().setDataType("Integer");
		interconnect.getContextArguments().add(new ContextArgumentType());
		interconnect.getContextArguments().get(1).setCpId(99);
		interconnect.getContextArguments().get(1).setValue(new ContextArgumentValueType());
		interconnect.getContextArguments().get(1).getValue().setValue("6.6");
		interconnect.getContextArguments().get(1).getValue().setDataType("Float");
		interconnect.getContextArguments().add(new ContextArgumentType());
		interconnect.getContextArguments().get(2).setCpId(97);
		interconnect.getContextArguments().get(2).setContextContract(new ContextContract());
		interconnect.getContextArguments().get(2).getContextContract().setCcName("Star");
		interconnect.getContextArguments().get(2).getContextContract().setAbstractComponent(new AbstractComponentType());
		interconnect.getContextArguments().get(2).getContextContract().getAbstractComponent().setName("Star");
		//END INTERCONNECT

		//BEGIN CLUSTER
		ContextContract ccc = new ContextContract();
		ccc.setCcName("MDCC-CPU-Large");
		ccc.setOwnerId(4);
		ccc.setAbstractComponent(new AbstractComponentType());
		ccc.getAbstractComponent().setName("Cluster");
		ccc.getContextArguments().add(new ContextArgumentType());
		ccc.getContextArguments().get(0).setCpId(23);
		ccc.getContextArguments().get(0).setContextContract(node);
		ccc.getContextArguments().add(new ContextArgumentType());
		ccc.getContextArguments().get(1).setCpId(24);
		ccc.getContextArguments().get(1).setContextContract(interconnect);
		ccc.getContextArguments().add(new ContextArgumentType());
		ccc.getContextArguments().get(2).setCpId(26);
		ccc.getContextArguments().get(2).setContextContract(new ContextContract());
		ccc.getContextArguments().get(2).getContextContract().setCcId(144);
		ccc.getContextArguments().add(new ContextArgumentType());
		ccc.getContextArguments().get(3).setCpId(27);
		ccc.getContextArguments().get(3).setValue(new ContextArgumentValueType());
		ccc.getContextArguments().get(3).getValue().setValue("8");
		ccc.getContextArguments().get(3).getValue().setDataType("Double");
		ccc.getContextArguments().add(new ContextArgumentType());
		ccc.getContextArguments().get(4).setCpId(35);
		ccc.getContextArguments().get(4).setContextContract(new ContextContract());
		ccc.getContextArguments().get(4).getContextContract().setCcId(172);


		try {
			DBHandler.getConnection().setAutoCommit(false);
//			addPlatformContextContract(ccc);
			DBHandler.getConnection().commit();
			DBHandler.getConnection().setAutoCommit(true);

			System.out.println(XMLHandler.getContextContract(cc));
			FileHandler.toHardDisk("/home/wagner/log_shelf.log", XMLHandler.getContextContract(cc).getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		}
		return true;
	}
}
