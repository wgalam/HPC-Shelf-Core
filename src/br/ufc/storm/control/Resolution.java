package br.ufc.storm.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.FunctionException;
import br.ufc.storm.exception.ResolveException;
import br.ufc.storm.exception.ShelfException;
import br.ufc.storm.export.FormalFormat;
import br.ufc.storm.io.LogHandler;
import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.CalculatedArgumentType;
import br.ufc.storm.jaxb.CalculatedParameterType;
import br.ufc.storm.jaxb.CandidateListType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextArgumentValueType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.PlatformProfileType;
import br.ufc.storm.model.ArgumentTable;
import br.ufc.storm.model.MaxElement;
import br.ufc.storm.model.ResolutionNode;
import br.ufc.storm.sql.CalculatedArgumentHandler;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.sql.ContextParameterHandler;
import br.ufc.storm.sql.ResolutionHandler;
import br.ufc.storm.xml.XMLHandler;

public class Resolution{

	/**
	 * Only for tests purpose
	 * @param args
	 * @throws DBHandlerException
	 */
	public static final int EQUAL = 3;
	public static final int BIGGER = 4;
	public static final int SMALLER = 5;

	public static void main(String args[]) throws DBHandlerException{
		ContextContract cc = new ContextContract();
		cc.setCcName("Multiplicação de Matrizes do Wagner");
		cc.setOwnerId(1);
		cc.setAbstractComponent(new AbstractComponentType());
		cc.getAbstractComponent().setIdAc(313);//218
		cc.setPlatform(new PlatformProfileType());
		cc.getPlatform().setPlatformContract(new ContextContract());
		cc.getPlatform().getPlatformContract().setAbstractComponent(new AbstractComponentType());
		cc.getPlatform().getPlatformContract().getAbstractComponent().setIdAc(19);
		
		int x = 0; //Indice do argumento de plataforma
		int y = 0; //Indice do argumento de contexto
		
//[Argumento de Plataforma]Testando a filtragem por argumento de contexto (localizado em Fortaleza) (só a plataforma Sasquatch passa)
//		cc.getPlatform().getPlatformContract().getContextArguments().add(new ContextArgumentType());
//		cc.getPlatform().getPlatformContract().getContextArguments().get(x).setCpId(26);
//		cc.getPlatform().getPlatformContract().getContextArguments().get(x).setContextContract(new ContextContract());
//		cc.getPlatform().getPlatformContract().getContextArguments().get(x).getContextContract().setCcId(322);
//		x++;
		
				//[Argumento de Contexto] Definir o tipo de dado da matriz L
//				cc.getContextArguments().add(new ContextArgumentType());
//				cc.getContextArguments().get(y).setCpId(151);
//				cc.getContextArguments().get(y).setContextContract(new ContextContract());
//				cc.getContextArguments().get(y).getContextContract().setCcId(44);
//				y++;

		// Restringe GPU
//				cc.getPlatform().getPlatformContract().getContextArguments().add(new ContextArgumentType());
//				cc.getPlatform().getPlatformContract().getContextArguments().get(x).setCpId(101);
//				cc.getPlatform().getPlatformContract().getContextArguments().get(x).setContextContract(new ContextContract());
//				cc.getPlatform().getPlatformContract().getContextArguments().get(x).getContextContract().setCcId(184);
//				x++;

		
		// Restringe virtualização 
		//		cc.getPlatform().getPlatformContract().getContextArguments().add(new ContextArgumentType());
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(x).setCpId(35);
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(x).setContextContract(new ContextContract());
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(x).getContextContract().setCcId(175);
		//		x++;
		
		//Restringe a quantidade de nós
//		cc.getPlatform().getPlatformContract().getContextArguments().add(new ContextArgumentType());
//		cc.getPlatform().getPlatformContract().getContextArguments().get(x).setCpId(27);
//		cc.getPlatform().getPlatformContract().getContextArguments().get(x).setValue(new ContextArgumentValueType());
//		cc.getPlatform().getPlatformContract().getContextArguments().get(x).getValue().setDataType("Integer");
//		cc.getPlatform().getPlatformContract().getContextArguments().get(x).getValue().setValue("10");
//		x++;
//		//*******************************************************
//
//		ContextArgumentType arg0 = new ContextArgumentType();
//		arg0.setCpId(107);
//		ContextArgumentValueType cav = new ContextArgumentValueType();
//		cav.setValue("10");
//		arg0.setValue(cav);
//		cc.getContextArguments().add(arg0);
//		x++;

		System.out.println(XMLHandler.getContextContract(cc));
//		System.out.println(FormalFormat.exportContextContract(cc, null));
		//Testando a filtragem por parametro de qualidade (total de núcleos superior a este valor)
		//		CalculatedArgumentType cat = new CalculatedArgumentType();
		//		cat.setCpId(1);
		//		cat.setValue(1.0);
		//		cc.getPlatform().getPlatformContract().getQualityArguments().add(cat);

		//Testando a filtragem por parametro de custo (só o gpu passa)
		//				cat = new CalculatedArgumentType();
		//				cat.setCpId(112);
		//				cat.setValue(100.0);
		//				cc.getPlatform().getPlatformContract().getCostArguments().add(cat);


		//*******************************************************
		//------------------------------------------------------- Inners
		//		ContextContract inner1 = new ContextContract();
		//		inner1.setCcName("Aninhado do contrato da plataforma");
		//		AbstractComponentType innerAbstractComponent1 = new AbstractComponentType();
		//		innerAbstractComponent1.setIdAc(123);
		//		inner1.setAbstractComponent(innerAbstractComponent1);
		//		cc.getInnerComponents().add(inner1);


		System.out.println("Aguarde buscando componentes...");
		CandidateListType resolve = null;
		try {
			resolve = resolve(cc);
			//Reordena conforme o preset 3
			//			resolve = sortCandidateList(resolve, 3);
			int cont = 0;
			for(ContextContract a:resolve.getCandidate()){
				/*
				  	try {
						System.out.println(BackendHandler.instantiatePlatftorm(a.getPlatform()));
					} catch (ShelfRuntimeException e) {
						e.printStackTrace();
					}
				 */
				//			System.out.println("Candidato compatível ("+ cont++ +"): "+XMLHandler.getContextContract(a));

//				if(a.getCcId()==229){
////					System.out.println(FormalFormat.exportContextContract(a, null));
//					System.out.println("Candidato compatível ("+ cont++ +"): "+XMLHandler.getContextContract(a));
//				}
				//				###########18/07/2016
				System.out.println(FormalFormat.exportContextContract(a, null));
				//				###########
			}
			//						System.out.println("Último Candidato Compatível\n"+XMLHandler.getContextContract(resolve.getCandidate().get(resolve.getCandidate().size()-1)));
			//						System.out.println("Último Candidato Compatível\n"+FormalFormat.exportContextContract(resolve.getCandidate().get(resolve.getCandidate().size()-1), null));
		} catch (ResolveException e) {
			System.out.println("Erro enquanto estava resolvendo um contrato");
			e.printStackTrace();
		}
		System.out.println("\n\n\nSize of resolve: "+resolve.getCandidate().size());

		//		System.out.println("################### Start Deploy #####################");
		//		LogHandler.getLogger().info("Starting to deploy an application...");
		//		ComputationalSystemType ppt = null;
		//		try {
		//			ppt = BackendHandler.deployComponent(resolve);
		//				System.out.println("Deploy success");
		//		} catch (ShelfRuntimeException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		System.out.println("################### Start Instantiate #####################");
		//		try {
		//			String str = BackendHandler.instantiateComponent(ppt);
		//			System.out.println("Instantiate success\n"+str);
		//		} catch (ShelfRuntimeException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		
		//		LogHandler.close();
		////		System.out.println(XMLHandler.getComputationalSystem(ppt));
		//		BackendHandler.releasePlatform(ppt);
		//		




		/*ContextContract cc = new ContextContract();
		AbstractComponentType ac = new AbstractComponentType();
		ac.setIdAc(109);
		cc.setAbstractComponent(ac);
		ContextContract plat = new ContextContract();
		plat.setCcName("Plataforma do Wagner");
		plat.setAbstractComponent(new AbstractComponentType());
		plat.getAbstractComponent().setIdAc(19);
		cc.setPlatform(new PlatformProfileType());
		cc.getPlatform().setPlatformContract(plat);
		//------------------------------------------------------- Inners
		ContextContract inner1 = new ContextContract();
		inner1.setCcName("Aninhado do contrato da plataforma");
		AbstractComponentType innerAbstractComponent1 = new AbstractComponentType();
		innerAbstractComponent1.setIdAc(123);
		inner1.setAbstractComponent(innerAbstractComponent1);
		cc.getInnerComponents().add(inner1);
		System.out.println("Aguarde buscando componentes...");
		CandidateListType resolve;
		try {
			resolve = resolve(cc, null, null);
			int cont = 0;
			for(ContextContract a:resolve.getCandidate()){
				System.out.println("Candidato compatível ("+cont+++"): "+XMLHandler.getContextContract(a));
			}
		} catch (ResolveException e) {
			System.out.println("Erro enquanto estava resolvendo um contrato");
			e.printStackTrace();
		}*/
	}

