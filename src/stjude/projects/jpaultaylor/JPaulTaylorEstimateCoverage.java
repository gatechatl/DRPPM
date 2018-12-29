package stjude.projects.jpaultaylor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import statistics.general.MathTools;


/**
 * Calcuate the coverage of Brian Maxwell's peptide list
 * @author tshaw
 */
public class JPaulTaylorEstimateCoverage {
	public static String type() {
		return "JPaulTaylor";
	}
	public static String description() {
		return "Calcuate the coverage of Brian Maxwell's peptide list.";
	}
	public static String parameter_info() {
		return "[core fasta file] [inputPeptideFile] [id_txt_file]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap countPeptide = new HashMap();
			String fastaFile = args[0];
			String inputPeptideFile = args[1];
			String id_txt_file = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String uniprot_id = "";
			HashMap seq = new HashMap();
			FileInputStream fstream = new FileInputStream(fastaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {					
					String[] split = str.replaceAll(">", "").split(" ");
					uniprot_id = split[0];
				} else {
					if (seq.containsKey(uniprot_id)) {
						String prev_seq = (String)seq.get(uniprot_id);
						seq.put(uniprot_id, prev_seq + str.trim());
					} else {
						seq.put(uniprot_id, str.trim());
					}
				}
			}
			in.close();
			
			
			fstream = new FileInputStream(inputPeptideFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (countPeptide.containsKey(split[0])) {
					int count = (Integer)countPeptide.get(split[0]);
					count = count + 1;
					countPeptide.put(split[0], count);
				} else {
					countPeptide.put(split[0], 1);
				}
			}
			in.close();
			int count = 0;
			HashMap outlier = new HashMap();			
			HashMap hit_peptide = new HashMap();
			HashMap total_peptide = new HashMap();
			fstream = new FileInputStream(id_txt_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(";");
				if (split.length > 14) {
					String id = split[1];
					String peptide = split[0];												
					if (countPeptide.containsKey(peptide) && seq.containsKey(id)) {
						String sequence = (String)seq.get(id);					
						count++;
						int pepCount = (Integer)countPeptide.get(peptide);
						hit_peptide.put(peptide, split[14] + "\t" + pepCount + "\t" + id + "\t" + sequence.length());
						
						
					}
				}
			}
			in.close();
			
			Iterator itr = hit_peptide.keySet().iterator();
			while (itr.hasNext()) {
				String peptide = (String)itr.next();
				String line = (String)hit_peptide.get(peptide);
				String[] split_line = line.split("\t");
				String id = split_line[2];
				double pepCount = new Double(split_line[1]);
				// keep track the outlier
				if (outlier.containsKey(id)) {
					List<Double> list = (List<Double>)outlier.get(id);
					list.add(new Double(pepCount));
					outlier.put(id, list);
					
				} else {
					List<Double> list = new ArrayList<Double>();
					list.add(new Double(pepCount));
					outlier.put(id, list);							
				}
			}
			/*Iterator itr = outlier.keySet().iterator();
			while (itr.hasNext()) {
				String id = (String)itr.next();
				List<Double> list = (LinkedList<Double>)outlier.get(id);
				List<Double> outlier_list = MathTools.getOutliers(list);
				
			}*/
			System.out.println("Found uniq countPeptide: " + countPeptide.size());
			System.out.println(hit_peptide.size());
			
			double[] histogram = new double[100];
			for (int i = 0; i < histogram.length; i++) {
				histogram[i] = 0;
			}
			itr = hit_peptide.keySet().iterator();
			while (itr.hasNext()) {
				String peptide = (String)itr.next();
				String line = (String)hit_peptide.get(peptide);
				String[] split_line = line.split("\t");
				String id = split_line[2];
				double pepCount = new Double(split_line[1]);
				double start = new Double(split_line[0].split("to")[0].replaceAll("AA", "")) / new Double(split_line[3]) * 100;
				double end = new Double(split_line[0].split("to")[1].replaceAll("AA", "")) / new Double(split_line[3]) * 100;
				List<Double> list = (List<Double>)outlier.get(id);
				boolean outlier_bool = false;
				if (list.size() >= 5) {
					List<Double> outlier_list = MathTools.getOutliers(list);
					//System.out.println(id + "\t" + outlier_list.size());
					/*Iterator temp_itr = outlier_list.iterator();
					while (temp_itr.hasNext()) {
						double val = (Double)temp_itr.next();
						System.out.println(id + "\t" + val + "\t" + pepCount);
					}*/
					
					if (outlier_list.contains(pepCount)) {
						outlier_bool = true;
					}
				}
				//System.out.println(peptide + "\t" + line);
				if (list.size() >= 5 && !outlier_bool) {
					for (int i = 0; i < 100; i++) {
						if (start <= (i + 1) && (i + 1) <= end) {
							//histogram[i] += pepCount;
							histogram[i] += 1;
						}
					}
				}
				
				out.write(peptide + "\t" + line + "\t" + outlier_bool + "\n");
			}
			out.close();
			
			for (int i = 0; i < 100; i++) {
				System.out.println(i + "\t" + histogram[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
