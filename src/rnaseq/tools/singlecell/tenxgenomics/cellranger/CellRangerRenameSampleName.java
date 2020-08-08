package rnaseq.tools.singlecell.tenxgenomics.cellranger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * After we generate the matrix, we should add a tag to make the sample unique
 * @author tshaw
 *
 */
public class CellRangerRenameSampleName {

	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "After we generate the matrix, we should add a tag to make the sample unique";
	}
	public static String parameter_info() {
		return "[inputMatrix] [orig_tag] [new_tag] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			String inputMatrix = args[0];
			String orig_tag = args[1];
			String new_tag = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[0]);
			for (int i = 1; i < split_header.length; i++) {
				split_header[i] = split_header[i].replaceAll(orig_tag,  new_tag);
				out.write("\t" + split_header[i]);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				out.write(str + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
