package expressionanalysis.tools.headermod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Modify the header of the matrix
 * Example: /rgs01/project_space/bakergrp/NTRK/common/10X_fastq_files/processed/10xSingleCell/10xSingleCell/Combined_NesCre_GfapCreER_Additional_Analysis/ssGSEA
 * @author tshaw
 *
 */
public class ModifyHeaderOfMatrix {

	public static String description() {
		return "Modify the header of the matrix";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[matrixFile] [orig text] [replacement text] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String matrixFile = args[0];
			String orig = args[1];
			String replace = args[2];
			String outputFile = args[3];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header.replaceAll(orig, replace) + "\n");
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
