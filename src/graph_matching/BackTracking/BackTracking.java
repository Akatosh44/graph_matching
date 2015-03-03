package graph_matching.BackTracking;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.ejml.simple.SimpleMatrix;

import graph_matching.EVDLambda;
import graph_matching.FuEvalG;

public class BackTracking {

	private double alpha;
	private double dalpha;
	private SimpleMatrix incr;
	private double result;
	private ArrayList<Double> values;
	
	public ArrayList<Double> getValues() {
		return values;
	}

	public BackTracking(double min, double max, SimpleMatrix P,SimpleMatrix X,EVDLambda lambda, double gamma){
		alpha=0;
		dalpha=0.5;
		result=0;
		
		//First test
		if(upOrDown(P,X,lambda,gamma)==-1){
			
			while(dalpha>0.001){
				System.out.println(alpha);
				if(upOrDownFurther(P,X,lambda,gamma)==-1){
					alpha+=dalpha;
					System.out.println(alpha);
				}
				dalpha/=2;
			}
		}else{
			alpha=0;
		}

		values=new ArrayList<Double>();
		values.add(gamma);
		double i=0;
		while(i<1){
			incr=P.plus((X.minus(P)).scale(i));
			for(int n=0;n<incr.getMatrix().getNumRows();n++){
				for(int m=0;m<incr.getMatrix().getNumRows();m++){
					if(incr.get(n, m)<0){
						incr.set(n, m, 0);
					}
				}
			}
			values.add(computeMin(lambda, gamma,incr));
			i+=0.1;
		}

		
	}
	private int upOrDownFurther(SimpleMatrix P,SimpleMatrix X, EVDLambda lambda,double gamma){
		
		double nResult=computeMin(lambda, gamma,P.plus((X.minus(P)).scale(alpha+dalpha)));
		double nResult2=computeMin(lambda, gamma,P.plus((X.minus(P)).scale(alpha+dalpha+0.0000001)));
		double fResult=nResult2-nResult;
		if(fResult>0){
			return 1;
		}else{
			return -1;
		}
		
	}
	private int upOrDown(SimpleMatrix P,SimpleMatrix X, EVDLambda lambda,double gamma){
		
		double nResult=computeMin(lambda, gamma,P.plus((X.minus(P)).scale(alpha)));
		double nResult2=computeMin(lambda, gamma,P.plus((X.minus(P)).scale(alpha+0.001)));
		double fResult=nResult2-nResult;
		if(fResult>0){
			return 1;
		}else{
			return -1;
		}
		
	}
	public double getAlpha() {
		return alpha;
	}
	public double computeMin(EVDLambda lambda, double gamma, SimpleMatrix incr){
		FuEvalG.evaluate(incr,lambda,gamma);
		return FuEvalG.getResultat();
	}
	public static void exportFunction(ArrayList<ArrayList<Double>> values){
		String csvFile = "data/result/BT.txt";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(csvFile));
			for(int i=0;i<values.size();i++){
				bw.write(values.get(i).toString());
				bw.newLine();
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
