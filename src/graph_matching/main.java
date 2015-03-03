package graph_matching;

import graph_matching.BackTracking.BackTracking;
import graph_matching.BackTracking.NewBackTracking;
import graph_matching.Hongrois.HungarianAlgorithmEdu;

import java.util.ArrayList;
import java.util.Random;

import org.ejml.simple.SimpleMatrix;


public class main {
	public static SimpleMatrix As;
	public static SimpleMatrix Am;
	public static ArrayList<Double> resultValues;
	public static void main(String[] args) {
		resultValues=new ArrayList<Double>();
		for(int k=0;k<1;k++){
		
		SimpleMatrix a = new SimpleMatrix(4,4);
		a.set(0, 0, 1);
		a.set(0, 1, 0);
		a.set(0, 2, 0);
		a.set(0, 3, 0);
		a.set(1, 0, 0);
		a.set(1, 1, 0);
		a.set(1, 2, 1);
		a.set(1, 3, 1);
		a.set(2, 0, 0);
		a.set(2, 1, 1);
		a.set(2, 2, 0);
		a.set(2, 3, 0);
		a.set(3, 0, 0);
		a.set(3, 1, 1);
		a.set(3, 2, 0);
		a.set(3, 3, 0);


		//ImportGraph.exportMatrixToCSV("data/retrmat1", matImport);
		
		double startTime = System.nanoTime();	
		
		//SOURCE IMPORT
		System.out.println("------IMPORT : SOURCE MATRIX (CSV FILES)------");
		startTime = System.nanoTime();	
		//Am=ImportGraph.removeZeros(ImportGraph.importGraphFromCSV("data/16"));
		//As=ImportGraph.removeZeros(ImportGraph.importGraphFromCSV("data/16"));
		Random rand = new Random();
		
		Am=SimpleMatrix.random(10,10,0,0,new Random());
		for(int i=0;i<Am.getMatrix().getNumRows();i++){
			for(int j=0;j<i;j++){
				int nombreAleatoire = rand.nextInt(2);
				Am.set(j, i, nombreAleatoire);
			}
		}
		Am=Am.plus(Am.transpose());
		Am=ImportGraph.removeZeros(Am);
		//Am=a;
		As=Am;
		//Am=ImportGraph.removeZeros(ImportGraph.importGraphFromCSV("data/rennes_extracted"));
		//As=ImportGraph.removeZeros(ImportGraph.importGraphFromCSV("data/rennes_acquisition"));

		//Am=ImportGraph.importAdjFromCSV("data/nantes_geom_modele");
		//As=ImportGraph.importAdjFromCSV("data/nantes_geom_modele");
		Am.print();
		As.print();

		double endTime = System.nanoTime();
		double totalTime = HungarianAlgorithmEdu.getTime(startTime, endTime);
		System.out.print("Total time required: ");
		HungarianAlgorithmEdu.printTime(totalTime);
		HungarianAlgorithmEdu.insertLines(1);
		System.out.println("------IMPORT : DONE------\n");
		
		
		//EIGEN DECOMPOSITION
		System.out.println("------DECOMPOSITION : EIGEN OF KROENECKER PRODUCTS------");
		startTime = System.nanoTime();	
		EVDLambda lambda=new EVDLambda(As,Am);
		endTime = System.nanoTime();
		totalTime = HungarianAlgorithmEdu.getTime(startTime, endTime);
		System.out.print("Total time required: ");
		HungarianAlgorithmEdu.printTime(totalTime);
		HungarianAlgorithmEdu.insertLines(1);
		System.out.println("------DECOMPOSITION : DONE------\n");
		
		//INITIALIZATION
		System.out.println("------INITIALIZATION : FRANCK-WOLFE ALGORITHM------");
		startTime = System.nanoTime();	
		double ns=As.numCols();
		double nm=Am.numCols();
		//SimpleMatrix P=SimpleMatrix.random((int)nm, (int)ns, 1.0/ns, 1.0/ns, new Random());
		SimpleMatrix P=SimpleMatrix.random((int)nm, (int)ns, 1.0/ns, 1.0/ns, new Random());
		P.print();
		//SimpleMatrix P=SimpleMatrix.identity(5);
		double gamma=0;
		double dgamma=0.00001;
		double epsilon=0.001;
		boolean convLV1=false;
		boolean convLV2=false;
		int iter=0;
		endTime = System.nanoTime();
		totalTime = HungarianAlgorithmEdu.getTime(startTime, endTime);
		System.out.print("Total time required: ");
		HungarianAlgorithmEdu.printTime(totalTime);
		HungarianAlgorithmEdu.insertLines(1);
		System.out.println("------INITIALIZATION : DONE------\n");
		
		ArrayList<ArrayList<Double>> valuesOfBT = new ArrayList<ArrayList<Double>>();
		

			/*FuEvalG.evaluate(a,lambda,1);
			double resultat=
					(
					P.transpose()
					.mult(P)
					.mult(main.As.transpose())
					.mult(P.transpose())
					.mult(P)
					.mult(main.As)
					)
					.trace();
			//System.out.println("RES1: "+resultat);
			resultat-=
					2.0*(
						main.Am.transpose()
						.mult(P)
						.mult(main.As)
						.mult(P.transpose())
						)
						.trace();
			//resultat+=a.transpose().mult(a).trace();
			System.out.println(FuEvalG.getResultat());
			System.out.println("RESULTAT DEPUIS FONCTION SIGMA : "+resultat);
			SimpleMatrix temp=Am.minus(P.mult(As).mult(P.transpose()));
			System.out.println("VALUE OF OBJECTIVE FUNCTION :"+(temp.transpose().mult(temp)).trace());
			FuEvalG.evaluate(P,lambda,1);
			System.out.println(FuEvalG.getResultat());*/
		
		
		
		while(!convLV1){
			iter=0;
			
			double oldValue=FuEvalG.getResultat();
			FuEvalG.evaluate(P,lambda,gamma);
			System.out.println("CONVERGENCE " + Math.abs(FuEvalG.getResultat()-oldValue));
			if(Math.abs(FuEvalG.getResultat()-oldValue)>0.1){
				dgamma/=2;
				if(gamma<0.00001){
					gamma=0.00001;
				}
				//FIND NEW P THAT MINIMIZES argmin(Fgammanew)
				while(!convLV2){
					//GRAD EVALUATION
					FuEvalG.evaluate(P,lambda,gamma);
					GradFuEvalG.evaluate(P,lambda,gamma);
				
					//HUNGARIAN TO FIND X
					System.out.println("------1st STEP : HUNGARIAN ALG FOR ARGMIN------");
					double[][] array = toArray(HungarianAlgorithmEdu.getPositive(GradFuEvalG.getResultat()));
					
					String sumType = "min";
					startTime = System.nanoTime();	
					int[][] assignment = new int[array.length][2];
					//System.out.println("PASSED");
					assignment = HungarianAlgorithmEdu.hgAlgorithm(array, sumType);
					endTime = System.nanoTime();
					totalTime = HungarianAlgorithmEdu.getTime(startTime, endTime);
					//System.out.print("Total time required: ");
					//HungarianAlgorithmEdu.printTime(totalTime);
					//HungarianAlgorithmEdu.insertLines(1);
					//NEW X
					SimpleMatrix X=SimpleMatrix.random((int)nm, (int)ns, 0, 0, new Random());
	
					for (int i=0; i<assignment.length; i++){
						X.set(assignment[i][0], assignment[i][1], 1);
					}
					
					//System.out.println((X.mult(GradFuEvalG.getResultat())).trace());
					System.out.println("------1st STEP : DONE------\n");
					SimpleMatrix temp1=Am.minus(P.mult(As).mult(P.transpose()));
					System.out.println("VALUE OF OBJECTIVE FUNCTION :"+(temp1.transpose().mult(temp1)).trace());
					//BACKTRACKING
					double alpha;
					startTime = System.nanoTime();	
					//System.out.println("------2nd STEP : BACKTRACKING ALG FOR ARGMIN------");
					NewBackTracking algoBT = new NewBackTracking(0,1,P,X,lambda,gamma);
					endTime = System.nanoTime();
					totalTime = HungarianAlgorithmEdu.getTime(startTime, endTime);
					//System.out.print("Total time required: ");
					//HungarianAlgorithmEdu.printTime(totalTime);
					//HungarianAlgorithmEdu.insertLines(1);
					alpha=algoBT.getAlpha();
					valuesOfBT.add(algoBT.getValues());
					
					//System.out.println("------2nd STEP : DONE------\n");
					
					//alpha=(double)(2.0)/((double)(iter+2));
					//System.out.println("ALPHA -------------    : "+alpha);
					
					
					//NEW P
					//System.out.println("------3rd STEP : TESTING P CONVERGENCE------");
					startTime = System.nanoTime();	
					//NEW VALUES WITH THE NEW P
					P=P.plus((X.minus(P)).scale(alpha));
					
					FuEvalG.evaluate(P,lambda,gamma);
					System.out.println(gamma+" "+alpha+" "+FuEvalG.getResultat());
					GradFuEvalG.evaluate(P,lambda,gamma);
					//CONVERGENCE RATIO
					double convergenceFactor=0;
					if((Math.abs(FuEvalG.getResultat()+GradFuEvalG.getResultat().transpose().mult(X.minus(P)).trace()))!=0){
						convergenceFactor=Math.abs((GradFuEvalG.getResultat().transpose().mult(P.minus(X)).trace()))/(Math.abs(FuEvalG.getResultat()+GradFuEvalG.getResultat().transpose().mult(X.minus(P)).trace()));
					}
					FuEvalG.evaluate(P,lambda,gamma-dgamma);
					double previous_result=FuEvalG.getResultat();
					FuEvalG.evaluate(P,lambda,gamma);
					//convergenceFactor=Math.abs(FuEvalG.getResultat()-previous_result);
					System.out.println("CONVERGENCE "+Math.abs(FuEvalG.getResultat()-previous_result));
					//System.out.println(convergenceFactor);
					convLV2=(convergenceFactor<=epsilon && iter>0);
					
					endTime = System.nanoTime();
					totalTime = HungarianAlgorithmEdu.getTime(startTime, endTime);
					//System.out.print("Total time required: ");
					//HungarianAlgorithmEdu.printTime(totalTime);
					//HungarianAlgorithmEdu.insertLines(1);
					//System.out.println("------3rd STEP : DONE------\n");
					
					iter++;
				}
			}else{
				dgamma*=2;
			}
		
			if(gamma+dgamma<1){
				
				
				convLV2=false;
			}else{
				convLV1=true;
			}
			gamma+=dgamma;
		}
		
		System.out.println("------ALGORITHM : ENDED------");
		System.out.println(iter+" iteration(s)");
		System.out.println("\nFinal permutation matrix P=");
		P.print();
		
		Am.print();
		SimpleMatrix temp1=Am.minus(P.mult(As).mult(P.transpose()));
		temp1.print();
		double objValue=(temp1.transpose().mult(temp1)).trace()*(1.0/P.getMatrix().getNumRows());
		System.out.println("VALUE OF OBJECTIVE FUNCTION :"+objValue);
		ImportGraph.exportMatrixToCSV("data/retrmat1", P.mult(As).mult(P.transpose()));
		endTime = System.nanoTime();
		totalTime = HungarianAlgorithmEdu.getTime(startTime, endTime);
		System.out.print("Total time required to find P: ");
		HungarianAlgorithmEdu.printTime(totalTime);
		HungarianAlgorithmEdu.insertLines(1);
		NewBackTracking.exportFunction(valuesOfBT);
		resultValues.add(objValue);
		ImportGraph.exportArray(resultValues);
		}
		
	}
	private static double[][] toArray(SimpleMatrix matrix) 
	{
	    double array[][] = new double[matrix.numRows()][matrix.numCols()];
	    for (int r=0; r<matrix.numRows(); r++)
	    { 
	        for (int c=0; c<matrix.numCols(); c++)
	        {
	            array[r][c] = matrix.get(r,c);
	        }
	    }
	    return array;
	}

}
