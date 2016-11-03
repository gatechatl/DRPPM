package WholeExonTool.SNPPopulationDistribution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;


/**
 * Examine the high.20's rsSNP distribution
 * @author tshaw
 *
 */
public class SNPrsPopulation {

	public static String description() {
		return "Examine the high20 file to count rsSNP";
	}
	public static String type() {
		return "SNP";
	}
	public static String parameter_info() {
		return "[high20File] [rsSNPOutput]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			int count_SNPs = 0;
			int count_rsSNPs = 0;
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				count_SNPs++;
				String[] split = str.split("\t");
				
				if (split[34].contains("rs")) {
					out.write(str + "\n");
					count_rsSNPs++;					
				} 
							
				
			}
			in.close();
			out.close();
			File f = new File(inputFile);
			System.out.println(f.getName() + "\t" + count_rsSNPs + "\t" + count_SNPs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
