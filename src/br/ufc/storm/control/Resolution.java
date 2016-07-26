package br.ufc.storm.control;

import java.util.Hashtable;
import java.util.List;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.FunctionException;
import br.ufc.storm.exception.ResolveException;
import br.ufc.storm.exception.ShelfException;
import br.ufc.storm.export.FormalFormat;
import br.ufc.storm.io.FileHandler;
import br.ufc.storm.io.LogHandler;
import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.CalculatedArgumentType;
import br.ufc.storm.jaxb.CalculatedParameterType;
import br.ufc.storm.jaxb.CandidateListType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.CostArgumentType;
import br.ufc.storm.jaxb.CostParameterType;
import br.ufc.storm.jaxb.PlatformProfileType;
import br.ufc.storm.jaxb.QualityArgumentType;
import br.ufc.storm.jaxb.QualityParameterType;
import br.ufc.storm.model.ArgumentTable;
import br.ufc.storm.model.ResolutionNode;
import br.ufc.storm.sql.CalculatedArgumentHandler;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.sql.ResolutionHandler;
import br.ufc.storm.xml.XMLHandler;

public class Resolution{

	static ResolutionNode resolutionTree = null;
	/**
	 * Only for tests purpose
	 * @param args
	 * @throws DBHandlerException
	 */
	public static void main(String args[]) throws DBHandlerException{

		ContextContract cc = new ContextContract();
		AbstractComponentType ac = new AbstractComponentType();
		ac.setIdAc(161);
		cc.setAbstractComponent(ac);
		ContextContract plat = new ContextContract();
		plat.setCcName("Plataforma do Wagner");
		plat.setAbstractComponent(new AbstractComponentType());
		plat.getAbstractComponent().setIdAc(19);
		cc.setPlatform(new PlatformProfileType());
		cc.getPlatform().setPlatformContract(plat);
		cc.setOwnerId(1);

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
			resolve = resolve(cc, null);
			System.out.println("Size of resolve: "+resolve.getCandidate().size());
			int cont = 0;
			for(ContextContract a:resolve.getCandidate()){
				//				try {
				//					System.out.println(BackendHandler.instantiatePlatftorm(a.getPlatform()));
				//				} catch (ShelfRuntimeException e) {
				//					// TODO Auto-generated catch block
				//					e.printStackTrace();
				//				}
				//System.out.println("Candidato compatível ("+ cont++ +"): "+XMLHandler.getContextContract(a));
				//System.out.println(FormalFormat.exportContextContract(a.getPlatform().getPlatformContract(), null));
			}
			//System.out.println("Último Candidato Compatível\n"+XMLHandler.getContextContract(resolve.getCandidate().get(resolve.getCandidate().size()-1)));
			System.out.println("Último Candidato Compatível\n"+FormalFormat.exportContextContract(resolve.getCandidate().get(resolve.getCandidate().size()-1).getPlatform().getPlatformContract(), null));
		} catch (ResolveException e) {
			System.out.println("Erro enquanto estava resolvendo um contrato");
			e.printStackTrace();
		}

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
	public static CandidateListType resolve(ContextContract application, ContextContract applicationPlatform) throws ResolveException{
		LogHandler.getLogger().info("Resolution Class: Starting to resolve a component!");
		int calculated;
		if(resolutionTree == null){
			try {
				resolutionTree = ResolutionHandler.generateResolutionTree();
			} catch (DBHandlerException e) {
				throw new ResolveException("Can not create resolution tree: ",e);
			}
		}
		try {
			ContextContractHandler.completeContextContract(application);
		} catch (DBHandlerException e1) {
			throw new ResolveException("Can not complete context contract data: ",e1);
		}
		CandidateListType candidateList = new CandidateListType();
		CandidateListType newCandidateList = new CandidateListType();
		List<ContextContract> concreteComponentCandidatesList;
		LogHandler.getLogger().info("Generating Software Component Candidate List...");
		try {
			concreteComponentCandidatesList = ResolutionHandler.generateCandidates(application.getAbstractComponent().getSupertype().getIdAc(), application.getAbstractComponent().getIdAc(), resolutionTree);
		} catch (DBHandlerException e1) {
			return new CandidateListType();
		}
		LogHandler.getLogger().info(concreteComponentCandidatesList.size()+" components were found, now it will be filtered");
		int index = 0;
		if(concreteComponentCandidatesList.size() > 0){
			for(ContextContract candidate:concreteComponentCandidatesList){
				try {
					if(componentSubTypeRecursiveTest(application, candidate, null, application.getAbstractComponent(),resolutionTree)){
						if(candidate.getPlatform() == null){
							//Will test the next software contract
							continue;
						}
						List<ContextContract> componentCandidatePlatformlist = null;
						LogHandler.getLogger().info("Generating Hardware Candidate List for candidate: "+index);
						try {
							//							System.out.println(candidate.getPlatform().getPlatformContract().getCcName());
							componentCandidatePlatformlist = ResolutionHandler.generateCompliantPlatformCandidates(candidate.getPlatform().getPlatformContract().getAbstractComponent().getIdAc(), resolutionTree);
						} catch (DBHandlerException e3) {
							//Will test the next software contract
							continue;
						}
						LogHandler.getLogger().info(componentCandidatePlatformlist.size()+" platforms were found for software component "+index);
						if(componentCandidatePlatformlist.size() > 0){
							for(int i = 0; i < componentCandidatePlatformlist.size(); i++){
								//								System.out.println(componentCandidatePlatformlist.size());
								ContextContract platform = componentCandidatePlatformlist.get(i);
								if(applicationPlatform!=null){
									candidate.getPlatform().setPlatformContract(applicationPlatform);
									application.getPlatform().setPlatformContract(applicationPlatform);
								}
								if(isPlatformSubTypeTest(candidate.getPlatform().getPlatformContract(), platform, null,resolutionTree, applicationPlatform)){
									if(isPlatformSubTypeTest(application.getPlatform().getPlatformContract(), platform, null, resolutionTree, applicationPlatform)){
										ContextContract cct = XMLHandler.cloneContextContract(candidate);//Cloning component
										cct.getPlatform().setPlatformContract(XMLHandler.cloneContextContract(platform));
										if(application.getInnerComponents().size() > 0){
											boolean fit = true;
											for(int x = 0; x < application.getInnerComponents().size(); x++ ){
												ContextContract applicationInner = XMLHandler.cloneContextContract(application.getInnerComponents().get(x));
												applicationInner.getPlatform().setPlatformContract(XMLHandler.cloneContextContract(platform));
												CandidateListType inners = Resolution.resolve(applicationInner.getPlatform().getPlatformContract(), XMLHandler.cloneContextContract(platform));
												if(inners.getCandidate().size()>0 ){
													cct.getInnerComponentsResolved().add(inners);
												}else{
													fit=false;
												}
											}
											if(fit){
												candidateList.getCandidate().add(cct);
											}
										}else{
											candidateList.getCandidate().add(cct);
										}
									}
								}
							}
							//partialComponentList has a list with all candidate components passed in first filter
							//----------------------------------------------------------------------------------------------------------------------------------------------
							LogHandler.getLogger().info("Calculating arguments...");
							for(ContextContract cc:candidateList.getCandidate()){

								ArgumentTable argumentTable = new ArgumentTable(cc.getPlatform().getPlatformContract());//Store argument table of a platform

								/*if(cc.getPlatform().getPlatformContract().getCcId()==127){
									System.out.println(argumentTable.toString());
								}*/

								//calcula parametros de qualidade
								try {
									
									calculated = FunctionHandler.calulateContextContractArguments(cc.getPlatform().getPlatformContract(), resolutionTree, argumentTable, 1);
									//FunctionHandler.calulateContextContractQualityArguments(cc.getPlatform().getPlatformContract(), resolutionTree);
								} catch (FunctionException e2) {
									//do nothing
								} 
								try {
									if(Resolution.isSubTypeByCalculatedArgument(application.getPlatform().getPlatformContract(), cc.getPlatform().getPlatformContract(), null, resolutionTree, 1)){
										//calcula parametros de custo
										try {
											FunctionHandler.calulateContextContractArguments(cc.getPlatform().getPlatformContract(), resolutionTree, argumentTable, 2);
											//										FunctionHandler.calulateContextContractCostArguments(cc.getPlatform().getPlatformContract(), resolutionTree);
										} catch (FunctionException e1) {
											//do nothing
										} 
										if(Resolution.isSubTypeByCalculatedArgument(application.getPlatform().getPlatformContract(), cc.getPlatform().getPlatformContract(), null, resolutionTree, 2)){
											//calcula parametros de ranqueamento
											try {
												FunctionHandler.calulateContextContractArguments(cc.getPlatform().getPlatformContract(), resolutionTree,argumentTable, 3);
												//											FunctionHandler.calulateContextContractRankingArguments(cc.getPlatform().getPlatformContract(), resolutionTree);
											} catch (FunctionException e) {
												//do nothing
											} 
											newCandidateList.getCandidate().add(cc);
											LogHandler.getLogger().info("Candidate "+cc.getCcName()+" added");
										}
									}
								} catch (ShelfException e) {
									e.printStackTrace();
								}
							}
						}
					}
				} catch (DBHandlerException e) {
					//Will test the next software contract
					continue;
				}
				index++;
			}
		}
		//Excluir somente teste
		//		newCandidateList.getCandidate().addAll(candidateList.getCandidate());
		//
		newCandidateList.setUserId(application.getOwnerId());
		return newCandidateList;
	}

