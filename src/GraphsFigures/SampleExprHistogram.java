package GraphsFigures;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class SampleExprHistogram {

	public static void main(String[] args) {
		
	}
	public static void execute(String[] args) {
		String inputFile = args[0];
		String outputPath = args[1];
		boolean takeLog = Boolean.valueOf(args[2]);
		double min = readNum(args[3]);
		double max = readNum(args[4]);
		System.out.println(createHistogram(inputFile, outputPath, takeLog, min, max));
		
	}
	public static double readNum(String str) {
		if (str.toUpperCase().equals("NA")) {
			return -1;
		}
		if (isNumeric(str)) {
			return new Double(str);
		}
		return -1;
	}
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	public static String createHistogram(String inputFile, String outputPath, boolean logFile, double min, double max) {
		LinkedList sampleNames = new LinkedList();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			
			String[] split = header.split("\t");
			for (int i = 1; i < split.length; i++) {
				sampleNames.add(split[i]);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String script = "";
		script += "library(ggplot2);\n";
		script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=T, row.names=1);\n";
		
		Iterator itr = sampleNames.iterator();
		while (itr.hasNext()) {
			String sampleName = (String)itr.next();
			script += "png(file = \"" + outputPath + "/" + sampleName + "_histogram.png\", width=1000,height=800)\n";
			if (logFile) {															
				if (min >= 0 && max >= 0) {
					script += "qplot(log2(data[,'" + sampleName + "']), xlim=c(" + min + "," + max + "));\n";
				} else {
					script += "qplot(log2(data[,'" + sampleName + "']));\n";
				}
			} else {
				if (min >= 0 && max >= 0) {
					script += "qplot(data[,'" + sampleName + "'], xlim=c(" + min + "," + max + "));\n";
				} else {
					script += "qplot(data[,'" + sampleName + "']);\n";
				}				
			}
			script += "dev.off();\n";
		}
		return script;
	}
}
