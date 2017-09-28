package carleton;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Helper {
static final int costPerLocation = 100;//USD
static final double []areaScale = {100,100};
static final double percent2Cloud = 0.01;
static final double kmeanTimeout=4;
static final int xaxis=1;
static final int yaxis=2;
static final String outputFileName = "30-5-june-30-with-10150users-after-meeting";
//static final String outputFileName = null;//generate new instance;

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
        return ( o1.getValue() ).compareTo( o2.getValue() );
    }
} );

Map<K, V> result = new LinkedHashMap<>();
for (Map.Entry<K, V> entry : list)
{
    result.put( entry.getKey(), entry.getValue() );
}
return result;
}

}