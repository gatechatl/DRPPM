package stjude.projects.jpaultaylor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class ProteinTruncation {

	
	public static void main(String[] args) {
		
		try {
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\tayl1grp\\ALS_Family3\\common\\CMPB\\BioinfoCore\\ProteinTruncation\\LOS_snps_indels_20180211.txt";
			
        	FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            HashMap count = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\tayl1grp\\ALS_Family3\\common\\CMPB\\BioinfoCore\\ProteinTruncation\\all_samples_snps_indels_v2.tsv";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[27].contains("*")) {
					System.out.println(split[27]);
					out.write(str + "\n");
					if (count.containsKey(split[23])) {
						int n = (Integer)count.get(split[23]);
						n = n + 1;
						count.put(split[23], n);
					} else {
						count.put(split[23], 1);
					}
				}
				
			}
			in.close();
			
			out.close();
			
			String outputCountFile = "Z:\\ResearchHome\\ProjectSpace\\tayl1grp\\ALS_Family3\\common\\CMPB\\BioinfoCore\\ProteinTruncation\\Count_LOS_snps_indels_20180211.txt";			
        	fwriter = new FileWriter(outputCountFile);
            out = new BufferedWriter(fwriter);
            out.write("Gene\tCountLOF\n");
            Iterator itr = count.keySet().iterator();
            while (itr.hasNext()) {
            	String gene = (String)itr.next();
            	int n = (Integer)count.get(gene);
            	out.write(gene + "\t" + n + "\n");
            }
            out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
