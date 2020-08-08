package rnaseq.tools.singlecell.tenxgenomics.cellranger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Grab the header and cell type for 10X
 * @author tshaw
 *
 */
public class SamHeader2CellType {

	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "Grab the header and cell type for 10X";
	}
	public static String parameter_info() {
		return "[samFile] [fastqFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap sam_header = new HashMap();
			HashMap sam_duplicate = new HashMap();
			HashMap sam2seq = new HashMap();
			String samFile = args[0];
			String fastqFile = args[1];
			String outputFile = args[2];
			FileInputStream fstream = new FileInputStream(samFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[5].contains("96M")) {
					if (!sam_header.containsKey(split[0])) {
						sam_header.put(split[0], split[2]);
						sam2seq.put(split[0], split[9]);
					} else {
						String header = (String)sam_header.get(split[0]);
						if (!header.equals(split[2])) {
							sam_duplicate.put(split[0], split[0]);
						}
					}
				}
			}
			in.close();
			
			
			HashMap sam2fastq = new HashMap();
			fstream = new FileInputStream(fastqFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String header = in.readLine();
				String[] split_header = header.split(" ");
				String seq = in.readLine();
				String skip = in.readLine();
				String qual = in.readLine();
				String tag = seq.substring(0, 16);
				if (!sam_duplicate.containsKey(split_header[0].replaceAll("@", "")) && sam_header.containsKey(split_header[0].replaceAll("@", ""))) {
					sam2fastq.put(split_header[0].replaceAll("@", ""), tag);
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			Iterator itr = sam2fastq.keySet().iterator();
			while (itr.hasNext()) {
				String header = (String)itr.next();
				String tag = (String)sam2fastq.get(header);
				String type = (String)sam_header.get(header);
				out.write(tag + "\t" + type + "\t" + header + "\t" + sam2seq.get(header) + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
