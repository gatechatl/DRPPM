package proteomics.phospho.tools.kinase.report;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import proteomics.phospho.tools.SummaryTools;
import misc.CommandLine;

/**
 * For each kinase, generate a folder with 
 * 1. text file: describe evidence of active state? pvalue for enrichment? 
 * 2. provide basic functional information about the kinase.
 * 3. heatmap of its known substrates
 * 4. heatmap for all predicted substrates
 * 5. draw correlation plot
 * 6. draw network for neighbor for interacting partners. 
 * @author tshaw
 *
 */
public class SummarizeKinaseInformation {

	public static String parameter_info() {
		return "[phospho_modfile] [kinaselist_file] [activated_file] [inhibited_file] [motif_all_reference_name] [up_family_enrichment_file] [dn_family_enrichment_file] [up_fasta_file] [dn_fasta_file] [all_phosphoreg_network_file] [output_kinase_summary_file] [kinase_heatmap_matrix_folder] [kinase_heatmap_image_folder]";
	}
	public static void execute(String[] args) {
		try {
			String phospho_modfile = args[0];
			String kinaselist_file = args[1]; // list of kinases
			String activated_file = args[2];
			String inhibited_file = args[3];
			String motif_all_reference_name = args[4];			
			String up_family_enrichment_file = args[5];
			String dn_family_enrichment_file = args[6];
			String up_fasta_file = args[7];
			String dn_fasta_file = args[8];
			String all_phosphoreg_network_file = args[9]; // this is the final file generated from JUMPkn
			
			String output_kinase_summary_file = args[10];
			String kinase_heatmap_matrix_folder = args[11]; // output folder for all the kinase matrix
			String kinase_heatmap_image_folder = args[12]; // output folder for all the images
			String kinase_phosphorylation_matrix_folder = args[13]; // contains a list of substrates currently on the kinase
			FileWriter fwriter = new FileWriter(output_kinase_summary_file);
			BufferedWriter out = new BufferedWriter(fwriter);		
			out.write("KinaseName\tPhosphoEvidenceActivated\tPhosphoEvidenceInhibited\tSubstrateEvidenceActivated\tSubstrateEvidenceInhibited\n");
			HashMap kinase2modsite = generateKinase2ModSiteMap(all_phosphoreg_network_file);
			HashMap expression_map = extractExpression(phospho_modfile);
			HashMap kinaselist_list = getKinaseList(kinaselist_file);
			HashMap accession2geneName = convertAccession2GeneName(phospho_modfile);
			Iterator itr = kinaselist_list.keySet().iterator();
			while (itr.hasNext()) {
				String kinase_name = (String)itr.next();
				String activated_flag = checkActivity(kinase_name, activated_file, up_fasta_file, dn_fasta_file);
				String inhibited_flag = checkActivity(kinase_name, inhibited_file, up_fasta_file, dn_fasta_file);
				double upreg_kinase_family_pvalue = checkEnrichmentFamily(kinase_name, motif_all_reference_name, up_family_enrichment_file);
				double dnreg_kinase_family_pvalue = checkEnrichmentFamily(kinase_name, motif_all_reference_name, dn_family_enrichment_file);
				String kinase_expression_file = kinase_phosphorylation_matrix_folder + "/" + kinase_name + "_phosphosite.txt";
				generate_kinase_phosphosite(expression_map, accession2geneName, kinase_name, kinase_expression_file);
				out.write(kinase_name + "\t" + activated_flag + "\t" + inhibited_flag + "\t" + upreg_kinase_family_pvalue + "\t" + dnreg_kinase_family_pvalue + "\n");
				
			}
			out.close();
			
			
			GenerateMatrixHeatmap(expression_map, kinase2modsite, kinase_heatmap_matrix_folder, kinase_heatmap_image_folder);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HashMap convertAccession2GeneName(String inputFile) {
		HashMap map = new HashMap();
		try {

			String[] expression_tags = {"sig126", "sig127N", "sig127C", "sig128N", "sig128C", "sig129N", "sig129C", "sig130N", "sig130C", "sig131"};
			String[] Mod_sites = {"Mod sites"};
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();						
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[2].split("\\|")[1];
				String geneName = split[4];
				map.put(accession, geneName);
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static HashMap getKinaseList(String inputFile) {
		HashMap uniq_kinase_map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String kinase_name = in.readLine().trim();
				uniq_kinase_map.put(kinase_name, kinase_name);
				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uniq_kinase_map;
	}
	/**
	 * First go through the reference_name to find the kinase family
	 * Based on the fisher exact test find the pvalue for enrichment.
	 * @param kinase_name
	 * @param reference_name
	 * @param inputFile
	 * @return
	 */
	public static double checkEnrichmentFamily(String kinase_name, String reference_name, String enrichmentFile) {
		double corrected_pvalue = -1;
		try {
			
			// First find kinase in reference motif_all list
			String family_name = "";
			FileInputStream fstream = new FileInputStream(reference_name);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneNames = split[2];
				for (String geneName: geneNames.split(",")) {
					if (geneName.equals(kinase_name.toUpperCase())) {
						family_name = split[7];
					}
				}
			}
			in.close();
			
			
			fstream = new FileInputStream(enrichmentFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals(family_name)) {
					double Diff_MotifFound = new Double(split[3]);
					double Diff_NotFound = new Double(split[4]);
					double NoDiff_Found = new Double(split[5]);
					double NoDiff_NotFound = new Double(split[6]);
					if ((Diff_MotifFound / Diff_NotFound) > (NoDiff_Found / NoDiff_NotFound)) {
						corrected_pvalue = new Double(split[2]);
					} else {
						corrected_pvalue = 1;
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return corrected_pvalue;
		
	}
	
	/**
	 * If a particular kinase name is present in the gene list 
	 * then it is considered to be active
	 * @param kinase_name
	 * @param inputFile
	 * @return
	 */
	public static String checkActivity(String kinase_name, String inputFile, String up_fasta, String dn_fasta) {
		boolean active = false;
		boolean upreg = false;
		boolean dnreg = false;
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].contains(kinase_name.toUpperCase())) {
					active = true;
				}
			}
			in.close();
			
			fstream = new FileInputStream(up_fasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					if (str.contains(kinase_name.toUpperCase())) {
						upreg = true;
					}
				}
			}
			in.close();
			
			fstream = new FileInputStream(dn_fasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					if (str.contains(kinase_name.toUpperCase())) {
						dnreg = true;
					}
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = "NA";
		if (active) {
			if (upreg) {
				result = "Found_and_ActivityUpReg";
			} else if (dnreg) {
				result = "Found_and_ActivityDnReg";
			} else {
				result = "Found_and_NoChangeInActivity";
			}
		}
		return result;
	}
	
	public static HashMap generateKinase2ModSiteMap(String all_data_file) {
		HashMap map = new HashMap();
		try {
			String[] phosphosite_kianse_header = {"Phophosite_GeneName"};
			
			FileInputStream fstream = new FileInputStream(all_data_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			int[] phosphosite_kianse_index = SummaryTools.header_expr_info(header, phosphosite_kianse_header);
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
					String phosphosite_kinases = split[phosphosite_kianse_index[0]];
					if (!phosphosite_kinases.equals("NA")) {
						for (String phosphosite_kinase: phosphosite_kinases.split(",")) {
							phosphosite_kinase = phosphosite_kinase.toUpperCase();
							if (map.containsKey(phosphosite_kinase)) {
								HashMap modsite = (HashMap)map.get(phosphosite_kinase);
								modsite.put(key, key);
								map.put(phosphosite_kinase, modsite);
							} else {
								HashMap modsite = new HashMap();
								modsite.put(key, key);
								map.put(phosphosite_kinase, modsite);								
							}
						}
					}
				}
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	public static void generate_kinase_phosphosite(HashMap expression_map, HashMap accession2geneName, String kinase_name, String outputFile) {
		
		
		try {
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);		
			out.write("accession\tsig126\tsig127N\tsig127C\tsig128N\tsig128C\tsig129N\tsig129C\tsig130N\tsig130C\tsig131\n"); 
			Iterator itr = expression_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				String accession = key.split("\t")[0];
				String geneName = (String)accession2geneName.get(accession);
				String line = (String)expression_map.get(key);
				if (geneName.toUpperCase().equals(kinase_name)) {
					out.write(line + "\n");
				}
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Input MODFILE from JUMPQ
	 * @param inputFile
	 * @return
	 */
	public static HashMap extractExpression(String inputFile) {
		HashMap expression_info = new HashMap();
		try {
			String[] expression_tags = {"sig126", "sig127N", "sig127C", "sig128N", "sig128C", "sig129N", "sig129C", "sig130N", "sig130C", "sig131"};
			String[] Mod_sites = {"Mod sites"};
			
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
						expression_info.put(key, split[2] + "_" + mod_site + "\t" + expr);
					}
				}
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expression_info;
	}
	/**
	 * For each kinase containing modsite
	 * Generate a matrix table file for expression
	 * @param expression_map
	 * @param kinase2modsite
	 * @param outputMatrixFile
	 */
	public static void GenerateMatrixHeatmap(HashMap expression_map, HashMap kinase2modsite, String outputMatrixFolder, String outputImageFolder) {
		
		try {
			HashMap unique_loc = new HashMap();
			String[] expression_tags = {"sig126", "sig127N", "sig127C", "sig128N", "sig128C", "sig129N", "sig129C", "sig130N", "sig130C", "sig131"};
			String[] Mod_sites = {"Mod sites"};
			
			Iterator itr = kinase2modsite.keySet().iterator();
			while (itr.hasNext()) {
				String kinase_name = (String)itr.next();
				HashMap modsites = (HashMap)kinase2modsite.get(kinase_name);
				
				FileWriter fwriter = new FileWriter(outputMatrixFolder + "/" + kinase_name + "_matrix.txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write("Name");
				for (String str: expression_tags) {
					out.write("\t" + str);
				}
				out.write("\n");
				int num = 0;
				Iterator itr2 = modsites.keySet().iterator();
				while (itr2.hasNext()) {
					String modsite = (String)itr2.next();
					if (expression_map.containsKey(modsite)) {
						String expr = (String)expression_map.get(modsite);
						out.write(expr + "\n");
						num++;
					}
				}
				out.close();
				if (num > 0) {
					String script = generateHeatmap(outputMatrixFolder + "/" + kinase_name + "_matrix.txt", outputImageFolder + "/" + kinase_name + ".png");
					CommandLine.writeFile("temp.r", script);
					CommandLine.executeCommand("R --vanilla < temp.r");
					File f = new File("temp.r");
					f.delete();
					
				}
			}

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
		
		script += "png(file = \"" + outputFile + "\", width=700,height=800)\n";
		script += "pheatmap(all, cluster_col = F, cluster_row = T, fontsize_row = 13, color=hmcols)\n";
		script += "dev.off();\n";
		return script;
	}
}
