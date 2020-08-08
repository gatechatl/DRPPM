package stjude.projects.metabolomics;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class PlotIsotopicBarPlots {
	
	public static String description() {
		return "Generate isotopic barplots";
	}
	public static String type() {
		return "METABOLOMICS";
	}
	public static String parameter_info() {
		return "[inputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String prev = "";
			String metabolite = "";
			String inputFile = args[0];
			String stockScript = "dat <- read.table(text = \"";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				stockScript += split_header[i] + "  ";
			}
			boolean first = true;
			int index = 1;
			stockScript = stockScript.substring(0, stockScript.length() - 2) + "\n";
			String line = "";
			String endScript = "\", header = TRUE);\nbarplot(as.matrix(dat), srt = 45, adj = 1, xpd = TRUE, cex.names = 0.65, cex.lab = 0.65,las=2)";
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");		
				String values = "";
				for (int i = 1; i < split.length; i++) {
					values += "  " + new Double(split[i]).intValue();
				}
				if (split[0].contains(prev) && !prev.equals("")) {
					index++;
				} else {
					index = 1;
				}
				if (index == 1) {
					if (!first) {
						line += endScript;
						line += "\ntitle('" + metabolite + "')\n";
						line += "\ndev.off();\n";
						System.out.println(line);
					}
					first = false;
					prev = split[0];
					metabolite = split[0];
					line = "png(\"" + split[0] + ".png\");\n" + stockScript + index + values + "\n"; 
				} else {
					line += index + values + "\n";
				}
				
			}
			line += endScript;
			line += "\ntitle(\"" + metabolite + "\")\n";
			line += "\ndev.off();\n";
			System.out.println(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
