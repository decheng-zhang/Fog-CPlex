package carleton;

import java.awt.Font;

import edu.princeton.cs.introcs.Draw;

public class InstancesRunner {
	

	
	public double[][] getNodes() {
		return Nodes;
	}
	
	public double [][] nodeAdder(double radius, int degreecut,int level) {
		if( Nodes == null){
		Nodes =  new double [degreecut*level+1][2];
		Nodes[Nodes.length-1][0]=0;
		Nodes[Nodes.length-1][1]=0;
		}
		for(int i = 0;i<degreecut;i++) {
			//System.out.println(Math.toRadians(360/(double)degreecut));
			Nodes[i+degreecut*(level-1)][0]=radius*Math.cos(Math.toRadians(i*360/(double)degreecut));
			Nodes[i+degreecut*(level-1)][1]=radius*Math.sin(Math.toRadians(i*360/(double)degreecut));
		}
		if(level!=1) {
			nodeAdder(radius*2,degreecut, level-1);	
		}

		return Nodes;
		
	}
	
	public static void main(String[] args) {
        // mess around with this, try 7, 8, 9, 10, 11, 12, 15
        // probably have to turn down the spring force to keep it stable after that...
        int n = Integer.parseInt(args[0]);
       // screenTerminal(Algorithms);
        
        Draw draw1= new Draw("Spring Algorithm");
       Draw draw2 = new Draw("Kmean Algorithm");
        Draw draw3 = new Draw("Vicinity Algorithm");
       
        // set up the drawing area
        draw1.setXscale(0, 100);
        draw1.setYscale(0, 100);
        draw1.setPenColor(Draw.BLUE);
        draw1.setPenRadius(0.0025);
       draw2.setXscale(0, 100);
       draw2.setYscale(0, 100);
        draw2.setPenColor(Draw.BLUE);
        draw2.setPenRadius(0.0025);
        draw3.setXscale(0, 100);
        draw3.setYscale(0, 100);
        draw3.setPenColor(Draw.BLUE);
        draw3.setPenRadius(0.0025);
        
  
  		
        Springs s = new Springs(n,draw1);
        s.nodeAdder(10.0,12,3);
        Kmeans k = new Kmeans(draw2);
        Vicinity v = new Vicinity(draw3);
        
   
      

        int tii = 1;
        double lltime =0;
        
        while (true) {
        	//CountDown; 
        	/*if(Springs.elapsedTime()-lltime >1)
               {
               	System.out.println(tii++);
               	lltime = Springs.elapsedTime();
               }
        	*/
         
           //Comment out if what stand point (aka no client mobility consideration)
         /*for(int t=0;t<n;t++) {
        	  rx[t] +=(Math.random()-0.5)*10*timeStep;
        	  ry[t] += (Math.random()-0.5)*10*timeStep;
          
            rx[t]= (rx[t]>100)? 100:rx[t];
            rx[t]= (rx[t]<0)? 0:rx[t];
            ry[t]= (ry[t]>100)? 100:ry[t];
            ry[t]= (ry[t]<0)? 0:ry[t];
            
        }
		*/
            // clear
            draw1.clear();
            draw2.clear();
            draw3.clear();
            s.start();
            k.start();
            v.start();
            // draw Sites[]
            for ( int si=0; si<Nodes.length;si++) {
            	draw1.filledSquare(Nodes[si][0]+50,Nodes[si][1]+50, 1.0);
            	
            }
           
            for (int ic = 0; ic < n; ic++) {
                // draw a circle for each node
            	
                draw1.filledCircle(rx[ic], ry[ic], 0.4);
                draw1.setFont(new Font("", Font.BOLD, 10));
                draw1.textLeft(rx[ic]+1, ry[ic]+1,String.valueOf(ic));
                draw2.filledCircle(rx[ic], ry[ic], 0.4);
                draw2.setFont(new Font("", Font.BOLD, 10));
                draw2.textLeft(rx[ic]+1, ry[ic]+1,String.valueOf(ic));
                draw3.filledCircle(rx[ic], ry[ic], 0.4);
                draw3.setFont(new Font("", Font.BOLD, 10));
                draw3.textLeft(rx[ic]+1, ry[ic]+1,String.valueOf(ic));
                // draw the connections between every 2 nodes
            }
            

            draw1.setFont(new Font("", Font.BOLD, 20));
			draw1.text(35.0, 5.0, String.valueOf(s.getNetworkUsage()));
          
            

            // show and wait
            draw1.show(10);
            draw2.show(10);
            draw3.show(10);
         
        }
    }
}
