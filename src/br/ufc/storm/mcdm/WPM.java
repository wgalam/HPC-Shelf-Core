package br.ufc.storm.mcdm;


public class WPM {
	

	public static void main(String[] args) {
		double weight[] = {0.4504, 0.1231, 0.0848, 0.3417};
		double d[][]={{0.9381, 0.3501, 0.8811, 0.5646},{0.7691, 0.4812, 0.1679, 0.9336},{0.9445, 0.1138, 0.2219, 0.0135},{0.1768, 0.0221, 0.9462, 0.1024}};
//		for(int i=0; i < x.d.length; i++){
//			for(int j=0; j < x.d[0].length; j++){
//				System.out.print(x.d[i][j]+" ");
//			}
//			System.out.println();
//		}
		int [] y = eval(weight, d, null);
		System.out.println("Resultado");
		for(int i:y){
			System.out.println(i);
		}
	}
	
	public static int[] eval(double weight[], double d[][], String cd[]){
		int x[] = new int[d.length];
		for(int i = 0; i < d.length; i++){
			x[i]=i+1;
		}
		double sum=0;
		for(int i=0; i < weight.length;i++){
			sum+=weight[i];
		}
		if(sum==1.0){
			for(int i=0; i < d[0].length;i++){
				for(int j=i+1; j < d.length; j++){
					double ratio = 1;
					for(int k=0; k < weight.length;k++){
						ratio=ratio*(Math.pow((d[i][k]/d[j][k]), weight[k]));
					}
					if(ratio < 1){
						int aux = x[i];
						x[i] = x[j];
						x[j] = aux;
					}
//					System.out.println("A["+(i+1)+","+(j+1)+"] "+ratio);
				}
			}
		}else{
			System.out.println("The weight sum should be one: "+sum);
			return null;
		}
		return x;
	}
}
