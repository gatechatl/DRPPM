package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Perform check whether the files have the same gene order.
 * If satisfy the condidtion, then merge files into a single matrix
 * @author tshaw
 *
 */
public class CombineMultipleMatrixTogetherByRow {
	
	public static String description() {
		return "Perform check whether the files have the same gene order. If satisfy the condidtion, then merge files into a single matrix";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile1] [inputFile2] [inputFile3] [...]";
	}
	public static void execute(String[] args) {
		try {
			LinkedList geneList = new LinkedList();
			HashMap map = new HashMap();
			String first_file = args[0];			
			FileInputStream fstream = new FileInputStream(first_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			System.out.println(header);
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split_header.length != split.length) {
					System.out.println("Number of samples does't match number of values in each row.");
					System.exit(0);;
				}
				
			}
			in.close();
			
			for (String inputFile: args) {
				int i = 0;
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				String new_header = in.readLine();
			
				String[] split_new_header = new_header.split("\t");
				if (!header.equals(new_header)){
					System.out.println("Number of features does't match.");
					System.exit(0);;
				}
				
				while (in.ready()) {
					String str = in.readLine();
				
					String[] split = str.split("\t");
					if (split_header.length != split.length) {
						System.out.println("Number of samples does't match number of values in each row.");
						System.exit(0);;
					}
					System.out.println(str);
				}
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
