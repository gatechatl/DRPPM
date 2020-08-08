package graph.interactive.javascript.scatterplot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Based on the meta information append a semi-unique color to the matrix. The matrix must have a column with categorical data.
 * @author tshaw
 *
 */
public class AppendColorAsMetaInfo {

	public static String type() {
		return "AppendColor";
	}
	public static String description() {
		return "Based on the meta information append a semi-unique color to the matrix. The matrix must have a column with categorical data.";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [type_index] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String[] colors = {"#a83232", "#a86b32", "#a8a632", "#8ea832", "#6ba832", "#32a83e", "#32a88b", "#3289a8", "#3263a8", "#3244a8", "#4e32a8", "#8532a8", "#a8329e", "#ffcccc", "#ffeccc", "#fffecc", "#e9ffcc", "#ccffda", "#ccfcff", "#ccdeff", "#d7ccff", "#f0ccff", "#ffccf0", "#ff0000", "#ff8c00", "#fffb00", "#8cff00", "#00ff11", "#00ffd5", "#00b7ff", "#006eff", "#1100ff", "#6f00ff", "#c300ff", "#ff00d9", "#050000"};
			HashMap appended_color_map = new HashMap();
			int color_index = 0;
			String inputMatrixFile = args[0];
			int type_index = new Integer(args[1]); 
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\t" + "Color\tOpacity\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String tag = split[type_index];
				if (!appended_color_map.containsKey(tag)) {
					System.out.println("Color_index: " + color_index);
					appended_color_map.put(tag, colors[color_index]);
					color_index++;
					if (color_index > colors.length - 1) {
						color_index = 0;
					}
				}
				String current_color = (String)appended_color_map.get(tag);
				out.write(str + "\t" + current_color + "\t" + 1.0 + "\n");
			}
			in.close();			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
