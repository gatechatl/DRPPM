package stjude.projects.xiaotuma.aml.rnaseq.qc.checknaming;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class XiaotuMaAMLGenerateRunTable {

	
	public static void main(String[] args) {
		
		try {
					
			HashMap patient_id_map = new HashMap();
			

			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\final_updated_name_run_table.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String annotation_name = "DiagBM\tDiagPB\tRelapBM\tRelapPB\tDiagBM_RBS\tDiagPB_RBS\tRelapBM_RBS\tRelapPB_RBS\tDiagBM_srt\tDiagPB_srt\tRelapBM_srt\tRelapPB_srt\tDiagBM_RBS_srt\tDiagPB_RBS_srt\tRelapBM_RBS_srt\tRelapPB_RBS_srt\tNormBM\tNormPB\tNormBM_RBS\tNormPB_RBS\tNormBM_srt\tNormPB_srt\tNormBM_RBS_srt\tNormPB_RBS_srt\tFinal_Annotation";
			out.write(annotation_name + "\n");
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\final_updated_name.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[1];
				String[] sample_name_split = sampleName.split("-");
				String target = sample_name_split[0];
				String disease_type = sample_name_split[1];
				String patient_id = sample_name_split[2];				
				String data_type = sample_name_split[3];
				String add_info = sample_name_split[4];
				
				boolean tumor = false;
				boolean diagnosis = false;
				boolean relapse = false;
				
				
				boolean srt = false;
				if (add_info.contains("srt")) {
					srt = true;
				}
				boolean rbs = false;
				if (add_info.contains("RBS")) {
					rbs = true;
				}
				
				String DiagBM = "NA";
				String DiagPB = "NA";
				String RelapBM = "NA";
				String RelapPB = "NA";
				String DiagBM_RBS = "NA";
				String DiagPB_RBS = "NA";
				String RelapBM_RBS = "NA";
				String RelapPB_RBS = "NA";
				String DiagBM_srt = "NA";
				String DiagPB_srt = "NA";
				String RelapBM_srt = "NA";
				String RelapPB_srt = "NA";
				String DiagBM_RBS_srt = "NA";
				String DiagPB_RBS_srt = "NA";
				String RelapBM_RBS_srt = "NA";
				String RelapPB_RBS_srt = "NA";
				String NormBM = "NA";
				String NormPB = "NA";
				String NormBM_RBS = "NA";
				String NormPB_RBS = "NA";
				String NormBM_srt = "NA";
				String NormPB_srt = "NA";
				String NormBM_RBS_srt = "NA";
				String NormPB_RBS_srt = "NA";

				if (patient_id_map.containsKey(patient_id)) {
					String annotation = (String)patient_id_map.get(patient_id);
					String[] split_annotation = annotation.split("\t");
					DiagBM = split_annotation [0];
					DiagPB = split_annotation [1];
					RelapBM = split_annotation [2];
					RelapPB = split_annotation [3];
					DiagBM_RBS = split_annotation [4];
					DiagPB_RBS = split_annotation [5];
					RelapBM_RBS = split_annotation [6];
					RelapPB_RBS = split_annotation [7];
					DiagBM_srt = split_annotation [8];
					DiagPB_srt = split_annotation [9];
					RelapBM_srt = split_annotation [10];
					RelapPB_srt = split_annotation [11];
					DiagBM_RBS_srt = split_annotation [12];
					DiagPB_RBS_srt = split_annotation [13];
					RelapBM_RBS_srt = split_annotation [14];
					RelapPB_RBS_srt = split_annotation [15];
					NormBM = split_annotation [16];
					NormPB = split_annotation [17];
					NormBM_RBS = split_annotation [18];
					NormPB_RBS = split_annotation [19];
					NormBM_srt = split_annotation [20];
					NormPB_srt = split_annotation [21];
					NormBM_RBS_srt = split_annotation [22];
					NormPB_RBS_srt = split_annotation [23];;
				}				
				
				
				
				// if sample is a normal tissue
				if (disease_type.equals("00")) {
					// if sample is from bone marrow
					if (data_type.equals("14A")) {
						if (rbs && srt) {
							if (NormBM_RBS_srt.equals("NA")) {
								NormBM_RBS_srt = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else if (rbs) {
							if (NormBM_RBS.equals("NA")) {
								NormBM_RBS = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else if (srt) {
							if (NormBM_srt.equals("NA")) {		
								NormBM_srt = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else {
							if (NormBM.equals("NA")) {															
								NormBM = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						}
					} else {
						System.out.println("Something wrong: " + sampleName);
					}
				} else if (disease_type.equals("20")) {
					tumor = true;
					if (data_type.equals("03A")) {
						diagnosis = true;
						relapse = false;
						if (rbs && srt) {
							if (NormPB_RBS_srt.equals("NA")) {
								DiagPB_RBS_srt = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else if (rbs) {
							if (DiagPB_RBS.equals("NA")) {
								DiagPB_RBS = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else if (srt) {
							if (DiagPB_srt.equals("NA")) {		
								DiagPB_srt = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else {
							if (DiagPB.equals("NA")) {															
								DiagPB = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						}
					} else if (data_type.equals("04A")) {
						diagnosis = false;
						relapse = true;
						if (rbs && srt) {
							if (NormPB_RBS_srt.equals("NA")) {
								RelapPB_RBS_srt = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else if (rbs) {
							if (RelapPB_RBS.equals("NA")) {
								RelapPB_RBS = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else if (srt) {
							if (RelapPB_srt.equals("NA")) {		
								RelapPB_srt = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else {
							if (RelapPB.equals("NA")) {															
								RelapPB = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						}
						
					} else if (data_type.equals("09A")) {
						diagnosis = true;
						relapse = false;
						if (rbs && srt) {
							if (NormPB_RBS_srt.equals("NA")) {
								DiagBM_RBS_srt = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else if (rbs) {
							if (DiagBM_RBS.equals("NA")) {
								DiagBM_RBS = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else if (srt) {
							if (DiagBM_srt.equals("NA")) {		
								DiagBM_srt = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else {
							if (DiagBM.equals("NA")) {															
								DiagBM = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						}
						
					} else if (data_type.equals("40A")) {
						diagnosis = false;
						relapse = true;
						if (rbs && srt) {
							if (NormPB_RBS_srt.equals("NA")) {
								RelapBM_RBS_srt = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else if (rbs) {
							if (RelapBM_RBS.equals("NA")) {
								RelapBM_RBS = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else if (srt) {
							if (RelapBM_srt.equals("NA")) {		
								RelapBM_srt = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						} else {
							if (RelapBM.equals("NA")) {															
								RelapBM = sampleName;
							} else {
								System.out.println("Something wrong: " + sampleName);
							}
						}						
					} else {
						System.out.println("Something wrong: " + sampleName);
					}
				} else {
					System.out.println("Something wrong: " + sampleName);
				}
				
				
				String annotation = DiagBM + "\t" + DiagPB + "\t" + RelapBM + "\t" + RelapPB + "\t" + DiagBM_RBS + "\t" + DiagPB_RBS + "\t" + RelapBM_RBS + "\t" + RelapPB_RBS + "\t" + DiagBM_srt + "\t" + DiagPB_srt + "\t" + RelapBM_srt + "\t" + RelapPB_srt + "\t" + DiagBM_RBS_srt + "\t" + DiagPB_RBS_srt + "\t" + RelapBM_RBS_srt + "\t" + RelapPB_RBS_srt + "\t" + NormBM + "\t" + NormPB + "\t" + NormBM_RBS + "\t" + NormPB_RBS + "\t" + NormBM_srt + "\t" + NormPB_srt + "\t" + NormBM_RBS_srt + "\t" + NormPB_RBS_srt;
				patient_id_map.put(patient_id,  annotation);
			}
			in.close();			
			
			Iterator itr = patient_id_map.keySet().iterator();
			while (itr.hasNext()) {
				String patient_id = (String)itr.next();
				String line = (String)patient_id_map.get(patient_id);
				String[] split_annotation = line.split("\t");
				String DiagBM = split_annotation [0];
				String DiagPB = split_annotation [1];
				String RelapBM = split_annotation [2];
				String RelapPB = split_annotation [3];
				String DiagBM_RBS = split_annotation [4];
				String DiagPB_RBS = split_annotation [5];
				String RelapBM_RBS = split_annotation [6];
				String RelapPB_RBS = split_annotation [7];
				String DiagBM_srt = split_annotation [8];
				String DiagPB_srt = split_annotation [9];
				String RelapBM_srt = split_annotation [10];
				String RelapPB_srt = split_annotation [11];
				String DiagBM_RBS_srt = split_annotation [12];
				String DiagPB_RBS_srt = split_annotation [13];
				String RelapBM_RBS_srt = split_annotation [14];
				String RelapPB_RBS_srt = split_annotation [15];
				String NormBM = split_annotation [16];
				String NormPB = split_annotation [17];
				String NormBM_RBS = split_annotation [18];
				String NormPB_RBS = split_annotation [19];
				String NormBM_srt = split_annotation [20];
				String NormPB_srt = split_annotation [21];
				String NormBM_RBS_srt = split_annotation [22];
				String NormPB_RBS_srt = split_annotation [23];
				boolean diagnosis = false;
				boolean relapse = false;
				boolean normal = false; 
				if (DiagBM.contains("TARGET") || DiagPB.contains("TARGET") || DiagBM_RBS.contains("TARGET") || DiagPB_RBS.contains("TARGET") || DiagBM_srt.contains("TARGET") || DiagPB_srt.contains("TARGET") || DiagBM_RBS_srt.contains("TARGET") || DiagPB_RBS_srt.contains("TARGET")) {
					diagnosis = true;
				}
				if (RelapBM.contains("TARGET") || RelapPB.contains("TARGET") || RelapBM_RBS.contains("TARGET") || RelapPB_RBS.contains("TARGET") || RelapBM_srt.contains("TARGET") || RelapPB_srt.contains("TARGET") || RelapBM_RBS_srt.contains("TARGET") || RelapPB_RBS_srt.contains("TARGET")) {
					relapse = true;
				}
				if (NormBM.contains("TARGET") || NormPB.contains("TARGET") || NormBM_RBS.contains("TARGET") || NormPB_RBS.contains("TARGET") || NormBM_srt.contains("TARGET") || NormPB_srt.contains("TARGET") || NormBM_RBS_srt.contains("TARGET") || NormPB_RBS_srt.contains("TARGET")) {
					normal = true;
				}
				String final_annotation = "";
				if (diagnosis) {
					final_annotation += "D";
				}
				if (relapse) {
					final_annotation += "R";
				}
				if (normal) {
					final_annotation += "N";
				}
				System.out.println(patient_id + "\t" + line);
				out.write(patient_id + "\t" + line + "\t" + final_annotation + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
