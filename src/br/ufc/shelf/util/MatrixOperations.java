package br.ufc.shelf.util;

public class MatrixOperations {
	public static String print_matrix(Double m[][]){
		String str = "";
		for(int i=0; i < m.length; i++){
			for(int j=0; j < m[0].length; j++){
				str+=m[i][j]+", ";
			}
			str+="\n";
		}
		return str;
	}
}
