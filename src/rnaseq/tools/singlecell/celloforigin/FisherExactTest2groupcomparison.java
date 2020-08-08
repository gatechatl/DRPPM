package rnaseq.tools.singlecell.celloforigin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Performs fisher exact test to compare whether one group has more variant than the other group.
 * @author tshaw
 *
 */
public class FisherExactTest2groupcomparison {

	public static void main(String[] args) {
		String str = "10|0";
		System.out.println(str.split("\\|")[0]);
		System.out.println(str.split("\\|")[1]);
	}
	public static String type() {
		return "SNV";
	}
	public static String description() {
		return "Performs fisher exact test to compare whether one group has more variant than the other group.";
	}
	public static String parameter_info() {
		return "[snvTableMatrix] [group1File] [group2File] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
	
			String snvTable = args[0];
			String group1File = args[1];
			String group2File = args[2];
			String outputFile = args[3];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);		
			out.write("VariantName\tPvalue\tLog2Enrichment\tGroup1hasVariant\tGroup2hasVariant\tGroup1noVariant\tGroup2noVariant\n");
			LinkedList group1_list = new LinkedList();
			FileInputStream fstream = new FileInputStream(group1File);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				group1_list.add(str.trim().replaceAll("-", "."));
				//System.out.println(str.trim().replaceAll("-", "."));
			}
			in.close();
			
			LinkedList group2_list = new LinkedList();
			fstream = new FileInputStream(group2File);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				group2_list.add(str.trim().replaceAll("-", "."));
			}
			in.close();
			
			fstream = new FileInputStream(snvTable);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine().replaceAll("-", ".");
			//System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] header_split = header.split("\t");
				int group1_hasVariant = 0;
				int group2_hasVariant = 0;
				int group1_noVariant = 0;
				int group2_noVariant = 0;
				for (int i = 1; i < split.length; i++) {
					if (group1_list.contains(header_split[i])) {
						//System.out.println(split[i]);
						//System.out.println(split[i].split("\\|")[0]);
						//System.out.println(split[i].split("\\|")[1]);
						double tumor = new Double(split[i].split("\\|")[0]);
						double normal = new Double(split[i].split("\\|")[1]);
						double total = tumor + normal;
						if (tumor + normal > 0) {
							if (tumor / total >= 0.1 && tumor >= 3) {
								group1_hasVariant++;
							} else {
								group1_noVariant++;
							}
						}
					}
					if (group2_list.contains(header_split[i])) {
						//System.out.println(split[i]);
						//System.out.println(split[i].split("\\|")[0]);
						//System.out.println(split[i].split("\\|")[1]);
						double tumor = new Double(split[i].split("\\|")[0]);
						double normal = new Double(split[i].split("\\|")[1]);
						double total = tumor + normal;
						if (tumor + normal > 0) {
							if (tumor / total >= 0.1 && tumor >= 3) {
								group2_hasVariant++;
							} else {
								group2_noVariant++;
							}
						}
					}
				}
				double group1_ratio = (new Double(group1_hasVariant) + 0.0001) / ((group1_hasVariant + group1_noVariant) + 0.0001);
				double group2_ratio = (new Double(group2_hasVariant) + 0.0001) / ((group2_hasVariant + group2_noVariant) + 0.0001);
				double enrichment = group1_ratio / group2_ratio;
				double pval = MathTools.fisherTest(group1_hasVariant, group2_hasVariant, group1_noVariant, group2_noVariant);
				double log2Enrichment = MathTools.log2(enrichment + 0.0001);
				out.write(split[0] + "\t" + pval + "\t" + log2Enrichment + "\t" + group1_hasVariant + "\t" + group2_hasVariant + "\t" + group1_noVariant + "\t" + group2_noVariant + "\n");
			}
			in.close();
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
