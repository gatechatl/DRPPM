package rnaseq.tools.summary;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Calculate the percent of gene that is covered at 1X 2X 5X 10X 20X 30X
 * @author tshaw
 *
 */
public class GenerateRNASEQCoverageStatistics {

	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Calculate the percent of gene that is covered at 1X 2X 5X 10X 20X 30X";
	}
	public static String parameter_info() {
		return "[inputCoverageBed]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			//String referenceBED = args[1];
			String prev_chr = "";
			String prev_start = "";
			String prev_end = "";
			double oneX = 0;
			double twoX = 0;
			double fiveX = 0;
			double tenX = 0;
			double twentyX = 0;
			double thirtyX = 0;
			double fourtyX = 0;
			double fiftyX = 0;
			double total_size = 0;			

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[0];
				String start = split[1];
				String end = split[2];
				//int length = end - start + 1;
				int read_size = new Integer(split[8]);
				if (!(chr.equals(prev_chr) && start.equals(prev_start) && end.equals(prev_end))) {
					total_size += read_size;
				}
				
				prev_chr = chr;
				prev_start = start;
				prev_end = end;
			}
			in.close();
			
			prev_chr = "";
			prev_start = "";
			prev_end = "";
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[0];
				String start = split[1];
				String end = split[2];
				if (!(chr.equals(prev_chr) && start.equals(prev_start) && end.equals(prev_end))) {
					double reads = new Integer(split[6]);
				//double proportion = new Double(split[9]);
				//if (reads > 0) {
					double geneLength = new Integer(split[7]);
					reads = reads * 100 / geneLength;
					//total_size += geneLength;
					
					if (reads >= 1) {
						oneX += geneLength;
					}
					if (reads >= 2) {
						twoX += geneLength;
					}
					if (reads >= 5) {
						fiveX += geneLength;
					}
					if (reads >= 10) {
						tenX += geneLength;
					}
					if (reads >= 20) {
						twentyX += geneLength;
					}
					if (reads >= 30) {
						thirtyX += geneLength;
					}
				}
				//}
				prev_chr = chr;
				prev_start = start;
				prev_end = end;
			}
			in.close();		
			System.out.println(inputFile + "\t" + (oneX / total_size) + "\t" + (twoX / total_size) + "\t" + (fiveX / total_size) + "\t" + (tenX / total_size) + "\t" + (twentyX / total_size) + "\t" + (thirtyX / total_size));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
