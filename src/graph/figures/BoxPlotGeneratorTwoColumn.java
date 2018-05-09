package graph.figures;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Generate box plot from input file that contains two column
 * @author tshaw
 */
public class BoxPlotGeneratorTwoColumn {

	public static String type() {
		return "GRAPH";
	}
	public static String description() {
		return "Generate box plot from input file that contains two column";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split = header.split("\t");
			System.out.println(create_script(inputFile, outputFile, split[0], split[1]));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String create_script(String inputFile, String outputFile, String columnX, String columnY) {
		String script = "data = read.csv(\"" + inputFile + "\", sep=\"\\t\")\n";
		script += "library(ggplot2)\n";
		script += "png(file=\"" + outputFile + "\",width=700, height=500)\n";
		script += "p1 = ggplot(data, aes(x=" + columnX + ",y=" + columnY + ")) + geom_boxplot() + theme(axis.text.x=theme_text(angle=-90))\n";
		script += "p1\n";
		script += "dev.off();\n";
		return script;
	}
}
