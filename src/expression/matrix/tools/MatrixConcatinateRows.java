package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Concatenate the rows across different matrix files
 * @author tshaw
 *
 */
public class MatrixConcatinateRows {

	public static String description() {
		return "Concatenate the rows across different matrix files";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[expression file1] [expression file2] [...]";
	}
	public static void execute(String[] args) {
		
		try {
			
			boolean is_first_header = true;
			String first_header = "";
			for (String inputFile: args) {
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));				
				String header = in.readLine();
				if (is_first_header) {
					first_header = header;
					is_first_header = false;
					System.out.println(header);
				}
				if (!header.equals(first_header)) {
					System.out.println("Headers didn't match up.");
					System.exit(0);
				}
				while (in.ready()) {
					String str = in.readLine();
					System.out.println(str);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
