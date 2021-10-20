package rnaseq.tools.singlecell.mapping.pipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Remove genes that are NA
 * @author tshaw
 *
 */
public class RemoveNAGenes {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Remove genes that are NA";
	}
	public static String parameter_info() {
		return "[inputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];

			File f = new File(outputFile);
			/*if (f.exists()) {
				System.out.println(outputFile + " already exist. Please delete it before running.");
				System.exit(0);
			}*/
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!(split[0].equals("NA") || split[0].equals("null"))) {
					boolean good = true;
					for (int i = 1; i < split.length; i++) {
						if (split[i].equals("NaN")) {
							good = false;
						}
					}
					if (good) {
						out.write(str + "\n");
					}
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
