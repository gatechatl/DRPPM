package rnaseq.splicing.csiminer.reference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;


/**
 * Generate the uniprot fasta file based on the candidate list
 * @author tshaw
 *
 */
public class CSIMinerCandidateRegion2Fasta {
	

	public static String description() {
		return "Generate the uniprot fasta file based on the candidate list";
	}
	public static String type() {
		return "CSIMINER";
	}
	public static String parameter_info() {
		return "[geneList] [inputUniprotFasta] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			HashMap geneMap = new HashMap();
			String geneList = args[0] ;//"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\TSNE_ECM_CellSurface\\comprehensive_gene_list.txt";
			String inputUniprotFasta = args[1]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_full_exon_analysis\\Homo_sapiens_uniprot_sprot_combined.fasta";
			String outputFile = args[2]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\TSNE_ECM_CellSurface\\comprehensive_gene_list.txt.fasta";
			FileInputStream fstream = new FileInputStream(geneList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				geneMap.put(str, str);
			}
			in.close();
			

			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			String uniprotID = "NA";
			String geneName = "NA";
			boolean write = false;
			
			fstream = new FileInputStream(inputUniprotFasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					write = false;
					String[] split = str.split(" ");
					uniprotID = split[0].replaceAll(">", "").split("\\|")[1];
					geneName = "NA";
					if (!str.contains("##Decoy")) {
						for (String stuff: split) {
							if (stuff.contains("GN=")) {
								geneName = stuff.split("=")[1];
							}
						}					
					} else {
						uniprotID = "NA";
					}
					if (geneMap.containsKey(geneName)) {
						write = true;
					}
					if (write) {
						out.write("\n" + str.split(" ")[0] + "\n");
					}
				} else {
					if (write) {
						out.write(str);
					}
				}			
			}
			
			out.close();
			
		} catch (Exception e ){
			e.printStackTrace();
		}
	}
}

