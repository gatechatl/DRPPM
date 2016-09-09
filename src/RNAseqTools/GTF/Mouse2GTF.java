package RNAseqTools.GTF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * 
 * @author tshaw
 *
 */
public class Mouse2GTF {

	public static String description() {
		return "Add mchr to GTF";
	}
	public static String type() {
		return "GTF";
	}
	public static String parameter_info() {
		return "[inputFile] [appendStr] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String appendStr = args[1];
			String outputFile = args[2];			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				out.write(appendStr + str + "\n");
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
