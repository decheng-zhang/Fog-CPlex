
public class cplexRunner {

	public static void main(String[] args) {
		for(int i = 16;i<21;i++) {
			for(int j =2;j<3;j++) {
				String input = (i*5)+"-"+(j*5)+"__ver1-try";
				//FogPlanningProblem fpp = new FogPlanningProblem(input,10);// TODO Auto-generated method stub
				FPPProblem fpp = new FogPlanningProblem (input,10)
				fpp.solve("aug27");
			}
		}
		
	}

}
