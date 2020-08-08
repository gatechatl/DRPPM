package stjude.projects.xiaotuma.fredhutch.amlproject.fusion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Merge RNAindel with CICERO result
 * @author tshaw
 *
 */
public class XiaotuMaRNAindelAppendCICEROResult {

	
	public static void main(String[] args) {
		
		try {
			
			//String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\AML_CICERO_Manual_Check_List_Clean_Fusion_PatientID_20200302.txt";
			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rna_indel\\RNAindel_Results_Kohei_Unreviewed_SJID.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);						
			
			
			HashMap map = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\SJID2TARGETID_20200117.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[1], split[0]);
			}
			in.close();
			
			HashMap hit = new HashMap();
			//inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\AML_CICERO_Manual_Check_List_Clean_Fusion_20200302.txt";
			inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rna_indel\\RNAindel_Results_Kohei_Unreviewed.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			out.write("SJID\t" + header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sjid = "NA";
				if (map.containsKey(split[0])) {
					sjid = (String)map.get(split[0]);
					hit.put(split[0], split[0]);
				}
				out.write(sjid + "\t" + str + "\n");
				
			}
			in.close();
			/*
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String sjid = (String)itr.next();
				if (!hit.containsKey(sjid)) {
					String patient_id = (String)map.get(sjid);
					out.write(patient_id + "\t" + sjid + "\t" + "empty\tempty\tempty\n");
				}
			}*/
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
