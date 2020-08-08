package rnaseq.quantification.kallisto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Iterate through each Kallisto tsv file and generate the count file. 
 * Also generate a meta data file including sample name and path
 * @author tshaw
 *
 */
public class KallistoGenerateCountFile {
	

	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Generate count file and meta data sheet";
	}
	public static String parameter_info() {
		return "[fastq_lst: 3 column] [KallistoInputRootFolder] [outputMetaFileSheet]";
	}
	public static void execute(String[] args) {				
		try {
			
			String fastq_lst = args[0];
			String current_path = args[1];
			String outputMatrix = args[2];
			
			FileWriter fwriter = new FileWriter(outputMatrix);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("sample\tpath\n");
			FileInputStream fstream = new FileInputStream(fastq_lst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String abundance_path = current_path + "/" + split[2] + "_kallisto/abundance.tsv";
				
				String count_path = current_path + "/" + split[2] + "_kallisto/count.txt";
				
				FileWriter fwriter2 = new FileWriter(count_path);
				BufferedWriter out2 = new BufferedWriter(fwriter2);
				boolean check_valid = true;
				boolean all_zero = true;
				FileInputStream fstream2 = new FileInputStream(abundance_path);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				in2.readLine(); // skip the header
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					if (new Double(split2[3]).isNaN() || new Double(split2[3]) == (Double.POSITIVE_INFINITY) || new Double(split2[3]) == (Double.NEGATIVE_INFINITY)) {						
						check_valid = false;
					}
					if (new Double(split2[3]) > 0) {
						all_zero = false;
					}
					out2.write(split2[0] + "\t" + new Double(split2[3]).intValue() + "\n");
				}
				in2.close();
				if (check_valid && !all_zero) {
					out.write(split[2] + "\t" + count_path + "\n");
				}
				out2.close();
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
