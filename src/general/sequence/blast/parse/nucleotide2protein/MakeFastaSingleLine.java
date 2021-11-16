package general.sequence.blast.parse.nucleotide2protein;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class MakeFastaSingleLine {

	public static String description() {
		return "Regenerate fasta file with a single sequence line";
	}
	public static String type() {
		return "FASTA";
	}
	public static String parameter_info() {
		return "[inputFastaFile] [outputFastaFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			boolean first = true;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					if (first) {
						out.write(str + "\n");
						first = false;
					} else {
						out.write("\n" + str + "\n");
					}
				} else {
					out.write(str);
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
