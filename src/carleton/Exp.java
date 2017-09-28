package carleton;

import java.util.concurrent.CountDownLatch;

public class Exp implements Runnable{
	private final CountDownLatch doneSignal;
	Algorithm algorithm =null;
	Instance isinexp = null;
	double oldUsage = 0;
	String name;
	private volatile boolean exit = false;
	public Exp(Instance is, String al, Boolean showOrNot,CountDownLatch doneSignal) {
		this.doneSignal = doneSignal;
		name = al;
		isinexp = is;
		init(al);
	}
	public void stop() {
		exit = true;
	}
	public void run() {
		while(!exit) {
			algorithm.iterate();
			
			if(algorithm.done()) {
				this.stop();
			}
		}
		algorithm.output();
		//String indent = "	";
		
		Object []tems  = {name,(algorithm.getTotalDelay()*1000),(algorithm.getBandwidthToCloud()/1000000),(algorithm.getCost()),(algorithm.getNetworkUsage()),(algorithm.getInnerDelay()),(algorithm.drop)};
		//System.out.println(name+(algorithm.getTotalDelay()*1000)+(algorithm.getBandwidthToCloud()/1000000)+(algorithm.getCost())+(algorithm.getNetworkUsage()));
		isinexp.results.add(tems);
		doneSignal.countDown();
		Thread.currentThread().interrupt();
		
	}
	protected void init(String alg) {
		String[] parts = alg.split("-");
		
		switch(parts[0].toLowerCase()) {
		case "spring" :
			algorithm = new Springs(isinexp,Integer.parseInt(parts[1]));
			break;
		case "vicinity" :
			algorithm = new Vicinity(isinexp,Integer.parseInt(parts[1]));
			break;
		case "kmean":
			algorithm = new Kmeans(isinexp,Integer.parseInt(parts[1]));
			break;
		}
	}
	   public static void main(String args[]) {
		   Instance t = new Instance(50,6,null);
		   for(int i=1;i<8;i++) {
	        (new Thread(new Exp(t,("spring-"+3),true))).start();
		   }
	       // (new Thread(new Exp(new Instance(50,6),"vicinity-4",true))).start();
	        //(new Thread(new Exp(new Instance(50,6),"kmean-4",true))).start();
	    }
}
