package stjude.projects.xiaotuma.aml.rnaseq.fusion.checkmissing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XiaotuMaCheckMissingFusions {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap sjid2targetid = new HashMap();
			HashMap targetid2sjid= new HashMap();
			String inputFileSize = "Z:\\ResearchHome\\ProjectSpace\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\SJID2TARGETID_20200117.txt";
			FileInputStream fstream = new FileInputStream(inputFileSize);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sjid2targetid.put(split[0], split[1]);
				targetid2sjid.put(split[1], split[0]);
			}
			in.close();
			
			HashMap missing_cases = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\missing90cases.txt"; // 
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 	
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				str = str.replaceAll("\\.", "-");
				System.out.println(str);
				missing_cases.put(str, str);
			}
			in.close();
			

			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\Missing_CICERO_Cases.txt"; //
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\CICERO_Diag_Relapse_Combine.txt"; // 
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 		
			out.write("TARGETID\t" + in.readLine() + "\n");;
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].replaceAll("_RBS",  "").replaceAll("_srt", "");
				String target = (String)sjid2targetid.get(split[0]);
				if (missing_cases.containsKey(target)) {
					out.write(target + "\t" + str + "\n");
				}
				//System.out.println(split[0] + "\t" + target);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
