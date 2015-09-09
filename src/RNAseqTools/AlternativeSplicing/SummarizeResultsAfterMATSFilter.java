package RNAseqTools.AlternativeSplicing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SummarizeResultsAfterMATSFilter {
	public static void execute(String[] args) {
		
		try {
			
			String inputFilePaths = args[0];
			String[] split_inputFilePaths = inputFilePaths.split(",");
			String sampleName = args[1];
			String[] split_sampleName = sampleName.split(",");
			String[] files = {"A3SS.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "A5SS.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "MXE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "RI.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "SE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt"};
			
			System.out.print("SampleNames");
			for (String file: files) {
				System.out.print("\t" + file);
			}
			System.out.println();
			for (int i = 0; i < split_inputFilePaths.length; i++) {
				System.out.print(split_sampleName[i]);
				for (String file: files) {				
					
					HashMap map = new HashMap();
					int count = 0;
					FileInputStream fstream = new FileInputStream(split_inputFilePaths[i] + "/" + file);
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					in.readLine();
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						count++;
					}
					in.close();
					System.out.print("\t" + count);					
				}
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
