package carleton;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.*;

import edu.princeton.cs.introcs.Draw;

public class Kmeans extends Algorithm{
	double []rx  =null;
	double []ry = null;
	double []w  = null;
	int k_count;
	double [][] sites = null;
	List<Kmeanpoint> clientsloca=null;
	Draw d = null;
	Stopwatch timer1 =new Stopwatch();

	//double networkUsage;
	KMeansPlusPlusClusterer<Kmeanpoint> clusterer;
	List<CentroidCluster<Kmeanpoint>> clusterResults=null;
	//List<CentroidCluster<Kmeanpoint>> lastClusterResults = null;

	
	//TODO deal with correct parameter

	public Kmeans(Instance ins,int k_cnt) {
		
		super(ins);
		k_count = k_cnt;
		d = new Draw("Kmean Algorithm-"+(k_cnt));
		 d.setXscale(0, 100);
	     d.setYscale(0, 100);
	     d.setPenColor(Draw.BLUE);
	     d.setPenRadius(0.0025);
		rx = ins.rx;
		ry = ins.ry;
		w  = ins.w;
		
		sites = ins.Nodes;
		clientsloca = new ArrayList<Kmeanpoint>(rx.length);
		clusterer = new KMeansPlusPlusClusterer<Kmeanpoint>(k_cnt, 10000);
		instance =ins;
	
	}

	
	private List<Kmeanpoint> updateClients(){
		List<Kmeanpoint> temploclist = new ArrayList<Kmeanpoint>(rx.length);
		rx  = instance.rx;
		ry = instance.ry;
		
		for(int i=0;i<rx.length;i++) {
			double []point = {rx[i],ry[i]};
			temploclist.add(new Kmeanpoint(point,w[i],i));
		}
		return temploclist;
	}
	@Override
	void iterate () {
		d.clear();
		Update();
		draw();
        for (int ic = 0; ic < rx.length; ic++) {
            // draw a circle for each node
        	
            d.filledCircle(rx[ic], ry[ic], 0.4);
            d.setFont(new Font("", Font.BOLD, 10));
            d.textLeft(rx[ic]+1, ry[ic]+1,String.valueOf(ic));

        }
		d.show(10);
		doneCheck();
		
	}
	@Override
	public void doneCheck(){
		if(timer1.elapsedTime()>Helper.kmeanTimeout)
			exit = true;
	}
	void Update() {
		super.Update();
		caluNetworkUsage();
	}
	@Override
	void output() {
		List<FogEntity> flist = new ArrayList<FogEntity>();
		//calculate cost;
		for(int i =0;i<k_count;i++) {
		double []po = clusterResults.get(i).getCenter().getPoint();
		FogEntity e = new FogEntity(super.closestProjector(po));
	
		for(Kmeanpoint point:clusterResults.get(i).getPoints()) {
			e.addClient(point.getIdx());	
			
			}
		flist.add(e);
		}
		
		cost = calCost(flist);
		//calculate delay;
		//bandwidthToCloud = 0.0;
		for(FogEntity f:flist) {
			
			for(Integer i:f.clientIdxList) {
				
				double []ic = {instance.rx[i],instance.ry[i]};
				if(!secondDropList.contains(i)) {
				totalDelay +=getUserToFogDelay(i,f.getDist2Client(ic));
				
				bandwidthToCloud += (double)instance.bandwidth[i]*Helper.percent2Cloud;
				
			}else {
				totalDelay +=getUserToCloudDelay(i);
				bandwidthToCloud += (double)instance.bandwidth[i];
				
			}
				
		}
		finalShow(d,flist);
		//calculate bandwidth to cloud;
	}
	}
	private double caluNetworkUsage() {
		List<CentroidCluster<Kmeanpoint>> tempclusterResults=null;
		double nu =0;
		clientsloca = updateClients();
		tempclusterResults=clusterer.cluster(clientsloca);
		for(int i =0;i<k_count;i++) {
			double []po = tempclusterResults.get(i).getCenter().getPoint();
			
			for(Kmeanpoint point:tempclusterResults.get(i).getPoints()) {
				double[] clpo = point.getPoint();
				double weight = point.getWeight();
				int idx = point.getIdx();
				double dx =clpo[0]-po[0];
				double dy = clpo[1]-po[1];
				double distance =Math.sqrt(dx*dx+dy*dy);
				nu += distance*weight;
				
			}	
		}
		if(lastNetworkUsage> nu) {
			lastNetworkUsage=nu;
			
			//System.out.println("Kmean update occurs");
			clusterResults = tempclusterResults;
			}
		networkUsage  = nu;
		return nu;
	}
	void draw() {
		
		for(int i =0;i<k_count;i++) {
			double []po = clusterResults.get(i).getCenter().getPoint();
			d.filledCircle(po[0], po[1], 1);
			d.textLeft(po[0]+5, po[1]+5, String.valueOf(i));
			for(Kmeanpoint point:clusterResults.get(i).getPoints()) {
				double[] clpo = point.getPoint();
				
				d.line(clpo[0], clpo[1], po[0]	, po[1]);
			
		
			}	
		}
		for ( int si=0; si<sites.length;si++) {
        	d.filledSquare(sites[si][0],sites[si][1], 1.0);
        	d.setFont(new Font("", Font.BOLD, 10));
        	d.textLeft(sites[si][0], sites[si][1], String.valueOf(si));
		
		d.setFont(new Font("", Font.BOLD, 20));
		d.text(35.0, 5.0, String.valueOf(networkUsage));
	}

}
}