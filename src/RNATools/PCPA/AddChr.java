package RNATools.PCPA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Specialized class for adding chr to bed files
 * @author tshaw
 *
 */
public class AddChr {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Specialized class for adding chr to bed files";
	}
	public static String parameter_info() {
		return "[inputBedFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String tmpFile = inputFile + ".tmp";
			FileWriter fwriter = new FileWriter(tmpFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				out.write("chr" + str + "\n");
			}
			in.close();
			out.close();

			fwriter = new FileWriter(inputFile);
			out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(tmpFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				out.write(str + "\n");
			}
			in.close();
			out.close();
			File f = new File(tmpFile);
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
