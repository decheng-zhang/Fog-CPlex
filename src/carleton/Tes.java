package carleton;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
public class Tes {
	    int a;
	    Tes() {
	        System.out.println("in parent");
	       // a = 10;
	    }
	   /*int method() {
		   System.out.println("ever been hear?");
		   a=10
	       return a ;
	    }*/
	    public static <K, V extends Comparable<? super V>> Map<K, V> 
	    sortByValue( Map<K, V> map )
	{
	    List<Map.Entry<K, V>> list =
	        new LinkedList<>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	    {
	        @Override
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	        {
	            return ( o2.getValue() ).compareTo( o1.getValue() );
	        }
	    } );

	    Map<K, V> result = new LinkedHashMap<>();
	    for (Map.Entry<K, V> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}

	    public static void main(String[] args) {

	        /*JavaPlot p = new JavaPlot("C:/Program Files/gnuplot/bin/gnuplot.exe");
	       
	        double tab[][];

	        tab = new double[2][2];
	        tab[0][0] = 0.0000;
	        tab[0][1] = 2.0000;
	        tab[1][0] = 1.0000;
	        tab[1][1] = 6.0000;
	        PlotStyle myPlotStyle = new PlotStyle();
	        myPlotStyle.setStyle(Style.POINTS);
	        DataSetPlot s = new DataSetPlot(tab);
	       // .set("xrange","[0:pi]");
	        myPlotStyle.setPointSize(4);
	       
	        //DataSetPlot.setPlotStyle(myPlotStyle);
	        s.setPlotStyle(myPlotStyle);
	        //p.newGraph();
	        //s.set("xrange","[0:pi]");
	        p.addPlot(s);
	       //p.set("xrange","[0:pi]");
	       
	        p.set("label", String.format("at %f %f \"TestPoint\" point pointtype 7 pointsize 2",tab[0][1],tab[1][0]));
	        System.out.println(String.format("at %f %f \"TestPoint\" point pointsize 2",tab[0][1],tab[1][0]));
	        //p.newGraph();
	        p.plot();
	        p.setPersist(true);
	       */
	    	Random r = new Random();
	    	Map<String, Integer> testMap = new HashMap<String, Integer>(1000);
	    	for(int i = 0 ; i < 1000 ; ++i) {
	            testMap.put( "SomeString" + r.nextInt(), r.nextInt());
	        }
	    	testMap= sortByValue(testMap);
	    	for(Map.Entry<String, Integer> entry:testMap.entrySet()) {
	    		System.out.println(entry.getValue());
	    	}
	    }
 
	}

