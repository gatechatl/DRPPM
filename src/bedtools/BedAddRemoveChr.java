package bedtools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Append or remove chr tag to bed files
 * @author tshaw
 *
 */
public class BedAddRemoveChr {

	public static String type() {
		return "EXPRESSION";
	}
	public static String description() {
		return "Append or remove chr tag to bed files";
	}
	public static String parameter_info() {
		return "[inputBEDFile] [Append/Remove yes/no] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputBEDFile = args[0];
			String append_tag = args[1];
			String outputFile = args[2];			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputBEDFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				if (append_tag.toUpperCase().equals("YES") || append_tag.toUpperCase().equals("APPEND")) {
					out.write("chr" + str + "\n");
				} else if (append_tag.toUpperCase().equals("NO") || append_tag.toUpperCase().equals("REMOVE")) {
					if (str.substring(0, 3).equals("chr")) {
						out.write(str.substring(3, str.length()) + "\n");
					}
				} else {
					System.out.println("For the second parameter, please input either 'yes', 'no', 'append', 'remove'");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
