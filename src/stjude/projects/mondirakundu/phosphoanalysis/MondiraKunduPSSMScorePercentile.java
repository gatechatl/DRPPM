package stjude.projects.mondirakundu.phosphoanalysis;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import statistics.general.MathTools;

public class MondiraKunduPSSMScorePercentile {

	
	public static void main(String[] args) {
		
		try {
			
			String path = "Z:\\ResearchHome\\ClusterHome\\tshaw\\PROTEOMICS\\REFERENCE\\PSSM\\HUMAN_UNIPROT_PSSM\\";
			
			LinkedList[] kinase_score = new LinkedList[300];
			for (int i = 0; i < 300; i++) {
				kinase_score[i] = new LinkedList();
			}
			Double[] Q16821_38_scores = new Double[300];
			Double[] Q16821_42_scores = new Double[300];
			FileInputStream fstream = new FileInputStream(path + "Q16821.txt");
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals("Q16821_38")) {
					for (int i = 2; i < split.length; i++) {
						Q16821_38_scores[i - 2] = new Double(split[i]);
						
					}
				}
				if (split[0].equals("Q16821_42")) {
					for (int i = 2; i < split.length; i++) {
						
						Q16821_42_scores[i - 2] = new Double(split[i]);
					}
				}
			}
			in.close();
			for (int i = 2; i < 302; i++) {
				//System.out.println(Q16821_scores[i - 2]);
			}

			int count = 0;
			File file = new File(path);
			for (File f: file.listFiles()) {
				fstream = new FileInputStream(f.getPath());
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					for (int i = 2; i < split.length; i++) {
						kinase_score[i - 2].add(split[i]);
					}
				}
				in.close();
				count++;
				//System.out.println(count);
				if (count > 2000) {
					break;
				}
			}
			for (int i = 2; i < 302; i++) {
				double[] scores = MathTools.convertListStr2Double(kinase_score[i - 2]);
				double sd = MathTools.standardDeviation(scores);
				double avg = MathTools.mean(scores);
				System.out.println(split_header[i] + "\t" + Q16821_38_scores[i - 2] + "\t" + ((Q16821_38_scores[i - 2] - sd) / avg) + "\t" + Q16821_42_scores[i - 2] + "\t" + ((Q16821_42_scores[i - 2] - sd) / avg));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