	/**
	 * This method generates and returns a list of all components compatibles with contract
	 * @param application
	 * @param resolutionTree
	 * @param applicationPlatform
	 * @return
	 * @throws ResolveException 
	 */
	public static CandidateListType resolve(ContextContract application) throws ResolveException{
		long start = System.currentTimeMillis();
		LogHandler.getLogger().info("Resolution Class: Starting to resolve a component!");
		ResolutionNode.setup();
		Hashtable <Integer , Hashtable <Integer , ArgumentTable>> tableOfSWidArgumentTable = new Hashtable<Integer, Hashtable <Integer , ArgumentTable>>();
		try {
			ContextContractHandler.completeContextContract(application);
		} catch (DBHandlerException e1) {
			throw new ResolveException("Can not complete context contract data: ",e1);
		}
		CandidateListType candidateList = new CandidateListType();
		List<ContextContract> concreteComponentCandidatesList;
		LogHandler.getLogger().info("Generating Software Component Candidate List...");
		try {
			concreteComponentCandidatesList = ResolutionHandler.generateCandidates(application.getAbstractComponent().getSupertype().getIdAc(), application.getAbstractComponent().getIdAc());
		} catch (DBHandlerException e1) {
			return new CandidateListType();
		}
		LogHandler.getLogger().info(concreteComponentCandidatesList.size()+" components were found, now it will be filtered");
		ContextContract candidate;	
		for(int i = 0; i < concreteComponentCandidatesList.size(); i++){
			candidate = concreteComponentCandidatesList.get(i);
			try {
				if(componentSubTypeRecursiveTest(application, candidate, null, application.getAbstractComponent())){
					if(candidate.getPlatform() == null){
						//Will test the next software contract
						continue;
					}
					Resolution.mergeContract(application, candidate);						

					List<ContextContract> componentCandidatePlatformlist = null;
					LogHandler.getLogger().info("Generating Hardware Candidate List for candidate: "+i);
					try {
						componentCandidatePlatformlist = ResolutionHandler.generateCompliantPlatformCandidates(candidate.getPlatform().getPlatformContract().getAbstractComponent().getIdAc());
					} catch (DBHandlerException e3) {
						//Will test the next software contract
						continue;
					}
					
					LogHandler.getLogger().info(componentCandidatePlatformlist.size()+" platforms were found for software component "+i);
					if(componentCandidatePlatformlist.size() > 0){
						CandidateListType c = filterPlatformCandidateList(componentCandidatePlatformlist, application, candidate, tableOfSWidArgumentTable);
						
						candidateList.getCandidate().addAll(c.getCandidate());
						if(candidateList.getUserId()==null){
							candidateList.setUserId(c.getUserId());
						}
					}
				}
			} catch (DBHandlerException e) {
				//Will test the next software contract
				continue;
			}
		}

		candidateList.setUserId(application.getOwnerId());
		Resolution.rankCandidates(candidateList, tableOfSWidArgumentTable); //Rank all candidates
		Resolution.sortCandidateList(candidateList, 0);//Primeira função de ranqueamento
		long elapsed = System.currentTimeMillis() - start;
		elapsed/=1000;
		System.out.println("Resolution Time: "+(int)(elapsed/60)+" minutos e "+elapsed % 60+" segundos");
		return candidateList;
	}

