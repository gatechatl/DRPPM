package rnaseq.tools.EXONJUNCTION;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Convert the exonjunction into a mtrix
 * @author tshaw
 *
 */
public class ExonJunctionMatrix {

	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split = header.split("\t");
			out.write(split[0].replaceAll(":", "_").replaceAll("\\+", "_").replaceAll(",", "_") + split[1]);
			for (int i = 7; i < split.length; i++) {
				out.write("\t" + split[i]);
			}
			out.write("\n");			
			while (in.ready()) {
				String str = in.readLine();
				split = str.split("\t");				
				out.write(split[0].replaceAll(":", "_").replaceAll("\\+", "_").replaceAll(",", "_") + split[1]);
				for (int i = 7; i < split.length; i++) {
					out.write("\t" + split[i]);
				}
				out.write("\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
