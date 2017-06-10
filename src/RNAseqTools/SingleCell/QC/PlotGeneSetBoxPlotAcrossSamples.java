package RNAseqTools.SingleCell.QC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import MISC.CommandLine;

public class PlotGeneSetBoxPlotAcrossSamples {

	public static String type() {
		return "SingleCellQC";
	}
	public static String description() {
		return "Take gene coverage file and generate boxplot";
	}
	public static String parameter_info() {
		return "[inputFileList of GeneCoverageFile] [outputRFormatMatrix] [outputRscript]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFileList = args[0];
			String outputRFormatMatrix = args[1];
			String outputRscript = args[2];

		    HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				File f = new File(str);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(str);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header = in2.readLine();
					while (in2.ready()) {			
						String str2 = in2.readLine();
						String[] split = str2.split("\t");
						String geneName = split[0].split("\\(")[0].replaceAll("\\/", "_");
						//if (geneName.split("_").length > 1) {
						//	geneName = geneName.split("_")[0] + "_" + geneName.split("_")[1];
						//}
						map.put(geneName, geneName);
					}
					in2.close();
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				String outputMatrixFile = geneName + "_" + outputRFormatMatrix;
				FileWriter fwriter = new FileWriter(outputMatrixFile);
			    BufferedWriter out = new BufferedWriter(fwriter);
			    out.write("SampleName\tGeneSetName\tValue\n");
			    
			    
				fstream = new FileInputStream(inputFileList);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));			
				while (in.ready()) {
					String str = in.readLine();
					File f = new File(str);
					if (f.exists()) {
						FileInputStream fstream2 = new FileInputStream(str);
						DataInputStream din2 = new DataInputStream(fstream2);
						BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));	
						String header = in2.readLine();
						while (in2.ready()) {			
							String str2 = in2.readLine();
							String[] split = str2.split("\t");
							String geneName2 = split[0].split("\\(")[0].replaceAll("\\/", "_");
							//if (geneName2.split("_").length > 1) {							
							//	geneName2 = geneName2.split("_")[0] + "_" + geneName2.split("_")[1];
							//}
							if (geneName.equals(geneName2)) {
								for (int i = 1; i < split.length; i++) {
									double value = new Double(split[i]);
									String name = f.getName();
									if (name.split("_").length > 1) {
										name = name.split("_")[0] + "_" + name.split("_")[1];
									}
									out.write(name + "\t" + geneName + "\t" + value + "\n");
								}
							}
						}
						in2.close();
					} else {
						System.out.println("File doesn't exists: " + str);
					}
				}
				in.close();
				out.close();
				
				FileWriter fwriter2 = new FileWriter(outputRscript);
			    BufferedWriter out2 = new BufferedWriter(fwriter2);
			    out2.write(generateViolinPlot(outputMatrixFile, outputMatrixFile + ".png"));
			    out2.close();			    
			    CommandLine.executeCommand("R --vanilla < " + outputRscript);			    
			}					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generateViolinPlot(String inputFile, String outputFile) {
		String script = " \n";
		script += "library(ggplot2)\n";
		script += "data = read.table(\"" + inputFile + "\", sep=\"\\t\",header=T);\n";
		//script += "p1 = ggplot(data, aes(SampleName, Value)) + geom_violin() + geom_boxplot(width=0.1) + theme(plot.title=element_text(size=12), axis.text.x = element_text(size=12), axis.text.y = element_text(size=12), axis.title = element_text(size=12), legend.text = element_text(size=12), legend.title = element_text(size=12)) + geom_jitter(width = 0.2) + theme(axis.text.x = element_text(angle = 90, hjust = 1));\n";
		script += "p1 = ggplot(data, aes(SampleName, Value)) + geom_violin() + geom_boxplot(width=0.1) + theme(plot.title=element_text(size=35), axis.text.x = element_text(size=35), axis.text.y = element_text(size=35), axis.title = element_text(size=35), legend.text = element_text(size=35), legend.title = element_text(size=35)) + theme(axis.text.x = element_text(angle = 90, hjust = 1));\n";
		script += "png(file = \"" + outputFile + "\", width=1600,height=1000)\n";
		script += "p1\n";
		script += "dev.off();\n";
		return script;
	}
}
