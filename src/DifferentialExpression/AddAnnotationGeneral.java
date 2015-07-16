package DifferentialExpression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class AddAnnotationGeneral {

	public static void main(String[] args) {
		
		try {
			
			/*
			BLALOCK_ALZHEIMERS_DISEASE_DN_List.txt            Histone_Modification_List.txt
			BLALOCK_ALZHEIMERS_DISEASE_INCIPIENT_DN_List.txt  KEGG_Alzheimer_List.txt
			BLALOCK_ALZHEIMERS_DISEASE_INCIPIENT_UP_List.txt  KEGG_Neurotrophin_Signaling_Pathway_List.txt
			BLALOCK_ALZHEIMERS_DISEASE_UP_List.txt            Kinase_List.txt
			Cell_Differentiation_List.txt                     LEE_NEURAL_CREST_STEM_CELL_DN_List.txt
			Chromatin_Remodeling_List.txt                     LEE_NEURAL_CREST_STEM_CELL_UP_List.txt
			Cytokines_Growth_Factor_List.txt                  Oncogene_List.txt
			DNA_Modification_List.txt                         Transcription_Factor_List.txt
			*/
			
			String outputFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\PengMMALZ\\FPKM_08222014\\SJMMALZ_RNAseq_Exon_Read_Count_gene_fpkm_uniq_annotation.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			String metaFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\PengMMALZ\\metadata.txt";
			HashMap map = generateHashMapList(metaFile);
			Iterator itr = map.keySet().iterator();
			String additional_header = "";
			LinkedList tags = new LinkedList();
			while (itr.hasNext()) {
				String tag = (String)itr.next();
				tags.add(tag);
				additional_header += "\t" + tag;
			}
			additional_header += "\tMETAINFOHIT";
			String inputFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\PengMMALZ\\FPKM_08222014\\SJMMALZ_RNAseq_Exon_Read_Count_gene_fpkm_uniq.txt"; //args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine() + additional_header;
			out.write(header + "\n");
			System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].replaceAll("\"", "");
				String annotation = "";
				boolean isTRUE = false;
				itr = tags.iterator();
				while (itr.hasNext()) {
					String tag = (String)itr.next();
					HashMap geneList = (HashMap)map.get(tag);
					if (geneList.containsKey(geneName)) {
						annotation += "\tTRUE";
						isTRUE = true;
					} else {
						annotation += "\tFALSE";	
					}
				}
				if (isTRUE) {
					annotation += "\tTRUE";
				} else {
					annotation += "\tFALSE";
				}
				
				out.write(str + annotation + "\n");
				System.out.println(str + annotation);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void execute(String[] args) {
		try {
									
			/*
			BLALOCK_ALZHEIMERS_DISEASE_DN_List.txt            Histone_Modification_List.txt
			BLALOCK_ALZHEIMERS_DISEASE_INCIPIENT_DN_List.txt  KEGG_Alzheimer_List.txt
			BLALOCK_ALZHEIMERS_DISEASE_INCIPIENT_UP_List.txt  KEGG_Neurotrophin_Signaling_Pathway_List.txt
			BLALOCK_ALZHEIMERS_DISEASE_UP_List.txt            Kinase_List.txt
			Cell_Differentiation_List.txt                     LEE_NEURAL_CREST_STEM_CELL_DN_List.txt
			Chromatin_Remodeling_List.txt                     LEE_NEURAL_CREST_STEM_CELL_UP_List.txt
			Cytokines_Growth_Factor_List.txt                  Oncogene_List.txt
			DNA_Modification_List.txt                         Transcription_Factor_List.txt
			*/
			
			String inputFile = args[0];
			HashMap map = generateHashMapList(args[1]);
			int index = new Integer(args[2]);
			Iterator itr = map.keySet().iterator();
			String additional_header = "";
			LinkedList tags = new LinkedList();
			while (itr.hasNext()) {
				String tag = (String)itr.next();
				tags.add(tag);
				additional_header += "\t" + tag;
			}
			additional_header += "\tMETAINFOHIT";
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine() + additional_header;
			System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				boolean isTRUE = false;
				String[] split = str.split("\t");
				String geneName = split[index].replaceAll("\"", "");
				String annotation = "";
				itr = tags.iterator();
				while (itr.hasNext()) {
					String tag = (String)itr.next();
					HashMap geneList = (HashMap)map.get(tag);
					if (geneList.containsKey(geneName)) {
						annotation += "\tTRUE";
						isTRUE = true;
					} else {
						annotation += "\tFALSE";	
					}
				}
				if (isTRUE) {
					annotation += "\tTRUE";
				} else {
					annotation += "\tFALSE";
				}
				System.out.println(str + annotation);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap generateHashMapList(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream2 = new FileInputStream(inputFile);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			while (in2.ready()) {
				String str = in2.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				String fileName = split[1];
				HashMap geneList = createGeneList(fileName);
				map.put(name, geneList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap createGeneList(String inputFile) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				map.put(str, str);
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
