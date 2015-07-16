package PhosphoTools.PeptideCoverage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Examine the coverage of a single gene and compare it across TMT samples
 * @input JUMPq output pep.txt
 * @author tshaw
 *
 */
public class PeptideCoverageSingleGeneComparison {

	public static void execute(String[] args) {
		
		try {
			
			String outputFile = args[1];
			String splitStr = args[3];
			int colNum = new Integer(args[4]);
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String[] terms = args[2].toUpperCase().split(",");
			String inputFile = args[0];
			
			HashMap nonuniq = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(splitStr);
				
				if (split.length >= colNum) {
					//System.out.println(split[colNum - 1]);
					boolean found = false;
					for (String term: terms) {
						if (split[colNum - 1].toUpperCase().contains(term)) {
							//out.write(str + "\n");
							found = true;
						}				
					}
					if (!found) {
						nonuniq.put(split[0], split[0]);
					}
				}
			}			
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(splitStr);
				
				if (split.length >= colNum) {
					//System.out.println(split[colNum - 1]);
					for (String term: terms) {
						if (split[colNum - 1].toUpperCase().contains(term)) {
							if (!nonuniq.containsKey(split[0])) {
								out.write(str + "\n");
							}
						}				
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
