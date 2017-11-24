package RNAseqTools.SingleCell.StemnessCalculation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Random;

import statistics.general.MathTools;

public class CalculateStemness {

	public static int SEED = 99; 
	
	

	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "Calculate Oligodendrocyte Astrocyte and Stemness Score";
	}
	public static String parameter_info() {
		return "[inputFile] [astrocyteGeneFile] [oligoGeneFile] [stemnessFile] [FPKMcutoff] [outputFile] [outputDebug]";
	}
	public static void execute(String[] args) {
		
		try {
			
			// calculate the binning
			HashMap map = new HashMap();						
			String inputMatrixFile = args[0];
			String astrocyteGeneFile = args[1];
			String oligoGeneFile = args[2];
			String stemnessFile = args[3];
			double lower_cutoff = new Double(args[4]);
			String outputFile = args[5];
			String outputDebug = args[6];
			double max = 0;
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);


			FileWriter fwriter_debug = new FileWriter(outputDebug);
			BufferedWriter out_debug = new BufferedWriter(fwriter_debug);

			
			int num_bin = 25;
			HashMap astrocyte = readGeneFile(astrocyteGeneFile);
			HashMap oligodendrocyte = readGeneFile(oligoGeneFile);
			HashMap stemness = readGeneFile(stemnessFile);
			HashMap average_expression = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine().replaceAll("\"", "");
			out_debug.write(header + "\n");
			String[] split_header = header.split("\t");
			HashMap[] astrocyteValue= new HashMap[split_header.length - 1];
			HashMap[] oligoValue= new HashMap[split_header.length - 1];
			HashMap[] stemnessValue= new HashMap[split_header.length - 1];
			for (int i = 0; i < split_header.length - 1; i++) {
				
				astrocyteValue[i] = new HashMap();
				oligoValue[i] = new HashMap();
				stemnessValue[i] = new HashMap();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double min = Double.MAX_VALUE;
				for (int i = 1; i < split.length; i++) {
					double val = new Double(split[i]);
					if (val > 0 && min > val) {
						min = val;
					}
				}
				String copy_str = str;
				str = split[0];
				if (min < Double.MAX_VALUE) {
					for (int i = 1; i < split.length; i++) {
						if (min > new Double(split[i])) {
							str += "\t" + (new Double(min) + 1.0); 
						} else {
							str += "\t" + (new Double(split[i]) + 1.0);
						}
					}
					split = str.split("\t");
					String geneName = split[0];
					map.put(geneName, str);
					double average = 0;
					for (int i = 1; i < split.length; i++) {
						double fpkm = new Double(split[i]);
						average += fpkm;
					}
					
					average = average / (split.length - 1);
					if (max < average) {
						max = average;
						
					}
					if (average > lower_cutoff) {
						
						
						// generate Er(Gj,i)
						String new_str = str_normalize2zscore(str);
						if (!new_str.contains("Infinity") && !new_str.contains("NaN")) {
							average_expression.put(geneName, average);
							String[] norm_split = new_str.split("\t");
							if (astrocyte.containsKey(geneName)) {
								for (int i = 1; i < norm_split.length; i++) {
									astrocyteValue[i - 1].put(geneName, new Double(norm_split[i]));
								}
							}
							if (oligodendrocyte.containsKey(geneName)) {
								for (int i = 1; i < norm_split.length; i++) {
									oligoValue[i - 1].put(geneName, new Double(norm_split[i]));
								}
							}
							if (stemness.containsKey(geneName)) {
								for (int i = 1; i < norm_split.length; i++) {
									stemnessValue[i - 1].put(geneName, new Double(norm_split[i]));
									//System.out.println("Add GeneName: " + geneName);
								}
							}
						} else {
							//System.out.println(str + "\n");
							//System.out.println(new_str + "\n");
						}
					}
				} // good expression
				
				
				
				
			}
			in.close();
			
			// generate random bins
			double diff = max / num_bin;
			System.out.println(diff);
			HashMap bin = new HashMap();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				String str = (String)map.get(geneName);
				out_debug.write(str + "\n");
				String[] split = str.split("\t");
				double average = 0;
				for (int i = 1; i < split.length; i++) {
					double fpkm = new Double(split[i]);
					average += fpkm;
				}
				average = average / split.length - 1;
				
				for (int i = 1; i <= num_bin; i++) {
					if (average >= diff * (i - 1) && average < diff * (i)) {
						double cutoff = diff * (i);
						if (bin.containsKey(cutoff)) {
							LinkedList list = (LinkedList)bin.get(cutoff);
							String new_str = str_normalize2zscore(str);
							if (!new_str.contains("Infinity") && !new_str.contains("NaN")) {
								list.add(new_str);
								bin.put(cutoff, list);
							}
						} else {
							LinkedList list = new LinkedList();
							String new_str = str_normalize2zscore(str);
							if (!new_str.contains("Infinity") && !new_str.contains("NaN")) {
								list.add(new_str);
								bin.put(cutoff, list);
							}
						}
					}
				}
				
			}
			System.out.println("bin size: " + bin.size());
			// fill will minimum value for all 0s
			HashMap bin_control = new HashMap();
			itr = bin.keySet().iterator();
			while (itr.hasNext()) {
				double cutoff = (Double)itr.next();
				System.out.println("Sampling Cutoff: " + cutoff);
				LinkedList list = (LinkedList)bin.get(cutoff);
				LinkedList newList = sample100(list);
				//System.out.println("newList.size(): " + newList.size());
				bin_control.put(cutoff, newList);
			}
			System.out.println("Finished Sampling");
			out.write("Gene");
			for (int i = 1; i < split_header.length; i++) {
				out.write("\t" + split_header[i]);
			}
			out.write("\n");
			
			System.out.println("Finished Sampling");
			// astrocyte
			LinkedList combined = new LinkedList();
			itr = astrocyteValue[0].keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				double avgExpr = (Double)average_expression.get(geneName);
				
				for (int j = 1; j <= num_bin; j++) {
					if (avgExpr >= diff * (j - 1) && avgExpr < diff * (j)) {
						LinkedList list = (LinkedList)bin_control.get(diff * j);
						combined = combine(list, combined);
					}
				}
			}
			
			System.out.println("combined.size(): " + combined.size());
			out.write("Astrocyte");
			System.out.println("Astrocyte");
			// calculate Lini,j
			for (int i = 0; i < split_header.length - 1; i++) {				
				double relativeExpression = 0.0;
				itr = astrocyteValue[i].keySet().iterator();
				while (itr.hasNext()) {
					String geneName = (String)itr.next();
					System.out.println(geneName);
					relativeExpression += (Double)astrocyteValue[i].get(geneName);	
					System.out.println("Astrocute: " + (Double)astrocyteValue[i].get(geneName));;
				}								
				relativeExpression = relativeExpression / astrocyteValue[i].size();
				double controlRE = averageList(combined, i);
				//out.write("\t" + (relativeExpression - controlRE));
				out.write("\t" + (relativeExpression - controlRE));
			}
			out.write("\n");
			
			// oligodendrocyte
			combined = new LinkedList();
			itr = oligoValue[0].keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				System.out.println(geneName);
				double avgExpr = (Double)average_expression.get(geneName);
				
				for (int j = 1; j <= num_bin; j++) {
					if (avgExpr >= diff * (j - 1) && avgExpr < diff * (j)) {
						LinkedList list = (LinkedList)bin_control.get(diff * j);
						combined = combine(list, combined);
					}
				}
			}
			System.out.println("Oligodendrocyte");
			out.write("Oligodendrocyte");
			// calculate Lini,j
			for (int i = 0; i < split_header.length - 1; i++) {				
				double relativeExpression = 0.0;
				itr = oligoValue[i].keySet().iterator();
				while (itr.hasNext()) {
					String geneName = (String)itr.next();
					relativeExpression += (Double)oligoValue[i].get(geneName);					
				}								
				relativeExpression = relativeExpression / oligoValue[i].size();
				double controlRE = averageList(combined, i);
				//out.write("\t" + (relativeExpression - controlRE));
				out.write("\t" + (relativeExpression - controlRE));
			}
			out.write("\n");
			
			// stemness score			
			combined = new LinkedList();
			itr = stemnessValue[0].keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				System.out.println("stemnessGenes: " + geneName);
				double avgExpr = (Double)average_expression.get(geneName);				
				for (int j = 1; j <= num_bin; j++) {
					if (avgExpr >= diff * (j - 1) && avgExpr < diff * (j)) {
						LinkedList list = (LinkedList)bin_control.get(diff * j);
						combined = combine(list, combined);
					}
				}
			}
			out.write("StemnessRaw");
			System.out.println("StemnessRaw");
			// calculate Lini,j
			for (int i = 0; i < split_header.length - 1; i++) {				
				double relativeExpression = 0.0;
				itr = stemnessValue[i].keySet().iterator();
				while (itr.hasNext()) {
					String geneName = (String)itr.next();
					relativeExpression += (Double)stemnessValue[i].get(geneName);					
				}								
				relativeExpression = relativeExpression / stemnessValue[i].size();
				double controlRE = averageList(combined, i);
				//out.write("\t" + (relativeExpression - controlRE));
				out.write("\t" + (relativeExpression - controlRE));
			}
			out.write("\n");			
			out.close();
			
			out_debug.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static LinkedList combine(LinkedList list, LinkedList list2) {
		LinkedList combined = new LinkedList();
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String line = (String)itr.next();
			combined.add(line);
		}
		itr = list2.iterator();
		while (itr.hasNext()) {
			String line = (String)itr.next();
			combined.add(line);
		}
		return combined;
	}
	
	public static double averageList(LinkedList list, int index) {				
		LinkedList values = new LinkedList();
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String line = (String)itr.next();
			String[] split = line.split("\t");
			values.add(split[index + 1]);
			//System.out.println("averageList: " + new Double(split[index + 1]));
		}
		double[] subsample_double = MathTools.convertListStr2Double(values);
		return MathTools.mean(subsample_double);
	}
	/**
	 * Calculate the control Gjcont, i
	 * @param list
	 * @param index
	 * @return
	 */
	public static LinkedList sample100(LinkedList list) {
		
		LinkedList subsample = new LinkedList();
		Random rand = new Random(SEED);
		int number = 0;
		while (subsample.size() < 100 && number < 110) {
			int i = rand.nextInt(list.size());
			String line = (String)list.get(i);
			if (!line.contains("Infinity")) {
				subsample.add(line);
			}
			number++;
		}

		//
		
		return subsample;
	}
	public static HashMap readGeneFile(String inputFile) {
		HashMap map = new HashMap();
		try {
						
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				map.put(str.trim(), str);				
			}
			in.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static String str_normalize2zscore(String str) {
		
		String[] split = str.split("\t");
		double[] values = new double[split.length - 1]; 
		for (int i = 1; i < split.length; i++) {
			//values[i - 1] = MathTools.log2(new Double(split[i]));
			values[i - 1] = new Double(split[i]);
		}
		
		double[] zscores = MathTools.zscores(values);
		String result = split[0];
		for (double zscore: zscores) {
			result += "\t" + zscore;
		}
		return result;
	}
}
