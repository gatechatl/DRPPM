package PhosphoTools.PSSM.ScoreDistribution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * A misc program to create a supplementary table for the manuscript.
 * @author tshaw
 *
 */
public class PSSMCreateSupplementaryTable {

	public static String type() {
		return "PSSM";
	}
	public static String description() {
		return "PSSM generate supplementary file";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [hr2File] [hr16File] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String hr2File = args[1];		
			String hr16File = args[2];
			String outputFile = args[3];
			HashMap map = new HashMap();
			HashMap map2h = new HashMap();
			HashMap map16h = new HashMap();
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(hr2File);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[split.length - 1].equals("NA")) {
					double score = new Double(split[split.length - 1]);
					if (score >= 18) {
						map2h.put(split[0], score);
					}
				}
			}
			in.close();
			
			fstream = new FileInputStream(hr16File);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[split.length - 1].equals("NA")) {
					double score = new Double(split[split.length - 1]);
					
					if (score >= 18) {
						map16h.put(split[0], score);
					}
				}
			}
			in.close();
			
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));	
			out.write(in.readLine() + "\t2hr\t16hr\tScore\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String flag2h = "no";
				String flag16h = "no";
				String score = "NA";
				if (map2h.containsKey(split[0])) {
					flag2h = "yes";
					score = map2h.get(split[0]) + "";
				}
				if (map16h.containsKey(split[0])) {
					flag16h = "yes";
					score = map16h.get(split[0]) + "";
				}
				out.write(str + "\t" + flag2h + "\t" + flag16h + "\t" + score + "\n");
			}
			in.close();
			out.close();
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
