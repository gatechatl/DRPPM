package ProteinFeature.AminoAcidResidue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import misc.CommandLine;
import statistics.general.MathTools;


/**
 * This will show the patches where the protein have particular
 * @author tshaw
 *
 */
public class CalculateResidueMotifBootstrap4 {

	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String description() {
		return "Calculate the frequency for particular residues";
	}
	public static String parameter_info() {
		return "[inputFile] [fastaFile] [buffer] [cutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			// using the perl script to read through the D2P2 database
			String inputFile = args[0];
			String fastaFile = args[1];
			int buffer = new Integer(args[2]);
			double cutoff = new Double(args[3]);
			String outputFile = args[4];

			LinkedList inputFileList = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				inputFileList.add(str);
			}
			in.close();
			HashMap map = readFastaFile(fastaFile);
			
			
			FileWriter fwriter2 = new FileWriter(outputFile);
            BufferedWriter out2 = new BufferedWriter(fwriter2);
            //out2.write("residues" + "\t" + "median_GRPR" + "\t" + "median_PR_ONLY" + "\t" + "median_GR_ONLY" + "\t" + "median_PROTEOME" + "\n");
            out2.write("residues" + "\t" + "median_GRPR" + "\t" + "median_PR_ONLY" + "\t" + "median_GR_ONLY" + "\t" + "median_PROTEOME" + "\t" + "GRPR" + "\t" + "PR_ONLY" + "\t" + "GR_ONLY" + "\t" + "PROTEOME" + "\n");
			String[] possible_residues = {"R", "H", "K", "D", "E", "S", "T", "N", "Q", "C", "U", "G", "P", "A", "V", "I", "L", "M", "F", "Y", "W"};
			String[] random_residues = {"R", "H", "K", "S", "T", "N", "Q", "C", "U", "G", "P", "A", "V", "I", "L", "M", "F", "Y", "W"};
			//String[] random_residues = {"D", "E"};
			
