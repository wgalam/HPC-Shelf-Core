package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.SliceType;
import br.ufc.storm.model.ACSLICE;

public class SliceHandler extends DBHandler {

	private static final String INSERT_SLICE = "INSERT INTO slice (inner_component_id, inner_unit_id, ac_id) VALUES (?,?,?) RETURNING slice_id;";
	private static final String INSERT_SLICE_BY_NAME = "INSERT INTO slice (inner_component_id, inner_unit_id, ac_id) VALUES ((SELECT ic_id FROM abstract_component A, inner_components B WHERE A.ac_name = ? AND A.ac_id = B.ac_id AND B.parent_id = (SELECT ac_id FROM abstract_component WHERE ac_name = ?)), (SELECT abstract_unit_id FROM abstract_unit WHERE au_name = ?), (SELECT ac_id FROM abstract_component WHERE ac_name = ?)) RETURNING slice_id;";
	private static final String SELECT_SLICE = "SELECT * FROM slice WHERE slice_id = ?;";
	private static final String SELECT_SLICE_LIST = "SELECT * FROM slice WHERE ac_id = ?;";
	private static final String SELECT_ALL_SLICES = "SELECT * FROM slice;";
	
	public static int addSlice(SliceType st, int ac_id) throws DBHandlerException {
		try {
			PreparedStatement prepared = getConnection().prepareStatement(INSERT_SLICE); 
			prepared.setInt(1, st.getInnerComponentId());
			prepared.setInt(2, st.getInnerUnitId());  
			prepared.setInt(3, ac_id);
			ResultSet result = prepared.executeQuery(); 
			if(result.next()){
				return result.getInt("slice_id");
			}else{
				throw new DBHandlerException("Slice can't be added: ");	
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}
	
	public static int addSlice(String innerComponent, String innerUnit, String ac) throws DBHandlerException {
		try {
			PreparedStatement prepared = getConnection().prepareStatement(INSERT_SLICE_BY_NAME); 
			prepared.setString(1, innerComponent);
			prepared.setString(2, ac);
			prepared.setString(3, innerUnit);  
			prepared.setString(4, ac);
			ResultSet result = prepared.executeQuery(); 
			if(result.next()){
				return result.getInt("slice_id");
			}else{
				throw new DBHandlerException("Slice can't be added: ");	
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}

	public static SliceType getSlice(int slice_id) throws DBHandlerException{
		try {
			PreparedStatement prepared = getConnection().prepareStatement(SELECT_SLICE); 
			prepared.setInt(1, slice_id);
			ResultSet result = prepared.executeQuery(); 
			if(result.next()){
				SliceType st = new SliceType();
				st.setInnerComponentId(result.getInt("inner_component_id"));
				st.setInnerUnitId(result.getInt("inner_unit_id"));
				st.setSliceId(result.getInt("slice_id"));
				return st;
			}else{
				throw new DBHandlerException("Slice not found ");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}

	public static List<SliceType> getSlices(int ac_id) throws DBHandlerException{
		List<SliceType> list = new ArrayList<SliceType>();
		try {
			PreparedStatement prepared = getConnection().prepareStatement(SELECT_SLICE_LIST); 
			prepared.setInt(1, ac_id);
			ResultSet result = prepared.executeQuery(); 
			while(result.next()){
				SliceType st = new SliceType();
				st.setInnerComponentId(result.getInt("inner_component_id"));
				st.setInnerUnitId(result.getInt("inner_unit_id"));
				st.setSliceId(result.getInt("slice_id"));
				list.add(st);
			}
			return list;
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}

	public static List<ACSLICE> getAllSlices() throws DBHandlerException{
		List<ACSLICE> list = new LinkedList<ACSLICE>();
		try {
			PreparedStatement prepared = getConnection().prepareStatement(SELECT_ALL_SLICES); 
			ResultSet result = prepared.executeQuery(); 
			while(result.next()){
				SliceType st = new SliceType();
				st.setInnerComponentId(result.getInt("inner_component_id"));
				st.setInnerUnitId(result.getInt("inner_unit_id"));
				st.setSliceId(result.getInt("slice_id"));
				list.add(new ACSLICE(result.getInt("ac_id"), st));
			}
			return list;
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}
	
	/*private static List<SliceType> getSlices(int ac_id) throws DBHandlerException {

		try {
			Connection con = getConnection();
			ArrayList<SliceType> slices = new ArrayList<SliceType>();
			PreparedStatement prepared = con.prepareStatement(SELECT_ALL_SLICES);
			prepared.setInt(1, ac_id);
			ResultSet resultSet = prepared.executeQuery();
			while (resultSet.next()) {				
				SliceType slc = new SliceType();
				slc.setSliceId(resultSet.getInt("slice_id"));
				slc.setInnerComponentId(resultSet.getInt("inner_component_id"));
				slc.setInnerUnityId(resultSet.getInt("inner_unit_id"));
				slices.add(slc);
			} 
			return slices; 
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}

	}*/

	
	
	
}
