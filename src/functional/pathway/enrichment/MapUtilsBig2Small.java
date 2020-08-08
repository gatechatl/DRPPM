package functional.pathway.enrichment;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

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

			public Comparator<Entry<K, V>> reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			public Comparator<Entry<K, V>> thenComparing(
					Comparator<? super Entry<K, V>> other) {
				// TODO Auto-generated method stub
				return null;
			}

			public <U> Comparator<Entry<K, V>> thenComparing(
					Function<? super Entry<K, V>, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			public <U extends Comparable<? super U>> Comparator<Entry<K, V>> thenComparing(
					Function<? super Entry<K, V>, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			public Comparator<Entry<K, V>> thenComparingInt(
					ToIntFunction<? super Entry<K, V>> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			public Comparator<Entry<K, V>> thenComparingLong(
					ToLongFunction<? super Entry<K, V>> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			public Comparator<Entry<K, V>> thenComparingDouble(
					ToDoubleFunction<? super Entry<K, V>> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
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