package carleton;

import java.util.ArrayList;
import java.util.List;

public class FogEntity {
	List<Integer> clientIdxList;
	int fogtype=-1;
	double[] location =new double [2];
	FogEntity(double[] lo){
		clientIdxList= new ArrayList<Integer>();
		location = lo;
	}
	/**
	 * @return the fogtype
	 */
	public int getFogtype() {
		return fogtype;
	}
	/**
	 * @param fogtype the fogtype to set
	 */
	public void setFogtype(int fogtype) {
		this.fogtype = fogtype;
	}
	public void addClient(int idx){
		clientIdxList.add(idx);
	}
	public List<Integer> getClients() {
		return clientIdxList;
	}
	public double[]	getLocation() {
		return location;
	}
	public double getDist2Client(double[] cli) {
		double tempdx = cli[0]-location[0];
		double tempdy = cli[1]-location[1];
			double dist=Math.sqrt(tempdx*tempdx+tempdy*tempdy);
			return dist;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
