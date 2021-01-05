package idconversion.tools;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;


public class GTFFileExon {

	public static HashMap geneid2geneName = new HashMap();
	public static HashMap geneid_clean2geneName = new HashMap();
	public static HashMap gene2transcript = new HashMap();
	public static HashMap exon2transcript = new HashMap();
	public static HashMap coord_exon2transcript = new HashMap();
	public static HashMap transcript2gene = new HashMap();


	public static HashMap transcript2exon = new HashMap();
	public static HashMap transcript2coord_exon = new HashMap();

	public static void initialize(String fileName) {
		
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				String[] split = str.split("\t");
				if (split.length > 8) {
					String chr = split[0];
					String start = split[3];
					String end = split[4];
					String direct = split[6];
					String meta = split[8];
					
					// grab all the meta data information from the GTF file
					String gene_id = "";
					String transcript_id = ""; 
					String exon_number = ""; 
					String gene_biotype = "";
					
					if (meta.contains("gene_id")) {
						gene_id = grabMeta(meta, "gene_id");
					}
					if (meta.contains("transcript_id")) {
						transcript_id = grabMeta(meta, "transcript_id");
					}
					if (meta.contains("exon_number")) {
						exon_number = grabMeta(meta, "exon_number");
					}
					if (meta.contains("gene_biotype")) {
						gene_biotype = grabMeta(meta, "gene_biotype");
					} else if (meta.contains("gene_type")) {
						gene_biotype = grabMeta(meta, "gene_type");
					} else if (meta.contains("transcript_type")) {
						gene_biotype = grabMeta(meta, "transcript_type");
					}
					String gene_name = grabMeta(meta, "gene_name");
					String biotype = ""; //grabMeta(meta, "gene_biotype");
					if (meta.contains("gene_biotype")) {
						biotype = grabMeta(meta, "gene_biotype");
					} else if (meta.contains("gene_type")) {
						biotype = grabMeta(meta, "gene_type");
					} else if (meta.contains("transcript_type")) {
						biotype = grabMeta(meta, "transcript_type");
					}
					
				
					
					geneid2geneName.put(gene_id, gene_name); // convert gene id to gene name
					geneid_clean2geneName.put(gene_id.split("\\.")[0], gene_name);
					
					if (gene2transcript.containsKey(gene_id)) {
						String orig_transcript = (String)gene2transcript.get(gene_id);
						orig_transcript += "," + transcript_id;
						gene2transcript.put(gene_id, orig_transcript);
					} else {
						gene2transcript.put(gene_id, transcript_id);
					}
					transcript2gene.put(transcript_id, gene_id);

					// the tag for the exon
					String new_exon = chr + ":" + start + "-" + end + ":" + direct + ":" + exon_number;
					
					if (exon2transcript.containsKey(new_exon)) {
						String orig_transcript = (String)exon2transcript.get(new_exon);
						orig_transcript += "," + transcript_id;
						exon2transcript.put(new_exon, orig_transcript);
					} else {
						exon2transcript.put(new_exon, transcript_id);
					}
					
					String new_coord_exon = chr + ":" + start + "-" + end + ":" + direct;					
					if (coord_exon2transcript.containsKey(new_coord_exon)) {
						String orig_transcript = (String)coord_exon2transcript.get(new_coord_exon);
						orig_transcript += "," + transcript_id;
						coord_exon2transcript.put(new_coord_exon, orig_transcript);
					} else {
						coord_exon2transcript.put(new_coord_exon, transcript_id);
					}
					
					if (split[2].equals("exon")) {
						if (transcript2exon.containsKey(transcript_id)) {
							String orig_exon = (String)transcript2exon.get(transcript_id);
							orig_exon += "," + new_exon;
							transcript2exon.put(transcript_id, orig_exon);
						} else {
							transcript2exon.put(transcript_id, new_exon);
						}
					}
					
					if (split[2].equals("exon")) {
						if (transcript2coord_exon.containsKey(transcript_id)) {
							String orig_coord_exon = (String)transcript2coord_exon.get(transcript_id);
							orig_coord_exon += "," + new_coord_exon;
							transcript2coord_exon.put(transcript_id, orig_coord_exon);
						} else {
							transcript2coord_exon.put(transcript_id, new_coord_exon);
						}
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			if (text.split(id).length > 1) {
				String val = text.split(id)[1].split(";")[0].trim();
				val = val.replaceAll("\"", "");
				val.trim();
				returnval = val;
			}
		}
		return returnval;
	}

}
