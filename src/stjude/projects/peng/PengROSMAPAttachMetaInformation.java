package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class PengROSMAPAttachMetaInformation {

	public static String description() {
		return "Append the meta clinical information to the ROSMAP intron retention summary table.";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[idkeyFile] [clinicalFile] [RNAseq_RinFile] [intron_summary_file]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap rnaseqID2projID = new HashMap();
			HashMap rnaseqID2batch = new HashMap();
			String idkeyFile = args[0];
			String clinicalFile = args[1];
			String rnaseq_rin_file = args[2];
			String intron_summary_file = args[3];
			FileInputStream fstream = new FileInputStream(idkeyFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			//System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String rnaseqID = split[4];
				String[] split_rnaseqID = rnaseqID.split("_");
				String batch = "";
				String tag = split_rnaseqID[0];
				if (split_rnaseqID.length > 1) {
					tag += "_" + split_rnaseqID[1];
				}
				if (split_rnaseqID.length > 2) {
					batch = split_rnaseqID[2];
				}
				rnaseqID2projID.put(tag, split[1]);
				rnaseqID2batch.put(tag, batch);
			}
			in.close();
			
			HashMap projID2clinical = new HashMap();
			fstream = new FileInputStream(clinicalFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String clinical_header = in.readLine();
			//System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				projID2clinical.put(split[0], str);				
			}
			in.close();
			
			HashMap rnaseqID2RIN = new HashMap();
			fstream = new FileInputStream(rnaseq_rin_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				rnaseqID2RIN.put(split[1],split[4]);
			}
			in.close();
			
			fstream = new FileInputStream(intron_summary_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			System.out.println(header + "\tbatch\t" + clinical_header + "\tRIN_num");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String key = split[0].replaceAll(".bam.bed", "");
				
				String projID = (String)rnaseqID2projID.get(key);
				String clinical = (String)projID2clinical.get(projID);
				String batch = (String)rnaseqID2batch.get(key);
				String rin = (String)rnaseqID2RIN.get(key);
				System.out.println(str + "\t" + batch + "\t" + clinical + "\t" + rin);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
