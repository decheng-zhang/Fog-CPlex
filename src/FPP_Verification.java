
public class FPP_Verification {

	static int findMinIndex(double [] t) {
		int min =0;
		for(int i=1;i<t.length;i++) {
			if (t[i]<t[min]) {
				min = i;
			}
		}
		return min;
	}
	public static void main(String[] args) {
		FogPlanningProblem test = new FogPlanningProblem();
		double[][] d = test.delay_matrix;// TODO Auto-generated method stub
		for(int i=0;i<d.length;i++) {
			int t = findMinIndex(d[i]);
			System.out.println("customer choice:  "+ (t+1));
			
		}
		
	}

}
