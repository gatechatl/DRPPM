package stjude.projects.jinghuizhang.dexseq.exon.cart.candidate.membrane;

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
public class JinghuiZhangGenerateFastaFileFromCandidate {

	
	public static void main(String[] args) {
		
		try {
			HashMap geneMap = new HashMap();
			String geneList = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\TSNE_ECM_CellSurface\\comprehensive_gene_list.txt";
			FileInputStream fstream = new FileInputStream(geneList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				geneMap.put(str, str);
			}
			in.close();
			

			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\TSNE_ECM_CellSurface\\comprehensive_gene_list.txt.fasta";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			String uniprotID = "NA";
			String geneName = "NA";
			boolean write = false;
			String inputUniprotFasta = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_full_exon_analysis\\Homo_sapiens_uniprot_sprot_combined.fasta";
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
