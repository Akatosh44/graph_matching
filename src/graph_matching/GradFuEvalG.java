package graph_matching;
import org.ejml.simple.SimpleMatrix;

public class GradFuEvalG{

	private static SimpleMatrix resultat;

	public static void evaluate(SimpleMatrix P, EVDLambda lambda, double gamma){
		resultat=new SimpleMatrix();
		resultat=(P.mult(((main.As.transpose().mult(P.transpose()).mult(P).mult(main.As)).plus(main.As.mult(P.transpose()).mult(P).mult(main.As.transpose()))))).scale(2.0);
		resultat=resultat.minus((main.Am.mult(P).mult(main.As.transpose()).plus(main.Am.transpose().mult(P).mult(main.As))).scale(2));
		resultat=resultat.plus((P.mult(P.transpose()).mult(P)).scale(4.0*((1-gamma)*lambda.getC1V()+gamma*lambda.getC1C())));
		resultat=resultat.plus(P.scale(2.0*((1-gamma)*lambda.getC2V()+gamma*lambda.getC2C())));
	}
	
	public static SimpleMatrix getResultat() {
		return resultat;
	}
}
