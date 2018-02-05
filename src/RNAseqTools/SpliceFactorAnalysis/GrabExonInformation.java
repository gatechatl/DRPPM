package RNAseqTools.SpliceFactorAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import misc.CommandLine;
import NucleicAcidTools.NucleicAcidToolBox;


/**
 * Take a look at the skipped exon sequence
 * @author tshaw
 *
 */
public class GrabExonInformation {

	public static String parameter_info() {
		return "[inputFile] [outputFile] [humanDbType path]";
	}
	
	public static void execute(String[] args) {
		String inputFile = args[0];
		String outputFile = args[1];
		String human_db_type = args[2];		
		grabExon(inputFile, outputFile, human_db_type);
	}
	public static void grabExon(String inputFile, String outputFile, String human_db_type) {
		
		try {
			HashMap reverse = new HashMap();
			HashMap map = new HashMap();

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[0];
				if (!chr.contains("chr")) {
					chr = "chr" + chr;
				}
				String direction = split[1];
				String start = split[2];
				String end = split[3];
				
				String wget = "samtools faidx " + human_db_type + " " + chr + ":" + start + "-" + end + " > temp.txt";
				CommandLine.executeCommand(wget);
							
				String name = "";
				boolean find = false;
				FileInputStream fstream2 = new FileInputStream("temp.txt");
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					if (str2.contains(">")) {
						name = str2;
						if (map.containsKey(name)) {
							find = true;
						}
					} else {
						if (!find) {
							if (direction.equals("-")) {
								reverse.put(name, name);
							}
							//map.put(name, str2.trim());
							if (map.containsKey(name)) {
								String seq = (String)map.get(name);
								map.put(name, str2.trim() + seq);
							} else {
								map.put(name, str2.trim());
							}
						}
					}
				}
				in2.close();
				
			}
			in.close();		
			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				String seq = (String)map.get(name);
				seq = seq.toUpperCase();
				if (reverse.containsKey(name)) {					
					String reverseComplement = NucleicAcidToolBox.getReverseComplement(seq);
					out.write(name + " revcomp\n" + reverseComplement + "\n");
				} else {
					out.write(name + "\n" + seq + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
