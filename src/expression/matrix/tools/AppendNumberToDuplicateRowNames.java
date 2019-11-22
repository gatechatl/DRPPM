package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppendNumberToDuplicateRowNames {

	public static String description() {
		return "Append tag to duplicated rownames.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputMatrix] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String outputFile = args[1];
			String inputMatrix = args[0];
			
			HashMap map = new HashMap();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			

			FileInputStream fstream = new FileInputStream(inputMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					for (int i = 1; i <= 100; i ++) {
						if (!map.containsKey(split[0] + "_Dup" + i)) {
							out.write(split[0] + "_Dup" + i);
							for (int j = 1; j < split.length; j++) {
								out.write("\t" + split[j]);
							}
							out.write("\n");
							map.put(split[0] + "_Dup" + i, split[0]);
							break;
						}
					}
				} else {
					map.put(split[0], split[0]);
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
