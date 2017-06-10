package RNAseqTools.Mapping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class FastaAddRemoveChr {

	public static String description() {
		return "Add or remove chr tag in a fasta file.";
	}
	public static String type() {
		return "RNASEQ";
	}
	public static String parameter_info() {
		return "[inputFile] [addFlag true/false] [tag: chr or mchr] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFiles = args[0];
			String addFlag = args[1];
			String tag = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputFiles);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (str.contains(">")) {
					if (addFlag.toUpperCase().equals("TRUE")) {
						out.write(">" + tag + str.replaceAll(">", "") + "\n");
					} else {
						if (split[0].contains(">" + tag)) {
							String new_str = "";
							for (int i = 0; i < split.length; i++) {
								if (i == 0) {
									new_str = split[0].replaceAll(">" + tag, ">");
								} else {
									new_str += "\t" + split[i];
								}
							}
							out.write(new_str + "\n");
						}
					}
				} else {
					out.write(str + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
