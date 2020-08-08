package functional.pathway.enrichment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * After FDR filtering perform filter based on user defined criteria.
 * @author tshaw
 *
 */
public class FilterORAResultsFlex {
	public static String dependencies() {
		return "Filter the pathway over representation analysis result.";
	}
	public static String type() {
		return "ENRICHMENT";
	}
	public static String description() {
		return "Filter the result based on pval and fdr. All pathways with enrichment of less than 1 are also filtered out.";
	}
	public static String parameter_info() {
		return "[inputFDRFile] [pval_cutoff] [fdr_cutoff] [outputFile] [UpKeyWord] [DnKeyWord] [UpRegOutputFile] [DnRegOutputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFDRFile = args[0];
			double pval_cutoff = new Double(args[1]);
			double fdr_cutoff = new Double(args[2]);
			String all_outputFile = args[3];
			String upreg_keyword = args[4];
			String dnreg_keyword = args[5];
			String upreg_outputFile = args[6];
			String dnreg_outputFile = args[7];
			FileWriter fwriter = new FileWriter(all_outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter up_fwriter = new FileWriter(upreg_outputFile);
			BufferedWriter up_out = new BufferedWriter(up_fwriter);
			
			FileWriter dn_fwriter = new FileWriter(dnreg_outputFile);
			BufferedWriter dn_out = new BufferedWriter(dn_fwriter);
			
			LinkedList up_list = new LinkedList();
			LinkedList dn_list = new LinkedList();
			
			FileInputStream fstream = new FileInputStream(inputFDRFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			out.write(header + "\n");
			up_out.write(header + "\n");
			dn_out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double pval = new Double(split[2]);
				double enrichment = new Double(split[3]);
				double fdr = new Double(split[9]);
				if (enrichment > 1 && pval < pval_cutoff && fdr < fdr_cutoff) {
					out.write(str + "\n");
					if (split[0].equals(dnreg_keyword)) {
						//dn_out.write(str + "\n");
						dn_list.add(str);
					}
					if (split[0].equals(upreg_keyword)) {
						//up_out.write(str + "\n");
						up_list.add(str);
					}
				}
			}
			in.close();
			out.close();
			
			// iterate through the list and rank the list based on enrichment score.
			while(up_list.size() > 0) {
				double max = 0;
				String highest = "";
				Iterator itr = up_list.iterator();
				while (itr.hasNext()) {
					String line = (String)itr.next();
					String[] split = line.split("\t");
					double enrichment = new Double(split[3]);
					if (max < enrichment) {
						max = enrichment;
						highest = line;
					}
				}
				itr = up_list.iterator();
				while (itr.hasNext()) {
					String line = (String)itr.next();
					if (line.equals(highest)) {
						up_out.write(line + "\n");
						up_list.remove(line);
						break;
					}
				}
			}
			
			while(dn_list.size() > 0) {
				double max = 0;
				String highest = "";
				Iterator itr = dn_list.iterator();
				while (itr.hasNext()) {
					String line = (String)itr.next();
					String[] split = line.split("\t");
					double enrichment = new Double(split[3]);
					if (max < enrichment) {
						max = enrichment;
						highest = line;
					}
				}
				itr = dn_list.iterator();
				while (itr.hasNext()) {
					String line = (String)itr.next();
					if (line.equals(highest)) {
						dn_out.write(line + "\n");
						dn_list.remove(line);
						break;
					}
				}
			}
			
			dn_out.close();
			up_out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
