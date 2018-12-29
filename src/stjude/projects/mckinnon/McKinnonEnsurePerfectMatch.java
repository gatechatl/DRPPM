package stjude.projects.mckinnon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class McKinnonEnsurePerfectMatch {

	public static String description() {
		return "McKinnon ensure perfect match for blat.";
	}
	public static String type() {
		return "MCKINNON";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
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
			out.write(header + "\n");
			header = in.readLine();
			out.write(header + "\n");
			header = in.readLine();
			out.write(header + "\n");
			header = in.readLine();
			out.write(header + "\n");
			
			while (in.ready()) {
				String str = in.readLine();			
				String[] split = str.split("\t");
				if (split.length > 12) {
					int total_size = new Integer(split[10]);
					if (split[11].equals("0") && total_size == new Integer(split[12]) && total_size == new Integer(split[0]) && split[17].equals("1")) {
						out.write(str + "\n");
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
