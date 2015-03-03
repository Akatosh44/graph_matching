package graph_matching;

import java.util.Random;

import org.ejml.simple.SimpleMatrix;


public class FuEvalG {

	private static double resultat;
	
	public static void evaluate(SimpleMatrix P,EVDLambda lambda, double gamma){
	
		/*double resultC=0;
		resultC+=(FuEval.getVecPtp().transpose().mult(lambda.getU1()).mult(lambda.getLambda1().plus(SimpleMatrix.identity(lambda.getNumEigenVal1()).scale(lambda.getC1C()))).mult(lambda.getU1().transpose()).mult(FuEval.getVecPtp())).trace();
		resultC+=(FuEval.getVecP().transpose().mult(lambda.getU2()).mult((SimpleMatrix.identity(lambda.getNumEigenVal2()).scale(lambda.getC2C()).minus(lambda.getLambda2().scale(2)))).mult(lambda.getU2().transpose()).mult(FuEval.getVecP())).trace();
		
		double resultV=0;
		resultV+=(FuEval.getVecPtp().transpose().mult(lambda.getU1()).mult(lambda.getLambda1().plus(SimpleMatrix.identity(lambda.getNumEigenVal1()).scale(lambda.getC1V()))).mult(lambda.getU1().transpose()).mult(FuEval.getVecPtp())).trace();
		resultV+=(FuEval.getVecP().transpose().mult(lambda.getU2()).mult((SimpleMatrix.identity(lambda.getNumEigenVal2()).scale(lambda.getC2V()).minus(lambda.getLambda2().scale(2)))).mult(lambda.getU2().transpose()).mult(FuEval.getVecP())).trace();
	
		resultat=((1-gamma)*resultV+gamma*resultC);
		*/
		
		resultat=
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
		//System.out.println("RES2: "+resultat);
		//resultat+=(main.Am.transpose().mult(main.Am)).trace();
		//System.out.println("RES2: "+resultat);
		resultat+=
				(
				(1-gamma)*lambda.getC1V()
				+gamma*lambda.getC1C()
				)
				*
				(
				P.transpose()
				.mult(P)
				.mult(P.transpose())
				.mult(P)
				)
				.trace();
		//System.out.println("RES3: "+resultat);
		resultat+=
				(
				(1-gamma)*lambda.getC2V()
				+gamma*lambda.getC2C()
				)
				*
				(
				P.transpose()
				.mult(P)
				)
				.trace();
		//System.out.println("RES4: "+resultat);
		//resultat-=((1-gamma)*(lambda.getC1V()+lambda.getC2V())+gamma*(lambda.getC1C()+lambda.getC2C()))*(double)(main.Am.getMatrix().getNumCols());
		//System.out.println("RES4: "+resultat);

		
		//NEW EVAL
		/*SimpleMatrix I1 = SimpleMatrix.identity(lambda.getLambda1().getMatrix().getNumRows());
		SimpleMatrix I2 = SimpleMatrix.identity(lambda.getLambda2().getMatrix().getNumRows());
		SimpleMatrix fuc=(vec(P.transpose().mult(P)).transpose()
			    .mult(
				lambda.getU1().transpose())
				.mult(
				lambda.getLambda1().plus(I1.scale(lambda.getC1C()))))
				.mult(lambda.getU1())
				.mult(vec(P.transpose().mult(P)))
				.plus(
			    vec(P).transpose().mult(
				lambda.getU2().transpose())
				.mult(
				(I2.scale(lambda.getC2C())).minus(lambda.getLambda2().scale(2.0)))
				.mult(
				lambda.getU2()).mult(vec(P)));
		SimpleMatrix fuv=(vec(P.transpose().mult(P)).transpose()
			    .mult(
				lambda.getU1().transpose())
				.mult(
				lambda.getLambda1().plus(I1.scale(lambda.getC1V()))))
				.mult(lambda.getU1())
				.mult(vec(P.transpose().mult(P)))
				.plus(
			    vec(P).transpose().mult(
				lambda.getU2().transpose())
				.mult(
				(I2.scale(lambda.getC2V())).minus(lambda.getLambda2().scale(2.0)))
				.mult(
				lambda.getU2()).mult(vec(P)));
		SimpleMatrix linGam=(fuv.scale(1-gamma)).plus(fuc.scale(gamma));
		//linGam.print();
		resultat=linGam.get(0,0);*/
	}
	public static double getResultat() {
		return resultat;
	}
	
	private static SimpleMatrix vec(SimpleMatrix mat){
		//mat.print();
		SimpleMatrix result = new SimpleMatrix(mat.getMatrix().getNumRows()*mat.getMatrix().getNumRows(),1);
		int index=0;
		for(int j=0;j<mat.getMatrix().getNumCols();j++){
			for(int i=0;i<mat.getMatrix().getNumRows();i++){
				result.set(index,0,mat.get(i,j));
				index++;
			}
		}
		//result.print();
		return result;
	}


}
