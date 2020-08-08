package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;

import misc.RunRScript;
import statistics.general.MathTools;

/**
 * Plot vcf coverage for 
 * @author tshaw
 *
 */
public class LeventakiGenerateVCFPlot {

	public static String type() {
		return "LEVENTAKI";
	}
	public static String description() {
		return "To generate a plot representing the coverage and b-allele %";
	}
	public static String parameter_info() {
		return "[input VCFFile] [window_size: 100?] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputVCFFile = args[0];
			int window_size = new Integer(args[1]); // 1000?
			int large_window_size = new Integer(args[2]);
			String outputFile = args[3];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Index\tChr\tVaf_Avg\tCoverage_Avg\tColor\tVaf_Avg_Large\tVaf_Avg_Gain_Large\tVaf_Avg_Loss_Large\tCoverage_Avg_Large\n");
			int count = 0;
			int pos = 0;
			LinkedList values = new LinkedList();
			LinkedList total_reads_values = new LinkedList();
			
			LinkedList values_gain_larger = new LinkedList();
			LinkedList values_loss_larger = new LinkedList();
			LinkedList values_larger = new LinkedList();
			LinkedList total_reads_values_larger = new LinkedList();
			
			FileInputStream fstream = new FileInputStream(inputVCFFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 8 && !split[0].contains("#")) {
					String chr = split[0];
					String loc = split[1];
					String vaf_info = split[9];
					String alt = split[4];
					double total_reads = new Double(vaf_info.split(":")[vaf_info.split(":").length - 2]);
					double ref_reads = new Double(vaf_info.split(":")[vaf_info.split(":").length - 1].split(",")[0]);
					double alt_reads = new Double(vaf_info.split(":")[vaf_info.split(":").length - 1].split(",")[1]);
					if (alt_reads != total_reads && total_reads >= 10 && ref_reads >= 5 && alt_reads >= 5 && !alt.equals("<*>")) {
						System.out.println(str + "\t" + total_reads + "\t" + ref_reads + "\t" + alt_reads);
						if (values.size() > window_size) {
							values.removeFirst();
							
						}
						values.add(alt_reads / total_reads);
						
						/*
						if (alt_reads / total_reads > 0.5) {
							if (values_gain_larger.size() > 1000) {
								values_gain_larger.removeFirst();
							}														
							values_gain_larger.add(alt_reads / total_reads);
						} else {
							if (values_loss_larger.size() > 1000) {
								values_loss_larger.removeFirst();
							}														
							values_loss_larger.add(alt_reads / total_reads);
						}*/
						if (values_larger.size() > large_window_size) {
							values_larger.removeFirst();
						}														
						values_larger.add(alt_reads / total_reads);
						
						if (total_reads_values.size() > window_size) {
							total_reads_values.removeFirst();
							
						}
						total_reads_values.add(total_reads);
						
						if (total_reads_values_larger.size() > large_window_size) {
							total_reads_values_larger.removeFirst();
						}
						total_reads_values_larger.add(total_reads);
						
						double vaf_avg = MathTools.mean(MathTools.convertListDouble2Double(values));
						double total_reads_avg = MathTools.mean(MathTools.convertListDouble2Double(total_reads_values));
						
						double vaf_avg_larger = MathTools.mean(MathTools.convertListDouble2Double(values_larger));
						double vaf_stdev_larger = MathTools.standardDeviation(MathTools.convertListDouble2Double(values_larger));
						double vaf_avg_gain_larger = vaf_avg_larger + vaf_stdev_larger;
						double vaf_avg_loss_larger = vaf_avg_larger - vaf_stdev_larger;
						double total_reads_avg_larger = MathTools.mean(MathTools.convertListDouble2Double(total_reads_values_larger));
						
						count++;
						if (count % window_size == 0) {
							String color = "black";
							
							if (!(chr.equals("X") || chr.equals("Y"))) {
								if (new Double(chr) % 2 == 0) {
									color = "black";
								} else {
									color = "blue";
								}
								
							} 
							if (chr.equals("X")) {
								color = "green";
							}
							if (chr.equals("Y")) {
								color = "yellow";
							}
							pos++;
							out.write(pos + "\t" + chr + "\t" + vaf_avg + "\t" + total_reads_avg + "\t" + color + "\t" + vaf_avg_larger + "\t" + vaf_avg_gain_larger + "\t" + vaf_avg_loss_larger + "\t" + total_reads_avg_larger + "\n");
						}
					}
				}
			}
			in.close();
			out.close();
			RunRScript.writeFile(outputFile + ".r", generate_script(outputFile));
			RunRScript.runRScript(outputFile + ".r");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_script(String fileName) {
		


		String script = "data = read.table(\"" + fileName + "\", header = T)\n";
		script += "png(file='" + fileName + ".png',width = 2000, height = 600)\n";
		script += "par(mfrow= c(2, 1))\n";
		script += "plot(data$Index, data$Vaf_Avg,ylab='B-allel')\n";
		script += "for (i in 1:length(data$Index)) {\n";
		script += "points(data$Index[i],data$Vaf_Avg[i],col=data$Color[i]);\n";
		script += "}\n";
		////script += "lines(data$Index,data$Vaf_Avg_Gain_Large,col=\"blue\");\n";
		script += "lines(data$Index,data$Vaf_Avg_Large,col=\"blue\");\n";
		//script += "lines(data$Index,data$Vaf_Avg_Loss_Large,col=\"blue\");\n";
		script += "plot(data$Index, data$Coverage_Avg,ylim=c(0,80),ylab='Coverage');\n";
		script += "for (i in 1:length(data$Index)) {\n";
		script += "points(data$Index[i],data$Coverage_Avg[i],col=data$Color[i]);\n";
		script += "}\n";
		script += "lines(data$Index, data$Coverage_Avg_Large,col=\"blue\");\n";
		script += "dev.off();\n";
		return script;
	}
}
