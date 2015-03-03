package graph_matching;

import java.util.ArrayList;
import java.util.Collections;

import org.ejml.simple.SimpleEVD;
import org.ejml.simple.SimpleMatrix;
import org.ejml.data.Complex64F;

import com.google.common.primitives.Doubles;

public class EVDLambda {
	
	private SimpleMatrix U1;
	private SimpleMatrix U2;
	private SimpleMatrix lambda1;
	private SimpleMatrix lambda2;
	
	
	private ArrayList<Complex64F> eigenva1;
	private ArrayList<Complex64F> eigenva2;

	private double C1C;
	private double C2C;
	private double C1V;
	private double C2V;
	
	public EVDLambda(SimpleMatrix As, SimpleMatrix Am){
		SimpleMatrix asas=As;
		SimpleMatrix asam=Am;
		//AS KROEN AS
				SimpleEVD<SimpleMatrix> decAsAs=asas.eig();
				U1=new SimpleMatrix(decAsAs.getNumberOfEigenvalues(),decAsAs.getNumberOfEigenvalues());
				for(int i=0;i<decAsAs.getNumberOfEigenvalues();i++){
					double eigenve []=decAsAs.getEigenVector(i).getMatrix().getData();
					U1.setColumn(i, 0, eigenve);
				}
				eigenva1=new ArrayList<Complex64F>();
				for(int i=0;i<decAsAs.getNumberOfEigenvalues();i++){
						eigenva1.add(decAsAs.getEigenvalue(i));
				}
				
				//lambda1 = SimpleMatrix.diag(Doubles.toArray(getKroneckerEigenValues(eigenva1,eigenva1)));

				
				//AS KROEN AM
				SimpleEVD<SimpleMatrix> decAsAm=asam.eig();
				
				U2=new SimpleMatrix(decAsAm.getNumberOfEigenvalues(),decAsAm.getNumberOfEigenvalues());
				for(int i=0;i<decAsAm.getNumberOfEigenvalues();i++){
					double eigenve []=decAsAm.getEigenVector(i).getMatrix().getData();
					U2.setColumn(i, 0, eigenve);
				}
				eigenva2=new ArrayList<Complex64F>();
				lambda2=null;
				for(int i=0;i<decAsAm.getNumberOfEigenvalues();i++){
						eigenva2.add(decAsAm.getEigenvalue(i));
				}
				//lambda2 = SimpleMatrix.diag(Doubles.toArray(eigenva2));
				
				double temp=0;
				temp=Collections.max(getKroneckerEigenValues(eigenva1,eigenva1));
				if(temp>0){
					C1C=-temp;
				}else{
					C1C=0;
				}
				temp=Collections.min(getKroneckerEigenValues(eigenva1,eigenva2));
				if(temp<0){
					C2C=2*temp;
				}else{
					C2C=0;
				}
				temp=Collections.min(getKroneckerEigenValues(eigenva1,eigenva1));
				if(temp<0){
					C1V=-temp;
				}else{
					C1V=0;
				}
				temp=Collections.max(getKroneckerEigenValues(eigenva1,eigenva2));
				if(temp>0){
					C2V=2*temp;
				}else{
					C2V=0;
				}

				System.out.println(getKroneckerEigenValues(eigenva1,eigenva1));
				System.out.println("C1C "+C1C+" C2C "+C2C+" C1V "+C1V+" C2V "+C2V);
				/*
				SimpleMatrix AsKAs = asas.kron(asas);
				SimpleEVD<SimpleMatrix> EAsKAs=AsKAs.eig();
				SimpleMatrix KU1=new SimpleMatrix(EAsKAs.getNumberOfEigenvalues(),EAsKAs.getNumberOfEigenvalues());
				for(int i=0;i<EAsKAs.getNumberOfEigenvalues();i++){
					double eigenve []=EAsKAs.getEigenVector(i).getMatrix().getData();
					KU1.setColumn(i, 0, eigenve);
				}
				KU1=KU1.transpose();
				ArrayList<Double> VAskAs=new ArrayList<Double>();
				for(int i=0;i<EAsKAs.getNumberOfEigenvalues();i++){
					VAskAs.add(EAsKAs.getEigenvalue(i).getReal());
				}
				C1C=-Collections.max(VAskAs);
				C2C=2.0*Collections.min(VAskAs);
				U1=KU1;
				lambda1=SimpleMatrix.diag(Doubles.toArray(VAskAs));
				
				SimpleMatrix AsKAm = asas.kron(asas);
				SimpleEVD<SimpleMatrix> EAsKAm=AsKAm.eig();
				SimpleMatrix KU2=new SimpleMatrix(EAsKAm.getNumberOfEigenvalues(),EAsKAm.getNumberOfEigenvalues());
				for(int i=0;i<EAsKAm.getNumberOfEigenvalues();i++){
					double eigenve []=EAsKAm.getEigenVector(i).getMatrix().getData();
					KU2.setColumn(i, 0, eigenve);
				}
				KU2=KU2.transpose();
				ArrayList<Double> VAskAm=new ArrayList<Double>();
				for(int i=0;i<EAsKAm.getNumberOfEigenvalues();i++){
					VAskAm.add(EAsKAm.getEigenvalue(i).getReal());
				}

				C1V=-Collections.min(VAskAm);
				C2V=2.0*Collections.max(VAskAm);
				U2=KU2;
				lambda2=SimpleMatrix.diag(Doubles.toArray(VAskAm));*/
				/*TEST OF EIGEN*/
				
				
	}
	public double getC1C() {
		return C1C;
	}

	public double getC2C() {
		return C2C;
	}

	public double getC1V() {
		return C1V;
	}

	public double getC2V() {
		return C2V;
	}
	public SimpleMatrix getU1() {
		return U1;
	}
	public SimpleMatrix getU2() {
		return U2;
	}
	public SimpleMatrix getLambda1() {
		return lambda1;
	}
	public SimpleMatrix getLambda2() {
		return lambda2;
	}

	public int getNumEigenVal1(){
		return eigenva1.size();
	}
	public int getNumEigenVal2(){
		return eigenva2.size();
	}
	private ArrayList<Double> getKroneckerEigenValues(ArrayList<Complex64F> valuesA, ArrayList<Complex64F> valuesB){
		ArrayList<Double> result = new ArrayList<Double>();
		for(int i=0;i<valuesA.size();i++){
			for(int j=0;j<valuesB.size();j++){
				Complex64F temp=new Complex64F();
				temp.setReal(valuesA.get(i).getReal()*valuesB.get(j).getReal()-valuesA.get(i).getImaginary()*valuesB.get(j).getImaginary());
				temp.setImaginary(valuesA.get(i).getReal()*valuesB.get(j).getImaginary()+valuesA.get(i).getImaginary()*valuesB.get(j).getReal());
				if(temp.getImaginary()==0){
					result.add(temp.getReal());
				}
			}
		}
		return result;
		
	}


}
