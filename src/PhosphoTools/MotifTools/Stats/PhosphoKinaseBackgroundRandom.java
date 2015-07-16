package PhosphoTools.MotifTools.Stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import PhosphoTools.MotifTools.AddRelativeQuantification;
import Statistics.General.MathTools;

public class PhosphoKinaseBackgroundRandom {

	public static void execute(String[] args) {
		try {
			
			String ascoreFile = args[0];
			String totalProteomeFile = args[1];
			String geneName = args[2].toUpperCase();
			String groupInfo = args[3];
			String outputFile = args[4];
			String outputFileEverything = args[5];
			
			FileWriter fwriter_geneName = new FileWriter(outputFileEverything);
			BufferedWriter out_geneName = new BufferedWriter(fwriter_geneName);
			out_geneName.write("Name\tPearsonCor\tSpearmanCor\tKinaseName\tPDGFRA\tNTRK1_1\tNTRK1_2\tCNTRL\tSubstrateName\tPDGFRA\tNTRK1_1\tNTRK1_2\tCNTRL\n");
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Name\tPearsonCor\tSpearmanCor\n");
			
			System.out.println("Running Grab Data From Ascore");
			HashMap ascore = AddRelativeQuantification.grabDataFromAscore(ascoreFile, groupInfo);
			HashMap peptide2uniprot = AddRelativeQuantification.grabUniprotNameFromPeptide(ascoreFile);
			HashMap peptide2ascore = AddRelativeQuantification.grabAScoreFromPeptide(ascoreFile);
			System.out.println("Load Ascore File");
			HashMap total = AddRelativeQuantification.grabDataFromTotal(totalProteomeFile, groupInfo);
			System.out.println("Load Total Proteome File");
			
			
			//String kinase_peptide = split[index2];
			//String kinase_name = kinase_peptide.split("_")[0];
			
			HashMap ensure_uniq = new HashMap();
			Random rand = new Random();
			
			//String totalData = (String)total.get(geneName);
			System.out.println("Searching for: " + geneName);
			HashMap peptideHash = (HashMap)total.get(geneName);
			Iterator itr2 = peptideHash.keySet().iterator();
			while (itr2.hasNext()) {
				String peptideStr = (String)itr2.next();
				String totalData = (String)peptideHash.get(peptideStr);
				System.out.println(peptideStr);
				String[] totalDataSplit = totalData.split("\t");
				double[] totalDataNum = new double[totalDataSplit.length];
				for (int i = 0; i < totalDataSplit.length; i++) {
					totalDataNum[i] = new Double(totalDataSplit[i]);
				}
				
				
				
				//int num = rand.nextInt(ascore.size());
				int count = 0;
				String ascoreData = "";
				Iterator itr = ascore.keySet().iterator();
				String peptide = "";
				while (itr.hasNext()) {
					String key = (String)itr.next();
					//if (count == num) {
					peptide = key;
					ascoreData = (String)ascore.get(key);
					//}
					count++;
				
					String[] ascoreDataSplit = ascoreData.split("\t");
					double[] ascoreDataNum = new double[ascoreDataSplit.length];
					for (int i = 0; i < ascoreDataSplit.length; i++) {
						ascoreDataNum[i] = new Double(ascoreDataSplit[i]);
					}
					double pearson = MathTools.PearsonCorrel(ascoreDataNum,  totalDataNum);
					double spearman = MathTools.SpearmanRank(ascoreDataNum,  totalDataNum);
					
					if (!ensure_uniq.containsKey(peptideStr + "_" + peptide)) {
						out.write(peptideStr + "\t" + pearson + "\t" + spearman + "\n");
						out_geneName.write(peptideStr + "_" + peptide + "\t" + pearson + "\t" + spearman + "\t" + geneName);
						for (int i = 0; i < totalDataSplit.length; i++) {
							out_geneName.write("\t" + totalDataSplit[i]);
						}
						out_geneName.write("\t" + (String)peptide2uniprot.get(key) + "_" + key);
						for (int i = 0; i < ascoreDataSplit.length; i++) {
							out_geneName.write("\t" + ascoreDataSplit[i]);
						}
						out_geneName.write("\t" + (String)peptide2ascore.get(key) + "\n");
					}
					ensure_uniq.put(peptideStr + "_" + peptide, peptideStr + "_" + peptide);
				}
			}
			out.close();
			out_geneName.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
