package stjude.projects.mckinnon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class McKinnonGCScatterPlotTTS {

	public static String description() {
		return "McKinnon GC Scatter Plot on Transcriptional Termination Site.";
	}
	public static String type() {
		return "MCKINNON";
	}
	public static String parameter_info() {
		return "[inputBEDFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputBEDFile = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			HashMap names = new HashMap();
			HashMap sample_expr = new HashMap();
			FileInputStream fstream = new FileInputStream(inputBEDFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[4];
				String chr = split[0];
				String start = split[1];
				String end = split[2];
				String dir = split[5];
				String tag = chr + ":" + start + "-" + end + "(" + dir + ")";
				names.put(geneName, geneName);				
			}
			in.close();			
			
			out.write("library(ggplot2)\n");
			out.write("library(gridExtra)\n");
			Iterator itr = names.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				//String path = "\\\\\\\\Sjm5vwpfs01.stjude.sjcrh.local\\\\data\\\\ResearchHome\\\\ProjectSpace\\\\mckingrp\\\\HGG\\\\common\\\\TimFiles\\\\McKinnon\\\\YoungdonManuscript\\\\Complete_GC_Scanning_TTS\\\\" + geneName + "_1000.fasta.ScannerResult.txt";
				
				String test_path = "Complete_GC_Scanning_TTS/" + geneName + "_1000.fasta.ScannerResult.txt";
				String path = test_path;
				File f = new File(test_path);
				if (f.exists()) {
					out.write("data = read.table(\"" + path + "\", sep=\"\\t\",header=T)\n");
					//out.write("tiff(filename = \"\\\\\\\\Sjm5vwpfs01.stjude.sjcrh.local\\\\data\\\\ResearchHome\\\\ProjectSpace\\\\mckingrp\\\\HGG\\\\common\\\\TimFiles\\\\McKinnon\\\\YoungdonManuscript\\\\Complete_GC_Scanning_TTS_Images\\\\" + geneName + "_1000_ScannerResult.txt.tif\",res=300, width=1600, height=1200)\n");
					out.write("png(filename = \"Complete_GC_Scanning_TTS_Images/" + geneName + "_1000_ScannerResult.txt.png\",res=300, width=1600, height=1200)\n");
					out.write("p1 <- ggplot(data, aes(x=Position, y=GC.)) +\n");
					out.write("geom_line(col=\"red\",size=2) + theme_bw() + scale_y_continuous(name=\"GC%\", limits=c(0, 1)) + geom_hline(yintercept = 0.5, linetype=\"dotted\")\n");
					out.write("p2 <- ggplot(data, aes(x=Position, y=GC_Skew)) +\n");
					out.write("geom_line(col=\"blue\",size=2) + theme_bw() + scale_y_continuous(name=\"GC Skew\", limits=c(-1, 1)) + geom_hline(yintercept = 0, linetype=\"dotted\")\n"); 
					out.write("grid.arrange(p2, p1, nrow = 2)\n");
					out.write("dev.off();\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
