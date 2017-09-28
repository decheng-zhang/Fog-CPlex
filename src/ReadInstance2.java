
import carleton.Instance;
import carleton.InstanceBeta;

public class ReadInstance2 {
	private String fileName_;         // name of the file containing the instance
	  private int [][]   a_matrix;   // a matrix describing the problem
	  private int [][][] b_matrixs;  // k b matrixs describing the problem
	   public int fogs = -1;          // number of facilities
	   public int objectives_ = 2;          // number of objectives  
	   public int clients = -1;
	   
	   double [][][] d_matrix;
	   
	   int [][] cpu_demand;

	   int [][] bandwidth_demand;
	
	public ReadInstance2(String name) {
		InstanceBeta r = new InstanceBeta(30,10,name);
		fogs = r.fogs;
		clients = r.clents;
		d_matrix = new double[r.season_cnt][clients][fogs+1];
		cpu_demand = r.cpu;
		bandwidth_demand = r.bandwidth;
		for(int k=0;k<r.season_cnt;k++) {
		for(int i=0;i<clients;i++){
			for(int j = 0;j<fogs;j++){
				double dist = r.distClientToFog(i,j);
				double b = (double)r.packets[k][i]*12000/((double)r.wifiType[k][i]*1000000)+ dist/177000 +0.0000125;
				assert b >0.0: "b is hahha";
				d_matrix[k][i][j] =b*1000000 ;
			}
			double a = (double)r.bandwidth[k][i]*8;
			//System.out.println("bandwith in bits "+ (a));
			double t = a/((double)r.wifiType[k][i]*1000000)+ 0.0056+ 0.000075;
			assert Double.compare(t, 0.0) > 0: "hahaha "+a;
			d_matrix[k][i][fogs] =t*1000000 ;
		}
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
