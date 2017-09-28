package carleton;

import org.apache.commons.math3.ml.clustering.DoublePoint;

public class Kmeanpoint extends DoublePoint {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double w ;
	int index;
	public Kmeanpoint(double[]location,double weight,int idx){
			super(location);
			w = weight;
			index =idx;
		}
	public double getWeight() {
		return w;
	}
	public int getIdx() {
		return index;
	}
}
