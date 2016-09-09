package EnrichmentTool;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Downloaded from http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
 */
public class MapUtilsBig2Small
{
    public static <K, V extends Comparable<? super V>> Map<K, V> 
        sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
            new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );
        Map<K, V> result = new LinkedHashMap<K, V>();
        List<Map.Entry<K, V>> list2 = new LinkedList<Map.Entry<K, V>>();         
        for (int i = list.size() - 1; i >= 0; i--) {
        	Map.Entry<K, V> entry = list.get(i);
        	result.put( entry.getKey(), entry.getValue() );
        }        
        return result;
    }
}