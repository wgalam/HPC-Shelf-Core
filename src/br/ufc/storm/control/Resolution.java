package br.ufc.storm.control;

import java.util.List;

import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.CandidateListType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.CostArgumentType;
import br.ufc.storm.jaxb.CostParameterType;
import br.ufc.storm.jaxb.QualityArgumentType;
import br.ufc.storm.jaxb.QualityParameterType;
import br.ufc.storm.model.ResolutionNode;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.sql.ResolutionHandler;
import br.ufc.storm.xml.XMLHandler;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.FunctionException;
import br.ufc.storm.exception.ResolveException;

public class Resolution{

	/**
	 * Only for tests purpose
	 * @param args
	 * @throws DBHandlerException 
	 */
	public static void main(String args[]) throws DBHandlerException{

		ContextContract cc = new ContextContract();
		AbstractComponentType ac = new AbstractComponentType();
		ac.setIdAc(109);
		cc.setAbstractComponent(ac);
		ContextContract plat = new ContextContract();
		plat.setCcName("Plataforma do Wagner");
		plat.setAbstractComponent(new AbstractComponentType());
		plat.getAbstractComponent().setIdAc(19);
		cc.setPlatform(plat);
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
		}

	}

	/**
	 * This method generates and returns a list of all components compatibles with contract
	 * @param application
	 * @param resolutionTree
	 * @param applicationPlatform
	 * @return
	 * @throws ResolveException 
	 */
