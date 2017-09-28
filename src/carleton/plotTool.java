package carleton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;

public class plotTool {
	private  ConcurrentLinkedQueue<Object[]> data; 
	private int xlable=Helper.xaxis;
	private int ylabel = Helper.yaxis;
	public plotTool(ConcurrentLinkedQueue<Object[]> d ) {
		data=d;
	}
	public void writeData() throws IOException{
		BufferedWriter out = new BufferedWriter(new FileWriter("file.dat"));
		
		
		Iterator<Object []> iter = data.iterator();
		while(iter.hasNext()) {
			//System.out.println(t.results.poll());
			//System.out.format("%-10s %-5.3f   %-5.5f %10.1f %10.1f %n", t.results.poll());
			Object[] edata =iter.next();
			out.write(String.format("%s %-10.3f %-10.3f %-10.1f %10.3f %n",edata));
		}
		out.close();
	}
	public void plot() {
		double [][] tdata = new double[data.size()][2];
		
		int idx =0;
		Iterator<Object []> iter = data.iterator();
		while(iter.hasNext()) {
			//System.out.println(t.results.poll());
			//System.out.format("%-10s %-5.3f   %-5.5f %10.1f %10.1f %n", t.results.poll());
			Object[] edata =iter.next();
			tdata [idx][0] = (double)edata[xlable];
			tdata [idx][1] = (double)edata[ylabel];
			idx++;
		}
		JavaPlot p = new JavaPlot("C:/Program Files/gnuplot/bin/gnuplot.exe");
		PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.POINTS);
        myPlotStyle.setPointSize(2);
        DataSetPlot s = new DataSetPlot(tdata);
        //s.set("label","center \"TTTTTT\"");
        
        
        s.setPlotStyle(myPlotStyle);
        
        p.addPlot(s);
        p.newGraph();
        p.plot();
        p.setPersist(true);
	}
	
    public static void main(String[] args) {
    	
    }
}
