package stjude.projects.jinghuizhang.tcga.reference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Generate a comprehensive reference connecting the TCGA file to the exon
 * 
 * @author tshaw
 *
 */
public class JinghuiZhangTCGAOrganizeData {

	public static String description() {
		return "Link the TCGA exon with TCGA gene expression.";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[exon_folderFile] [tcga_folder] [outputFile] [output_sampleList]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			
			String exon_folderFile = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\legacy\\";
			String tcga_folder = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\";
			String outputFile = args[2]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\legacy\\legacy_sample_meta_informaion_table.txt";
			String output_sampleList = args[3]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\matching_sample_list.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("file_name\tfile_id\tTCGA_id\n");
			
			File folders = new File(exon_folderFile);
			for (File folder: folders.listFiles()) {
				if (folder.getName().contains("TCGA") && folder.isDirectory()) {
					File manifest = new File(folder.getPath());
					for (File check: manifest.listFiles()) {
						if (check.getName().contains("json") && !check.getName().contains("json.swp")) {
							String inputFile = check.getPath();
							System.out.println(inputFile);
							String fileName1 = "NA";
							String fileName2 = "NA";
							String file_id = "NA";
							String submitter_id = "NA";
							String project_id = "NA";
							FileInputStream fstream = new FileInputStream(inputFile);
							DataInputStream din = new DataInputStream(fstream);
							BufferedReader in = new BufferedReader(new InputStreamReader(din));
							while (in.ready()) {
								String str = in.readLine();
								String[] split = str.split("\t");
								if (str.contains("file_name") && str.contains("exon_quantification.txt")) {
									fileName1 = str.replaceAll(" ", "").replaceAll("\"", "").replaceAll(":", "").replaceAll(",", "").replaceAll("file_name", "");
								}
								if (str.contains("file_name") && str.contains("exon.quantification.txt")) {
									fileName2 = str.replaceAll(" ", "").replaceAll("\"", "").replaceAll(":", "").replaceAll(",", "").replaceAll("file_name", "");
								}
								if (str.contains("file_id") && !str.contains("    ")) {
									file_id = str.replaceAll(" ", "").replaceAll("\"", "").replaceAll(":", "").replaceAll(",", "").replaceAll("file_id", "");
								}
								if (str.contains("submitter_id") && str.contains("TCGA") && !str.contains("edu")) {
									submitter_id = str.replaceAll(" ", "").replaceAll("\"", "").replaceAll(":", "").replaceAll(",", "").replaceAll("submitter_id", "").replaceAll("_exposure", "").replaceAll("entity_", "");
								}
								if (str.contains("project_id") && str.contains("TCGA") && !str.contains("edu")) {
									project_id = str.replaceAll(" ", "").replaceAll("\"", "").replaceAll(":", "").replaceAll(",", "").replaceAll("project_id", "").replaceAll("_exposure", "").replaceAll("entity_", "");
								}
								if (str.contains("file_id") && !str.contains("    ")) {
									out.write(fileName1 + "\t" + fileName2 + "\t" + file_id + "\t" + project_id + "\t" + submitter_id + "\n");
									String new_id = project_id + "_" + submitter_id;
									if (submitter_id.split("-").length > 1) {
										new_id = project_id + "_" + submitter_id.split("-")[0] + "-" + submitter_id.split("-")[1] + "-" + submitter_id.split("-")[2];
									}
									System.out.println(new_id);
									map.put(new_id, new_id);
									
									fileName1 = "NA";
									fileName2 = "NA";
									file_id = "NA";
									submitter_id = "NA";
									project_id = "NA";
								}							
							}
							in.close();
							
							
						}
					}
				}
			}
			out.close();
		

			
			HashMap found_map = new HashMap();
			// gene expression files
			String[] tumortypes = {"SARC", "PCPG", "PAAD", "READ", "ESCA", "TGCT", "MESO", "UVM", "ACC", "KICH", "UCS", "CHOL", "LAML", "DLBC", "BLCA", "BRCA", "CESC", "COAD", "GBM", "HNSC", "KIRC", "KIRP", "LGG", "LIHC", "LUAD", "LUSC", "OV", "PRAD", "SKCM", "STAD", "THCA", "THYM", "UCEC"};
			
			boolean do_once = true;
			for (String tumortype: tumortypes) {
				
				String inputFile = tcga_folder + "/" + tumortype + ".fpkm.txt";
				
				String outputFinalExpressionMatrix = tcga_folder + "/" + tumortype + ".updated.fpkm.txt";
				FileWriter fwriter_matrix = new FileWriter(outputFinalExpressionMatrix);
				BufferedWriter out_matrix = new BufferedWriter(fwriter_matrix);
				
				File f = new File(inputFile);
				if (f.exists()) {
					FileInputStream fstream = new FileInputStream(inputFile);
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					String header = in.readLine();
					String[] split_header = header.split("\t");
					out_matrix.write("GeneName");
					LinkedList list_index = new LinkedList();					
					for (int i = 1; i < split_header.length; i++) {
						String sample = split_header[i];
						if (map.containsKey(sample)) {
							found_map.put(sample, sample);
							list_index.add(i);
							out_matrix.write("\t" + sample);
						}
					}			
					out_matrix.write("\n");
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						out_matrix.write(split[0]);
						Iterator itr = list_index.iterator();
						while (itr.hasNext()) {
							int index = (Integer)itr.next();
							out_matrix.write("\t" + split[index]);
						}
						out_matrix.write("\n");
					}
					in.close();
					out_matrix.close();
				}
			}

						
			FileWriter fwriter2 = new FileWriter(output_sampleList);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			Iterator itr = found_map.keySet().iterator();			
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				//String newName = sampleName.split("_")[1];
				out2.write(sampleName + "\n");
			}
			out2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
