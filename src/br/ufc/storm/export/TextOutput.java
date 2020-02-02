package br.ufc.storm.export;

import br.ufc.storm.jaxb.CandidateListType;
import br.ufc.storm.mcdm.DecisionMatrix;
import br.ufc.storm.sql.CalculatedArgumentHandler;

public class TextOutput {
	public static String toSpreadshetTable(DecisionMatrix d){
		return "\n\n"+d.toString();
	}

	public static String toRInput(CandidateListType cl){
		String str = "";
		for(int k = 0; k < CalculatedArgumentHandler.decisionMatrix.size(); k++){
			DecisionMatrix d = CalculatedArgumentHandler.decisionMatrix.get(k);
			str+=("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n");
			str+=("\n----------------------------------------\n");
			str+=(d.toRmcdmFunction("TOPSISVector", null));
			str+=("\n----------------------------------------\n");
			str+=(d.toRmcdmFunction("VIKOR", (float) 0.5));
			str+=("\n----------------------------------------\n");
			str+=(d.toRmcdmFunction("WASPAS", (float) 0.0));
			str+=("\n----------------------------------------\n");
			str+=(d.toRmcdmFunction("TOPSISLinear", null));
			str+=("\n----------------------------------------\n");
			str+=(d.toPromethee("PROMETHEE", null,"Linear"));
			str+=("\n\n");
		}
		return str;
	}

	public static String toLatexTable(CandidateListType cl, DecisionMatrix d, double compareMatrix[][]){
		String str = "";
		for(int k = 0; k < CalculatedArgumentHandler.decisionMatrix.size(); k++){
			char separator = '&';
			//			Printing
			str+=("\\begin{table}\n    \\centering\n    \\caption{Matriz de resultados do processo de classificação do \\alite}\\label{tab:rank"+k+"}\n    {\\tiny \\begin{tabular}{|c|c|c|c|c|c|c|c|c|}\n\\hline");
			str+=("%Rank "+k+"\n");
			str+=("        Alternativa "+separator);
			for(int i = 0; i < d.getCriterionList().size(); i++){
				str+=(d.getCriteriaName(i)+"["+d.getWeight()[i]+"] "+separator+" ");
			}
			str+=(" TOPSISVector"+separator+" VIKOR "+separator+" WPM"+separator+"TOPSISLinear\\\\"+"\n");

			for(int i = 0; i < compareMatrix.length; i++){
				//				System.out.printf("        %d "+separator+" ", i);
				str+="        "+i+" "+separator+" ";
				for(int j = 0; j < d.getCriterionList().size(); j++){
					str+=(compareMatrix[i][j]+" "+separator+" ");
				}
				for(int j = 3; j < compareMatrix[0].length; j++){
					str+=(compareMatrix[i][j]);
					if(j<compareMatrix[0].length-1){
						str+=(separator+" ");
					}else{
						str+=("\\\\");
					}
				}
				str+=("\n");
			}
			str+=("\\hline \n \\end{tabular}}\n\\begin{center}\n     Fonte: Próprio autor.\n\\end{center}\n\\end{table}\n");
			str+=("\n%--------------------------------------\n\n");
		}
		return str;
	}

	public static String toSystatInput(CandidateListType cl, DecisionMatrix d, double compareMatrix[][]){

		String [] methods = {"TOPSISVector","VIKOR","WPM","TOPSISLinear"};
		char separator = ' ';
		String str = "\n\n\n";
//		str+="+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n";
		str+="ALTERNATIVA"+separator+"METODO$"+separator+"MEDIDA"+"\n";
		for(int i = 1; i <= cl.getCandidate().size(); i++){
			for(int j = 3; j < compareMatrix[0].length; j++){
				str+= RomanNumber.toRoman(i) + separator + methods[j-3] + separator + (compareMatrix[i-1][j] + "\n");
			}
		}
		return str;
	}
}
