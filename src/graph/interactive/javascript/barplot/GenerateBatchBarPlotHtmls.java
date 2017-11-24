package graph.interactive.javascript.barplot;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class GenerateBatchBarPlotHtmls {


	public static String description() {
		return "Generate html-javascript file for displaying barplot in batchs";
	}
	public static String type() {
		return "JAVASCRIPT";
	}
	public static String parameter_info() {
		return "[inputMatrix] [expr_indices] [color_indices] [xaxis] [yaxis] [fontSize] [outputFolder]";
	}
	
	
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String expr_indices = args[1];
			String color_indices = args[2];
			String xaxis = args[3];
			String yaxis = args[4];
			String fontSize = args[5];
			String outputFolder = args[6];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			String header = "";			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				System.out.println("drppm -GenerateVerticalBarPlotJavaScript " + inputFile + " " + split[0] + " \"" + expr_indices + "\" " + "\"" + color_indices + "\" " + split[0] + " " + xaxis + " " + yaxis + " " + fontSize + " > " + outputFolder + "/" + split[0] + ".html");
				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
