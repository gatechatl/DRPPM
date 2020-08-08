package stjude.projects.suzannebaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

import org.apache.commons.math3.stat.inference.TTest;

import specialized.algorithm.SpecializedAlgorithms;
import statistics.general.MathTools;

/**
 * Generate a matrix based on the gene list for generating heatmap
 * @author tshaw
 *
 */
public class GenerateHeatmapFromGMTPipeline {

	public static String type() {
		return "SUZANNEBAKER";
	}
	public static String description() {
		return "Generate a matrix based on the gene list for generating heatmap";
	}
	public static String parameter_info() {
		return "[matrixFile] [pathwayFile] [sampleComparisonIndex example: \"1,2,3:4,5,6;7,8,9:10,11,12\"] [outputFolder] [outputShellScript]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String matrixFile = args[0];
			String pathwayFile = args[1];
			String ttest_samplecomparison = args[2];	
			String sampleInfo = args[3];
			String outputFolder = args[4];
			String outputScript = args[5];
			
			FileWriter fwriter2 = new FileWriter(outputScript);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			FileInputStream fstream = new FileInputStream(pathwayFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String pathwayName = split[0];
				//String[] genes = split[4].split(",");
				LinkedList list = new LinkedList();
				for (int i = 2; i < split.length; i++) {
					list.add(split[i]);
				}
				map.put(pathwayName, list);
			}
			in.close();
			
			HashMap data_matrix = new HashMap();
			
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			//out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				data_matrix.put(split[0], str);
			}
			in.close();
			
			LinkedList finalList = new LinkedList();
			
			HashMap map2 = new HashMap();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String pathway = (String)itr.next();

				FileWriter fwriter = new FileWriter(outputFolder + "/" + pathway + "_matrix.txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				
				//FileWriter fwriter3 = new FileWriter(outputFolder + "/" + pathway + ".sh");
				//BufferedWriter out3 = new BufferedWriter(fwriter3);
				
				out2.write("drppm -MatrixZscoreNormalizationWithOriginalValues " + outputFolder + "/" + pathway + "_matrix.txt " + outputFolder + "/" + pathway + "_zscore_matrix.txt\n");
				out2.write("drppm -GenerateHeatmapZscoreWithOriginalValuesJavaScript " + outputFolder + "/" + pathway + "_zscore_matrix.txt " + sampleInfo + " 1500 1500 > " + outputFolder + "/" + pathway + "_zscore_matrix.txt.html\n");
				
				out.write(header + "\n");
				HashMap lines_map = new HashMap();
				LinkedList list = (LinkedList)map.get(pathway);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String gene = (String)itr2.next();
					if (data_matrix.containsKey(gene)) {
						String line = (String)data_matrix.get(gene);
						//System.out.println(gene + "\t" + line);
						String[] split2 = line.split("\t");
						double avg = 0;
						for (int i = 1; i < split2.length; i++) {
							avg += new Double(split2[i]);
						}
						avg = avg / (split2.length - 1);
						if (avg > 1.0) {
							double combined_tdist_score = multiple_ttest(line, ttest_samplecomparison);
							lines_map.put(line, combined_tdist_score);
						}
						//out.write(line + "\n");
						/*String[] split = line.split("\t");
						if (map2.containsKey(split[0])) {
							int num = 2;
							String newLine = split[0] + "_" + num;
							while (map2.containsKey(newLine)) {
								num++;
								newLine = split[0] + "_" + num;
							}
							map2.put(newLine, newLine);
							for (int i = 1; i < split.length; i++) {
								newLine += "\t" + split[i];
							}
							finalList.add(newLine);
							
						} else {
							finalList.add(line);
							map2.put(split[0], split[0]);
						}
						*/
					}
				}
				
				TreeMap treeMap = SpecializedAlgorithms.sortMapByValue(lines_map);
				Iterator itr3 = treeMap.keySet().iterator();
				while (itr3.hasNext()) {
					String line = (String)itr3.next();		
					double combined_tdist_score = (Double)lines_map.get(line);
					//String line = (String)lines_map.get(geneName);							
					out.write(line + "\n");
					
				}
				out.close();
			}
			out2.close();
			/*Iterator itr2 = finalList.iterator();
			while (itr2.hasNext()) {
				String line = (String)itr2.next();
				out.write(line + "\n");
			}
			out.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double multiple_ttest(String line, String str_comparisons) {
		String[] split = line.split("\t");
		String[] comparisons = str_comparisons.split(";");
		double combined_tdist = 0;
		TTest ttest = new TTest();
		for (int i = 0; i < comparisons.length; i++) {
			String group1_str = comparisons[i].split(":")[0];
			String group2_str = comparisons[i].split(":")[1];
			String[] group1 = group1_str.split(",");
			double[] group1_values = new double[group1.length];
			String[] group2 = group2_str.split(",");
			double[] group2_values = new double[group2.length];
			String group1_line = "";
			String group2_line = "";
			for (int j = 0; j < group1.length; j++) {
				group1_values[j] = MathTools.log2(new Double(split[new Integer(group1[j])]) + 1);
				//group1_line += group1_values[j] + ",";
			}
			for (int j = 0; j < group2.length; j++) {
				group2_values[j] = MathTools.log2(new Double(split[new Integer(group2[j])]) + 1);
				//group2_line += group2_values[j] + ",";
			}
			double tdist = ttest.t(group1_values, group2_values);
			/*System.out.println(group1_str);
			System.out.println(group1_line);
			System.out.println(group2_str);
			System.out.println(group2_line);
			System.out.println(tdist);*/
			if (!new Double(tdist).isNaN())
				combined_tdist += tdist;
		}
		return combined_tdist;
	}
}
