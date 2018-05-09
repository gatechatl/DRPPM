package proteomics.phospho.tools.annotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

import misc.CommandLine;
import PhosphoTools.SummaryTools;

/**
 * Based on the phosphosite annotation of gene activity
 * Generate matrix and heatmap for the annotation.
 * @author tshaw
 *
 */
public class ProteinActivityAnnotation {

	public static String parameter_info() {
		return "[phosphositeRegFile] [all_NetworkFile] [diff_NetworkFile] [phosphoFile] [outputAllNetworkFile] [outputDiffNetworkFile] [outputActiveModFile] [outputInhibitModFile] [outputActiveHeatmapFile] [outputInhibitHeatmapFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String phosphositeRegFile = args[0];
			String all_NetworkFile = args[1]; // Taking
			String diff_NetworkFile = args[2]; // Taking
			String phosphoFile = args[3];
			String outputAllNetworkFile = args[4];
			String outputDiffNetworkFile = args[5];
			String outputActiveModFile = args[6];
			String outputInhibitModFile = args[7];
			String outputActiveHeatmapFile = args[8];
			String outputInhibitHeatmapFile = args[9];
			
			HashMap activating_map = readPhosphoActivityFile(phosphositeRegFile, true);
			HashMap inhibiting_map = readPhosphoActivityFile(phosphositeRegFile, false);
			AppendProteinActivityInformation(all_NetworkFile, activating_map, inhibiting_map, outputAllNetworkFile);
			AppendProteinActivityInformation(diff_NetworkFile, activating_map, inhibiting_map, outputDiffNetworkFile);
			extractExpression(phosphoFile, activating_map, inhibiting_map, outputActiveModFile, outputInhibitModFile);
			String script = generateHeatmap(outputActiveModFile, outputActiveHeatmapFile);
			CommandLine.writeFile("temp1.r", script);
			CommandLine.executeCommand("R --vanilla < temp1.r");
			File f = new File("temp1.r");
			f.delete();
			
			script = generateHeatmap(outputInhibitModFile, outputInhibitHeatmapFile);
			CommandLine.writeFile("temp2.r", script);
			CommandLine.executeCommand("R --vanilla < temp2.r");
			f = new File("temp2.r");
			f.delete();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void extractExpression(String inputFile, HashMap activating_map, HashMap inhibiting_map, String outputActiveFile, String outputInhibitFile) {
		
		try {
			HashMap unique_loc = new HashMap();
			String[] expression_tags = {"sig126", "sig127N", "sig127C", "sig128N", "sig128C", "sig129N", "sig129C", "sig130N", "sig130C", "sig131"};
			String[] Mod_sites = {"Mod sites"};
			

			FileWriter fwriter_activating = new FileWriter(outputActiveFile);
			BufferedWriter out_activating = new BufferedWriter(fwriter_activating);
			out_activating.write("Name");
			for (String str: expression_tags) {
				out_activating.write("\t" + str);
			}
			out_activating.write("\n");
			FileWriter fwriter_inhibiting = new FileWriter(outputInhibitFile);
			BufferedWriter out_inhibiting = new BufferedWriter(fwriter_inhibiting);
			out_inhibiting.write("Name");
			for (String str: expression_tags) {
				out_inhibiting.write("\t" + str);
			}
			out_inhibiting.write("\n");
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			int[] expr_index = SummaryTools.header_expr_info(header, expression_tags);
			int[] modsite_index = SummaryTools.header_expr_info(header, Mod_sites);
			
			
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[2].split("\\|")[1];	
				if (split[2].split("\\|").length > 1) {
					String mod_sites = split[modsite_index[0]];
					String[] mod_sites_split = mod_sites.split(",");
					String expr = "";
					for (int i = 0; i < expr_index.length; i++) {
						if (expr.equals("")) {
							expr = split[expr_index[i]];
						} else {
							expr += "\t" + split[expr_index[i]];
						}
					}
					
					for (String mod_site: mod_sites_split) {
						String key = accession + "\t" + mod_site;
						if (activating_map.containsKey(key) && !unique_loc.containsKey(key)) {
							out_activating.write(split[2] + "_" + mod_site + "\t" + expr + "\n");
						}
						if (inhibiting_map.containsKey(key) && !unique_loc.containsKey(key)) {
							out_inhibiting.write(split[2] + "_" + mod_site + "\t" + expr + "\n");
						}
						unique_loc.put(key, key);
					}
				}
			}
			in.close();
			
			out_activating.close();
			out_inhibiting.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateHeatmap(String inputFile, String outputFile) {
		String script = "";

		script += "allDat = read.table(\"" + inputFile + "\", header=TRUE, row.names=1 );\n";
		script += "scaled = apply(allDat, 1, scale);\n";
		script += "all = apply(scaled, 1, rev)\n";
		//script += "newall = apply(all, 2, function(x) ifelse(x > 3, 3, x))\n";
		//script += "newall = apply(newall, 2, function(x) ifelse(x < -3, -3, x))\n";
		//script += "all = newall;\n";
		script += "colnames(all) = colnames(allDat)\n";
		script += "library(pheatmap)\n";
		script += "minimum = -3;\n";
		script += "maximum = 3;\n";
		script += "bk = c(seq(minimum,minimum/2, length=100), seq(minimum/2,maximum/2,length=100),seq(maximum/2,maximum,length=100))\n";
		script += "hmcols<- colorRampPalette(c(\"dark blue\",\"blue\",\"white\",\"red\", \"dark red\"))(length(bk)-1)\n";
		
		script += "png(file = \"" + outputFile + "\", width=1000,height=1200)\n";
		script += "pheatmap(all, cluster_col = F, cluster_row = T, fontsize_row = 9, color=hmcols)\n";
		script += "dev.off();\n";
		return script;
	}
	public static void AppendProteinActivityInformation(String inputFile, HashMap active, HashMap inhibited, String outputFile) {
		try {
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tActivityInfo\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				String activity_note = "NA";
				if (name.split("\\|").length > 1) {
					String accession = name.split("\\|")[1];
					String site = split[1];
					if (site.contains("S")) {
						site = "S" + site.split("S")[0];
					} else if (site.contains("T")) {
						site = "T" + site.split("T")[0];
					} else if (site.contains("Y")) {
						site = "Y" + site.split("Y")[0];
					}
					//System.out.println(accession + "\t" + site);
					String key = accession + "\t" + site;
					
					boolean found_active = false;
					boolean found_inhibited = false;
					if (active.containsKey(key)) {
						found_active = true;
						activity_note = "active";
					}
					if (inhibited.containsKey(key)) {
						found_inhibited = true;
						if (activity_note.equals("active")) {
							activity_note = "conflict";
						} else {							
							activity_note = "inhibited";
						}
					}
				}
				out.write(str + "\t" + activity_note + "\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap readPhosphoActivityFile(String inputFile, boolean active) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (active) {
					if (split[4].contains("activity") && split[4].contains("induced") && !split[4].contains("inhibited")) {
						map.put(split[1] + "\t" + split[3].replaceAll("-p", ""), str);
						//System.out.println(str);
					}			
				} else {
					if (split[4].contains("activity") && !split[4].contains("induced") && split[4].contains("inhibited")) {
						//System.out.println(str);
						map.put(split[1] + "\t" + split[3].replaceAll("-p", ""), str);
					}	
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
