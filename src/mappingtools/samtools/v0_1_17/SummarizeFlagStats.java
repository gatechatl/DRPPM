package mappingtools.samtools.v0_1_17;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class SummarizeFlagStats {

	public static String description() {
		return "Summarize the flag stats from samtools v0.1.17";
	}
	public static String type() {
		return "SAMTOOLS";
	}
	public static String parameter_info() {
		return "[inputFlagStatLst]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			System.out.println("FilePath\tTotal\tDuplicate\tMapped\tPaired\tRead1\tRead2\tProperlyPaired\tWithItselfMateMapped\tSingleton\tMateMappedDiffChr\tMateMappedDiffChrHiQual");
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String filePath = in.readLine();
				File f = new File(filePath);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(filePath);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));			
					String totalread_str = in2.readLine().split(" ")[0];
					String duplicate_str = in2.readLine().split(" ")[0];
					String mapped_str = in2.readLine().split(" ")[0];
					String paired_str = in2.readLine().split(" ")[0];
					String read1_str = in2.readLine().split(" ")[0];
					String read2_str = in2.readLine().split(" ")[0];
					String paired_mapped_str = in2.readLine().split(" ")[0];
					String mate_mapped_str = in2.readLine().split(" ")[0];
					String singleton_str = in2.readLine().split(" ")[0];
					String matemapped_to_different_chr = in2.readLine().split(" ")[0];
					String matemapped_to_different_chr_hiqual = in2.readLine().split(" ")[0];
					in2.close();
					System.out.println(filePath + "\t" + totalread_str + "\t" + duplicate_str + "\t" + mapped_str + "\t" + paired_str
							+ "\t" + read1_str + "\t" + read2_str + "\t" + paired_mapped_str + "\t" + mate_mapped_str + "\t" + singleton_str
							+ "\t" + matemapped_to_different_chr + "\t" + matemapped_to_different_chr_hiqual);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
