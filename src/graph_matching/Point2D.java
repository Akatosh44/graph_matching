package graph_matching;

public class Point2D {
	private double cordx;
	private double cordy;
	
	public Point2D(){
		
	}
	public Point2D(double x, double y){
		setCordx(x);
		setCordy(y);
	}
	@Override
	public String toString(){
		return "("+this.getCordx()+","+this.getCordy()+")";
	}
	public double getCordx() {
		return cordx;
	}
	public void setCordx(double cordx) {
		this.cordx = cordx;
	}
	public double getCordy() {
		return cordy;
	}
	public void setCordy(double cordy) {
		this.cordy = cordy;
	}
}
