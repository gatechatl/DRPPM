package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class SplitFilesRows {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		String description = "Split the file with the specified number of rows\n";
		description += "[inputFile] a matrix\n";
		description += "[numRowPerFile] a numerical number/integer. number of rows per file\n";
		return description;
	}
	public static String parameter_info() {
		return "[InputFile] [numRowPerFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int numRowPerFile = new Integer(args[1]);

			int index = 0;
			FileWriter fwriter = new FileWriter(inputFile + "_" + index);
			BufferedWriter out = new BufferedWriter(fwriter);					
			
			int total = 0;
			int name = 0;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				total++;
				if (index == 0) {
					out.close();
					fwriter = new FileWriter(inputFile + "_" + name);
					out = new BufferedWriter(fwriter);					
					out.write(header + "\n");
				} else {
					out.write(str + "\n");
				}
				index++;
				if (index > numRowPerFile) {
					index = 0;
					name++;
				}
			}
			in.close();
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
