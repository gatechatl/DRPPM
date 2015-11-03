package PhosphoTools.Heatmap;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * This function combines different tag and keeps only the expression info
 * @author tshaw
 *
 */
public class PhosphoExpr2HeatmapFriendly {

	public static String parameter_info() {
		return "[inputFile] [tags_str separated by ,] [expr_start_index] [expr_end_index]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String tags_str = args[1];
			String[] tags_spilt = tags_str.split(",");
			int numeric_start = new Integer(args[2]);
			int numeric_end = new Integer(args[3]);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = "";
				for (int i = 0; i < tags_spilt.length; i++) {
					name += split[new Integer(tags_spilt[i])].replaceAll("#", "_") + "_";
				}
				String result = "";
						
				for (int i = numeric_start; i <= numeric_end; i++) {
					if (i == numeric_start) {
						result = split[i];
					} else {
						result += "\t" + split[i];
					}
				}
				System.out.println(name + "\t" + result);
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
