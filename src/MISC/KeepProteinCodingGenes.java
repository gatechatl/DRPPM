package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class KeepProteinCodingGenes {

	public static String description() {
		return "Keep only protein coding genes";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [gtf File] [meta_info: gene_id] [outputFileredMatrixFile]";
	}
	public static void execute(String[] args) {
		

		try {
			
			String noncodingFile = args[1];
			String type = args[2];
			HashMap proteinCodingGenes = proteincoding_genelist(noncodingFile, type);
			
			HashMap SJMMHGG = new HashMap();
			
			String outputFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_07282014\\SJMMHGG\\SJMMHGG_RNAseq_Exon_Read_Count_gene_fpkm_uniq_NRremove.txt";;
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String fileName = args[0]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_07282014\\SJMMHGG\\SJMMHGG_RNAseq_Exon_Read_Count_gene_fpkm_uniq.txt";
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String SJMMHGG_Tag = in.readLine();
			out.write(SJMMHGG_Tag + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split(",")[0].split(";")[0];
				if (proteinCodingGenes.containsKey(geneName)) {
					out.write(str + "\n");
				}
				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap proteincoding_genelist(String gtfFile, String meta_type) {
		HashMap map = new HashMap();
		try {						
			FileInputStream fstream = new FileInputStream(gtfFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 8) {
					String gene_type = grabMeta(split[8], "gene_type");
					String gene_biotype = grabMeta(split[8], "gene_biotype");
					String gene_name = grabMeta(split[8], meta_type);
					
					if (gene_type.equals("protein_coding") || gene_biotype.equals("protein_coding")) {
						map.put(gene_name, gene_name);
					}
				}								
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * Function for grabbing the meta information from the 
	 * @param text
	 * @param id
	 * @return
	 */
	public static String grabMeta(String text, String id) {
		String returnval = "";
		if (text.contains(id)) {
			String val = text.split(id)[1].split(";")[0].trim();
			val = val.replaceAll("\"", "");
			val.trim();
			returnval = val;
		}
		return returnval;
	}
}
