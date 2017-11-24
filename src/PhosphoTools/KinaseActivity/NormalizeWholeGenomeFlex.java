package PhosphoTools.KinaseActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Normalize the matrix for IKAP input
 * @author tshaw
 *
 */
public class NormalizeWholeGenomeFlex {

	public static String description() {
		return "Normalize the matrix for IKAP input, only normalize up to 10 columns";
	}
	public static String type() {
		return "KINASEACTIVITY";
	}
	public static String parameter_info() {
		return "[inputPhosphoFile] [index] [factor] [numSamples] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			//String inputProteome = args[1];
			String index = args[1];
			int factor = new Integer(args[2]);
			int numSamples = new Integer(args[3]);
			String outputFile = args[4];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			//out.write("Accession_Site\tData1\tData2\tData3\tData4\tData5\tData6\tData7\tData8\tData9\tData10\tKinase\n");
			out.write("GeneName");
			for (int i = 0; i < numSamples; i++) {
				out.write("\tData" + (i + 1));
			}
			out.write("\tKinase\n");
			
			HashMap count_kinase = new HashMap();
			//HashMap map = new HashMap();			
			HashMap value = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			//out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String accession = split[0];
				/*String kinase = split[numSamples].toUpperCase();
				map.put(accession, kinase);
				LinkedList list_kinase = new LinkedList();
				for (int i = 0; i < kinase.split(",").length; i++) {
					if (!kinase.split(",")[i].equals("")) {
						count_kinase.put(kinase.split(",")[i], "");
						if (!list_kinase.contains(kinase.split(",")[i])) {
							list_kinase.add(kinase.split(",")[i]);
						}
					}
				}
				kinase = "";*/
				/*Iterator itr = list_kinase.iterator();
				while (itr.hasNext()) {
					String val = (String)itr.next();
					if (kinase.equals("")) {
						kinase = val;
					} else {
						kinase += "," + val;
					}
				}
				*/
				
				String value_line = ""; 
				for (int i = split.length - numSamples; i < split.length; i++) {
					if (value_line.equals("")) {
						value_line = split[i];
					} else {
						value_line += "\t" + split[i];
					}
				}
				if (!accession.equals("NA")) {
					if (value.containsKey(accession)) {
						LinkedList list = (LinkedList)value.get(accession);
						list.add(value_line);
						value.put(accession, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(value_line);
						value.put(accession, list);					
					}
				}
				//value.put(accession, value_line);								
			}
			in.close();
			
			
			double max_val = 0;
			Iterator itr = value.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				LinkedList list = (LinkedList)value.get(key);
				String value_line = combineAvg(list, numSamples);			
				//String key = accession + "_" + site;
				
				//String kinases = "";
				//String kinases = (String)map.get(key);
								
				String[] indexes = index.split(",");
				int[] val_index = new int[indexes.length];
				for (int i = 0; i < indexes.length; i++) {
					val_index[i] = new Integer(indexes[i]);
				}
				String accession = key;
				value_line = normalize(value_line, val_index);
				
				String[] split = value_line.split("\t");
				for (int i = split.length - numSamples; i < split.length; i++) {
					double norm_val = new Double(split[i]);
					norm_val = Math.abs(norm_val);
					if (max_val < norm_val) {
						max_val = norm_val;
					}
					//out.write("\t" + split[i]);
				}
				
			}
			
			HashMap uniq = new HashMap();
			HashMap new_map = new HashMap();
			itr = value.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				LinkedList list = (LinkedList)value.get(key);
				String value_line = combineAvg(list, numSamples);
				new_map.put(key, value_line);
				//String key = accession + "_" + site;
				
				//String kinases = "";
				//String kinases = (String)map.get(key);
				
				/*list = (LinkedList)map.get(key);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String kinase = (String)itr2.next();						
					if (kinases.equals("")) {
						kinases = kinase.toUpperCase();
					} else {
						kinases += "," + kinase.toUpperCase();
					}
				}*/
				String[] indexes = index.split(",");
				int[] val_index = new int[indexes.length];
				for (int i = 0; i < indexes.length; i++) {
					val_index[i] = new Integer(indexes[i]);
				}
				String accession = key.split(":")[0];
				value_line = normalize(value_line, val_index);
				
				String line = key.toUpperCase();
				//out.write(key);
				String[] split = value_line.split("\t");
				for (int i = split.length - numSamples; i < split.length; i++) {
					double old_value = new Double(split[i]);
					double new_value = old_value / max_val * factor;
					line += "\t" + new_value;
					//out.write("\t" + new_value);
					//writeline += "\t" + split[i];
				}
				//out.write("\t" + kinases + "\n");				
				//line += "\t" + kinases;
				if (!uniq.containsKey(line)) {
					//if (!kinases.equals("NA") && !kinases.equals("")) {
					out.write(line + "\n");
					//}
					uniq.put(line, line);
				}
			}

			out.close();
			
			//System.out.println(count_kinase.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String normalize_wholeproteome(String phospho_line, String whole_line) {
		String[] psplit = phospho_line.split("\t");
		String[] wsplit = whole_line.split("\t");
		String result = "";
		for (int i = 0; i < psplit.length; i++) {
			double pho = new Double(psplit[i]);
			double whole = new Double(wsplit[i]);
			double val = MathTools.log2(pho / whole);
			if (result.equals("")) {
				result = "" + val;
			} else {
				result += "\t" + val;
			}
		}
		return result;
	}
	public static String normalize(String line, int[] cntrl) {
		String[] split = line.split("\t");
		double total = 0;
		for (int i = 0; i < cntrl.length; i++) {
			total += new Double(split[cntrl[i]]);
		}
		String result = "";
		double avg = total / cntrl.length;
		for (int i = 0; i < split.length; i++) {
			double value = new Double(split[i]) / avg;
			value = MathTools.log2(value);
			if (result.equals("")) {
				result = "" + value;
			} else {
				result += "\t" + value;
			}
		}
		return result;
	}
	public static String combineAvg(LinkedList list, int numSamples) {
		double[] total = new double[numSamples];
		for (int i = 0; i < total.length; i++) {
			total[i] = 0;
		}
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String line = (String)itr.next();
			String[] split = line.split("\t");
			for (int i = 0; i < split.length; i++) {
				total[i] += new Double(split[i]);
			}
		}
		String result = "";
		for (int i = 0; i < total.length; i++) {
			if (result.equals("")) {
				result = "" + total[i] / list.size();
			} else {
				result += "\t" + total[i] / list.size();
			}
		}
		return result;
	}
}
