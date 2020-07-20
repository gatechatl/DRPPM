package stjude.projects.xiaotuma.fredhutch.amlproject.fusion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class XiaotuMaAppendSampleIDComprehensiveRelapse {

	
	public static void main(String[] args) {
		try {
			
			HashMap map = new HashMap();
			
			
			//String outputUpdatedCiceroIDFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Report_To_Soheil\\RNAseq_CICERO_Fusion_List_Updated_v0.1_01062020.txt";			
			String outputUpdatedCiceroIDFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\AML_CICERO_Manual_Check_List_Clean_Fusion_Relapse_TARGETID_20200413.txt";
			FileWriter fwriter = new FileWriter(outputUpdatedCiceroIDFile);
			BufferedWriter out = new BufferedWriter(fwriter);						
			
			//String inputSJIDsPatientIDs = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Report_To_Soheil\\SJID_patient_sample_information_20200106.txt";
			String inputSJIDsPatientIDs = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\SJID2TARGETID_20200117.txt";
			FileInputStream fstream = new FileInputStream(inputSJIDsPatientIDs);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//map.put(split[0].split("-")[0], str);
				map.put(split[0], split[1]);
			}
			in.close();
			 
			HashMap hit = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\AML_CICERO_Manual_Check_List_Relapse_Clean_Fusion_20200413.txt";
			fstream = new FileInputStream(inputFile);
			//fstream = new FileInputStream(inputFileCiceroResult);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write("TARGET_ID\t" + header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String target_id = "NA";
				if (map.containsKey(split[0])) {
					target_id = (String)map.get(split[0]);
					hit.put(split[0], split[0]);
				}
				out.write(target_id + "\t" + str + "\n");
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String sjid = (String)itr.next();
				String patientid = (String)map.get(sjid);
				if (!hit.containsKey(sjid)) {
					if (sjid.contains("SJNORM")) {
						out.write(patientid + "\t" + sjid + "\tSJNORM\tSJNORM\tSJNORM\tSJNORM\tSJNORM\tSJNORM\n");
					} else {
						out.write(patientid + "\t" + sjid + "\tNA\tNA\tNA\tNA\tNA\tNA\n");
					}
				}
			}
			
			/*
			String inputFileCiceroResult = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Report_To_Soheil\\RNAseq_CICERO_Fusion_List_v0.1_01062020.txt";
			fstream = new FileInputStream(inputFileCiceroResult);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\tComp Bio Sample Name\tComp Bio Subject ID\tSJUID\tPreferred Sample ID\tSubject Disease Code\tSample Type Code\tTissue Bank ID\tPrior Comp Bio ID\tcustom header\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String line = (String)map.get(split[0]);
				out.write(str + "\t" + line + "\n");
			}
			in.close();
			*/
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