	/**
	 * This method resolves inner componentes. 
	 * The main difference in relation to resolve method is in the fact that only one platform subtype test is required, and than acquiring performance
	 * @param application
	 * @return
	 * @throws ResolveException
	 */
	
	public static CandidateListType resolveInner(ContextContract application, Hashtable<Integer, Hashtable <Integer,ArgumentTable>> tableOfSWidArgumentTable ) throws ResolveException{
				
		CandidateListType candidateList = new CandidateListType();
		List<ContextContract> electedComponentList;
		
		try {
			electedComponentList = ResolutionHandler.generateCandidates(application.getAbstractComponent().getSupertype().getIdAc(), application.getAbstractComponent().getIdAc());
		} catch (DBHandlerException e1) {
			return new CandidateListType();
		}
		ContextContract candidate;	
		for(int i = 0; i < electedComponentList.size(); i++){
			candidate = electedComponentList.get(i);
			try {
				if(componentSubTypeRecursiveTest(application, candidate, null, application.getAbstractComponent())){
					if(candidate.getPlatform() == null){
						//Will test the next software contract
						continue;
					}
					Resolution.mergeContract(application, candidate);						

					if(isPlatformSubType(candidate.getPlatform().getPlatformContract(), application.getPlatform().getPlatformContract(), null)){
						ContextContract cct = XMLHandler.cloneContextContract(candidate);//Cloning component
						cct.getPlatform().setPlatformContract(XMLHandler.cloneContextContract(application.getPlatform().getPlatformContract()));
						ContextContract cc = resolveInnerComponents(candidate, candidateList, cct, tableOfSWidArgumentTable);
						if(cc!=null){
							candidateList.getCandidate().add(cc);
						}
					}
				}
			} catch (DBHandlerException e) {
				//Will test the next software contract
				continue;
			}
		}
		candidateList.setUserId(application.getOwnerId());
//		Resolution.rankCandidates(candidateList, tableOfSWidArgumentTable); //Rank all candidates
//		Resolution.sortCandidateList(candidateList, 0);//Primeira função de ranqueamento
		return candidateList;
	}

