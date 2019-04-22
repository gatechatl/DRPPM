package graph.interactive.javascript.scatterplot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

/**
 * Customize the color based on their expression value
 * @author tshaw
 *
 */
public class UpdateScatterPlotColorBasedOnExpression {

	public static String description() {
		return "Regenerate the html with a new coloring scheme";
	}
	public static String type() {
		return "JAVASCRIPT";
	}
	public static String parameter_info() {
		return "[htmlPage] [geneExpressionMatrix] [geneName] [outputHTML]";
	}
	public static void execute(String[] args) {
		
		try {
			String htmlPage = args[0];
			String geneExpressionMatrix = args[1];
			String geneName = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap sample2expr = new HashMap();
			HashMap sample2color = new HashMap();
			double max = -1;
			double min = 99999999999.0;
			FileInputStream fstream = new FileInputStream(geneExpressionMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals(geneName)) {
					for (int i = 1; i < split.length; i++) {
						split[i] = MathTools.log2(new Double(split[i]) + 0.0001) + "";
						if (max < new Double(split[i])) {
							max = new Double(split[i]);
						}
						if (min > new Double(split[i])) {
							min = new Double(split[i]);
						}
						sample2expr.put(split_header[i], new Double(split[i]));
					}
				}
			}
			in.close();
			Iterator itr = sample2expr.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				double expr = (Double)sample2expr.get(sampleName);
				if (min == max) {
					String hex = String.format("#%02x%02x%02x", 0, 0, 0);
					sample2color.put(sampleName, hex);
				} else {
					int val = 255 - new Double((expr - min) / (max - min) * 255).intValue();					
					String hex = String.format("#%02x%02x%02x", 255, val, val);
					sample2color.put(sampleName, hex);
				}
			}
			fstream = new FileInputStream(htmlPage);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains("var inital_node_names = ")) {
					out.write("var inital_node_names = \"");
					Iterator itr2 = sample2color.keySet().iterator();
					while (itr2.hasNext()) {
						String sampleName = (String)itr2.next();
						String hex = (String)sample2color.get(sampleName);
						out.write(sampleName.split("-")[0] + " " + hex + "\\n");
					}
					out.write("\";\n");
				} else {
					out.write(str + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
