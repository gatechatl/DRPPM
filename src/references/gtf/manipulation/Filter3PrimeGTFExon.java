package references.gtf.manipulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class Filter3PrimeGTFExon {

	public static String type() {
		return "GTFFILE";
	}
	public static String description() {
		return "Filter the 3' end of the GTF exon annotation file";
	}
	public static String parameter_info() {
		return "[inputGTFFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputGTFFile = args[0];
			String outputFile = args[1];
			
			File f = new File(outputFile);
			if (f.exists()) {
				System.out.println(outputFile + " already exists. Please delete and rerun.");
				System.exit(0);
			}
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap transcripts = new HashMap();
			int total = 0;
			FileInputStream fstream = new FileInputStream(inputGTFFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				total++;
				String[] split = str.split("\t");
				if (split.length > 8) {
					String type = split[2];
					String geneID = grabMeta(split[8], "gene_id");
					String direction = split[6];
					String transcriptID = grabMeta(split[8], "transcript_id");
					int start = new Integer(split[3]);
					int end = new Integer(split[4]);
					if (type.equals("exon")) {
						if (direction.equals("+")) {
							if (transcripts.containsKey(transcriptID)) {
								String line = (String)transcripts.get(transcriptID);
								String[] line_split = line.split("\t");
								int line_start = new Integer(line_split[3]);
								int line_end = new Integer(line_split[4]);
								if (line_start < start) {
									transcripts.put(transcriptID, str);
								}
							} else {
								transcripts.put(transcriptID, str);
							}
						} else if (direction.equals("-")) {
							if (transcripts.containsKey(transcriptID)) {
								String line = (String)transcripts.get(transcriptID);
								String[] line_split = line.split("\t");
								int line_start = new Integer(line_split[3]);
								int line_end = new Integer(line_split[4]);
								if (line_end > end) {
									transcripts.put(transcriptID, str);
								}
							} else {
								transcripts.put(transcriptID, str);
							}
						}
					}
				} else {
					out.write(str + "\n");
				}
			}
			in.close();
			
			
			System.out.println("Total: " + total);
			
			//HashMap line = new HashMap();
			//Iterator itr = transcripts.keySet().iterator();
			//while (itr.hasNext()) {
			//	String line = (String)itr.next();
			//}
			total = 0;
			fstream = new FileInputStream(inputGTFFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				total++;
				if (total % 100000 == 0) {
					System.out.println(total);
				}
				String[] split = str.split("\t");
				if (split.length > 8) {
					String type = split[2];
					String geneID = grabMeta(split[8], "gene_id");
					String direction = split[6];
					String transcriptID = grabMeta(split[8], "transcript_id");
					
					if (transcripts.containsKey(transcriptID)) {
						String line = (String)transcripts.get(transcriptID);
						if (line.equals(str)) {
							out.write(line + "\n");
						}
					}
					
				}
			}
			in.close();
			out.close();
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
			String val = text.split(id)[1].split(";")[0].trim();
			val = val.replaceAll("\"", "");
			val.trim();
			returnval = val;
		}
		return returnval;
	}
}
