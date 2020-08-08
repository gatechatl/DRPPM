package general.sequence.blast.parse.nucleotide2protein;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class Extract100PercentMatch {

	public static void main(String[] args) {
		
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFileUNIPROT = args[0];
			String inputFileUCSC = args[1];
			String outputFile_found = args[2];
			String outputFile_notfound = args[3];
			
			HashMap uniprot = new HashMap();
			String geneName = "";
			
			FileInputStream fstream = new FileInputStream(inputFileUNIPROT);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					geneName = str.replaceAll(">", "");
				} else {
					if (uniprot.containsKey(geneName)) {
						String prev_str = (String)uniprot.get(geneName);
						uniprot.put(geneName, prev_str + str.trim());
					} else {
						uniprot.put(geneName, str.trim());
					}
					
				}
			}
			in.close();
			
			HashMap transcript = new HashMap();
			FileInputStream fstream2 = new FileInputStream(inputFileUCSC);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			while (in2.ready()) {
				String str = in2.readLine();
				if (str.contains(">")) {
					geneName = str.replaceAll(">", "");
					String seq = in2.readLine();
					if (seq.substring(seq.length() - 1, seq.length()).equals("Z")) {
						seq = seq.substring(0, seq.length() - 1);
					}					
					transcript.put(geneName, seq);
				}
			}
			in2.close();
			

			FileWriter fwriter_found = new FileWriter(outputFile_found);
			BufferedWriter out_found = new BufferedWriter(fwriter_found);	
			
			FileWriter fwriter_notfound = new FileWriter(outputFile_notfound);
			BufferedWriter out_notfound = new BufferedWriter(fwriter_notfound);	
			
			System.out.println(uniprot.size());
			int count = 0;
			HashMap found_uniprot = new HashMap();
			Iterator itr = uniprot.keySet().iterator();
			while (itr.hasNext()) {
				String uniprot_name = (String)itr.next();
				String uniprot_seq = (String)uniprot.get(uniprot_name);
				
				Iterator itr2 = transcript.keySet().iterator();
				while (itr2.hasNext()) {
					String transcript_name = (String)itr2.next();
					String transcript_seq = (String)transcript.get(transcript_name);
					if (uniprot_seq.equals(transcript_seq)) {
						found_uniprot.put(uniprot_name, transcript_name);
					}
					
				}
				if (count % 100 == 0) {
					System.out.println(count);
				}
				count++;
			}
			
			
			itr = uniprot.keySet().iterator();
			while (itr.hasNext()) {
				String uniprot_name = (String)itr.next();
				String uniprot_seq = (String)uniprot.get(uniprot_name);
				if (found_uniprot.containsKey(uniprot_name)) {
					out_found.write(">" + uniprot_name + "_" + (String)found_uniprot.get(uniprot_name) + "\n" + uniprot_seq + "\n");
				} else {
					out_notfound.write(">" + uniprot_name + "\n" + uniprot_seq + "\n");
				}
				
			}
			out_found.close();
			out_notfound.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
