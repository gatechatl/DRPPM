package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Generate a comprehensive table for leventaki's project
 * @author tshaw
 *
 */
public class SummarizeLeventakiProject {

	public static String type() {
		return "LEVENTAKI";
	}
	public static String description() {
		return "Combine the SNV detect result";
	}
	public static String parameter_info() {
		return "[pairedSNVdetectFileList] [pairedIndeldetectFileList] [unpairedSNVdetectFileList] [unpairedIndeldetectFileList] [pairedSNVdetectOutput] [pairedIndeldetectOutput] [unpairedSNVdetectOutput] [unpairedIndeldetectOutput]";
	}
	public static void execute(String[] args) {
		
		try {
				
			
			
			String pairedSNVdetectFileList = args[0];
			String pairedIndeldetectFileList = args[1];
			String unpairedSNVdetectFileList = args[2];
			String unpairedIndeldetectFileList = args[3];
			String pairedSNVdetectOutput = args[4];
			String pairedIndeldetectOutput = args[5];
			String unpairedSNVdetectOutput = args[6];
			String unpairedIndeldetectOutput = args[7];			
			HashMap recurrentGene = new HashMap();
			FileWriter fwriter_paired_snv = new FileWriter(pairedSNVdetectOutput);
			BufferedWriter out_paired_snv = new BufferedWriter(fwriter_paired_snv);
			
			FileWriter fwriter_paired_indel = new FileWriter(pairedIndeldetectOutput);
			BufferedWriter out_paired_indel = new BufferedWriter(fwriter_paired_indel);
			
			FileWriter fwriter_unpaired_snv = new FileWriter(unpairedSNVdetectOutput);
			BufferedWriter out_unpaired_snv = new BufferedWriter(fwriter_unpaired_snv);
			
			FileWriter fwriter_unpaired_indel = new FileWriter(unpairedIndeldetectOutput);
			BufferedWriter out_unpaired_indel = new BufferedWriter(fwriter_unpaired_indel);
			
			boolean header_paired_snv = false;
			boolean header_paired_indel = false;
			boolean header_unpaired_snv = false;
			boolean header_unpaired_indel = false;
			
			FileInputStream fstream = new FileInputStream(pairedSNVdetectFileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\\/");
				String sampleName = split[split.length - 1].split("_")[0];
				String fileName = str + "/" + sampleName + "_tier1_putative_mutation4_review.txt";
				File f = new File(fileName);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(fileName);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header = in2.readLine();
					//if (!header_paired_snv) {
						//out_paired_snv.write(header + "\n");
					//}
					//header_paired_snv = true;
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						String geneName = split2[0];
						String sample = split2[2];
						String sjqual = split2[1];
						String state = split2[16];
						double mutFreq = new Double(split2[9]);
						double refFreq = new Double(split2[10]);
						if ((sjqual.equals("SJHQ") || sjqual.equals("SJHLQ")) && !state.contains("bad") && !state.contains("Bad") && mutFreq / refFreq > 0.1) {
							if (recurrentGene.containsKey(geneName)) {							
								String orig_sample = (String)recurrentGene.get(geneName);
								if (!orig_sample.contains(sample)) {
									recurrentGene.put(geneName, orig_sample + "," + sample);
								}
							} else {
								recurrentGene.put(geneName, sample);
							}
						}
						//out_paired_snv.write(str2 + "\n");
					}
					in2.close();
				} else { // if file doesn't exists
					System.out.println("Missing SNV File: " + str + "/" + sampleName + "_tier1_putative_mutation4_review.txt");
				}
			}
			in.close();

			// parsing indel files
			fstream = new FileInputStream(pairedIndeldetectFileList);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\\/");				
				String sampleName = split[split.length - 2].split("_")[0];
				String D1 = ""; 
				//String G1 = "_G1"; 
				if (split[split.length - 2].equals("IndelAnalysis")) {
					sampleName = split[split.length - 3].split("_")[0];
					D1 = split[split.length - 3].split("_")[1];
					//G1 = "_G";
				} else {
					D1 = split[split.length - 2].split("_")[1];
					//G1 = "_G1";
				}

