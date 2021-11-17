package rnaseq.splicing.csiminer.exonannotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

/**
 * Attach the GTEx peptides to each exon region
 * @author 4472414
 *
 */
public class CSIMinerCheckGTExProteomicsHitsPipeline {

	
	public static String type() {
		return "CSI-Miner";
	}
	public static String description() {
		return "Append the GTEx peptide annotation.";
	}
	public static String parameter_info() {
		return "[inputGTExAnnotationFile] [inputExonAnnotation] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			HashMap protein2coord = new HashMap();
			
			String inputGTExAnnotationFile = args[0]; //"/Users/4472414/Projects/GTEx_Proteomics_Search/PSM/outputTable_annotation.txt";
			String inputExonAnnotationFile = args[1]; //"/Users/4472414/References/genome/Exon_Annotation/exon_annotation_updated_withblat.txt";
			String outputFile = args[2]; //"/Users/4472414/References/genome/Exon_Annotation/exon_annotation_updated_withblat_GTExPSM.txt";
			
			FileInputStream fstream = new FileInputStream(inputGTExAnnotationFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] protein_coords = split[4].split(",");
				
				
				for (String protein_coord: protein_coords) {			
					String protein_id = protein_coord.split(":")[0];
					if (protein2coord.containsKey(protein_id)) {
						HashMap protein_coord_map = (HashMap)protein2coord.get(protein_id);
						protein_coord_map.put(protein_coord, new Integer(split[1]));
						protein2coord.put(protein_id, protein_coord_map);
					} else {
						HashMap protein_coord_map = new HashMap();
						protein_coord_map.put(protein_coord, new Integer(split[1]));
						protein2coord.put(protein_id, protein_coord_map);
					}
				}
			}
			in.close();			
			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			fstream = new FileInputStream(inputExonAnnotationFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tGTEx_Protein_Coord\tPSM_Count\tPSM_Count_LengthNorm\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String protein_coord = split[5];
				if (!protein_coord.equals("NA")) {
					String protein_id = protein_coord.split(":")[0];
					int start = new Integer(protein_coord.split(":")[1]);
					int end = new Integer(protein_coord.split(":")[2]);
					
					int exon_total_count = 0;
					double exon_total_count_norm = 0;
					String hit = "";
					if (protein2coord.containsKey(protein_id)) {
						HashMap protein_coord_map = (HashMap)protein2coord.get(protein_id);
						Iterator itr = protein_coord_map.keySet().iterator();
						while (itr.hasNext()) {
							String gtex_protein_coord = (String)itr.next();
							int gtex_start = new Integer(gtex_protein_coord.split(":")[1]);
							int gtex_end = new Integer(gtex_protein_coord.split(":")[2]);
							double length = end - start;
							int gtex_count = (Integer)protein_coord_map.get(gtex_protein_coord);
							if (MathTools.overlap(start,  end,  gtex_start,  gtex_end)) {
								exon_total_count += gtex_count;
								exon_total_count_norm = gtex_count / length;
								
								hit += gtex_protein_coord + ",";
							}
						}					
					}
					if (hit.equals("")) {
						hit = "NA";
					}
					out.write(str + "\t" + hit + "\t" + exon_total_count + "\t" + exon_total_count_norm + "\n");
				} else {
					out.write(str + "\t" + "NA" + "\t" + "NA" + "\t" + "NA" + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
