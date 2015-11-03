package RNAseqTools.Cufflinks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Compare the differentially expressed genes
 * @author tshaw
 *
 */
public class ExtractDifferentiatedTranscriptOnly {

	
	public static void execute(String[] args) {
		
		try {
			String geneInputFile = args[0];
			String transcriptInputFile = args[1];
			String outputFile = args[2];
			HashMap map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(geneInputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[2];
				String significant = split[split.length - 1];
				if (significant.equals("yes")) {
					map.put(gene, gene);
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(transcriptInputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[2];
				String significant = split[split.length - 1];
				if (significant.equals("yes") && !map.containsKey(gene)) {
					//map.put(gene, gene);
					out.write(str + "\n");
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
