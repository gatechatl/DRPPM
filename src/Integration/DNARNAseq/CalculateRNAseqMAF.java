package Integration.DNARNAseq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CalculateRNAseqMAF {

	public static String parameter_info() {
		return "[inputFile] [outputFile] [outputCorrelationFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String outputFile = args[1];
			String outputSimple = args[2];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter2 = new FileWriter(outputSimple);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header_str = in.readLine();
			out2.write(header_str + "\tRNA_MUT\tRNA_TOTAL\tDNAMAF\tRNAMAF\n");
			
			out.write(header_str + "\tDNA_MUT\tDNA_TOTAL\tRNA_MUT\tRNA_TOTAL\tDNA_MAF\tRNA_MAF\n");
			String[] header = header_str.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sample = split[2];
				int DNA_Mut = new Integer(split[9]);
				int DNA_Total = new Integer(split[10]);
				double DNA_MAF = new Double(DNA_Mut) / DNA_Total;
				int RNA_Mut = 0;
				int RNA_Total = -1;
				double RNA_MAF = 0.0;
				for (int i = 0; i < header.length; i++) {
					if (header[i].contains(sample)) {
						String val = split[i];
						val = val.replaceAll("=", "");
						RNA_Mut = new Integer(val.split("\\/")[0]);
						RNA_Total = new Integer(val.split("\\/")[1]);
						RNA_MAF = new Double(RNA_Mut) / RNA_Total;
					}
				}
				if (RNA_Total > 7) {
					out2.write(str + "\t" + RNA_Mut + "\t" + RNA_Total + "\t" + DNA_MAF + "\t" + RNA_MAF + "\n");
				}
				out.write(str + "\t" + DNA_Mut + "\t" + DNA_Total + "\t" + RNA_Mut + "\t" + RNA_Total + "\t" + DNA_MAF + "\t" + RNA_MAF + "\n");
			}
			in.close();
			
			out2.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