	private static CandidateListType filterPlatformCandidateList( List<ContextContract> componentCandidatePlatformlist, ContextContract application, ContextContract candidate, Hashtable <Integer , Hashtable <Integer , ArgumentTable>> tableOfSWidArgumentTable) {
		//TODO: Error filtering location
		CandidateListType candidateList = new CandidateListType();
		for(int i = 0; i < componentCandidatePlatformlist.size(); i++){
			ContextContract platform = componentCandidatePlatformlist.get(i);
			if(isPlatformSubType(candidate.getPlatform().getPlatformContract(), platform, null)){
				if(isPlatformSubType(application.getPlatform().getPlatformContract(), platform, null)){
					ContextContract cct = XMLHandler.cloneContextContract(candidate);//Cloning component
					cct.getPlatform().setPlatformContract(XMLHandler.cloneContextContract(platform));
					ContextContract cc = resolveInnerComponents(candidate, candidateList, cct, tableOfSWidArgumentTable);
					if(cc!=null){
						candidateList.getCandidate().add(cc);
					}
				}
			}
		}
//		System.out.println(candidateList.getCandidate().size());
		LogHandler.getLogger().info("Calculating arguments...");
		calculateCalculatedArguments(candidateList, application, tableOfSWidArgumentTable);
		return candidateList;
	}

