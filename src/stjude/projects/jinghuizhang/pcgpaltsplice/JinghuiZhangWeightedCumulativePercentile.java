package stjude.projects.jinghuizhang.pcgpaltsplice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import statistics.general.MathTools;

/**
 * Read 100 samples at a time and calculate each sample's cumulative percentile.
 * @author tshaw
 *
 */
public class JinghuiZhangWeightedCumulativePercentile {

	public static String description() {
		return "Generate a weighted cumulative percentile matrix";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[input matrix file] [ouputPath] [final_ouputMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String matrixFile = args[0]; // 			
			String outputPath = args[1];
			String combinedOutput = args[2];
			
			FileWriter fwriter_combined = new FileWriter(combinedOutput);				
			BufferedWriter out_combined = new BufferedWriter(fwriter_combined);	
			
			LinkedList temp_outputFile_list = new LinkedList();
			LinkedList exon_list = new LinkedList();
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				exon_list.add(split[0]);
			}			
			String[] split_header = header.split("\t");
			int num_samples = split_header.length;
			in.close();
					
			int index = 1;
			while (index < num_samples) {
				int upper_limit = index + 100;
				if (upper_limit >= num_samples) {
					upper_limit = num_samples;
				}
				HashMap[] exon_value = new HashMap[num_samples];
				HashMap[] new_values = new HashMap[num_samples];
				double[] total = new double[num_samples];
				for (int i = 0; i < exon_value.length; i++) {
					exon_value[i] = new HashMap();
					new_values[i] = new HashMap();
					total[i] = 0.0;
				}
				temp_outputFile_list.add(outputPath + "/temp" + index);
				FileWriter fwriter = new FileWriter(outputPath + "/temp" + index);				
				BufferedWriter out = new BufferedWriter(fwriter);	
				
				
				fstream = new FileInputStream(matrixFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				split_header = in.readLine().split("\t");
				out.write("Exon");
				for (int i = index; i < upper_limit; i++) {
					out.write("\t" + split_header[i]);
				}
				out.write("\n");
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					String exon_name = split[0];
					for (int i = index; i < upper_limit; i++) {
						if (new Double(split[i]) < 1.0) {
							split[i] = "1.0";
						}
						exon_value[i].put(exon_name, MathTools.log2(new Double(split[i])));
						total[i] += MathTools.log2(new Double(split[i]));
					}
				}
				in.close();

				for (int i = index; i < upper_limit; i++) {
					sortByValues(exon_value[i]);
					
					/*
				    Set set = exon_value[i].entrySet();
				    Iterator iterator = set.iterator();
				    while(iterator.hasNext()) {
				        Map.Entry me = (Map.Entry)iterator.next();
				        System.out.print(me.getKey() + ": ");
				        System.out.println(me.getValue());
				    }*/
					double cumulative = 0.0;
				    HashMap map = sortByValues(exon_value[i]); 
				    System.out.println("Processing: " + split_header[i]);
				    Set set2 = map.entrySet();
				    Iterator iterator2 = set2.iterator();
				    while(iterator2.hasNext()) {
				        Map.Entry me2 = (Map.Entry)iterator2.next();
				        cumulative += (Double)me2.getValue();
				        double weighted_cumulative_val = cumulative / total[i];
				        new_values[i].put(me2.getKey(), weighted_cumulative_val);
				        //System.out.print(me2.getKey() + ": ");
				        //System.out.println(me2.getValue());
				    }
				}
				Iterator itr = exon_list.iterator();
				while (itr.hasNext()) {
					String exon_id = (String)itr.next();
					out.write(exon_id);
					for (int i = index; i < upper_limit; i++) {
						double updated_val = (Double)new_values[i].get(exon_id);
						updated_val = new Double(new Double(updated_val * 1000).intValue()) / 1000;
						out.write("\t" + updated_val);
					}
					out.write("\n");
				}
				out.close();
				// read the next 200 samples
				index = upper_limit;
			}
			
			FileInputStream[] fstreams = new FileInputStream[temp_outputFile_list.size()]; //new FileInputStream(matrixFile);
			DataInputStream[] dins = new DataInputStream[temp_outputFile_list.size()]; //new DataInputStream(fstreams);
			BufferedReader[] ins = new BufferedReader[temp_outputFile_list.size()]; //(new InputStreamReader(dins));
			Iterator itr = temp_outputFile_list.iterator();
			int i = 0;
			while (itr.hasNext()) {
				String outputFiles = (String)itr.next();
				fstreams[i] = new FileInputStream(outputFiles);
				dins[i] = new DataInputStream(fstreams[i]);
				ins[i] = new BufferedReader(new InputStreamReader(dins[i]));
				header = ins[i].readLine();
				split_header = header.split("\t");
				if (i == 0) {
					out_combined.write(header);
				} else {
					for (int j = 1; j < split_header.length; j++) {
						out_combined.write("\t" + split_header[j]);
					}
				}
				i++;
			}
			out_combined.write("\n");
			while (ins[0].ready()) {
				for (int j = 0; j < ins.length; j++) {				
					String line = ins[j].readLine();
					String[] split_line = line.split("\t");
					if (j == 0) {
						out_combined.write(line);
					} else {
						for (int k = 1; k < split_line.length; k++) {
							out_combined.write("\t" + split_line[k]);
						}
					}
					
				}
				out_combined.write("\n");
			}
			for (int j = 0; j < ins.length; j++) {	
				ins[j].close();
			}
			out_combined.close();
			
			itr = temp_outputFile_list.iterator();
			while (itr.hasNext()) {
				String file = (String)itr.next();
				File f = new File(file);
				f.delete();
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * Reference: https://beginnersbook.com/2013/12/how-to-sort-hashmap-in-java-by-keys-and-values/
	 */
    private static HashMap sortByValues(HashMap map) { 
	    List list = new LinkedList(map.entrySet());
	    // Defined Custom Comparator here
	    Collections.sort(list, new Comparator() {
	         public int compare(Object o1, Object o2) {
	            return ((Comparable) ((Map.Entry) (o1)).getValue())
	               .compareTo(((Map.Entry) (o2)).getValue());
	         }

			public Comparator reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			public Comparator thenComparing(Comparator other) {
				// TODO Auto-generated method stub
				return null;
			}

			public Comparator thenComparing(Function keyExtractor,
					Comparator keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			public Comparator thenComparing(Function keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			public Comparator thenComparingInt(ToIntFunction keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			public Comparator thenComparingLong(ToLongFunction keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			public Comparator thenComparingDouble(ToDoubleFunction keyExtractor) {
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
	    });

	    // Here I am copying the sorted list in HashMap
	    // using LinkedHashMap to preserve the insertion order
	    HashMap sortedHashMap = new LinkedHashMap();
	    for (Iterator it = list.iterator(); it.hasNext();) {
	           Map.Entry entry = (Map.Entry) it.next();
	           sortedHashMap.put(entry.getKey(), entry.getValue());
	    } 
	    return sortedHashMap;
    }
}
