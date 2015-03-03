package graph_matching.BackTracking;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import org.ejml.simple.SimpleMatrix;

import graph_matching.EVDLambda;
import graph_matching.FuEvalG;

public class NewBackTracking {

	private double alpha;
	private double dalpha;
	private SimpleMatrix incr;
	private double result;
	private ArrayList<Double> values;
	
	
	public NewBackTracking(double min, double max, SimpleMatrix P,SimpleMatrix X,EVDLambda lambda, double gamma){
		alpha=0;
		dalpha=0.0001;
		result=0;
		double nResult=0;
		boolean exit=false;
		incr=P.plus((X.minus(P)).scale(alpha));
		result=computeMin(lambda,gamma,incr);
		while(!exit){
			if(alpha+dalpha>1){
				exit=true;
			}
			nResult=computeMin(lambda,gamma,P.plus((X.minus(P)).scale(alpha+dalpha)));
			//System.out.println(result+" new : "+nResult);
			if(nResult<result){
				alpha+=dalpha;
				incr=P.plus(X.minus(P).scale(alpha));
				result=nResult;
			}else{
				exit=true;
			}

		}
		
		
		values=new ArrayList<Double>();
		values.add(gamma);
		double i=0;
		while(i<1){
			incr=P.plus((X.minus(P)).scale(i));
			for(int n=0;n<incr.getMatrix().getNumRows();n++){
				for(int m=0;m<incr.getMatrix().getNumCols();m++){
					if(incr.get(n, m)<0){
						incr.set(n, m, 0);
					}
				}
			}
			values.add(computeMin(lambda, gamma,incr));
			i+=0.1;
		}
		if(alpha==0){
			alpha=0.5;
		}else if(alpha>1){
			alpha=1;
		}
		
	}
	public double computeMin(EVDLambda lambda, double gamma, SimpleMatrix incr){
		FuEvalG.evaluate(incr,lambda,gamma);
		return FuEvalG.getResultat();
	}
	
	public double getAlpha() {
		return alpha;
	}
	public static void exportFunction(ArrayList<ArrayList<Double>> values){
		String csvFile = "data/result/BT.csv";
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
	public ArrayList<Double> getValues() {
		return values;
	}
}
