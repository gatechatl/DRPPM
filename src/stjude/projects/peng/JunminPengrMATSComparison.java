package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * We wanted to compare the delta psi value to examine how well they correlate between PolyA and Zero-depleted
 * @author tshaw
 *
 */
public class JunminPengrMATSComparison {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\penggrp\\Alzheimer\\common\\CompareTotalvsStranded\\Integrate_rMATS_Result\\Overlap.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("chr\texonStart_0base\texonEnd\tupstreamES\tupstreamEE\tdownstreamES\tdownstreamEE\tmRNAseq\tRibodepleted\n");
			String inputFile1 = "Z:\\ResearchHome\\ProjectSpace\\penggrp\\Alzheimer\\common\\CompareTotalvsStranded\\mRNA_MATS\\6M_N40KvsWT\\MATS_output\\SE.MATS.ReadsOnTargetAndJunctionCounts.txt";		
			FileInputStream fstream = new FileInputStream(inputFile1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[3];
				String loc = split[5] + "\t" + split[6] + "\t" + split[7] + "\t" + split[8] + "\t" + split[9] + "\t" + split[10];
				if (Math.abs(new Double(split[split.length - 1])) >= 0.0) {
					map.put(chr + "\t" + loc, split[split.length - 1]);
				}
			}
			in.close();
			
			HashMap overlap = new HashMap();
			String inputFile2 = "Z:\\ResearchHome\\ProjectSpace\\penggrp\\Alzheimer\\common\\CompareTotalvsStranded\\TotalStranded_MATS\\6WTvs6TG\\MATS_output\\SE.MATS.ReadsOnTargetAndJunctionCounts.txt";		
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[3];
				String loc = split[5] + "\t" + split[6] + "\t" + split[7] + "\t" + split[8] + "\t" + split[9] + "\t" + split[10];
				if (map.containsKey(chr + "\t" + loc)) {
					String val = (String)map.get(chr + "\t" + loc);
					if (Math.abs(new Double(split[split.length - 1])) >= 0.0) {
						out.write(chr + "\t" + loc + "\t" + val + "\t" + new Double(split[split.length - 1]) * -1 + "\n");
					}
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
