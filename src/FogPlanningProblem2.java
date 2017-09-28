import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class FogPlanningProblem2 extends FPPProblem{
	static final int[] fogprice	={0,67200,120000,170000,250000};
	static final int[]	fogcpu = {0,90,180,360,720};
	static final int[] linkprice = {0,225,1800,18000};
	static final int[]	linkCp = {0,13107200,125000000,1250000000};
	static final int rent = 1000;
	
	static final int est_size =1;
	int clients_tem=-1;
	int fogs_tem=-1;
	String problemFileName;
	ReadInstance2 ri;
	public double[][][] delay_matrix;
	public double maxTotalDelay_;
	public double minTotalDelay_;
	public double maxCost_;
	public int [][]demand_vector;
	public int [][]traffic_vector;
	public double[][] parametrics;
	public double[] params;
	public double[][] results ;
	public double dMax(double[][] A) {
		double tA=0.0;
		for(int i=0;i<A.length;i++) {
			tA +=findMax(A[i]);
		}
		return tA;
	}
	public double dMin(double[][] A) {
		double tA=0.0;
		for(int i=0;i<A.length;i++) {
			tA +=findMin(A[i]);
		}
		return tA;
	}
	public double findMax(double[] a) {
		 double max = a[0];
		    for (int ktr = 0; ktr < a.length; ktr++) {
		        if (a[ktr] > max) {
		            max = a[ktr];
		        }
		    }
		    return max;
	}
	public double findMin(double[] a) {
		 double min = a[0];
		    for (int ktr = 0; ktr < a.length; ktr++) {
		        if (a[ktr] < min) {
		            min = a[ktr];
		        }
		    }
		    return min;
	}
	public FogPlanningProblem2(String fileName,int granucount) {
		problemFileName=fileName;
		ri = new ReadInstance2(problemFileName);
		clients_tem = ri.clients;
		fogs_tem = ri.fogs;
		delay_matrix = ri.d_matrix;
		System.out.println("delaysss: " );
		for(int i =0;i<delay_matrix.length;i++)
			System.out.println((i+1) +"----"+Arrays.toString(delay_matrix[i]));
		demand_vector = ri.cpu_demand;
		traffic_vector = ri.bandwidth_demand;
		maxTotalDelay_ =449359;//dMax(ri.d_matrix);
		minTotalDelay_ =282653;//dMin(ri.d_matrix);
		maxCost_ = 2558825;//ri.fogs* (fogprice[fogprice.length-1]+rent+ linkprice[linkprice.length-1]);
		double step_size = Math.round(1000.0/granucount)/1000.0;
		parametrics = new double[granucount+1][2];
		results = new double[granucount+3][3];
		params = new double[2];
		for(int i=0;i<granucount+1;i++) {
			parametrics[i][0]= step_size*i;
			parametrics[i][1]=1.0-step_size*i;
		}
		
	}
	public void solve(String outputPrefix) {
		long initTime = System.currentTimeMillis();
		double[]temp = solveMe(1);
		maxTotalDelay_=temp[1];
		results[0]=temp;
		temp =solveMe(2);
		maxCost_ = temp[0];
		minTotalDelay_ = temp[1];
		results[1] = temp;
		for(int i=0;i<parametrics.length;i++) {
			params = parametrics[i];
			results[i+2]=solveMe(3);
		}
		long estimatedTime = System.currentTimeMillis()-initTime;
		System.out.println(Arrays.deepToString(results));
		System.out.println("Total execution time:  "+ estimatedTime + "ms");
		writeResult(outputPrefix+"_"+problemFileName+"_time_"+estimatedTime);
	}
	public void writeResult(String ofile) {
		try {
			FileOutputStream fos = new FileOutputStream("output/"+ofile);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write("MaxDelay, MinDelay and MaxCost ");
			for(int i=0;i<2;i++) {
				bw.newLine();
				double[] rs = results[i];
				bw.write(rs[0]+" "+rs[1]+" "+rs[2]);
				
			}
			bw.newLine();
			bw.write("cost "+"delay "+"gap");
			bw.newLine();
			for(int i=2;i<results.length;i++) {
				double[] rs = results[i];
				bw.write(rs[0]+" "+rs[1]+" "+rs[2]);
				
				bw.newLine();
				
			}
			bw.close();
		}catch(IOException e) {
			System.out.println("error writing file");
			e.printStackTrace();
		}
	}
	
	// trigger = 1:maxdelay 2:maxcostAndMinDelay 3 solving
	public double[] solveMe(int trigger) {
		double [] out = null ;
		try{
			//FogPlanningProblem test = new FogPlanningProblem();
			IloCplex cplex = new IloCplex();
			cplex.readParam("src/config.prm");
			IloNumVar[][] x = new IloNumVar[this.clients_tem][this.fogs_tem+1];
			for(int i = 0;i<this.clients_tem;i++) {
				x[i] =cplex.boolVarArray(this.fogs_tem+1);
			}
			
			IloNumVar[][] y = new IloNumVar[this.fogs_tem][fogprice.length];
			for (int i = 0;i<this.fogs_tem;i++) {
				y[i] = cplex.boolVarArray(fogprice.length);
			}
			IloNumVar[][] z = new IloNumVar[this.fogs_tem][linkprice.length];
			for (int i = 0;i<this.fogs_tem;i++) {
				z[i] = cplex.boolVarArray(linkprice.length);
			}
			IloLinearNumExpr obj1 = cplex.linearNumExpr();
			for(int i =0;i<this.fogs_tem;i++) {
				for(int j=1;j<fogprice.length;j++)
				obj1.addTerm(rent, y[i][j]);
			}
			for(int i =0;i<this.fogs_tem;i++) {
				for(int j=0;j<fogprice.length;j++)
				obj1.addTerm(fogprice[j], y[i][j]);
			}
			for(int i =0;i<this.fogs_tem;i++) {
				for(int j=0;j<linkprice.length;j++)
				obj1.addTerm(linkprice[j], z[i][j]);
			}
			
			
			IloLinearNumExpr obj2 = cplex.linearNumExpr();
			for(int i =0;i<this.fogs_tem+1;i++) {
				for(int j=0;j<this.clients_tem;j++)
					obj2.addTerm(this.delay_matrix[j][i], x[j][i]);// TODO Auto-generated method stub
			}//
			//Normalize two objectives
			IloNumExpr obj_1=cplex.prod(1.0/(this.maxCost_), obj1);
			IloNumExpr obj_2 = cplex.prod(1.0/(this.maxTotalDelay_-this.minTotalDelay_),cplex.abs(cplex.diff(this.minTotalDelay_,obj2)));
			//IloNumExpr wsobj =  cplex.sum(cplex.prod(1, obj2),cplex.prod(0.0, obj_1));
			IloNumExpr[] objs = new IloNumExpr[] {obj1,obj2};
			
			IloNumExpr wsobj = cplex.sum(cplex.prod(params[0], obj_1),cplex.prod(params[1],obj_2));
			switch(trigger) {
			case 1:
				cplex.addMinimize(obj1);
				break;
			case 2:
				cplex.addMinimize(obj2);
				break;
			case 3:
				cplex.addMinimize(wsobj);
				break;
			}
			
			//constraints
		
		//single source
			for(int i =0;i<this.clients_tem;i++) {
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for(int j= 0;j<this.fogs_tem+1;j++) {
					expr.addTerm(1.0, x[i][j]);
				}
				cplex.addEq(expr, 1.0);
			}
		//single fog type
			for(int j= 0;j<this.fogs_tem;j++)
			{
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for(int i =0;i<fogprice.length;i++)  {
					expr.addTerm(1.0, y[j][i]);
				}
				cplex.addEq(expr, 1.0);
			}
		//single link type
			for(int j= 0;j<this.fogs_tem;j++)
			{
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for(int i =0;i<linkprice.length;i++)  {
					expr.addTerm(1.0, z[j][i]);
				}
				cplex.addEq(expr, 1.0);
			}
			// openness constraint
			for(int j= 0;j<this.fogs_tem;j++)
			{
				IloLinearNumExpr expr1 = cplex.linearNumExpr();
				IloLinearNumExpr expr2 = cplex.linearNumExpr();
				for(int i =1;i<linkprice.length;i++)  {
					expr1.addTerm(1.0, z[j][i]);
				}
				for(int i =1;i<fogprice.length;i++)  {
					expr2.addTerm(1.0, y[j][i]);
				}
				cplex.addEq(expr1,expr2);
			}
			//capacity constraint
			for(int j= 0;j<this.fogs_tem;j++) {
				IloLinearNumExpr expr1 = cplex.linearNumExpr();
				IloLinearNumExpr expr2 = cplex.linearNumExpr();
				for(int i= 0;i< this.clients_tem;i++) {
					expr1.addTerm(this.demand_vector[i], x[i][j]);
				}
				for(int i =0;i<fogprice.length;i++)  {
					expr2.addTerm(fogcpu[i], y[j][i]);
				}
				cplex.addLe(expr1, expr2);
			}
			//link capacity constrint
			for(int j= 0;j<this.fogs_tem;j++) {
				IloLinearNumExpr expr3 = cplex.linearNumExpr();
				IloLinearNumExpr expr4 = cplex.linearNumExpr();
				for(int i= 0;i< this.clients_tem;i++) {
					expr3.addTerm(this.traffic_vector[i]*0.01, x[i][j]);
				}
				for(int i =0;i<linkprice.length;i++)  {
					expr4.addTerm(linkCp[i], z[j][i]);
				}
				cplex.add(cplex.ifThen(cplex.le(y[j][0], 0.5),cplex.le(expr3, expr4)));
				
			}
			if(cplex.solve()) {
				/*for(int i =0;i<this.clients_tem;i++)
					System.out.println("customer's choice " +Arrays.toString(cplex.getValues(x[i])));
				for(int i =0;i<this.fogs_tem;i++)
					System.out.println("fog result " +Arrays.toString(cplex.getValues(y[i])));
				for(int i =0;i<this.fogs_tem;i++)
					System.out.println("link result " +Arrays.toString(cplex.getValues(z[i])));
				System.out.println("wsobj is:  "+ cplex.getObjValue());
				System.out.println("obj_1 is:  "+ cplex.getValue(obj_1));
				System.out.println("obj_2 is:  "+ cplex.getValue(obj_2));
				System.out.println("-- " +this.minTotalDelay_);
				System.out.println("^^ " +this.maxTotalDelay_);
				System.out.println("obj1 is:  "+ cplex.getValue(obj1));
				System.out.println("obj2 is:  "+ cplex.getValue(obj2));*/
			
			
				
					out = new double[3];
					out[0]= cplex.getValue(obj1);
					out[1]= cplex.getValue(obj2);
					out[2]= cplex.getMIPRelativeGap();
			
				
			};
			cplex.end();
			cplex = null;
	}catch(IloException e) 
		{
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return out;
}}

