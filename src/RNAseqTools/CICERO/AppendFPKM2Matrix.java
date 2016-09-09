package RNAseqTools.CICERO;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class AppendFPKM2Matrix {

	public static String parameter_info() {
		return "";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			
			// extract data from matrix
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
