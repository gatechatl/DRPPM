package RNAseqTools.EXONJUNCTION;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

/**
 * Intake two list of samples, calculates a fold change
 * @author tshaw
 *
 */
public class OverlapLIMMAAndExonJunctionCount {

	public static String description() {
		return "Overlap LIMMA and Junction Exon Junction.";
	}
	public static String type() {
		return "EXONJUNCTION";
	}
	public static String parameter_info() {
		return "[exonJunctionFile] [sampleList1] [sampleList2] [limmaFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String exonJunctionFile = args[0];
			String[] sampleList1 = args[1].split(",");
			HashMap tumor = new HashMap();
			for (String sample: sampleList1) {
				tumor.put(sample, sample);
			}
			String[] sampleList2 = args[2].split(",");
			HashMap cntrl = new HashMap();
			for (String sample: sampleList2) {
				cntrl.put(sample, sample);
			}
			String limmaFile = args[3];
			String outputFile = args[4];

			HashMap limma = new HashMap();
			HashMap limma_pval = new HashMap();
			HashMap limma_fdr = new HashMap();
			HashMap limma_avgExpr = new HashMap();
			FileInputStream fstream = new FileInputStream(limmaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String limma_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].replaceAll("\"", "");
				limma.put(split[0], new Double(split[1]));		
				limma_avgExpr.put(split[0], new Double(split[2]));
				limma_pval.put(split[0], new Double(split[4]));
				limma_fdr.put(split[0], new Double(split[5]));
			}
			in.close();
			
			HashMap exonJunctionFC = new HashMap(); // contains the FC for each exon junction
			HashMap exonJunctionTumorAllCount = new HashMap(); // contains the FC based on the average junction
			HashMap exonJunctionCntrlAllCount = new HashMap(); // contains the FC based on the average junction
			HashMap totalJunctionCount = new HashMap();
			
			fstream = new FileInputStream(exonJunctionFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header[] = in.readLine().split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				for (int i = 0; i < header.length; i++) {
					String sample = header[i];
					if (tumor.containsKey(sample) || cntrl.containsKey(sample)) {
						if (totalJunctionCount.containsKey(sample)) {
							double total = (Double)totalJunctionCount.get(sample);
							total += new Double(split[i]);
							totalJunctionCount.put(sample, total);
						} else {
							totalJunctionCount.put(sample, new Double(split[i]));
						}
					}
				}
			}
			fstream = new FileInputStream(exonJunctionFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine().split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				double tumorCount = 0.0;
				
				for (int i = 0; i < header.length; i++) {
					String sample = header[i];
					
					if (tumor.containsKey(sample)) {
						double totalJC = (Double)totalJunctionCount.get(sample);
						tumorCount += (new Double(split[i])) / totalJC * 1000 / sampleList1.length;
						
						// grab total junction count for each gene
						if (exonJunctionTumorAllCount.containsKey(split[1])) {
							double totalGeneReads = (Double)exonJunctionTumorAllCount.get(split[1]);
							totalGeneReads += (new Double(split[i])) / totalJC * 1000 / sampleList1.length;
							exonJunctionTumorAllCount.put(split[1], totalGeneReads);
						} else {
							exonJunctionTumorAllCount.put(split[1], (new Double(split[i])) / totalJC * 1000 / sampleList1.length);
						}
					}
				}				
				//tumorCount = tumorCount / sampleList1.length;
				
				double cntrlCount = 0.0;
				for (int i = 0; i < header.length; i++) {
					String sample = header[i];
					
					if (cntrl.containsKey(sample)) {
						double totalJC = (Double)totalJunctionCount.get(sample);
						cntrlCount += (new Double(split[i])) / totalJC * 1000 / sampleList2.length;
						
						// grab total junction count for each gene
						if (exonJunctionCntrlAllCount.containsKey(split[1])) {
							double totalGeneReads = (Double)exonJunctionCntrlAllCount.get(split[1]);
							totalGeneReads += (new Double(split[i])) / totalJC * 1000 / sampleList2.length;
							exonJunctionCntrlAllCount.put(split[1], totalGeneReads);
						} else {
							exonJunctionCntrlAllCount.put(split[1], (new Double(split[i])) / totalJC * 1000 / sampleList2.length);
						}
							
					}
				}
				//cntrlCount = cntrlCount / sampleList2.length;
				
				double logfoldChange = MathTools.log2(tumorCount / cntrlCount);
				String name = split[1] + "\t" + split[0];
				if (tumorCount > 0 && cntrlCount > 0) {
					exonJunctionFC.put(name, logfoldChange);
				}				
			}
			in.close();

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("GeneName\tJunction\tGeneLevelJuncFC\tJunctionFC\tLimmaPval\tLimmaFDR\tLimmaFC\n");
			Iterator itr = exonJunctionFC.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				String geneName = name.split("\t")[0];
				String junction = name.split("\t")[1];
				double junctionFoldChange = (Double)exonJunctionFC.get(name);
				
				if (limma.containsKey(geneName)) {
					double expr = (Double)limma_avgExpr.get(geneName);
					if (expr > 1) {
						double geneLevelJCfoldChange = MathTools.log2((Double)exonJunctionTumorAllCount.get(geneName) / (Double)exonJunctionCntrlAllCount.get(geneName));
						double limmaFoldChange = (Double)limma.get(geneName);
						double limmaPval = (Double)limma_pval.get(geneName);
						double limmaFDR = (Double)limma_fdr.get(geneName);
						out.write(geneName + "\t" + junction + "\t" + geneLevelJCfoldChange + "\t" + junctionFoldChange + "\t" + limmaPval + "\t" + limmaFDR + "\t" + limmaFoldChange + "\n");
					}
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
