package WholeExonTool.Summarize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ExcapRNAseqMAFColumn {

	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header= in.readLine();
			String[] headers = header.split("\t");
			out.write(header + "\t" + "HaveRNAseq\tDNAMutTumor\tDNATotTumor\tRNAMutTumor\tRNATotTumor\tDNAMAF\tRNAMAF\n");
			while (in.ready()) {
				String str = in.readLine();
				String line = str;
				String[] split = str.split("\t");
				String sampleName = split[2];
				double DNAMutTumor = new Double(split[9]);
				double DNATotTumor = new Double(split[10]);
				 
				double DNAMAF = DNAMutTumor / DNATotTumor;
				int colIndex = findIndex(headers, sampleName);
				// doesn't have RNAseq
				if (colIndex == -1) {
					line += "\tno\t" + DNAMutTumor + "\t" + DNATotTumor + "\tNA\tNA\t" + DNAMAF + "\tNA";
				} else {
					String allele = split[colIndex].replaceAll("=", "");
					double RNAMutTumor = new Double(allele.split("\\/")[0]);
					double RNATotTumor = new Double(allele.split("\\/")[1]);
					//double RNATotTumor = RNAMutTumor + RNANorTumor;
					double RNAMAF = RNAMutTumor / RNATotTumor;
					line += "\tyes\t" + DNAMutTumor + "\t" + DNATotTumor + "\t" + RNAMutTumor + "\t" + RNATotTumor + "\t" + DNAMAF + "\t" + RNAMAF;
				}
				out.write(line + "\n");
			}
			in.close();
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int findIndex(String[] header, String sampleName) {
		for (int i = 0; i < header.length; i++) {
			if (header[i].contains(sampleName)) {
				return i;
			}
		}
		return -1;
	}
}
