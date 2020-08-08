package stjude.projects.jpaultaylor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Conversion of fasta sequence's Refseq ID to UCSC ID to Ensembl ID 
 * @author tshaw
 *
 */
public class FastaRefSeq2EnsemblNew {

	public static String description() {
		return "Grab seq and append ucsc ID and ensembl transcript ID";
	}
	public static String type() {
		return "IDCONVERSION";
	}
	public static String parameter_info() {
		return "[inputFastaFile] [kgXref] [known2Ensembl] [outputFile]";
	}
	public static void execute(String[] args) {
		
		
		try {
			String inputFastaFile = args[0];					
			String kgXref = args[1]; // table that can convert RefSeq to UCSC ID
			String known2Ensembl = args[2]; // table that contain UCSC ID that can convert to Ensembl Transcripts			
			String outputFile = args[3]; // output table
			
			HashMap refseq2ucsc = new HashMap();
			HashMap ucsc2ensembl = new HashMap();
			HashMap refseq2ensembl = new HashMap();
			
            
            FileInputStream fstream = new FileInputStream(known2Ensembl);
            DataInputStream din = new DataInputStream(fstream);
            BufferedReader in = new BufferedReader(new InputStreamReader(din));
            while (in.ready()) {
            	String str = in.readLine();
            	String[] split = str.split("\t");
            	ucsc2ensembl.put(split[0],  split[1]);            	            	
            }
            in.close();
            
           
            fstream = new FileInputStream(kgXref);
            din = new DataInputStream(fstream);
            in = new BufferedReader(new InputStreamReader(din));
            while (in.ready()) {
            	String str = in.readLine();
            	String[] split = str.split("\t");
            	refseq2ucsc.put(split[1],  split[0]);
            	if (ucsc2ensembl.containsKey(split[0])) {
            		String ensembl = (String)ucsc2ensembl.get(split[0]);
            		if (refseq2ensembl.containsKey(split[1])) {
            			LinkedList old_ensembl = (LinkedList)refseq2ensembl.get(split[1]);
            			if (!old_ensembl.contains(ensembl)) {
            				old_ensembl.add(ensembl);
            				//System.out.println("OLD: " + old_ensembl + "\t" + "NEW: " + ensembl);
            			}
            			refseq2ensembl.put(split[1], old_ensembl);
            		} else {
            			LinkedList list = new LinkedList();
            			list.add(ensembl);
            			refseq2ensembl.put(split[1], list);
            		}
            		
            	}
            }
            in.close();
            
        	FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            out.write("RefSeq\tUCSC_ID\tEnsemblTranscript_ID\tSeq\n");
            String refseq = "";
            String seq = "";
            int count = 0;
            fstream = new FileInputStream(inputFastaFile);
            din = new DataInputStream(fstream);
            in = new BufferedReader(new InputStreamReader(din));
            while (in.ready()) {
            	String str = in.readLine();
            	if (str.equals("")) {
            		//String ensembl = "NA";
            		String ucsc_id = "NA";
            		String combined_ensembl = "NA";
            		if (refseq2ucsc.containsKey(refseq)) {
            			ucsc_id = (String)refseq2ucsc.get(refseq);
            		}
            		if (refseq2ensembl.containsKey(refseq)) {
            			LinkedList ensembl_list = (LinkedList)refseq2ensembl.get(refseq);
            			Iterator itr = ensembl_list.iterator();
            			while (itr.hasNext()) {
            				String ensembl = (String)itr.next();
            				if (combined_ensembl.equals("NA")) {
            					combined_ensembl = ensembl;
            				} else {
            					combined_ensembl += ";" + ensembl;
            				}
            			}
            		} 
            		if (!ucsc_id.equals("NA")) {
            			out.write(refseq + "\t" + ucsc_id + "\t" + combined_ensembl + "\t" + seq + "\n");
            		}
            		seq = "";
            		refseq = "";
            		
            	} else {
	            	if (str.contains(">")) {
	            		refseq = str.replaceAll(">", "");
	            		String[] refseq_split = refseq.split(" ");
	            		refseq = refseq_split[0];
	            		
	            	} else {
	            		seq += str.toUpperCase().trim();
	            	}
	            	
            	}
            	if (count % 1000 == 0) {
            		System.out.println(count);
            	}
            	count++;
            }
            in.close();
            out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
