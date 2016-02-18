package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.CostParameterType;
import br.ufc.storm.jaxb.QualityParameterType;
import br.ufc.storm.jaxb.RankingParameterType;
import br.ufc.storm.model.ResolutionNode;

public class ResolutionHandler extends DBHandler {
	private static final String SELECT_CONTEXT_CONTRACT_BY_AC_ID = "select cc_id from context_contract where ac_id = ?;";
	private static final String SELECT_PLATFORM_BY_AC_ID = "select * from context_contract A, platform_owner B where A.cc_id = B.platform_cc_id and A.ac_id = ? and type_id = 1;";
	private final static String SELECT_ABSTRACT_COMPONENT_BY_SUPERTYPE_ID = "select ac_name, ac_id, supertype_id, enabled from abstract_component WHERE supertype_id = ?;";

	/**
	 * This method generates a list os all components candidates to a contract but only in the highest level, it will be refined.
	 * @param supertypeID
	 * @param requiredID
	 * @param resolutionTree 
	 * @return
	 * @throws DBHandlerException 
	 */

	public static List<ContextContract> generateCandidates(int supertypeID, int requiredID, ResolutionNode resolutionTree) throws DBHandlerException{
		PreparedStatement prepared;
		ResultSet resultSet;
		List<ContextContract> list = new ArrayList<ContextContract>();
		int history;
		int supertype = 0;
		int aux = requiredID;
		try { 
			Connection con = getConnection();
			do{
				prepared = con.prepareStatement(SELECT_CONTEXT_CONTRACT_BY_AC_ID);
				prepared.setInt(1, aux); 
				resultSet = prepared.executeQuery(); 
				while(resultSet.next()) { 
					Integer cc_id = resultSet.getInt("cc_id");
					ContextContract cc = ContextContractHandler.getContextContract(cc_id);
					//					--------------------------------------------------------------
					//					for(ContextContract icc: cc.getInnerComponents()){
					//						List<ContextContract> innerCandidates = DBHandler.generateCandidates(icc.getAbstractComponent().getSupertype().getIdAc(), icc.getAbstractComponent().getIdAc(), resolutionTree);
					//				//		Criar várias opções
					//					//	TODO: Concluir com a geração dos comonentes aninhados
					//					}





					//					--------------------------------------------------------------
					cc.getPlatform().setPlatformContract(PlatformHandler.getPlatform(cc_id));
					list.add(cc); 
				}
				supertype = resolutionTree.findNode(list.get(list.size()-1).getAbstractComponent().getIdAc()).getSupertype().getAc_id();
				history = aux;
				aux = supertype;
			}while(resultSet!= null && history != supertypeID );//supertype != supertypeID && supertype != 0
			return list;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 

	}

	/**
	 * This method generates a set of platforms stored in core
	 * @param supertypeID
	 * @param resolutionTree
	 * @return
	 * @throws DBHandlerException 
	 */

	public static List<ContextContract> generateCompliantPlatformCandidates(int requiredID, ResolutionNode resolutionTree) throws DBHandlerException{
		try {	
			List <ResolutionNode> subtype = new ArrayList<ResolutionNode>();
			List<ContextContract> list = new ArrayList<ContextContract>();
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(SELECT_PLATFORM_BY_AC_ID);
			prepared.setInt(1, requiredID); 
			ResultSet resultSet = prepared.executeQuery(); 
			while(resultSet.next()) { 
				Integer cc_id = resultSet.getInt("platform_cc_id");
				try {
					list.add(ContextContractHandler.getContextContract(cc_id));
				} catch (DBHandlerException e) {
					throw new RuntimeException("Platform can not be catched with ac_id "+requiredID);
				} 
			} 
			subtype = resolutionTree.findNode(requiredID).getSubtype();
			for(ResolutionNode rn:subtype){
				list.addAll(generateCompliantPlatformCandidates(rn.getAc_id(), resolutionTree));
			}
			return list;
		} catch (SQLException e) {
			throw new DBHandlerException("An error occurred while trying to generate a compliant platform", e);
		}
		
	}

	/**
	 * This method generates a resolution tree from an specified node 
	 * All abstract components must be a subtype of root or another subtype off root, because if not, this component won't be catched.
	 * @return
	 * @throws DBHandlerException 
	 */
	public static ResolutionNode generateResolutionTree(Integer i, ResolutionNode tree, List<ContextParameterType> cps, List<QualityParameterType> qps, List<CostParameterType> cops, List<RankingParameterType> rps, String parentPath) throws DBHandlerException{
		try {
			Connection con = DBHandler.getConnection();
			PreparedStatement prepared = con.prepareStatement(SELECT_ABSTRACT_COMPONENT_BY_SUPERTYPE_ID); 
			prepared.setInt(1, i);
			ResultSet resultSet = prepared.executeQuery(); 
			while (resultSet.next()) { 
				if(resultSet.getBoolean("enabled") == false){
					return null;
				}
				ResolutionNode node = new ResolutionNode();
				node.setName(resultSet.getString("ac_name"));
				node.setPath(parentPath);
				node.setAc_id(resultSet.getInt("ac_id"));
				node.setCps(ContextParameterHandler.getAllContextParameterFromAbstractComponent(node.getAc_id()));
				node.getCps().addAll(cps);
				node.setQps(QualityHandler.getQualityParameters(node.getAc_id()));
				if(node.getQps().size()>0){
					node.getQps().addAll(qps);
				}
				node.setRps(RankingHandler.getRankingParameters(node.getAc_id(), con));
				if(node.getRps().size()>0){
					node.getRps().addAll(rps);
				}
				node.setCops(CostHandler.getCostParameters(node.getAc_id()));
				if(node.getCops().size()>0){
					node.getCops().addAll(cops);
				}
				node.setSupertype(tree);
				tree.addSubtype(node);
				generateResolutionTree(node.getAc_id(), node, node.getCps(), node.getQps(), node.getCops(), node.getRps(), node.getPath()+"."+node.getName());

			} 
			return tree;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 	
		
	}

	/**
	 * This method generates all resolution tree of abstract components
	 * @return
	 * @throws DBHandlerException 
	 */
	public static ResolutionNode generateResolutionTree() throws DBHandlerException{
		ResolutionNode tree = new ResolutionNode();
		tree.setAc_id(0);
		tree.setName("root");
		tree.setPath("");
		tree.setSupertype(tree);
		ResolutionNode r = generateResolutionTree(0, tree, new ArrayList<ContextParameterType>(), new ArrayList<QualityParameterType>(), new ArrayList<CostParameterType>(), new ArrayList<RankingParameterType>(), "root");
		return r;
	}

}
