package stjude.projects.xiaotuma.aml.rnaseq.qc.checknaming;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class XiaotuMaCheckForMissingNames {

	public static void main(String[] args) {
		try {
			
		
			HashMap sjid2targetid = new HashMap();
			String inputFileSize = "Z:\\ResearchHome\\ProjectSpace\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\SJID2TARGETID_20200117.txt";
			FileInputStream fstream = new FileInputStream(inputFileSize);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sjid2targetid.put(split[0], split[1]);
			}
			in.close();
		
			HashMap finished_mapping = new HashMap();
			String sampleFile = "Z:\\ResearchHome\\ProjectSpace\\xmagrp\\AMLRelapse\\common\\FredHutch\\rnaseq_mapping_stats\\AMLFredHutch_RNAseq_Mapping_Stats.txt";
			fstream = new FileInputStream(sampleFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				finished_mapping.put(split[0], split[0]);
			}
			in.close();
		
			Iterator itr = sjid2targetid.keySet().iterator();
			while (itr.hasNext()) {
				String sjid = (String)itr.next();
				if (!finished_mapping.containsKey(sjid)) {
					System.out.println(sjid);
				}
			}
			
			System.out.println(sjid2targetid.size());
			System.out.println(finished_mapping.size());
		} catch (Exception e ){
			e.printStackTrace();
		}
	}
}
