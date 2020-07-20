package stjude.projects.xiaotuma.aml.rnaseq.runtable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * First iteration I left out relapse cases
 * @author tshaw
 *
 */
public class XiaotuMaSplitFusionsBasedOnRunTableRelapse {

	
	public static void main(String[] args) {
		
		
		try {
			
			HashMap patient_sjid = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\final_updated_name_run_table_20200217.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (patient_sjid.containsKey(split[0])) {
					String sjid1 = split[26].split("-")[0];
					String sjid2 = split[37].split("-")[0];
					String sjid3 = split[48].split("-")[0];
					LinkedList samples = (LinkedList)patient_sjid.get(split[0]);
					if (!sjid1.equals("NA")) {
						//samples.add(sjid1);
					}
					if (!sjid2.equals("NA")) {
						samples.add(sjid2);
					}
					if (!sjid3.equals("NA")) {
						samples.add(sjid3);
					}
					patient_sjid.put(split[0], samples);
				} else {
					String sjid1 = split[26].split("-")[0];
					String sjid2 = split[37].split("-")[0];
					String sjid3 = split[48].split("-")[0];
					LinkedList samples = new LinkedList();
					if (!sjid1.equals("NA")) {
						//samples.add(sjid1);
					}
					if (!sjid2.equals("NA")) {
						samples.add(sjid2);
					}
					if (!sjid3.equals("NA")) {
						samples.add(sjid3);
					}
					patient_sjid.put(split[0], samples);
				}
			}
			in.close();
			
			String outputFile1 = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\CICERO_Fusion_Group1_Relapse.txt";
			FileWriter fwriter1 = new FileWriter(outputFile1);
			BufferedWriter out1 = new BufferedWriter(fwriter1);
			
			String outputFile2 = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\CICERO_Fusion_Group2_Relapse.txt";
			FileWriter fwriter2 = new FileWriter(outputFile2);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			String outputFile3 = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\CICERO_Fusion_Group3_Relapse.txt";
			FileWriter fwriter3 = new FileWriter(outputFile3);
			BufferedWriter out3 = new BufferedWriter(fwriter3);
			
			String outputFile4 = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\CICERO_Fusion_Group4_Relapse.txt";
			FileWriter fwriter4 = new FileWriter(outputFile4);
			BufferedWriter out4 = new BufferedWriter(fwriter4);
			
			String outputFile5 = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\CICERO_Fusion_Group5_Relapse.txt";
			FileWriter fwriter5 = new FileWriter(outputFile5);
			BufferedWriter out5 = new BufferedWriter(fwriter5);

			LinkedList events = new LinkedList();
			String fusion_inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\20200217\\fus_tim_updated_20200217.txt";
			fstream = new FileInputStream(fusion_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				events.add(str);
			}
			in.close();
			out1.write(header + "\n");
			out2.write(header + "\n");
			out3.write(header + "\n");
			out4.write(header + "\n");
			out5.write(header + "\n");
			
			int count = 0;
			Iterator itr = patient_sjid.keySet().iterator();
			while (itr.hasNext()) {				
				String patient_id = (String)itr.next();
				int index = count % 5;
				LinkedList sjid_list = (LinkedList)patient_sjid.get(patient_id);
				Iterator itr2 = events.iterator();
				while (itr2.hasNext()) {
					String str = (String)itr2.next();
					String[] split = str.split("\t");
					if (sjid_list.contains(split[0])) {
						if (index == 0) {
							out1.write(str + "\n");
						} else if (index == 1) {
							out2.write(str + "\n");
						} else if (index == 2) {
							out3.write(str + "\n");
						} else if (index == 3) {
							out4.write(str + "\n");
						} else if (index == 4) {
							out5.write(str + "\n");
						}
					}
				}
				in.close();
				count++;
				System.out.println(count);
			}
			
			out1.close();
			out2.close();
			out3.close();
			out4.close();
			out5.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
