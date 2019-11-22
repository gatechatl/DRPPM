package stjude.projects.jinghuizhang.mutations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.inference.TTest;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

import statistics.general.MathTools;

/**
 * Append the mutation information on as meta information.
 * See example at /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/ImmuneSignatureAnalysis 
 * @author tshaw
 *
 */
public class JinghuiZhangAppendMutationInformationToMetaInfo {


	public static String type() {
		return "Immune";
	}
	public static String description() {
		return "Append the mutation information as a meta information table.";
	}
	public static String parameter_info() {
		return "[metaInformationTable] [fusionFile] [snvindelFile] [outputFile]";
	}
	public static void execute(String[] args) {				
		try {
			
			HashMap geneName2sampleName = new HashMap();
			HashMap sampleName2geneName = new HashMap();

			String inputMetaDataFile = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_immune_ssGSEA_transpose.txt";
			String fusionFile = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_SV.txt";
			String snvindelFile = args[2]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_SNVIndel_Simplified.txt";
			String cnvFile = args[3];
			String outputFile = args[4]; // "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\immune_signature_enriched_in_particular_mutations\\PanCancerImmuneSignature_Enrichment.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			FileInputStream fstream = new FileInputStream(fusionFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].split("\\.")[0].split("-")[0];
				if (!split[1].equals("NA")) {
					if (geneName2sampleName.containsKey(split[1])) {
						LinkedList list = (LinkedList)geneName2sampleName.get(split[1]);
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(split[1], list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(split[1], list);
					}
				}
				
				if (!split[2].equals("NA")) {
					if (geneName2sampleName.containsKey(split[2])) {
						LinkedList list = (LinkedList)geneName2sampleName.get(split[2]);
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(split[2], list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(split[2], list);
					}
				}
				
				if (sampleName2geneName.containsKey(split[0])) {					
					LinkedList list = (LinkedList)sampleName2geneName.get(split[0]);
					if (!split[1].equals("NA")) {
						if (!list.contains(split[1])) {
							list.add(split[1]);
						}
					}
					if (!split[2].equals("NA")) {
						if (!list.contains(split[2])) {
							list.add(split[2]);
						}
					}
					sampleName2geneName.put(split[0], list);
				} else {
					LinkedList list = new LinkedList();
					if (!split[1].equals("NA")) {
						if (!list.contains(split[1])) {
							list.add(split[1]);
						}
					}
					if (!split[2].equals("NA")) {
						if (!list.contains(split[2])) {
							list.add(split[2]);
						}
					}
					sampleName2geneName.put(split[0], list);
				}
			}
			in.close();
			
			
			fstream = new FileInputStream(snvindelFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].split("\\.")[0].split("-")[0];
				if (!split[1].equals("NA")) {
					if (geneName2sampleName.containsKey(split[1])) {
						LinkedList list = (LinkedList)geneName2sampleName.get(split[1]);
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(split[1], list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(split[1], list);
					}
				

					if (sampleName2geneName.containsKey(split[0])) {					
						LinkedList list = (LinkedList)sampleName2geneName.get(split[0]);
						if (!list.contains(split[1])) {
							list.add(split[1]);
						}
						sampleName2geneName.put(split[0], list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[1])) {
							list.add(split[1]);
						}
						sampleName2geneName.put(split[0], list);
					}
				}
			}
			in.close();
			
			System.out.println(sampleName2geneName.size());
			
			fstream = new FileInputStream(cnvFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].split("\\.")[0].split("-")[0];
				double length = new Double(split[3]) - new Double(split[2]);
				LinkedList tags = new LinkedList();
				String type = split[6].replaceAll("mattr:", "");
				if (length > 1000000) {
					//tags.add(type + "_CNV_1000000");
					tags.add("CNV_1000000");
				}
				if (length > 5000000) {
					// tags.add(type + "_CNV_5000000");
					tags.add("CNV_5000000");
				}
				if (length > 10000000) {
					// tags.add(type + "_CNV_10000000");
					tags.add("CNV_10000000");
				}
				if (length > 20000000) {
					// tags.add(type + "_CNV_20000000");
					tags.add("CNV_20000000");
				}
				if (length > 30000000) {
					// tags.add(type + "_CNV_30000000");
					tags.add("CNV_30000000");
				}

				Iterator itr_tag = tags.iterator();
				while (itr_tag.hasNext()) {
					String tag = (String)itr_tag.next();
					if (geneName2sampleName.containsKey(tag)) {
						LinkedList list = (LinkedList)geneName2sampleName.get(tag);
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(tag, list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(tag, list);
					}
				
	
					if (sampleName2geneName.containsKey(split[0])) {					
						LinkedList list = (LinkedList)sampleName2geneName.get(split[0]);
						if (!list.contains(tag)) {
							list.add(tag);
						}
						sampleName2geneName.put(split[0], list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(tag)) {
							list.add(tag);
						}
						sampleName2geneName.put(split[0], list);
					}
				}
			}
			in.close();
			HashMap sampleType = new HashMap();
			HashMap type2samples = new HashMap();
			HashMap type2samples_index = new HashMap();
			HashMap samples_with_mutation = new HashMap();
			int count = 0;
			
			fstream = new FileInputStream(inputMetaDataFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header);
			// iterate through all the mutations
			Iterator itr_mutations = geneName2sampleName.keySet().iterator();
			while (itr_mutations.hasNext()) {
				String geneName = (String)itr_mutations.next();
				out.write("\t" + geneName);
			}
			out.write("\n");
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0].split("\\.")[0].split("-")[0];;
				
				out.write(str);
				// iterate through all the mutations
				itr_mutations = geneName2sampleName.keySet().iterator();
				while (itr_mutations.hasNext()) {
					String geneName = (String)itr_mutations.next();
					LinkedList samples_with_mutation_list = (LinkedList)geneName2sampleName.get(geneName);
					
					if (samples_with_mutation_list.contains(sampleName)) {
						out.write("\ttrue");							
					} else {
						out.write("\tfalse");
					}				
				}
				out.write("\n");
			}
			in.close();
			out.close();
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