//Método está errado, só está encontrando um componente
	public static CandidateListType resolve(ContextContract application, ResolutionNode resolutionTree, ContextContract applicationPlatform) throws ResolveException{
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
		try {
			concreteComponentCandidatesList = ResolutionHandler.generateCandidates(application.getAbstractComponent().getSupertype().getIdAc(), application.getAbstractComponent().getIdAc(), resolutionTree);
		} catch (DBHandlerException e1) {
			return new CandidateListType();
		}
		if(concreteComponentCandidatesList.size() > 0){
			for(ContextContract candidate:concreteComponentCandidatesList){
				try {
					if(componentSubTypeRecursiveTest(application, candidate, null, application.getAbstractComponent(),resolutionTree)){
						if(candidate.getPlatform() == null){
							//Will test the next software contract
							continue;
						}
						List<ContextContract> componentCandidatePlatformlist = null;
						try {
							componentCandidatePlatformlist = ResolutionHandler.generateCompliantPlatformCandidates(candidate.getPlatform().getAbstractComponent().getIdAc(), resolutionTree);
						} catch (DBHandlerException e3) {
							//Will test the next software contract
							continue;
						}
						if(componentCandidatePlatformlist.size() > 0){
							for(int i = 0; i < componentCandidatePlatformlist.size(); i++){
								
								ContextContract platform = componentCandidatePlatformlist.get(i);
								if(applicationPlatform!=null){
									candidate.setPlatform(applicationPlatform);
									application.setPlatform(applicationPlatform);
								}
								if(isPlatformSubTypeTest(candidate.getPlatform(), platform, null,resolutionTree, applicationPlatform)){
									if(isPlatformSubTypeTest(application.getPlatform(), platform, null, resolutionTree, applicationPlatform)){
										ContextContract cct = XMLHandler.cloneContextContract(candidate);//Cloning component
										cct.setPlatform(XMLHandler.cloneContextContract(platform));
										if(application.getInnerComponents().size() > 0){
											boolean fit = true;
											for(int x = 0; x < application.getInnerComponents().size(); x++ ){
												ContextContract applicationInner = XMLHandler.cloneContextContract(application.getInnerComponents().get(x));
												applicationInner.setPlatform(XMLHandler.cloneContextContract(platform));
												CandidateListType inners = Resolution.resolve(applicationInner, resolutionTree, XMLHandler.cloneContextContract(platform));
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
							for(ContextContract cc:candidateList.getCandidate()){
								try {
									FunctionHandler.calulateContextContractQualityArguments(cc.getPlatform(), resolutionTree);
								} catch (FunctionException e2) {
									//do nothing
								} //calcula parametros de qualidade
								if(Resolution.isSubTypeByQuality(application.getPlatform(), cc.getPlatform(), null, resolutionTree)){
									try {
										FunctionHandler.calulateContextContractCostArguments(cc.getPlatform(), resolutionTree);
									} catch (FunctionException e1) {
										//do nothing
									} //calcula parametros de custo
									if(Resolution.isSubTypeByCost(application.getPlatform(), cc.getPlatform(), null, resolutionTree)){
										try {
											FunctionHandler.calulateContextContractRankingArguments(cc.getPlatform(), resolutionTree);
										} catch (FunctionException e) {
											//do nothing
										} //calcula parametros de custo
										newCandidateList.getCandidate().add(cc);									
									}
								}
							}
						}
					}
				} catch (DBHandlerException e) {
					//Will test the next software contract
					continue;
				}


			}
		}
		return newCandidateList;
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
					if(cat.getVariableCpId() == cp_id){
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
				candidate.setPlatform(candidatePlatform);
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
					subtype = subtype && componentSubTypeRecursiveTest(cat.getContextContract(), getContextContractFromArgument(cat,candidate), cat.getVariableCpId(), applicationContract.getAbstractComponent(), resolutionTree);
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
						subtype = subtype && isPlatformSubTypeTest(cat.getContextContract(), getContextContractFromArgument(cat,testedPlatformCandidate), cat.getVariableCpId(), resolutionTree, applicationPlatform);
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
	 */
	public static boolean isSubTypeByQuality(ContextContract applicationPlatform, ContextContract platformCandidate, Integer qp_id, ResolutionNode resolutionTree){
		boolean subtype = true;
		if(applicationPlatform != null){
			if(applicationPlatform.getQualityArguments().size() > 0){
				for(QualityArgumentType qat:applicationPlatform.getQualityArguments()){
					QualityParameterType qpt = null;
					if(qat.getValue()!=null){
						for(QualityParameterType cp : applicationPlatform.getAbstractComponent().getQualityParameters()){
							if(cp.getQualityArgument().getQpId()==qat.getQpId()){
								qpt = cp;
							}
						}
						subtype = subtype && isSubTypeByQuality(qat, getQualityArgumentValueFromCandidateArgument(qpt,platformCandidate), qpt.getKindId());
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
	public static boolean isSubTypeByQuality(QualityArgumentType application, QualityArgumentType candidate, int kind){
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
	public static boolean isSubTypeByCost(ContextContract applicationPlatform, ContextContract platformCandidate, Integer qp_id, ResolutionNode resolutionTree){
		boolean subtype = true;
		if(applicationPlatform != null){
			if(applicationPlatform.getCostArguments().size() > 0){
				for(CostArgumentType qat:applicationPlatform.getCostArguments()){
					CostParameterType qpt = null;
					if(qat.getValue()!=null){
						for(CostParameterType cp : applicationPlatform.getAbstractComponent().getCostParameters()){
							if(cp.getCostArgument().getCopId()==qat.getCopId()){
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
	/**
	 * 
	 * @param qpt
	 * @param platformCandidate
	 * @return
	 */
	private static CostArgumentType getCostArgumentValueFromCandidateArgument(CostParameterType qpt, ContextContract platformCandidate) {
		for(CostArgumentType app_cat: platformCandidate.getCostArguments()){
			if(app_cat.getCopId() == qpt.getCopId()){
				return app_cat;
			}
		}
		return null;
	}
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
				if(cat_from_app_var.getVariableCpId()==cat.getVariableCpId()){
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
		int cp_id = cat.getVariableCpId();
		for(ContextArgumentType app_cat: candidate.getContextArguments()){
			if(app_cat.getVariableCpId() == cp_id){
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
	 */
	private static QualityArgumentType getQualityArgumentValueFromCandidateArgument(QualityParameterType qpt, ContextContract candidate) {
		for(QualityArgumentType app_cat: candidate.getQualityArguments()){
			if(app_cat.getQpId() == qpt.getQpId()){
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
			if(cat.getVariableCpId() == cp_id){
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
