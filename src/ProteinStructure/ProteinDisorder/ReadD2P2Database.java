package ProteinStructure.ProteinDisorder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.UUID;

import misc.CommandLine;


/**
 * Based on the list of proteins from GR and PR
 * Obtain the disorder region
 * @author tshaw
 *
 */
public class ReadD2P2Database {

	public static String parameter_info() {
		return "[inputFile] [fastaFile] [d2p2_script] [buffer] [cutoff] [outputFolder] [outputFasta] [outputImgFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			
			// using the perl script to read through the D2P2 database
			String inputFile = args[0];
			String fastaFile = args[1];
			String d2p2_script = args[2];
			int buffer = new Integer(args[3]);
			double cutoff = new Double(args[4]);
			String outputFolder = args[5];
			String outputFasta = args[6];
			String outputImgFolder = args[7];
			HashMap map = readFastaFile(fastaFile);
			
			FileWriter fwriter2 = new FileWriter(outputFasta);
            BufferedWriter out2 = new BufferedWriter(fwriter2);

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = split[0];
				String geneName = split[1];
				String uniprotName = split[2];
				if (map.containsKey(uniprotName)) {
					
				} else {
					System.out.println("Missing UniprotName: " + uniprotName);
				}
				

			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
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
				
				String script = "python " + d2p2_script + " " + uniprotName + " > " + buffer_str + "_tmp_output";
				CommandLine.executeCommand(script);
				
				FileInputStream fstream2 = new FileInputStream(buffer_str + "_tmp_output");
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					int start = new Integer(split2[1]) - 1;
					int end = new Integer(split2[2]);
					if (end > range.length) {
						end = range.length;
					}
					for (int i = start; i < end; i++) {
						range[i] += new Double(1) / 9;
					}					
				}
				in2.close();
				
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
					smooth_range[i] = total / buffer;
				}
				for (int i = range.length - buffer; i < range.length; i++) {
					smooth_range[i] = smooth_range[i - 1];
				}
				
				int count = 0;
				boolean[] grabSeq = new boolean[range.length]; 
				for (int i = 0; i < range.length; i++) {
					if (smooth_range[i] > cutoff) {
						count++;
					} else {
						count = 0;
					}
					if (count >= buffer / 2) {
						grabSeq[i] = true;
					} else {
						grabSeq[i] = false;
					}
				}
				
				for (int i = 0; i < range.length - 5; i++) {
					count = 0;
					for (int j = 0; j < 5; j++) {
						if (grabSeq[i + j]) {
							count++;
						}
					}
					if (count < 5) {
						grabSeq[i] = false;
					}
				}
				
				String finalOutput = "";
				for (int i = 0; i < range.length; i++) {
					if (grabSeq[i]) {
						finalOutput += fasta.substring(i , i + 1);
					}
				}
				
				FileWriter fwriter = new FileWriter(outputFolder + "/" + uniprotName + "_" + geneName + "_" + type + ".txt");
	            BufferedWriter out = new BufferedWriter(fwriter);
	            out.write("AAPosition\tRawScore\tModScore\tAminoAcid\tType\n");
				for (int i = 0; i < range.length; i++) {
					String value = "NotDisorder_BelowCutoff";
					if (grabSeq[i]) {
						value = "Disorder_Predicted";
					}
					out.write((i + 1) + "\t" + range[i] + "\t" + smooth_range[i] + "\t" + fasta.substring(i , i + 1) + "\t" + value + "\n");
					//System.out.println(i + "\t" + range[i] + "\t" + smooth_range[i] + "\t" + fasta.substring(i , i + 1) + "\t" + grabSeq[i]);
				}
				out.close();
				String scatter_plot_script = generateScatterPlotScript(outputFolder + "/" + uniprotName + "_" + geneName + "_" + type + ".txt", outputImgFolder + "/" + uniprotName + "_" + geneName + "_" + type + ".png");
				CommandLine.writeFile("script.r", scatter_plot_script);
				CommandLine.executeCommand("R --vanilla < script.r");
				if (finalOutput.trim().length() > 0) {
					out2.write(">" + geneName + "_" + uniprotName + "_" + type + "_disorderRegion" + "\n");
					out2.write(finalOutput + "\n");
				}
				System.out.println(">" + geneName + "_" + uniprotName + "_disorderRegion");
				System.out.println(finalOutput);								
				
			}
			in.close();
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
