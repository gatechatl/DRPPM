package GenerateNUC2PROT;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;


public class ExtractSequenceFromAlignment {

	public static void execute(String[] args) {		
		try {
			String inputFile = args[0]; 
			String organism_arg = args[1];
			String outputFile = args[2];
			boolean flag_refGene = args[3].equals("yes");
			
			HashMap transcript = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.replaceAll(">", "").split("_");
					String geneName = "";
					String organism = "";
					if (flag_refGene) {
						geneName = split[0] + "_" + split[1];
						organism = split[2];
					} else {
						geneName = split[0];
						organism = split[1];						
					}
					if (organism.equals(organism_arg)) {
						String seq = in.readLine().trim().replaceAll("-",  "");
						if (transcript.containsKey(geneName)) {
							String new_seq = (String)transcript.get(geneName);
							transcript.put(geneName, new_seq + seq);
						} else {
							transcript.put(geneName, seq);
						}
					}
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			Iterator itr = transcript.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				String seq = (String)transcript.get(geneName);
				out.write(">" + geneName + "\n" + seq + "\n");
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