				String fileName = str + "/" + sampleName + "_putative_exon_somatic_indel_mutation.txt";
				File f = new File(fileName);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(fileName);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header = in2.readLine();

					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						String geneName = split2[0];
						String sample = split2[2].split("_")[0];
						String sjqual = split2[1];
						double mutFreq = new Double(split2[9]);
						double refFreq = new Double(split2[10]);
						if ((sjqual.equals("SJHQ") || sjqual.equals("SJHLQ")) && mutFreq / refFreq > 0.1) {
							if (recurrentGene.containsKey(geneName)) {							
								String orig_sample = (String)recurrentGene.get(geneName);
								if (!orig_sample.contains(sample)) {
									recurrentGene.put(geneName, orig_sample + "," + sample);
								}
							} else {
								recurrentGene.put(geneName, sample);
							}
						}
						//out_paired_snv.write(str2 + "\n");
					}
					in2.close();
				} else { // if file doesn't exists
					System.out.println("Missing Indel File: " + str + "/" + sampleName + "_tier1_putative_mutation4_review.txt");
				}
			}
			in.close();

			
			// unpaired SNVs
			fstream = new FileInputStream(unpairedSNVdetectFileList);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\\/");
				String sampleName = split[split.length - 1].split("_")[0];
				String fileName = str + "/" + sampleName + "_SNVforReview.txt";
				File f = new File(fileName);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(fileName);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header = in2.readLine();
					//if (!header_paired_snv) {
						//out_paired_snv.write(header + "\n");
					//}
					//header_paired_snv = true;
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						String geneName = split2[0];
						String sample = split2[2];
						String sjqual = split2[1];
						String state = split2[16];
						double mutFreq = new Double(split2[9]);
						double refFreq = new Double(split2[10]);
						if ((sjqual.equals("SJHQ") || sjqual.equals("SJHLQ")) && mutFreq / refFreq > 0.1) {
							if (recurrentGene.containsKey(geneName)) {							
								String orig_sample = (String)recurrentGene.get(geneName);
								if (!orig_sample.contains(sample)) {
									recurrentGene.put(geneName, orig_sample + "," + sample);
								}
							} else {
								recurrentGene.put(geneName, sample);
							}
						}
						//out_paired_snv.write(str2 + "\n");
					}
					in2.close();
				} else { // if file doesn't exists
					System.out.println("Missing Unpaired SNV File: " + str + "/" + sampleName + "_tier1_putative_mutation4_review.txt");
				}
			}
			in.close();

			//unpaired indel
			fstream = new FileInputStream(unpairedIndeldetectFileList);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\\/");				
				String sampleName = split[split.length - 1].split("_")[1];

				String fileName = str + "/" + sampleName + "_putative_exon_somatic_indel_mutation.4review.v2.txt";
				File f = new File(fileName);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(fileName);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header = in2.readLine();

					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						String geneName = split2[0];
						String sample = split2[2].split("_")[0];
						String sjqual = split2[1];
						double mutFreq = new Double(split2[9]);
						double refFreq = new Double(split2[10]);
						if ((sjqual.equals("SJHQ") || sjqual.equals("SJHLQ")) && mutFreq / refFreq > 0.1) {
							if (recurrentGene.containsKey(geneName)) {							
								String orig_sample = (String)recurrentGene.get(geneName);
								if (!orig_sample.contains(sample)) {
									recurrentGene.put(geneName, orig_sample + "," + sample);
								}
							} else {
								recurrentGene.put(geneName, sample);
							}
						}
						//out_paired_snv.write(str2 + "\n");
					}
					in2.close();
				} else { // if file doesn't exists
					System.out.println("Missing Unpaired Indel File: " + str + "/" + sampleName + "_tier1_putative_mutation4_review.txt");
				}
			}
			in.close();

			// output SNVs
			fstream = new FileInputStream(pairedSNVdetectFileList);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\\/");
				String sampleName = split[split.length - 1].split("_")[0];
				String D1 = split[split.length - 1].split("_")[1];
				String G1 = ""; //split[split.length - 1].split("_")[2];
				String pairSampleName = sampleName;
				if (D1.contains("D") || D1.contains("R")) {
					pairSampleName = sampleName + "_" + D1;
					//pairSampleName += pairSampleName + "_" + G1;
				}
				if (G1.contains("D") || G1.contains("R")) {
					pairSampleName = sampleName + "_" + G1;
					//pairSampleName += pairSampleName + "_" + D1;
				}
				
				String fileName = str + "/" + sampleName + "_tier1_putative_mutation4_review.txt";
				File f = new File(fileName);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(fileName);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header = in2.readLine();
					if (!header_paired_snv) {
						out_paired_snv.write("FileSampleName\t" + header + "\tRecurrent\n");
					}
					header_paired_snv = true;
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						if (recurrentGene.containsKey(split2[0])) {
						String sample2 = (String)recurrentGene.get(split2[0]);
							if (sample2.split(",").length > 1) {
								out_paired_snv.write(pairSampleName + "\t" + str2 + "\tyes\n");
							} else {
								out_paired_snv.write(pairSampleName + "\t" + str2 + "\tno\n");
							}
						} else {
							out_paired_snv.write(pairSampleName + "\t" + str2 + "\tno\n");
						}
						
					}
					in2.close();
				} // if file doesn't exists
			}
			in.close();						
			out_paired_snv.close();

			
			// output indel pipeline
			
			fstream = new FileInputStream(pairedIndeldetectFileList);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\\/");
				String sampleName = split[split.length - 2].split("_")[0];
				String D1 = ""; 
				String G1 = ""; 
				if (split[split.length - 2].equals("IndelAnalysis")) {
					sampleName = split[split.length - 3].split("_")[0];
					D1 = split[split.length - 3].split("_")[1];
					//G1 = split[split.length - 3].split("_")[2];
				} else {
					D1 = split[split.length - 2].split("_")[1];
					//G1 = split[split.length - 2].split("_")[2];
				}

				String pairSampleName = sampleName;
				if (D1.contains("D") || D1.contains("R")) {
					pairSampleName = sampleName + "_" + D1;
					//pairSampleName += pairSampleName + "_" + G1;
				}
				if (G1.contains("D") || G1.contains("R")) {
					pairSampleName = sampleName + "_" + G1;
					//pairSampleName += pairSampleName + "_" + D1;
				}
				
				String fileName = str + "/" + sampleName + "_putative_exon_somatic_indel_mutation.txt";
				File f = new File(fileName);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(fileName);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header = in2.readLine();
					if (!header_paired_indel) {
						out_paired_indel.write("FileSampleName\t" + header + "\tRecurrent\n");
					}
					header_paired_indel = true;
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						if (recurrentGene.containsKey(split2[0])) {
						String sample2 = (String)recurrentGene.get(split2[0]);
							if (sample2.split(",").length > 1) {
								out_paired_indel.write(pairSampleName + "\t" + str2 + "\tyes\n");
							} else {
								out_paired_indel.write(pairSampleName + "\t" + str2 + "\tno\n");
							}
						} else {
							out_paired_indel.write(pairSampleName + "\t" + str2 + "\tno\n");
						}
						
					}
					in2.close();
				} // if file doesn't exists
			}
			in.close();						
			out_paired_indel.close();

			// output unpaired SNVs
			fstream = new FileInputStream(unpairedSNVdetectFileList);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\\/");
				String sampleName = split[split.length - 1].split("_")[0];
				//String D1 = split[split.length - 1].split("_")[1];
				//String G1 = ""; //split[split.length - 1].split("_")[2];
				String unpairSampleName = sampleName;
				//if (D1.contains("D") || D1.contains("R")) {
					//unpairSampleName = sampleName + "_" + D1;
					//pairSampleName += pairSampleName + "_" + G1;
				//}
				//if (G1.contains("D") || G1.contains("R")) {
					//unpairSampleName = sampleName + "_" + G1;
					//pairSampleName += pairSampleName + "_" + D1;
				//}
								
				String fileName = str + "/" + sampleName + "_SNVforReview.txt";
				File f = new File(fileName);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(fileName);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header = in2.readLine();
					if (!header_unpaired_snv) {
						out_unpaired_snv.write("FileSampleName\t" + header + "\tRecurrent\n");
					}
					header_unpaired_snv = true;
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						if (recurrentGene.containsKey(split2[0])) {
						String sample2 = (String)recurrentGene.get(split2[0]);
							if (sample2.split(",").length > 1) {
								out_unpaired_snv.write(unpairSampleName + "\t" + str2 + "\tyes\n");
							} else {
								out_unpaired_snv.write(unpairSampleName + "\t" + str2 + "\tno\n");
							}
						} else {
							out_unpaired_snv.write(unpairSampleName + "\t" + str2 + "\tno\n");
						}
						
					}
					in2.close();
				} // if file doesn't exists
			}
			in.close();						
			out_unpaired_snv.close();

			// output unpaired indel pipeline
			
			fstream = new FileInputStream(unpairedIndeldetectFileList);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\\/");
				String sampleName = split[split.length - 1].split("_")[1];
				String unpairSampleName = sampleName;

				

				String fileName = str + "/" + sampleName + "_putative_exon_somatic_indel_mutation.4review.v2.txt";
				File f = new File(fileName);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(fileName);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header = in2.readLine();
					if (!header_unpaired_indel) {
						out_unpaired_indel.write("FileSampleName\t" + header + "\tRecurrent\n");
					}
					header_unpaired_indel = true;
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						if (recurrentGene.containsKey(split2[0])) {
						String sample2 = (String)recurrentGene.get(split2[0]);
							if (sample2.split(",").length > 1) {
								out_unpaired_indel.write(unpairSampleName + "\t" + str2 + "\tyes\n");
							} else {
								out_unpaired_indel.write(unpairSampleName + "\t" + str2 + "\tno\n");
							}
						} else {
							out_unpaired_indel.write(unpairSampleName + "\t" + str2 + "\tno\n");
						}
						
					}
					in2.close();
				} // if file doesn't exists
			}
			in.close();						
			out_unpaired_indel.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
