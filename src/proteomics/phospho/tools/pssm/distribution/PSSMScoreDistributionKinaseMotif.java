package proteomics.phospho.tools.pssm.distribution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class PSSMScoreDistributionKinaseMotif {
	
	public static String type() {
		return "PSSM";
	}
	public static String description() {
		return "PSSM score distribution";
	}
	public static String parameter_info() {
		return "[motifScoreFile] [inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String motifScoreFile = args[0];
			String raptor_dependent_inputFile = args[1];
			String raptor_dependent_ouputFile = args[2];
			FileInputStream fstream = new FileInputStream(motifScoreFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], new Double(split[1]));
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(raptor_dependent_ouputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(raptor_dependent_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			
			while (in.ready()) {
				String str = in.readLine();
				if (!str.contains(">")) {
					String[] split = str.split("_");
					
					String accession = split[1];
					//String site = split[2];
					String site = split[2].replaceAll("S",  "").replaceAll("T",  "").replaceAll("Y",  "");
					String[] split_sites = site.split(",");
					double max_score = Double.NEGATIVE_INFINITY;
					for (String s: split_sites) {
						String key = accession + "_" + s;
						if (map.containsKey(key)) {
							double score = (Double)map.get(key);
							if (max_score < score) {
								max_score = score;
							}
						}
					}												
					out.write(accession + "_" + site + "\t" + max_score + "\n");	
					/*String key = accession + "_" + site;
					if (map.containsKey(key)) {
						double score = (Double)map.get(key);
						out.write(key + "\t" + score + "\n");
					}*/
				}
			}
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
