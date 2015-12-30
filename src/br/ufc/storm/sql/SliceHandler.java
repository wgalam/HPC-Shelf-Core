package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.SliceType;

public class SliceHandler extends DBHandler {

	private static final String INSERT_SLICE = "INSERT INTO slice (inner_component_id, inner_unit_id, ac_id) VALUES (?,?,?) RETURNING slice_id;";
	private static final String SELECT_SLICE = "SELECT * FROM slice WHERE slice_id = ?;";
	private static final String SELECT_SLICE_LIST = "SELECT * FROM slice WHERE ac_id = ?;";
	
	public static int addSlice(SliceType st, int ac_id) throws DBHandlerException {
		try {
			PreparedStatement prepared = getConnection().prepareStatement(INSERT_SLICE); 
			prepared.setInt(1, st.getInnerComponentId());
			prepared.setInt(2, st.getInnerUnityId());  
			prepared.setInt(3, ac_id);
			ResultSet result = prepared.executeQuery(); 
			if(result.next()){
				return result.getInt("slice_id");
			}else{
				throw new DBHandlerException("Slice can't be added: ");	
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
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
				st.setInnerUnityId(result.getInt("inner_unit_id"));
				st.setSliceId(result.getInt("slice_id"));
				return st;
			}else{
				throw new DBHandlerException("Slice not found ");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
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
				st.setInnerUnityId(result.getInt("inner_unit_id"));
				st.setSliceId(result.getInt("slice_id"));
				list.add(st);
			}
			return list;
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		} 
	}

}
