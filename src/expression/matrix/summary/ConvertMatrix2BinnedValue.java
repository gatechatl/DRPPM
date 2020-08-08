package expression.matrix.summary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Convert matrix to a binned value
 * @author tshaw
 *
 */
public class ConvertMatrix2BinnedValue {

	public static String description() {
		return "Calculate the binned value of the matrix.";
	}
	public static String type() {
		return "DATAMATRIX";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [inputSummaryFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputMatrixFile = args[0];
			String inputSummaryFile = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputSummaryFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], str);
			}
			in.close();
			
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\n");
			split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				for (int i = 1; i < split.length; i++) {
					double new_value = Double.NaN;
					if (map.containsKey(split_header[i])) {
						String stats = (String)map.get(split_header[i]);
						
						String[] split_stats = stats.split("\t");
						double value = new Double(split[i]);
						new_value = 0;
						if (new Double(split_stats[3]) < value) {
							new_value = 1;
						}
						if (new Double(split_stats[4]) < value) {
							new_value = 2;
						}
						if (new Double(split_stats[5]) < value) {
							new_value = 3;
						}
					}
					out.write("\t" + new_value);
				}
				out.write("\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
