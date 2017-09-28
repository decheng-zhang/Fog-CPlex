package carleton;

import edu.princeton.cs.introcs.Draw;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.collections4.multimap.*;
@SuppressWarnings("deprecation")
public class Vicinity extends Algorithm{
	double []rx  =null;
	double []ry = null;
	double []w = null;
	static double vic  = 5;
	int k_count;

	/**
	 * @return the networkUsage
	 */
	public double getNetworkUsage() {
		return networkUsage;
	}
	Draw drawer=null;
	double [][]sites=null ;
	double mergelastime=0;
	Stopwatch timer1 =new Stopwatch();
	
	iterHashMap<Integer, List<Integer>,Integer> mapping=null;
	{
		//sites= new double[(Springs.Nodes).length][2];
		
		//System.out.println("what exactly that sites length" +String.valueOf(sites.length));
	}

	public Vicinity(Instance ins ,int k_cnt) {
		super(ins);
		instance = ins;
		k_count  = k_cnt;
		drawer = new Draw("Vicinity Algorithm-"+(k_cnt));
		drawer.setXscale(0, 100);
	    drawer.setYscale(0, 100);
	    drawer.setPenColor(Draw.BLUE);
	    drawer.setPenRadius(0.0025);
		rx = ins.rx;
		ry = ins.ry;
		w  = ins.w;
		
		sites = ins.Nodes;
		/*for ( int si=0; si<sites.length;si++) {
			sites[si][0] = sites[si][0]+50;
			sites[si][1] =sites[si][1]+50;
        }*/
		mapping = new iterHashMap<Integer, List<Integer>,Integer>();
	}
	@Deprecated
	public Vicinity( Draw d) {
		drawer = d;
		
		for ( int si=0; si<Springs.Nodes.length;si++) {
			sites[si][0] = Springs.Nodes[si][0]+50;
			sites[si][1] =Springs.Nodes[si][1]+50;
        }
		mapping = new iterHashMap<Integer, List<Integer>,Integer>();
		rx  = Springs.rx;
		ry = Springs.ry;
		w = Springs.w;
	}	
	

	public void Update() {
		super.Update();
		rx  = instance.rx;
		ry = instance.ry;
		
		
		if((elapsedTime()-mergelastime)> 1.5) {
			System.out.println("Time to grow vicinity radius");
			
			mergelastime = elapsedTime();
			mapping = new iterHashMap<Integer, List<Integer>,Integer>();
			updateCluster(k_count);
			vic= (vic>getThreshold())?vic:vic+5;
			System.out.println("Update finish, see any difference?");
		}
		
	}
	private double getThreshold(){
	
		return Helper.areaScale[0]/(Math.floor(Math.sqrt(k_count)));
	
	}
	private double elapsedTime(){
		return timer1.elapsedTime();
	}
	static double distance(double ix,double iy,double jx,double jy) {
		double rx = ix-jx;
		double ry = iy-jy;
		return Math.sqrt(rx*rx+ry*ry);
		
	}
	@Override
	public void iterate() {
		doneCheck();
		drawer.clear();
		
		Update();
		show();
		for (int ic = 0; ic < rx.length; ic++) {
            // draw a circle for each node
            drawer.filledCircle(rx[ic], ry[ic], 0.4);
            drawer.setFont(new Font("", Font.BOLD, 10));
            drawer.textLeft(rx[ic]+1, ry[ic]+1,String.valueOf(ic));
        }
		drawer.show(10);
		
	}
	@Override
	public void doneCheck(){
		exit = (vic>getThreshold())? true:false;
		
	}
	
