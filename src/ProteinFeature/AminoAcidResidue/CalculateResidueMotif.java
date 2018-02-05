package ProteinFeature.AminoAcidResidue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import misc.CommandLine;


/**
 * This will show the patches where the protein have particular
 * @author tshaw
 *
 */
public class CalculateResidueMotif {

	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String description() {
		return "Calculate the frequency for particular residues";
	}
	public static String parameter_info() {
		return "[inputFile] [fastaFile] [Residue] [buffer] [cutoff] [outputFolder] [outputFasta] [outputImgFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			
			// using the perl script to read through the D2P2 database
			String inputFile = args[0];
			String fastaFile = args[1];
			String residues = args[2];
			LinkedList residue = new LinkedList();
			for (String r: residues.split(",")) {
				residue.add(r);
			}
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
				
				for (int i = 0; i < range.length; i++) {
					if (grabSeq[i]) {
						finalOutput += fasta.substring(i , i + 1);
					}
				}
				
				FileWriter fwriter = new FileWriter(outputFolder + "/" + uniprotName + "_" + geneName + "_" + type + ".txt");
	            BufferedWriter out = new BufferedWriter(fwriter);
	            out.write("AAPosition\tRawScore\tModScore\tAminoAcid\tType\n");
				for (int i = 0; i < range.length; i++) {
					String value = "BelowCutoff";
					if (smooth_range[i] >= cutoff) {
						value = "AboveCutoff";
					}
					
					out.write((i + 1) + "\t" + range[i] + "\t" + smooth_range[i] + "\t" + fasta.substring(i , i + 1) + "\t" + value + "\n");
					//System.out.println(i + "\t" + range[i] + "\t" + smooth_range[i] + "\t" + fasta.substring(i , i + 1) + "\t" + grabSeq[i]);
				}
				out.close();
				//String scatter_plot_script = generateScatterPlotScript(outputFolder + "/" + uniprotName + "_" + geneName + "_" + type + ".txt", outputImgFolder + "/" + uniprotName + "_" + geneName + "_" + type + ".png");
				//CommandLine.writeFile("script.r", scatter_plot_script);
				//CommandLine.executeCommand("R --vanilla < script.r");
				if (finalOutput.trim().length() > 0) {
					out2.write(">" + geneName + "_" + uniprotName + "_" + type + "_abovecutoff" + "\n");
					out2.write(finalOutput + "\n");
				}
				System.out.println(">" + geneName + "_" + uniprotName + "_abovecutoff");
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
