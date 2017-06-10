package MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Reading a fasta sequence, extract 1000 sequences and generate a database from it
 * @author tshaw
 *
 */
public class ExtractRandomFastaSequence {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Read in a fasta file and output randomly sampled sequences";
	}
	public static String parameter_info() {
		return "[inputFastaFile] [numSeq][outputFastaFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap fasta_map = new HashMap();
			String name = "";
			String inputFile = args[0];
			int numSeq = new Integer(args[1]);
			String outputFile = args[2];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {					
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (str.contains(">")) {
					name = str;
				} else {
					if (!name.contains("Decoy")) {
						if (fasta_map.containsKey(name)) {
							String seq = (String)fasta_map.get(name);
							seq += str;
							fasta_map.put(name, seq);
						} else {
							fasta_map.put(name, str);
						}
					}
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			// find numSeq sequences from the fasta map
			for (int i = 0; i < numSeq; i++) {
				Random rand = new Random();
				int index = rand.nextInt(fasta_map.size());
				int count = 0;
				Iterator itr = fasta_map.keySet().iterator();
				while (itr.hasNext()) {
					name = (String)itr.next();
					String seq = (String)fasta_map.get(name); 
					if (index == count) {
						if (!map.containsKey(name)) {
							out.write(name + "\n" + seq + "\n");
							map.put(name, name);
						}
					}
					count++;
				}
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
