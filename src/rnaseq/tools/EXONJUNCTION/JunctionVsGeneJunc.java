package rnaseq.tools.EXONJUNCTION;

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
 * Take two list of junction count and calculates a fold change
 * @author tshaw
 *
 */
public class JunctionVsGeneJunc {

	public static String description() {
		return "Overlap GeneJunc and Junction Exon Junction.";
	}
	public static String type() {
		return "EXONJUNCTION";
	}
	public static String parameter_info() {
		return "[exonJunctionFile] [sampleList1] [sampleList2] [outputFile]";
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
			String outputFile = args[3];

			
			HashMap exonJunctionFC = new HashMap(); // contains the FC for each exon junction
			HashMap exonJunctionTumorAllCount = new HashMap(); // contains the FC based on the average junction
			HashMap exonJunctionCntrlAllCount = new HashMap(); // contains the FC based on the average junction
			HashMap totalJunctionCount = new HashMap();
			HashMap tumorCountMap = new HashMap();
			HashMap cntrlCountMap = new HashMap();
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(exonJunctionFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header[] = in.readLine().split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				double totalSampleReads = 0;
				for (int i = 0; i < header.length; i++) {
					String sample = header[i];
					if (tumor.containsKey(sample) || cntrl.containsKey(sample)) {
						
						totalSampleReads += new Double(split[i]);
						if (totalJunctionCount.containsKey(sample)) {
							double total = (Double)totalJunctionCount.get(sample);
							//if (new Double(split[i]) >= 3) {
								total += new Double(split[i]);
								totalJunctionCount.put(sample, total);
							//}
						} else {
							//if (new Double(split[i]) >= 3) {
								totalJunctionCount.put(sample, new Double(split[i]));
							//}
						}
					}
				}
				if (split[2].equals("core")) {
					String geneName = split[1];
					if (map.containsKey(geneName)) {
						HashMap exon = (HashMap)map.get(geneName);
						exon.put(split[0], totalSampleReads);
						map.put(geneName, exon);
					} else {						
						HashMap exon = new HashMap();
						exon.put(split[0], totalSampleReads);
						map.put(geneName, exon);
					}
				}				
			}
			in.close();
			
			HashMap median_map = new HashMap();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				HashMap exon = (HashMap)map.get(geneName);
				double[] num = new double[exon.size()];
				int i = 0;
				Iterator itr2 = exon.keySet().iterator();
				while (itr2.hasNext()) {
					String key = (String)itr2.next();
					double reads = (Double)exon.get(key);
					num[i] = reads;
					i++;
				}
				double median = MathTools.median(num);				
				exon = (HashMap)map.get(geneName);
				itr2 = exon.keySet().iterator();
				while (itr2.hasNext()) {
					String key  = (String)itr2.next();
					double reads = (Double)exon.get(key);
					if (median == reads) {
						median_map.put(geneName, key);
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
				if (split[2].equals("core")) {
					double tumorCount = 0.0;
					boolean checkReadNumFlag = false;
					for (int i = 0; i < header.length; i++) {
						String sample = header[i];
						if (tumor.containsKey(sample) || cntrl.containsKey(sample)) {
							if ((new Double(split[i])) >= 3) {
								checkReadNumFlag = true;
							}
						}
					}
					if (checkReadNumFlag) {
						for (int i = 0; i < header.length; i++) {
							String sample = header[i];
							
							if (tumor.containsKey(sample)) {
								double totalJC = (Double)totalJunctionCount.get(sample);


								tumorCount += (new Double(split[i])) * 1000000 / totalJC / sampleList1.length;
							
								
								// grab total junction count for each gene
								

								if (median_map.containsKey(split[1])) {
									String junctionName = (String)median_map.get(split[1]);
									if (junctionName.equals(split[0])) {									
										if (exonJunctionTumorAllCount.containsKey(split[1])) {
											double totalGeneReads = (Double)exonJunctionTumorAllCount.get(split[1]);
											totalGeneReads += (new Double(split[i])) * 1000000 / totalJC / sampleList1.length;
											exonJunctionTumorAllCount.put(split[1], totalGeneReads);
										} else {
											exonJunctionTumorAllCount.put(split[1], (new Double(split[i])) * 1000000 / totalJC  / sampleList1.length);
										}
									}
								}
							
								
							}
						}				
						//tumorCount = tumorCount / sampleList1.length;
						
						double cntrlCount = 0.0;
						for (int i = 0; i < header.length; i++) {
							String sample = header[i];
							
							if (cntrl.containsKey(sample)) {
								double totalJC = (Double)totalJunctionCount.get(sample);

								cntrlCount += (new Double(split[i])) * 1000000 / totalJC / sampleList2.length;
								// grab total junction count for each gene
								if (median_map.containsKey(split[1])) {
									String junctionName = (String)median_map.get(split[1]);
									if (junctionName.equals(split[0])) {
										if (exonJunctionCntrlAllCount.containsKey(split[1])) {
											double totalGeneReads = (Double)exonJunctionCntrlAllCount.get(split[1]);
											totalGeneReads += (new Double(split[i])) * 1000000 / totalJC / sampleList2.length;
											exonJunctionCntrlAllCount.put(split[1], totalGeneReads);
										} else {
											exonJunctionCntrlAllCount.put(split[1], (new Double(split[i])) * 1000000 / totalJC / sampleList2.length);
										}
									}
								}
									
							}
						}
						//cntrlCount = cntrlCount / sampleList2.length;
						
						double logfoldChange = MathTools.log2(tumorCount + 2) - MathTools.log2(cntrlCount + 2);
						String name = split[1] + "\t" + split[0];
						if (tumorCount > 0 && cntrlCount > 0) {
							exonJunctionFC.put(name, logfoldChange);
							tumorCountMap.put(name, tumorCount);
							cntrlCountMap.put(name, cntrlCount);
						}
					} // checkReadNumFlag
				} // filter core
			}
			in.close();

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("GeneName\tJunction\tGeneLevelJuncFC\tJunctionFC\n");
			itr = exonJunctionFC.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				String geneName = name.split("\t")[0];
				String junction = name.split("\t")[1];
				if (exonJunctionTumorAllCount.containsKey(geneName) && exonJunctionCntrlAllCount.containsKey(geneName)) {
					double junctionFoldChange = (Double)exonJunctionFC.get(name);				
					double tumorCount = (Double)tumorCountMap.get(name);
					double cntrlCount = (Double)cntrlCountMap.get(name);
					//if (limma.containsKey(geneName)) {
					//	double expr = (Double)limma_avgExpr.get(geneName);
					//	if (expr > 1) {
					double geneLevelJCfoldChange = MathTools.log2((Double)exonJunctionTumorAllCount.get(geneName) + 2) - MathTools.log2((Double)exonJunctionCntrlAllCount.get(geneName) + 2);
	
					out.write(geneName + "\t" + junction + "\t" + geneLevelJCfoldChange + "\t" + junctionFoldChange + "\t" + tumorCount + "\t" + cntrlCount + "\n");
					//	}
					//}
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
