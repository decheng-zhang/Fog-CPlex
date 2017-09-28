
import carleton.Instance;
import carleton.InstanceBeta;

public class ReadInstance {
	private String fileName_;         // name of the file containing the instance
	  private int [][]   a_matrix;   // a matrix describing the problem
	  private int [][][] b_matrixs;  // k b matrixs describing the problem
	   public int fogs = -1;          // number of facilities
	   public int objectives_ = 2;          // number of objectives  
	   public int clients = -1;
	   double [][] d_matrix;
	   double [][][] d2_matrix;
	   int [] cpu_demand;
	   int [][] cpu2_demand;
	   int [] bandwidth_demand;
	   int [][] bandwidth2_demand;
	//Instance r = null;
	public ReadInstance(String name){
		Instance r = new Instance(30,10,name);//instance scale here doesn't matter, file will always dominate the instance
		fogs = r.fogs;
		clients = r.clents;
		d_matrix = new double[clients][fogs+1];
		cpu_demand = r.cpu;
		bandwidth_demand = r.bandwidth;
		for(int i=0;i<clients;i++){
			for(int j = 0;j<fogs;j++){
				double dist = r.distClientToFog(i,j);
				double b = (double)r.packets[i]*12000/((double)r.wifiType[i]*1000000)+ dist/177000 +0.0000125;
				assert b >0.0: "b is hahha";
				d_matrix[i][j] =b*1000000 ;
			}
			double a = (double)r.bandwidth[i]*8;
			//System.out.println("bandwith in bits "+ (a));
			double t = a/((double)r.wifiType[i]*1000000)+ 0.0056+ 0.000075;
			assert Double.compare(t, 0.0) > 0: "hahaha "+a;
			d_matrix[i][fogs] =t*1000000 ;
		}
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
