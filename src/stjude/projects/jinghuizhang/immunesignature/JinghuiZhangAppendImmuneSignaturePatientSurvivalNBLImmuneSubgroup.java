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

public class JinghuiZhangAppendImmuneSignaturePatientSurvivalNBLImmuneSubgroup {
	
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
						
			HashMap scna_map = new HashMap();
			String nbl_scna_file = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TARGET_Reference\\sam_nbl_sca\\HyperScnaAnno.txt";
			fstream = new FileInputStream(nbl_scna_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().replaceAll("\"", "");
				String[] split = str.split("\t");
				if (split[0].contains("TARGET")) {
					if (patient2sjid.containsKey(split[0].split("-")[2])) {
						split[0] = (String)patient2sjid.get(split[0].split("-")[2]);
					}
				}
				scna_map.put(split[0], split[1]);
			}
			in.close();
			
			HashMap hyperdiploid_map = new HashMap();
			String nbl_hypderdiploid_file = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TARGET_Reference\\sam_nbl_sca\\PanNBL_ChromArmCalls.EntireChromCall.hyperDiploidCall.txt";
			fstream = new FileInputStream(nbl_scna_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().replaceAll("\"", "");
				String[] split = str.split("\t");
				if (split[0].contains("TARGET")) {
					if (patient2sjid.containsKey(split[0].split("-")[2])) {
						split[0] = (String)patient2sjid.get(split[0].split("-")[2]);
					}
				}
				hyperdiploid_map.put(split[0], split[1]);
			}
			in.close();
			
			HashMap immune_subtype_map = new HashMap();
			//String immune_subtype_file = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_target_annotation_immunesubtype.txt";
			String immune_subtype_file = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\Solid_Brain_pcgp_target_annotation_immunesubtype.txt";
			fstream = new FileInputStream(immune_subtype_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sjid = split[0].split("\\.")[0];
				immune_subtype_map.put(sjid, split[1]);
			}
			in.close();
			
			HashMap sjid_list = new HashMap();
			String inputPatientFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Survival_Analysis\\SJNBL_TARGET\\TARGET_NBL_Patient_Metadata.txt";			
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
					if (split_header[i].contains("SJNBL")) {
						values_list.add(new Double(split[i]));
					}					
				}
				double[] values = MathTools.convertListDouble2Double(values_list);
				double median = MathTools.median(values);
				for (int i = 1; i < split.length; i++) {
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
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Survival_Analysis\\SJNBL_TARGET\\TARGET_NBL_Patient_Metadata_ImmuneSubtype.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			String outputFile_script = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Survival_Analysis\\SJNBL_TARGET\\script.r";
			FileWriter fwriter_script = new FileWriter(outputFile_script);
			BufferedWriter out_script = new BufferedWriter(fwriter_script);

			inputPatientFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Survival_Analysis\\SJNBL_TARGET\\TARGET_NBL_Patient_Metadata.txt";			
			fstream = new FileInputStream(inputPatientFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\t" + "ImmuneSubtypeStr\tImmuneSubtype\tVitalStatus\tFirstEvent\tSJID\tSCNA\tHyperDiploid\tImmuneSuppressed");
			Iterator itr = feature_list.keySet().iterator();
			while (itr.hasNext()) {
				String feature = (String)itr.next();
				feature = feature.replaceAll("-", ".").replaceAll(" ", ".");
				out.write("\t" + feature);
				

				out_script.write("res.cox_nbl <- coxph(Surv(Overall_Survival_Time_in_Days, VitalStatus) ~ " + feature + ", data = data_nbl)\n");
				out_script.write("pval = summary(res.cox_nbl)$logtest[3]\n");
				out_script.write("write(paste('" + feature + "', 'OS',pval, sep=\" \"), file = \"\\\\\\\\gsc.stjude.org\\\\project_space\\\\zhanggrp\\\\AltSpliceAtlas\\\\common\\\\analysis\\\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\\\Survival_Analysis\\\\SJNBL_TARGET\\\\output.txt\", append = TRUE)\n\n");
				out_script.write("res.cox_nbl <- coxph(Surv(Event_Free_Survival_Time_in_Days, First_Event == \"Relapse\") ~ " + feature + ", data = data_nbl)\n");
				out_script.write("pval = summary(res.cox_nbl)$logtest[3]\n");
				out_script.write("write(paste('" + feature + "', 'First_Event_Relapse',pval, sep=\" \"), file = \"\\\\\\\\gsc.stjude.org\\\\project_space\\\\zhanggrp\\\\AltSpliceAtlas\\\\common\\\\analysis\\\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\\\Survival_Analysis\\\\SJNBL_TARGET\\\\output.txt\", append = TRUE)\n\n");
				
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
						String immune_suppressed = "NA";
						if (immune_subtype_str.contains("4") || immune_subtype_str.contains("5") || immune_subtype_str.contains("6")) {
							immune_suppressed = "1";
						}
						if (immune_subtype_str.contains("1") || immune_subtype_str.contains("2") || immune_subtype_str.contains("3")) {
							immune_suppressed = "0";
						}
						
						System.out.println(sjid + "\t" + immune_subtype_str);
						String vital_status = split[7];
						if (vital_status.equals("Alive")) {
							vital_status = "1";
						} else if (vital_status.equals("Dead")) {
							vital_status = "2";
						}
						String first_event = split[5];
						if (first_event.equals("None") || first_event.equals("Censored")) {
							first_event = "1";
						} else {
							first_event = "2";
						}
						
						String immune_activity = immune_subtype_str;
						String scna_status = "NA";
						String hyperdiploid_status = "NA";
						if (scna_map.containsKey(sjid)) {
							scna_status = (String)scna_map.get(sjid);
						}
						if (hyperdiploid_map.containsKey(sjid)) {
							hyperdiploid_status = (String)hyperdiploid_map.get(sjid);							
						}
						/*if (immune_subtype_str.equals("ImmClust1") || immune_subtype_str.equals("ImmClust2") || immune_subtype_str.equals("ImmClust3")) {
							immune_activity = "1";
						} else if (immune_subtype_str.equals("ImmClust4") || immune_subtype_str.equals("ImmClust5") || immune_subtype_str.equals("ImmClust6")) {
							immune_activity = "2";
						}*/
						out.write(str + "\t" + immune_subtype_str + "\t" + immune_activity + "\t" + vital_status + "\t" + first_event + "\t" + sjid + "\t" + scna_status + "\t" + hyperdiploid_status + "\t" + immune_suppressed);
						
						Iterator itr2 = feature_list.keySet().iterator();
						while (itr2.hasNext()) {
							String feature = (String)itr2.next();
							String value = "NA";
							if (immune_score_map.containsKey(sjid + "\t" + feature)) {
								value = (String)immune_score_map.get(sjid + "\t" + feature);
							}
							out.write("\t" + value);
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
