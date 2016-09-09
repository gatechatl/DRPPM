package WholeExonTools.AppendMutationTypeInfo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Calculate Transition vs Transversion
 * Append Mutation
 * Assumes last column contains the group name 
 * @author tshaw
 *
 */
public class AppendMutationType {

	public static void main(String[] args) {
		
		try {
			
			String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\EXCAP_SJMMHGG_McKinnon_MutantAlleleDistribution_v01_20151216.txt";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\EXCAP_SJMMHGG_McKinnon_MutantAlleleDistribution_appended_v01_20151216.txt";
			String outputFile2 = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\EXCAP_SJMMHGG_McKinnon_MutantAlleleDistribution_appended_v01_20151216.txt";
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String headers = in.readLine();
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String refallele = split[13];
				String mutallele = split[14];
				if ((refallele.equals("A") && mutallele.equals("G")) || (refallele.equals("G") && mutallele.equals("A"))) {
					
				}
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
