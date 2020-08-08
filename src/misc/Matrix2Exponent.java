package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Apply exponent to expression
 * @author tshaw
 *
 */
public class Matrix2Exponent {


	public static String description() {
		return "Undo a Log that was applied on";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [baseNum exp: 2] [outputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String fileName = args[0]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_07282014\\SJMMHGG\\SJMMHGG_RNAseq_Exon_Read_Count_gene_fpkm_uniq.txt";
			double baseNum = new Double(args[1]);
			String outputFile = args[2]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_07282014\\SJMMHGG\\SJMMHGG_RNAseq_Exon_Read_Count_gene_fpkm_uniq_NRremove.txt";;
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String SJMMHGG_Tag = in.readLine();
			out.write(SJMMHGG_Tag + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				for (int i = 1; i < split.length; i++) {
					double newVal = Math.pow(new Double(baseNum), new Double(split[i]));
					out.write("\t" + newVal);
				}
				out.write("\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
