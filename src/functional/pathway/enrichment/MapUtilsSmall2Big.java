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

public class MapUtilsSmall2Big {
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

		public <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> nullsFirst(
				Comparator<? super T> comparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> nullsLast(
				Comparator<? super T> comparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T, U> Comparator<T> comparing(
				Function<? super T, ? extends U> keyExtractor,
				Comparator<? super U> keyComparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T, U extends Comparable<? super U>> Comparator<T> comparing(
				Function<? super T, ? extends U> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> comparingInt(
				ToIntFunction<? super T> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> comparingLong(
				ToLongFunction<? super T> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> comparingDouble(
				ToDoubleFunction<? super T> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}
    } );

    Map<K, V> result = new LinkedHashMap<K, V>();
    for (Map.Entry<K, V> entry : list)
    {
        result.put( entry.getKey(), entry.getValue() );
    }
    return result;
}
}
