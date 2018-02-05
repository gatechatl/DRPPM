package stjude.projects.xiangchen;

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

public class XiangChenSpearmanCorrelation {

	public static String description() {
		return "Calculate spearman rank correlation";
	}
	public static String type() {
		return "XIANGCHEN";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void main(String[] args) {
		
		try {
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\chen1grp\\Wilms\\common\\2017-11-02_easton_mulder_850K_120449\\Methylation_Normalization\\SpearmanRankCorrelation\\SpearmanRankCorrelationMatrix.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputFile = "\\\\gsc.stjude.org\\project_space\\chen1grp\\Wilms\\common\\2017-11-02_easton_mulder_850K_120449\\Methylation_Normalization\\Easton_120449_sample_methylation_data_clean_append_meta_withNA_normalized.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			
			String[] header_split = header.split("\t");
			HashMap[] maps = new HashMap[header_split.length];
			for (int i = 0; i < maps.length; i++) {
				maps[i] = new HashMap();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					if (header_split[i].contains("AVG_Beta")) {
						maps[i].put(split[0], split[i]);
						
					}
				}								
			}
			in.close();
			out.write("Probe");
			for (int i = 1; i < header_split.length; i++) {
				out.write("\t" + header_split[i]);
			}
			out.write("\n");
			
			for (int i = 1; i < header_split.length; i++) {
				LinkedList list1 = new LinkedList();
				LinkedList list2 = new LinkedList();
				out.write(header_split[i]);
				for (int j = 1; j < header_split.length; j++) {
					boolean found = false;
					if (header_split[i].contains("AVG_Beta") && header_split[j].contains("AVG_Beta")) {
						Iterator itr = maps[i].keySet().iterator();
						while (itr.hasNext()) {
							String probeName = (String)itr.next();
							String val1 = (String)maps[i].get(probeName);
							String val2 = (String)maps[j].get(probeName);
							
							if (!(val1.trim().equals("") || val1.equals("NA")) && !(val2.trim().equals("") || val2.equals("NA"))) {
								double double_val1 = new Double(val1);
								double double_val2 = new Double(val2);
								list1.add(double_val1 + "");
								list2.add(double_val2 + "");
							}
						}
						
						double[] double_list1 = MathTools.convertListStr2Double(list1);
						double[] double_list2 = MathTools.convertListStr2Double(list2);
						double rho = MathTools.SpearmanRank(double_list1, double_list2);
						found = true;
						out.write("\t" + rho);
					}
					out.write("\tNA");
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