			HashMap map_combination = new HashMap();
			String orig_residues = "D,E";
			for (String res1: random_residues) {
				for (String res2: random_residues) {
						if (!res1.equals(res2)) {
							String residues = orig_residues + "," + res1 + "," + res2;
							LinkedList residue = new LinkedList();						
							for (String r: residues.split(",")) {
								residue.add(r);
								
							}
							Collections.sort(residue);
							boolean found = false;
							if (map_combination.containsKey(residue)) {
								found = true;
							} else {
								map_combination.put(residue, "");								
							}
							if (!found) {
							
								LinkedList length_PROTEOME = new LinkedList();
								LinkedList length_GRPR = new LinkedList();
								LinkedList length_GR_ONLY = new LinkedList();
								LinkedList length_PR_ONLY = new LinkedList();
								Iterator itr_list = inputFileList.iterator();
								while (itr_list.hasNext()) {
									String str = (String)itr_list.next();
								
									String[] split = str.split("\t");
									String type = split[0];
									String geneName = split[1];
									String uniprotName = split[2];
									String fasta = (String)map.get(uniprotName);								
									double[] range = new double[fasta.length()];
									for (int i = 0; i < range.length; i++) {
										range[i] = 0;
									}
									
									String buffer_str = UUID.randomUUID().toString();
									
									for (int i = 0; i < fasta.length(); i++) {
										if (residue.contains(fasta.substring(i, i + 1))) {
											range[i] = 1;
										}
									}
									
									File f = new File(buffer_str + "_" + "tmp_output");
									if (f.exists()) {
										f.delete();
									}
									
									double[] smooth_range = new double[range.length];
									for (int i = 0; i < range.length - buffer; i++) {
										double total = 0;
										for (int j = 0; j < buffer; j++) {
											total += range[i + j];
										}
										int midpoint =  i + (buffer / 2) + 1;
										smooth_range[midpoint] = total / buffer;
									}
									for (int i = range.length - buffer; i < range.length; i++) {
										if (i > 0) {
											smooth_range[i] = smooth_range[i - 1];
										}
									}
									
									int count = 0;
									boolean[] grabSeq = new boolean[range.length]; 
									for (int i = 0; i < range.length; i++) {
										if (smooth_range[i] >= cutoff) {
											count++;
										} else {
											count = 0;
										}
										if (count >= 1) {
											grabSeq[i] = true;
										} else {
											grabSeq[i] = false;
										}
									}
									
									/*for (int i = 0; i < range.length - 3; i++) {
										count = 0;
										for (int j = 0; j < 3; j++) {
											if (grabSeq[i + j]) {
												count++;
											}
										}
										if (count < 3) {
											grabSeq[i] = false;
										}
									}*/
									
									String finalOutput = "";
									String seq = "";
									String tempSeq = "";
									for (int i = 0; i < range.length; i++) {
										if (grabSeq[i]) {
											tempSeq += fasta.substring(i , i + 1);
											
										} else {
											if (seq.length() < tempSeq.length()) {
												seq = tempSeq;
											}
											tempSeq = "";
										}
									}
									if (seq.length() < tempSeq.length()) {
										seq = tempSeq;
									}
									if (type.equals("Human_Proteome")) {
										length_PROTEOME.add(seq.length());
									}
									if (type.equals("GRPR")) {
										length_GRPR.add(seq.length());
									}
									if (type.equals("GR_ONLY")) {
										length_GR_ONLY.add(seq.length());
									}
									if (type.equals("PR_ONLY")) {
										length_PR_ONLY.add(seq.length());
									}
									//String scatter_plot_script = generateScatterPlotScript(outputFolder + "/" + uniprotName + "_" + geneName + "_" + type + ".txt", outputImgFolder + "/" + uniprotName + "_" + geneName + "_" + type + ".png");
									//CommandLine.writeFile("script.r", scatter_plot_script);
									//CommandLine.executeCommand("R --vanilla < script.r");
									/*if (finalOutput.trim().length() > 0) {
										out2.write(">" + geneName + "_" + uniprotName + "_" + type + "_abovecutoff" + "\n");
										out2.write(finalOutput + "\n");
										length.add(finalOutput.length());
									}*/
									//System.out.println(">" + geneName + "_" + uniprotName + "_abovecutoff");
									//System.out.println(finalOutput);								
									
								}
								in.close();
//								out2.close();
								
								int i = 0;
								double satisfy_PROTEOME = 0;
								double[] values_PROTEOME = new double[length_PROTEOME.size()];
								Iterator itr = length_PROTEOME.iterator();
								while (itr.hasNext()) {
									int len = (Integer)itr.next();
									values_PROTEOME[i] = len;
									if (len >= 20) {
										satisfy_PROTEOME++;
									}
									i++;
								}
								double median_PROTEOME = MathTools.median(values_PROTEOME);
								
								i = 0;
								double satisfy_GRPR = 0;
								double[] values_GRPR = new double[length_GRPR.size()];
								itr = length_GRPR.iterator();
								while (itr.hasNext()) {
									int len = (Integer)itr.next();
									values_GRPR[i] = len;
									if (len >= 20) {
										satisfy_GRPR++;
									}
									i++;
								}
								double median_GRPR = MathTools.median(values_GRPR);
								i = 0;
								double satisfy_GR_ONLY = 0;
								double[] values_GR_ONLY = new double[length_GR_ONLY.size()];
								itr = length_GR_ONLY.iterator();
								while (itr.hasNext()) {
									int len = (Integer)itr.next();
									values_GR_ONLY[i] = len;
									if (len >= 20) {
										satisfy_GR_ONLY++;
									}
									i++;
								}
								double median_GR_ONLY = MathTools.median(values_GR_ONLY);
								i = 0;
								double satisfy_PR_ONLY = 0;
								double[] values_PR_ONLY = new double[length_PR_ONLY.size()];
								itr = length_PR_ONLY.iterator();
								while (itr.hasNext()) {
									int len = (Integer)itr.next();
									values_PR_ONLY[i] = len;
									if (len >= 20) {
										satisfy_PR_ONLY++;
									}
									i++;
								}
								double median_PR_ONLY = MathTools.median(values_PR_ONLY);
								
								
								//out2.write(residues + "\t" + median_GRPR + "\t" + median_PR_ONLY + "\t" + median_GR_ONLY + "\t" + median_PROTEOME + "\n");
								//out2.write(residues + "\t" + (satisfy_GRPR / length_GRPR.size()) + "\t" + (satisfy_PR_ONLY / length_PR_ONLY.size()) + "\t" + (satisfy_GR_ONLY / length_GR_ONLY.size()) + "\t" + (satisfy_PROTEOME / length_PROTEOME.size()) + "\n");
								out2.write(residues + "\t" + median_GRPR + "\t" + median_PR_ONLY + "\t" + median_GR_ONLY + "\t" + median_PROTEOME + "\t" + (satisfy_GRPR / length_GRPR.size()) + "\t" + (satisfy_PR_ONLY / length_PR_ONLY.size()) + "\t" + (satisfy_GR_ONLY / length_GR_ONLY.size()) + "\t" + (satisfy_PROTEOME / length_PROTEOME.size()) + "\n");
								out2.flush();
								
							}
						}
				}
			}
			out2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateScatterPlotScript(String inputFile, String outputFile) {
		String script = "library(ggplot2);\n";
		script += "data = read.csv(\"" + inputFile + "\", sep=\"\t\", header=T);\n";
		script += "png(file=\"" + outputFile + "\", width = 600, height = 400);\n";
		//script += "d = ggplot() + geom_point(data=data, aes(x=AAPosition,y=ModScore,colour=Type)) + geom_point(data=data, aes(x=AAPosition,y=AcceptFlag,colour=Type))\n";
		script += "d = ggplot() + geom_point(data=data, aes(x=AAPosition,y=ModScore,colour=Type)) + scale_y_continuous(limits = c(0.0, 1.2))\n";
		script += "d\n";
		script += "dev.off()\n";
		return script;
	}
	public static HashMap readFastaFile(String inputFile) {
		HashMap map = new HashMap();
		try {
			String uniprotName = "";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.split("\\|");
					uniprotName = split[1];					
					
				} else {
					if (map.containsKey(uniprotName)) {
						String seq = (String)map.get(uniprotName);
						seq += str.trim();
						map.put(uniprotName, seq);
					} else {
						map.put(uniprotName, str.trim());
					}
				}				
			}
			in.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}