	private static ContextContract resolveInnerComponents(ContextContract candidate, CandidateListType candidateList, ContextContract cct, Hashtable<Integer, Hashtable <Integer , ArgumentTable>> tableOfSWidArgumentTable){
		if(candidate.getInnerComponents().size() > 0){
			boolean fit = true;
			try{
				for(ContextContract cc: candidate.getInnerComponents()){//Para cada componente aninhado
					ContextContract applicationInner = XMLHandler.cloneContextContract(cc); //application inner o contrato do aninhado a ser resolvido

					if(applicationInner.getPlatform()==null){
						applicationInner.setPlatform(new PlatformProfileType());
					}
					//Define como perfil o perfil que já está sendo avaliado
					applicationInner.getPlatform().setPlatformContract(XMLHandler.cloneContextContract(cct.getPlatform().getPlatformContract()));//define-se como contrato da plataforma, a plataforma do candidato
					CandidateListType inners;
					try {
						inners = Resolution.resolveInner(applicationInner, tableOfSWidArgumentTable);
						if(inners.getCandidate().size()>0 ){
							cct.getInnerComponentsResolved().add(inners);
						}else{
							fit=false;
						}
					} catch (ResolveException e) {
						e.printStackTrace();
					}
				}
				if(fit){
					return cct;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			return cct;
		}
		return null;
	}

	public static void calculateCalculatedArguments(CandidateListType candidateList, ContextContract application, Hashtable <Integer , Hashtable <Integer , ArgumentTable>> tableOfSWidArgumentTable){
		Iterator<ContextContract> i = candidateList.getCandidate().iterator();
		while(i.hasNext()){
			ContextContract cc = (ContextContract) i.next();

			ArgumentTable argumentTable = new ArgumentTable(cc);//Store argument table of a platform
			if(tableOfSWidArgumentTable.get(cc.getCcId())==null){
				tableOfSWidArgumentTable.put(cc.getCcId(), new Hashtable<>());
			}
			tableOfSWidArgumentTable.get(cc.getCcId()).put(cc.getPlatform().getPlatformContract().getCcId(), argumentTable);
			//calcula parametros de qualidade
			try {
				CalculatedArgumentHandler.calulateContextContractArguments(cc, argumentTable, ContextParameterHandler.QUALITY);
			} catch (FunctionException e1) {
				//do nothing
			}
			try {
				if(Resolution.isSubTypeByCalculatedArgument(application, cc, ContextParameterHandler.QUALITY)){
					//calcula parametros de custo
					try {
						CalculatedArgumentHandler.calulateContextContractArguments(cc, argumentTable, ContextParameterHandler.COST);
					} catch (FunctionException e1) {
						//do nothing
					} 
					if(Resolution.isSubTypeByCalculatedArgument(application, cc, ContextParameterHandler.COST)){
						LogHandler.getLogger().info("Candidate "+cc.getCcName()+" added");
					}else{
						i.remove();
					}
				}else{
					i.remove();
				}
			} catch (ShelfException e) {
				e.printStackTrace();
			}
		}
	}




	/**
	 * This method tests recursively subtyping relation between platforms.
	 * @param componentRequiredPlatform
	 * @param platformCandidate
	 * @param cp_id
	 * @param resolutionTree
	 * @param applicationPlatformRequired
	 * @return
	 */

	public static boolean isPlatformSubType(ContextContract requiredPlatform, ContextContract candidatePlatform, Integer cp_id){
		try {
			boolean subtype = true;
			if(requiredPlatform.getCcId()==candidatePlatform.getCcId()){
				return true;
			}
			if(cp_id!=null){
				int candidateContractIdAc = candidatePlatform.getAbstractComponent().getIdAc(); //ID_ac candidato
				ResolutionNode supertype = ResolutionNode.resolutionTree.findNode(requiredPlatform.getAbstractComponent().getIdAc());
				if(supertype.findNode(candidateContractIdAc)== null){
					subtype = false;
				}
			}
			if(subtype){
				if(requiredPlatform.getContextArguments().size() > 0){
					for(ContextArgumentType cat:requiredPlatform.getContextArguments()){
						if(cat.getValue() == null){
							subtype = subtype && isPlatformSubType(cat.getContextContract(), getContextContractFromArgument(cat,candidatePlatform), cat.getCpId());
						}else{
							int kind = ContextParameterHandler.getKindFromContextParameter(cat.getCpId(), requiredPlatform.getAbstractComponent());
							subtype = subtype && isPlatformSubTypeNumeric(cat, getValueFromCandidateArgument(cat,candidatePlatform), kind);
						}
					}
				}
			}
			return subtype;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This method evaluate subtyping relation with numeric argument
	 * @param contract
	 * @param candidate
	 * @param kind
	 * @return
	 */
	public static boolean isPlatformSubTypeNumeric(ContextArgumentType contract, ContextArgumentType candidate, int kind){

		switch(kind){
		case EQUAL:
			//In this case, if both are equal, return true.
			if(contract.getValue().equals(candidate.getValue())){
				return true;
			}
			break;
		case BIGGER:
			//In this case a bigger or equal number is subtype
			if(Double.parseDouble(contract.getValue().getValue()) <= Double.parseDouble(candidate.getValue().getValue()) ){
				return true;
			}
			break;
		case SMALLER:
			//In this case a smaller number is subtype
			if(Double.parseDouble(contract.getValue().getValue()) >= Double.parseDouble(candidate.getValue().getValue()) ){
				return true;
			}
			break;
		}
		return false;
	}

	private static void mergeContract(ContextContract application, ContextContract candidate){
		for(ContextArgumentType acat: application.getContextArguments()){
			Integer i = findArgument(acat, candidate.getContextArguments());
			if(i!=null){
				candidate.getContextArguments().set(i, acat);

			}else{
				candidate.getContextArguments().add(acat);
			}	
		}		
	}

	private static Integer findArgument(ContextArgumentType acat, List <ContextArgumentType> list){
		ContextArgumentType ccat;
		for(int i = 0; i < list.size(); i++){
			ccat = list.get(i);
			if(acat.getCpId()==ccat.getCpId()){
				return i;
			}
		}
		return null;
	}

	/**
	 * This method receives a list of candidates and sort according its i rank parameter
	 * @param newCandidateList
	 * @param i
	 * @return 
	 */
	public static CandidateListType sortCandidateList(CandidateListType cl, int i) {
		Collections.sort (cl.getCandidate(), new ComparatorCandidates(false, i));
		return cl;
	}

	private static Hashtable <Integer , MaxElement> getMaximumValues(ContextContract cc, Hashtable <Integer , MaxElement> maximum){
		for(ContextArgumentType cat : cc.getContextArguments()){
			//se tiver valor faz o que segue
			if(cat.getValue()!=null){
				if(maximum.contains(cat.getCpId())){
					if(maximum.get(cat.getCpId()).getValue() < Double.parseDouble(cat.getValue().getValue())){
						maximum.get(cat.getCpId()).setValue(Double.parseDouble(cat.getValue().getValue()));
					}
				}else{
					maximum.put(cat.getCpId(), new MaxElement(Double.parseDouble(cat.getValue().getValue())));
				}
			}else{
				if(cat.getContextContract() != null){
					maximum.putAll(getMaximumValues(cat.getContextContract(), maximum));
					//Variável compartilhada, buscar valor compartilhado
				}
			}
		}

		for(CalculatedArgumentType cat : cc.getQualityArguments()){
			if(maximum.get(cat.getCpId()) != null){
				if(maximum.get(cat.getCpId()).getValue() < cat.getValue()){
					maximum.get(cat.getCpId()).setValue(cat.getValue());
				}
			}else{
				maximum.put(cat.getCpId(), new MaxElement(cat.getValue()));
			}
		}
		for(CalculatedArgumentType cat : cc.getCostArguments()){
			if(maximum.get(cat.getCpId()) != null){
				if(maximum.get(cat.getCpId()).getValue() < cat.getValue()){
					maximum.get(cat.getCpId()).setValue(cat.getValue());
				}
			}else{
				maximum.put(cat.getCpId(), new MaxElement(cat.getValue()));
			}
		}


		return maximum;
	}



	private static Hashtable <Integer , MaxElement> getMaximumValues(CandidateListType cl){
		Hashtable <Integer , MaxElement> maximum = new Hashtable<Integer, MaxElement>();
		for(ContextContract cc : cl.getCandidate()){
			maximum.putAll(getMaximumValues(cc, maximum));
			maximum.putAll(getMaximumValues(cc.getPlatform().getPlatformContract(), maximum));
		}

		//		para cada argumento, buscar o maior valor em todos os candidatos e manter na tabela.
		//		Será usado na normalização posteriormente

		return maximum;
	}

	public static CandidateListType rankCandidates(CandidateListType cl, Hashtable <Integer , Hashtable <Integer , ArgumentTable>> tableOfSWidArgumentTable){
		Hashtable <Integer , MaxElement> maximum = Resolution.getMaximumValues(cl);
		for(ContextContract cc:cl.getCandidate()){
			try {
				CalculatedArgumentHandler.calulateRankArguments(cc, tableOfSWidArgumentTable.get(cc.getCcId()).get(cc.getPlatform().getPlatformContract().getCcId()), maximum);
			} catch (FunctionException e) {
				// This occurs when the result of method get from hashtable is null
				e.printStackTrace();
			}




		}
		return cl;


	}

	/**
	 * 
	 * @param cc
	 * @param cp_id
	 * @return
	 */
	public static ContextArgumentType getArgumentRecursive(ContextContract cc, Integer cp_id){
		if(cc!=null){
			for(ContextArgumentType cat: cc.getContextArguments()){
				if(cat.getKind()==3 || cat.getKind()==4 || cat.getKind()==5){
					if(cat.getCpId() == cp_id){
						return cat;
					}
				}
			}
			for(ContextArgumentType cat: cc.getContextArguments()){
				if(cat.getKind()==1){
					return Resolution.getArgumentRecursive(cat.getContextContract(), cp_id);
				}
			}

			return null;
		}else{
			return null;
		}
	}

	/**
	 * 
	 * @param innerComponentId
	 * @param candidatePlatform
	 * @param applicationContract
	 * @return
	 */
	public static ContextContract getInnerComponenteContextContractAddingPlatform(int innerComponentId, ContextContract candidatePlatform, ContextContract applicationContract ){
		for(ContextContract candidate:applicationContract.getInnerComponents()){
			if(candidate.getCcId()==innerComponentId){
				candidate.getPlatform().setPlatformContract(candidatePlatform);
				return candidate;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param list
	 * @param innerComponent
	 * @return
	 * @throws DBHandlerException 
	 */
	public static List<ContextContract> applyInnerComponents(List<ContextContract> list, ContextContract innerComponent) throws DBHandlerException{
		for(ContextContract candidate:list){
			candidate.getInnerComponents().add(ContextContractHandler.getContextContract(innerComponent.getCcId()));
		}
		return list;
	}

	/**
	 * This method test if the second context contract is subtype of the first one.
	 * @param applicationContract
	 * @param candidate
	 * @param cp_id
	 * @param parentAc
	 * @param resolution
	 * @return
	 * @throws DBHandlerException 
	 */
	public static boolean componentSubTypeRecursiveTest(ContextContract applicationContract, ContextContract candidate, Integer cp_id, AbstractComponentType parentAc) throws DBHandlerException{
		boolean subtype = true;
		ContextContract bound;
		if(cp_id!=null){
			bound = getBoundFromContextParameter(parentAc, cp_id);
			int candidateContractIdAc = candidate.getAbstractComponent().getIdAc(); //ID_ac candidato
			ResolutionNode supertype = ResolutionNode.resolutionTree.findNode(applicationContract.getAbstractComponent().getIdAc());
			while(supertype.getAc_id() != bound.getAbstractComponent().getIdAc() && supertype.getAc_id() != 0 && supertype.getAc_id() != candidateContractIdAc){
				supertype = supertype.getSupertype();
			}
			if(supertype.getAc_id() != candidateContractIdAc){
				subtype = false;
			}
		}

		if(subtype){
			if(applicationContract.getContextArguments().size() > 0){
				for(ContextArgumentType cat:applicationContract.getContextArguments()){
					if(cat.getContextContract()!=null){
//						if(getContextContractFromArgument(cat,candidate)!=null){ //Tentando corrigir erro cp 151
							subtype = subtype && componentSubTypeRecursiveTest(cat.getContextContract(), getContextContractFromArgument(cat,candidate), cat.getCpId(), applicationContract.getAbstractComponent());
//						}else{
//							System.out.println(cat.getCpId()+"  >>> "+cat.getContextContract().getCcName());
//							throw new DBHandlerException("Error in validating resolving contract constrain with cp_id = "+cat.getCpId());
//						}
						
					}
				}
			}
		}
		return subtype;
	}

	/**
	 * This method tests recursively subtyping relation between platforms.
	 * @param candidate1step
	 * @param platformCandidate2step
	 * @param cp_id
	 * @param resolutionTree
	 * @return
	 * @throws ShelfException 
	 */
	public static boolean isSubTypeByCalculatedArgument(ContextContract application, ContextContract candidate, int calculatedArgumentType) throws ShelfException{
		ContextContract applicationPlatform = application.getPlatform().getPlatformContract();
		ContextContract platformCandidate = candidate.getPlatform().getPlatformContract();
		boolean subtype = true;
		if(applicationPlatform != null){
			List<CalculatedArgumentType> list = new ArrayList<>();
			List<CalculatedParameterType> parametersList = new ArrayList<>();
			switch (calculatedArgumentType) {
			case ContextParameterHandler.QUALITY:
				list.addAll(application.getQualityArguments());
				list.addAll(applicationPlatform.getQualityArguments());
				parametersList.addAll(application.getAbstractComponent().getQualityParameters());
				parametersList.addAll(applicationPlatform.getAbstractComponent().getQualityParameters());
				break;
			case ContextParameterHandler.COST:
				list.addAll(application.getCostArguments());
				list.addAll(applicationPlatform.getCostArguments());
				parametersList.addAll(application.getAbstractComponent().getCostParameters());
				parametersList.addAll(applicationPlatform.getAbstractComponent().getCostParameters());
				break;
			}
			if(list.size() > 0){
				for(CalculatedParameterType c: parametersList){
				}
				for(CalculatedArgumentType qat:list){
					CalculatedParameterType qpt = null;
					if(qat.getValue()!=null){
						for(CalculatedParameterType cp : parametersList){
							if(cp.getCalcId()==qat.getCpId()){
								qpt = cp;
							}
						}
						if(qpt==null||getCalculatedArgumentValueFromCandidateArgument(qat.getCpId(),platformCandidate, calculatedArgumentType) == null){
							subtype = false;
						}
						subtype = subtype && isSubTypeByCalculatedArgument(qat, getCalculatedArgumentValueFromCandidateArgument(qat.getCpId(),platformCandidate, calculatedArgumentType), qpt.getKindId(), calculatedArgumentType);
					}
				}
			}
		}

		return subtype;
	}


	/**
	 * 
	 * @param application
	 * @param candidate
	 * @param kind
	 * @return
	 */
	public static boolean isSubTypeByCalculatedArgument(CalculatedArgumentType application, CalculatedArgumentType candidate, int kind, int calculatedArgumentType){
		switch(kind){
		case EQUAL:
			//In this case, if both are equal, return true.
			if(application.getValue().equals(candidate.getValue())){
				return true;
			}
			break;
		case BIGGER:
			//In this case a bigger or equal number is subtype
			if(application.getValue() <= candidate.getValue() ){
				return true;
			}
			break;
		case SMALLER:
			//In this case a smaller number is subtype
			if(application.getValue() >= candidate.getValue()){
				return true;
			}
			break;
		}
		return false;
	}


	/**
	 * Given a context contract and a context argument, this method find the respectively context argument in the context contract
	 * @param cat_from_app_var
	 * @param candidate
	 * @return
	 */
	public static ContextContract getContextContractFromArgument(ContextArgumentType cat_from_app_var, ContextContract candidate){
		if(candidate.getContextArguments()!= null){
			for(ContextArgumentType cat: candidate.getContextArguments()){
				if(cat_from_app_var.getCpId().equals(cat.getCpId())){
					return cat.getContextContract();
				}
			}
		}
		return null;
	}

	/**
	 * This method find a specific value from a context argument
	 * @param cat
	 * @param candidate
	 * @return
	 */
	private static ContextArgumentType getValueFromCandidateArgument(ContextArgumentType cat, ContextContract candidate) {
		int cp_id = cat.getCpId();
		for(ContextArgumentType app_cat: candidate.getContextArguments()){
			if(app_cat.getCpId() == cp_id){
				return app_cat;
			}
		}
		return null;
	}


	private static CalculatedArgumentType getCalculatedArgumentValueFromCandidateArgument(int cp_id, ContextContract candidate, int type) throws ShelfException {
		List<CalculatedArgumentType> list;
		switch (type) {
		case ContextParameterHandler.QUALITY:
			list = candidate.getQualityArguments();
			break;
		case ContextParameterHandler.COST:
			list = candidate.getCostArguments();
			break;
		default:
			throw new ShelfException("Type of calculated parameter invalid: "+type);
		}
		for(CalculatedArgumentType app_cat: list){
			if(app_cat.getCpId() == cp_id){
				return app_cat;
			}
		}
		return null;
	}



	/**
	 * This method extract an argument from a context contract and an parameter id
	 * @param cc
	 * @param cp_id
	 * @return
	 */
	public static ContextArgumentType getArgument(ContextContract cc, Integer cp_id){
		for(ContextArgumentType cat: cc.getContextArguments()){
			if(cat.getCpId() == cp_id){
				return cat;
			}
		}
		return null;
	}

	/**
	 * This method returns a context parameter from an abstract component and a context parameter id
	 * @param ac
	 * @param cp_id
	 * @param resolutionTree 
	 * @return
	 * @throws DBHandlerException 
	 */
	public static ContextContract getBoundFromContextParameter(AbstractComponentType ac, int cp_id) throws DBHandlerException{
		for(ContextParameterType cp:ResolutionNode.resolutionTree.findNode(ac.getIdAc()).getCps()){
			if(cp.getCpId()==cp_id){
				return ContextContractHandler.getContextContract(cp.getBound().getCcId());
			}
		}
		return null;		
	}

}

@SuppressWarnings("rawtypes")
class ComparatorCandidates implements Comparator {  
	boolean crescente = true;  
	Integer i = null;

	public ComparatorCandidates(boolean crescente, Integer i) {  
		this.crescente = crescente;  
		this.i = i;
	}  

	public int compare(Object o1, Object o2) {  
		ContextContract p1 = (ContextContract) o1;  
		ContextContract p2 = (ContextContract) o2;  
		if (crescente) {  
			return p1.getRankingArguments().get(i).getValue() < p2.getRankingArguments().get(i).getValue() ? -1 : (p1.getRankingArguments().get(i).getValue() > p2.getRankingArguments().get(i).getValue() ? +1 : 0);  
		} else {  
			return p1.getRankingArguments().get(i).getValue() < p2.getRankingArguments().get(i).getValue() ? +1 : (p1.getRankingArguments().get(i).getValue() > p2.getRankingArguments().get(i).getValue() ? -1 : 0);  
		}  
	}  
}