package stjude.projects.jinghuizhang.immunesignature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class JinghuiZhangAppendImmuneSignaturePatientSurvivalOSAgain {

	
	public static void main(String[] args) {
		
		try {
			

			HashMap patient2sjid = new HashMap();
			String patient_id_conversion = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PAN_PCGP_TARGET_Genomic_Lesion_DB\\PanTARGET\\temp_patient_id_conversion.txt"; 		
			FileInputStream fstream = new FileInputStream(patient_id_conversion);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[2].equals("nosjid")) {
					patient2sjid.put(split[0], split[2]);
				} else if (!split[3].equals("nosjid")) {
					patient2sjid.put(split[0], split[3]);
				} else if (!split[4].equals("nosjid")) {
					patient2sjid.put(split[0], split[4]);
				}
				
			}
			in.close();
						
			HashMap immune_subtype_map = new HashMap();
			//String immune_subtype_file = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_target_annotation_immunesubtype.txt";
			String immune_subtype_file = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\Solid_Brain_pcgp_target_annotation_immunesubtype.txt";
			fstream = new FileInputStream(immune_subtype_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sjid = split[0].split("\\.")[0];
				immune_subtype_map.put(sjid, split[1]);
			}
			in.close();
			
			HashMap cnv_1000000_count = new HashMap();
			String inputCNV1000000File = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PCGP_TARGET_SCNA_1000000.txt";			
			fstream = new FileInputStream(inputCNV1000000File);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].split("-")[0].split("\\.")[0];
				if (cnv_1000000_count.containsKey(split[0])) {
					int count = (Integer)cnv_1000000_count.get(split[0]);
					count = count + 1;
					cnv_1000000_count.put(split[0], count);
				} else {
					cnv_1000000_count.put(split[0], 1);
				}
			}
			in.close();

			HashMap cnv_10000000_count = new HashMap();
			String inputCNV10000000File = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PCGP_TARGET_SCNA_10000000.txt";			
			fstream = new FileInputStream(inputCNV10000000File);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].split("-")[0].split("\\.")[0];
				if (cnv_10000000_count.containsKey(split[0])) {
					int count = (Integer)cnv_10000000_count.get(split[0]);
					count = count + 1;
					cnv_10000000_count.put(split[0], count);
				} else {
					cnv_10000000_count.put(split[0], 1);
				}
			}
			in.close();

			HashMap sjid_list = new HashMap();
			String inputPatientFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\Survival_Analysis\\SJOS_TARGET\\TARGET_OS_Patient_Metadata.txt";			
			fstream = new FileInputStream(inputPatientFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String patient_id = split[0].split("-")[2];
				if (patient2sjid.containsKey(patient_id)) {					
					String sjid = (String)patient2sjid.get(patient_id);
					sjid_list.put(sjid, sjid);
				}
			}
			in.close();
			
			HashMap immune_score_map = new HashMap();
			HashMap immune_score_values_map = new HashMap();
			HashMap feature_list = new HashMap();
			String immune_score_file = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\pcgp_immune_ssGSEA_T.txt";
			fstream = new FileInputStream(immune_score_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String feature = split[0];
				feature_list.put(feature, feature);
				LinkedList values_list = new LinkedList();				
				for (int i = 1; i < split.length; i++) {
					if (split_header[i].contains("SJOS")) {
						values_list.add(new Double(split[i]));
					}					
				}
				double[] values = MathTools.convertListDouble2Double(values_list);
				double median = MathTools.median(values);
				
				for (int i = 1; i < split.length; i++) {
					immune_score_values_map.put(split_header[i].split("\\.")[0] + "\t" + feature, new Double(split[i]));
					if (new Double(split[i]) > median) {
						immune_score_map.put(split_header[i].split("\\.")[0] + "\t" + feature, "AboveMedian");
					} else {
						immune_score_map.put(split_header[i].split("\\.")[0] + "\t" + feature, "BelowMedian");
					}					
				}
			}
			in.close();
			
			HashMap patient_id_map = new HashMap();
			int count = 0;
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Survival_Analysis\\SJOS_TARGET\\TARGET_OS_Patient_Metadata_ImmuneSubtype.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);


			String outputFile_script = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Survival_Analysis\\SJOS_TARGET\\script.r";
			FileWriter fwriter_script = new FileWriter(outputFile_script);
			BufferedWriter out_script = new BufferedWriter(fwriter_script);

			inputPatientFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Survival_Analysis\\SJOS_TARGET\\TARGET_OS_Patient_Metadata.txt";			
			fstream = new FileInputStream(inputPatientFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\t" + "ImmuneSubtypeStr\tImmuneSubtype\tVitalStatus\tFirstEvent\tSJID\tCNV1000000_Count\tCNV1000000_Above75\tCNV10000000_Count\tCNV10000000_Above30");
			Iterator itr = feature_list.keySet().iterator();
			while (itr.hasNext()) {
				String feature = (String)itr.next();
				

				feature = feature.replaceAll("-", ".").replaceAll(" ", ".");
				
				out.write("\t" + feature + "\t" + feature + "_values");
												
				out_script.write("res.cox_os <- coxph(Surv(Overall_Survival_Time_in_Days, VitalStatus) ~ " + feature + ", data = data_os)\n");
				out_script.write("pval = summary(res.cox_os)$logtest[3]\n");
				out_script.write("write(paste('" + feature + "', 'OS',pval, sep=\" \"), file = \"\\\\\\\\gsc.stjude.org\\\\project_space\\\\zhanggrp\\\\AltSpliceAtlas\\\\common\\\\analysis\\\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\\\Survival_Analysis\\\\SJOS_TARGET\\\\output.txt\", append = TRUE)\n\n");
				out_script.write("res.cox_os <- coxph(Surv(Event_Free_Survival_Time_in_Days, First_Event == \"Relapse\") ~ " + feature + ", data = data_os)\n");
				out_script.write("pval = summary(res.cox_os)$logtest[3]\n");
				out_script.write("write(paste('" + feature + "', 'First_Event_Relapse',pval, sep=\" \"), file = \"\\\\\\\\gsc.stjude.org\\\\project_space\\\\zhanggrp\\\\AltSpliceAtlas\\\\common\\\\analysis\\\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\\\Survival_Analysis\\\\SJOS_TARGET\\\\output.txt\", append = TRUE)\n\n");
				
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String patient_id = split[0].split("-")[2];
				if (patient2sjid.containsKey(patient_id)) {
					
					String sjid = (String)patient2sjid.get(patient_id);
					if (immune_subtype_map.containsKey(sjid)) {
						String immune_subtype_str = (String)immune_subtype_map.get(sjid);
						System.out.println(sjid + "\t" + immune_subtype_str);
						String vital_status = "NA";
						if (split.length > 7) {
							vital_status = split[7];
						}
						if (vital_status.equals("Alive")) {
							vital_status = "1";
						} else if (vital_status.equals("Dead")) {
							vital_status = "2";
						}
						String first_event = "NA";
						if (split.length > 5) {
							first_event = split[5];
						}
						if (first_event.equals("None") || first_event.equals("Censored")) {
							first_event = "1";
						} else {
							first_event = "2";
						}
						
						String immune_activity = immune_subtype_str;
						/*if (immune_subtype_str.equals("ImmClust1") || immune_subtype_str.equals("ImmClust2") || immune_subtype_str.equals("ImmClust3")) {
							immune_activity = "1";
						} else if (immune_subtype_str.equals("ImmClust4") || immune_subtype_str.equals("ImmClust5") || immune_subtype_str.equals("ImmClust6")) {
							immune_activity = "2";
						}*/
						out.write(str + "\t" + immune_subtype_str + "\t" + immune_activity + "\t" + vital_status + "\t" + first_event + "\t" + sjid);
						
						int count_1000000_cnv = 0;
						boolean above_75 = false;
						if (cnv_1000000_count.containsKey(sjid)) {
							count_1000000_cnv = (Integer)cnv_1000000_count.get(sjid);
							if (count_1000000_cnv > 75) {
								above_75 = true;
							}
						}
						
						int count_10000000_cnv = 0;
						boolean count_10000000_above_30 = false;
						if (cnv_10000000_count.containsKey(sjid)) {
							count_10000000_cnv = (Integer)cnv_10000000_count.get(sjid);
							if (count_10000000_cnv > 30) {
								count_10000000_above_30 = true;
							}
						}
						out.write("\t" + count_1000000_cnv + "\t" + above_75 + "\t" + count_10000000_cnv + "\t" + count_10000000_above_30);
						
						Iterator itr2 = feature_list.keySet().iterator();
						while (itr2.hasNext()) {
							String feature = (String)itr2.next();
							String value = "NA";
							double value_double = Double.NaN;
							if (immune_score_map.containsKey(sjid + "\t" + feature)) {
								value = (String)immune_score_map.get(sjid + "\t" + feature);
							}
							if (immune_score_values_map.containsKey(sjid + "\t" + feature)) {
								value_double = (Double)immune_score_values_map.get(sjid + "\t" + feature);
							}
							out.write("\t" + value + "\t" + value_double);
						}
						
						
						out.write("\n");
						count++;
					}
					patient_id_map.put(sjid, sjid);
					
				}
			}
			in.close();
			out.close();
			out_script.close();
			System.out.println(count);
			System.out.println(immune_subtype_map.size());
			
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}
}