	public void output() {
		//calculate cost
		List<FogEntity> flist = new ArrayList<FogEntity>();
		
		for(Integer k: mapping.keySet()) {
			double [] te = {sites[k][0],sites[k][1]};
			FogEntity e = new FogEntity(te);
			
			for(Integer v: mapping.get(k)) {
				e.addClient(v);
			}
			flist.add(e);
		}
		for(int i=0;i<rx.length;i++) {
			
			boolean found = false;
			for(Map.Entry<Integer,List<Integer>> entry:mapping.entrySet()) {
				if(entry.getValue().contains(new Integer(i))) {
					found =true;
					break;
				}
			}
			if(!found) {
				totalDelay+=getUserToCloudDelay(i);
				bandwidthToCloud +=instance.bandwidth[i];
				count++;
			}
			found = false;
			}
		super.outputend(drawer, flist);
		
	}
	private void updateCluster(int centroidnum){
		
		if (centroidnum<1){
			return;
		}
			else {
				updateCluster(centroidnum-1);
				System.out.println(centroidnum);
				double lastrate= 0;
				int index = -1;
				List<Integer> tempclis =null;
				Map<Integer,MultiValueMap<Integer, Integer>> maintanenceQuene=new HashMap<Integer,MultiValueMap<Integer,Integer>>();
				for(int i =0;i<sites.length;i++) {
					if (mapping.containsKey(new Integer(i))) {
						continue;}
					
					List<Integer> temlist= new ArrayList<Integer>();
					double hotrate = 0;
					int []bandwidth = instance.bandwidth;
					for(int j= 0;j<rx.length;j++) {
						//System.out.println(mapping.iterContainsValue(j));
						if(distance(sites[i][0],sites[i][1],rx[j],ry[j])>vic||notSuitForNewSite(i,j,maintanenceQuene)) {
							
							continue;}
						else{
							//System.out.println(">>>>>>"+String.valueOf(distance(sites[i][0],sites[i][1],rx[j],ry[j])));
							temlist.add(j);
							hotrate+=(double)bandwidth[j];
					}
						
						}
					
					if(hotrate>lastrate) {
						lastrate = hotrate;
						index = i;
						tempclis = temlist;
					}
					//System.out.println(String.valueOf(hotrate)+ "[["+ String.valueOf(lastrate));
				}
				if(index ==-1) {
					//System.out.println(centroidnum);
					System.out.println("no sites");
					//System.exit(1);	
				}
				else {
					//System.out.println("Adding site #" + String.valueOf(index));
					mapping.put(index, tempclis);
					if(maintanenceQuene.get(index)!=null){
					Iterator<Integer> mapIterator = maintanenceQuene.get(index).keySet().iterator();
					while (mapIterator.hasNext()) {
						int key = mapIterator.next();
						/*System.out.println("key:" + key + ", values=" + orderedMap.get(key));
						*/
						Collection<Integer> values = maintanenceQuene.get(index).getCollection(key);
						
						// iterate over the entries for this key in the map
						for(Iterator<Integer> entryIterator = values.iterator(); entryIterator.hasNext();) {
							int value = entryIterator.next();
							mapping.get(key).remove(new Integer(value));
							//System.out.println("    value:" + value);
						}
					
					}}
				}
				
			}
		
	}
	private boolean notSuitForNewSite(int siteIndex, int clientIndex,Map<Integer,MultiValueMap<Integer, Integer> >deleteQueue) {
		if(!mapping.iterContainsValue(clientIndex)) {
			return false;
		}
		else {
			if(mapping.iterContainsValueAt(clientIndex)!=null) {
				MultiValueMap<Integer,Integer> tempdeletelist=null;
				if(deleteQueue.containsKey(siteIndex)) {
					 tempdeletelist=deleteQueue.get(siteIndex);
				}else {
					tempdeletelist= new MultiValueMap<Integer,Integer> ();
				}
				int oldSiteIdx =mapping.iterContainsValueAt(clientIndex)[0];
				int oldClientidx = mapping.iterContainsValueAt(clientIndex)[1];
				if(distance(sites[oldSiteIdx][0],sites[oldSiteIdx][1],rx[clientIndex],ry[clientIndex])>
					distance(sites[siteIndex][0],sites[siteIndex][1],rx[clientIndex],ry[clientIndex])) {
					if(mapping.get(oldSiteIdx).get(oldClientidx).equals(new Integer(clientIndex))){
						tempdeletelist.put(oldSiteIdx, clientIndex);
						deleteQueue.put(siteIndex,tempdeletelist);
						return false;
					}else {
						System.out.println("not match");
						System.exit(1);
						
						}
					}
				return true;
				}
			System.out.println("get iterContainsValueAt is null");
			System.exit(1);
			
		}
		return false;
	}
	void show() {
		for(Integer k: mapping.keySet()) {
			drawer.filledCircle(sites[k][0],sites[k][1], 1);
			drawer.textLeft(sites[k][0]+5, sites[k][1], String.valueOf(k));
			for(Integer v: mapping.get(k)) {
				drawer.line(sites[k][0], sites[k][1], rx[v]	, ry[v]);
				double tempdistance = distance(sites[k][0], sites[k][1], rx[v]	, ry[v]);
				networkUsage+= w[v]*tempdistance;
				//totalDelay+=getUserToFogDelay(v, tempdistance);
				//bandwidthToCloud += instance.bandwidth[v]*Helper.percent2Cloud;
				
			}
			
			
		}
		drawer.setFont(new Font("", Font.BOLD, 20));
		drawer.text(35.0, 5.0, String.valueOf(getNetworkUsage()));
		for ( int si=0; si<sites.length;si++) {
        	drawer.filledSquare(sites[si][0],sites[si][1], 1.0);
        	drawer.setFont(new Font("", Font.BOLD, 10));
        	drawer.textLeft(sites[si][0], sites[si][1], String.valueOf(si));
        	
        }
	}
}