	/*
	 * 
	 * 
	 public static CandidateListType resolve(ContextContract application, ResolutionNode resolutionTree, ContextContract applicationPlatform) throws ResolveException{
		LogHandler.getLogger().info("Resolution Class: Starting to resolve a component!");
		if(resolutionTree==null){
			try {
				resolutionTree = ResolutionHandler.generateResolutionTree();
			} catch (DBHandlerException e) {
				throw new ResolveException("Can not create resolution tree: ",e);
			}
		}
		try {
			ContextContractHandler.completeContextContract(application);
		} catch (DBHandlerException e1) {
			throw new ResolveException("Can not complete context contract data: ",e1);
		}
		CandidateListType candidateList = new CandidateListType();
		CandidateListType newCandidateList = new CandidateListType();
		List<ContextContract> concreteComponentCandidatesList;
		LogHandler.getLogger().info("Generating Software Component Candidate List...");
		try {
			concreteComponentCandidatesList = ResolutionHandler.generateCandidates(application.getAbstractComponent().getSupertype().getIdAc(), application.getAbstractComponent().getIdAc(), resolutionTree);
		} catch (DBHandlerException e1) {
			return new CandidateListType();
		}
		LogHandler.getLogger().info(concreteComponentCandidatesList.size()+" components were found, now it will be filtered");
		int index = 0;
		if(concreteComponentCandidatesList.size() > 0){
			for(ContextContract candidate:concreteComponentCandidatesList){
				try {
					if(componentSubTypeRecursiveTest(application, candidate, null, application.getAbstractComponent(),resolutionTree)){
						if(candidate.getPlatform() == null){
							//Will test the next software contract
							continue;
						}
						List<ContextContract> componentCandidatePlatformlist = null;
						LogHandler.getLogger().info("Generating Hardware Candidate List for candidate: "+index);
						try {
							componentCandidatePlatformlist = ResolutionHandler.generateCompliantPlatformCandidates(candidate.getPlatform().getPlatformContract().getAbstractComponent().getIdAc(), resolutionTree);
						} catch (DBHandlerException e3) {
							//Will test the next software contract
							continue;
						}
						LogHandler.getLogger().info(componentCandidatePlatformlist.size()+" platforms were found for software component "+index);
						if(componentCandidatePlatformlist.size() > 0){
							for(int i = 0; i < componentCandidatePlatformlist.size(); i++){
//								System.out.println(componentCandidatePlatformlist.size());
								ContextContract platform = componentCandidatePlatformlist.get(i);
								if(applicationPlatform!=null){
									candidate.getPlatform().setPlatformContract(applicationPlatform);
									application.getPlatform().setPlatformContract(applicationPlatform);
								}
								if(isPlatformSubTypeTest(candidate.getPlatform().getPlatformContract(), platform, null,resolutionTree, applicationPlatform)){
									if(isPlatformSubTypeTest(application.getPlatform().getPlatformContract(), platform, null, resolutionTree, applicationPlatform)){
										ContextContract cct = XMLHandler.cloneContextContract(candidate);//Cloning component
										cct.getPlatform().setPlatformContract(XMLHandler.cloneContextContract(platform));
										if(application.getInnerComponents().size() > 0){
											boolean fit = true;
											for(int x = 0; x < application.getInnerComponents().size(); x++ ){
												ContextContract applicationInner = XMLHandler.cloneContextContract(application.getInnerComponents().get(x));
												applicationInner.getPlatform().setPlatformContract(XMLHandler.cloneContextContract(platform));
												CandidateListType inners = Resolution.resolve(applicationInner.getPlatform().getPlatformContract(), resolutionTree, XMLHandler.cloneContextContract(platform));
												if(inners.getCandidate().size()>0 ){
													cct.getInnerComponentsResolved().add(inners);
												}else{
													fit=false;
												}
											}
											if(fit){
												candidateList.getCandidate().add(cct);
											}
										}else{
											candidateList.getCandidate().add(cct);
										}
									}
								}
							}
							//partialComponentList has a list with all candidate components passed in first filter
							//----------------------------------------------------------------------------------------------------------------------------------------------
							LogHandler.getLogger().info("Calculating arguments...");
							for(ContextContract cc:candidateList.getCandidate()){
								try {
									FunctionHandler.calulateContextContractQualityArguments(cc.getPlatform().getPlatformContract(), resolutionTree);
								} catch (FunctionException e2) {
									//do nothing
								} //calcula parametros de qualidade
								if(Resolution.isSubTypeByQuality(application.getPlatform().getPlatformContract(), cc.getPlatform().getPlatformContract(), null, resolutionTree)){
									try {
										FunctionHandler.calulateContextContractCostArguments(cc.getPlatform().getPlatformContract(), resolutionTree);
									} catch (FunctionException e1) {
										//do nothing
									} //calcula parametros de custo
									if(Resolution.isSubTypeByCost(application.getPlatform().getPlatformContract(), cc.getPlatform().getPlatformContract(), null, resolutionTree)){
										try {
											FunctionHandler.calulateContextContractRankingArguments(cc.getPlatform().getPlatformContract(), resolutionTree);
										} catch (FunctionException e) {
											//do nothing
										} //calcula parametros de custo
										newCandidateList.getCandidate().add(cc);
										LogHandler.getLogger().info("Candidate "+cc.getCcName()+" added");
									}
								}
							}
						}
					}
				} catch (DBHandlerException e) {
					//Will test the next software contract
					continue;
				}
				index++;
			}
		}
		//Excluir somente teste
//		newCandidateList.getCandidate().addAll(candidateList.getCandidate());
		//
		newCandidateList.setUserId(application.getOwnerId());
		return newCandidateList;
	}
	 */
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
	public static boolean componentSubTypeRecursiveTest(ContextContract applicationContract, ContextContract candidate, Integer cp_id, AbstractComponentType parentAc, ResolutionNode resolutionTree) throws DBHandlerException{
		boolean subtype = true;
		ContextContract bound;

		if(cp_id!=null){
			bound = getBoundFromContextParameter(parentAc, cp_id, resolutionTree);
			int candidateContractIdAc = candidate.getAbstractComponent().getIdAc(); //ID_ac candidato
			ResolutionNode supertype = resolutionTree.findNode(applicationContract.getAbstractComponent().getIdAc());
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
					subtype = subtype && componentSubTypeRecursiveTest(cat.getContextContract(), getContextContractFromArgument(cat,candidate), cat.getCpId(), applicationContract.getAbstractComponent(), resolutionTree);
				}
			}
		}
		return subtype;
	}

	/**
	 * This method tests recursively subtyping relation between platforms.
	 * @param componentPlatformType
	 * @param testedPlatformCandidate
	 * @param cp_id
	 * @param resolutionTree
	 * @param applicationPlatform
	 * @return
	 */

	public static boolean isPlatformSubTypeTest(ContextContract componentPlatformType, ContextContract testedPlatformCandidate, Integer cp_id, ResolutionNode resolutionTree, ContextContract applicationPlatform){
		boolean subtype = true;
		if(applicationPlatform!=null){
			if(testedPlatformCandidate!=null){
				if(applicationPlatform.getCcId()==testedPlatformCandidate.getCcId()){
					return true;
				}else{
					return false;
				}
			}
		}
		//Test if a platform is subtype
		if(componentPlatformType != null && testedPlatformCandidate!=null){
			if(cp_id!=null){
				int candidateContractIdAc = testedPlatformCandidate.getAbstractComponent().getIdAc(); //ID_ac candidato
				ResolutionNode supertype = resolutionTree.findNode(componentPlatformType.getAbstractComponent().getIdAc());
				if(supertype.findNode(candidateContractIdAc)== null){
					subtype = false;
				}
			}
		}else{
			subtype = false;
		}
		if(subtype){
			if(componentPlatformType.getContextArguments().size() > 0){
				for(ContextArgumentType cat:componentPlatformType.getContextArguments()){
					if(cat.getValue()==null){
						subtype = subtype && isPlatformSubTypeTest(cat.getContextContract(), getContextContractFromArgument(cat,testedPlatformCandidate), cat.getCpId(), resolutionTree, applicationPlatform);
					}else{
						subtype = subtype && isPlatformSubTypeTest(cat, getValueFromCandidateArgument(cat,testedPlatformCandidate), cat.getKind());
					}

				}
			}
		}
		return subtype;
	}

	/**
	 * This method evaluate subtyping relation with numeric argument
	 * @param contract
	 * @param candidate
	 * @param kind
	 * @return
	 */
	public static boolean isPlatformSubTypeTest(ContextArgumentType contract, ContextArgumentType candidate, int kind){
		switch(kind){
		case 3:
			//In this case, if both are equal, return true.
			if(contract.getValue().equals(candidate.getValue())){
				return true;
			}
			break;
		case 4:
			//In this case a bigger or equal number is subtype
			if(Double.parseDouble(contract.getValue().getValue()) <= Double.parseDouble(candidate.getValue().getValue()) ){
				return true;
			}
			break;
		case 5:
			//In this case a smaller number is subtype
			if(Double.parseDouble(contract.getValue().getValue()) <= Double.parseDouble(candidate.getValue().getValue()) ){
				return true;
			}
			break;
		}
		return false;
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
	public static boolean isSubTypeByCalculatedArgument(ContextContract applicationPlatform, ContextContract platformCandidate, Integer qp_id, ResolutionNode resolutionTree, int calculatedArgumentType) throws ShelfException{
		boolean subtype = true;
		if(applicationPlatform != null){
			List<CalculatedArgumentType> list = null;
			List<CalculatedParameterType> parametersList = null;
			switch (calculatedArgumentType) {
			case 1:
				list = applicationPlatform.getQualityArguments();
				parametersList = applicationPlatform.getAbstractComponent().getQualityParameters();
				break;
			case 2:
				list = applicationPlatform.getCostArguments();
				parametersList = applicationPlatform.getAbstractComponent().getCostParameters();
				break;
			case 3:
				list = applicationPlatform.getRankingArguments();
				parametersList = applicationPlatform.getAbstractComponent().getRankingParameters();
				break;
			}
			if(list.size() > 0){
				for(CalculatedArgumentType qat:list){
					CalculatedParameterType qpt = null;
					if(qat.getValue()!=null){
						for(CalculatedParameterType cp : parametersList){
							if(cp.getCalculatedArgument().getCalcId()==qat.getCalcId()){
								qpt = cp;
							}
						}
						subtype = subtype && isSubTypeByCalculatedArgument(qat, getCalculatedArgumentValueFromCandidateArgument(qpt,platformCandidate, calculatedArgumentType), qpt.getKindId(), calculatedArgumentType);
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
		case 3:
			//In this case, if both are equal, return true.
			if(application.getValue().equals(candidate.getValue())){
				return true;
			}
			break;
		case 4:
			//In this case a bigger or equal number is subtype
			if(application.getValue() <= candidate.getValue() ){
				return true;
			}
			break;
		case 5:
			//In this case a smaller number is subtype
			if(application.getValue() <= candidate.getValue()){
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * This method tests recursively subtyping relation between platforms.
	 * @param candidate1step
	 * @param platformCandidate2step
	 * @param cp_id
	 * @param resolutionTree
	 * @return
	 */
	/*
	public static boolean isSubTypeByCost(ContextContract applicationPlatform, ContextContract platformCandidate, Integer qp_id, ResolutionNode resolutionTree){
		boolean subtype = true;
		if(applicationPlatform != null){
			if(applicationPlatform.getCostArguments().size() > 0){
				for(CostArgumentType qat:applicationPlatform.getCostArguments()){
					CalculatedParameterType qpt = null;
					if(qat.getValue()!=null){
						for(CalculatedParameterType cp : applicationPlatform.getAbstractComponent().getCostParameters()){
							if(cp.getCalculatedArgument().getCalcId()==qat.getCopId()){
								qpt = cp;
							}
						}
						subtype = subtype && isSubTypeByCost(qat, getCostArgumentValueFromCandidateArgument(qpt,platformCandidate), qpt.getKindId());
					}
				}
			}
		}

		return subtype;
	}
	 */
	/**
	 * 
	 * @param qpt
	 * @param platformCandidate
	 * @return
	 */
	/*
	private static CostArgumentType getCostArgumentValueFromCandidateArgument(CostParameterType qpt, ContextContract platformCandidate) {
		for(CostArgumentType app_cat: platformCandidate.getCostArguments()){
			if(app_cat.getCopId() == qpt.getCopId()){
				return app_cat;
			}
		}
		return null;
	}
	 */
	/**
	 * 
	 * @param application
	 * @param candidate
	 * @param kind
	 * @return
	 */
	public static boolean isSubTypeByCost(CostArgumentType application, CostArgumentType candidate, int kind){
		switch(kind){
		case 3:
			//In this case, if both are equal, return true.
			if(application.getValue().equals(candidate.getValue())){
				return true;
			}
			break;
		case 4:
			//In this case a bigger or equal number is subtype
			if(application.getValue() <= candidate.getValue() ){
				return true;
			}
			break;
		case 5:
			//In this case a smaller number is subtype
			if(application.getValue() <= candidate.getValue()){
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
				if(cat_from_app_var.getCpId()==cat.getCpId()){
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

	/**
	 * This method find a specific value from a context argument
	 * @param cat
	 * @param candidate
	 * @return
	 * @throws ShelfException 
	 */
	/*
	private static QualityArgumentType getQualityArgumentValueFromCandidateArgument(QualityParameterType qpt, ContextContract candidate) {
		for(QualityArgumentType app_cat: candidate.getQualityArguments()){
			if(app_cat.getQpId() == qpt.getQpId()){
				return app_cat;
			}
		}
		return null;
	}*/

	private static CalculatedArgumentType getCalculatedArgumentValueFromCandidateArgument(CalculatedParameterType qpt, ContextContract candidate, int type) throws ShelfException {
		List<CalculatedArgumentType> list;
		switch (type) {
		case 1:
			list = candidate.getQualityArguments();
			break;
		case 2:
			list = candidate.getCostArguments();
			break;
		case 3:
			list = candidate.getRankingArguments();
			break;
		default:
			throw new ShelfException("Type of calculated parameter invalid: "+type);
		}

		for(CalculatedArgumentType app_cat: list){
			if(app_cat.getCalcId() == qpt.getCalcId()){
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
	public static ContextContract getBoundFromContextParameter(AbstractComponentType ac, int cp_id, ResolutionNode resolutionTree) throws DBHandlerException{
		for(ContextParameterType cp:resolutionTree.findNode(ac.getIdAc()).getCps()){
			if(cp.getCpId()==cp_id){
				return ContextContractHandler.getContextContract(cp.getBound().getCcId());
			}
		}
		return null;		
	}

}
