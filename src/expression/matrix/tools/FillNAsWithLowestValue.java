package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Replace NA with lowest value in matrix. 
 * @author tshaw
 *
 */
public class FillNAsWithLowestValue {
	public static String description() {
		return "Replace NA with lowest value.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [outputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrixFile = args[0];
			String outputMatrixFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputMatrixFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				
				boolean found = false;
				LinkedList find_lowest_list = new LinkedList();
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA")) {
						found = true;
						//out.write("\t" + "0.0");
					} else {
						find_lowest_list.add(new Double(split[i]));
						//out.write("\t" + split[i]);
					}
				}			
				
				double[] find_lowest_array = MathTools.convertListDouble2Double(find_lowest_list);
				double min = MathTools.min(find_lowest_array);
				if (found) {
					out.write(split[0]);
					for (int i = 1; i < split.length; i++) {
						if (split[i].equals("NA")) {
							out.write("\t" + min);
							//out.write("\t" + "0.0");
						} else {
							
							out.write("\t" + split[i]);
						}
					}
					out.write("\n");
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
