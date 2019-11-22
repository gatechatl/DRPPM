package stjude.projects.xiaotuma.aml.rnaseq.fastqsoftlink;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class XiaotuMaAMLGenerateSoftlink {

	
	public static void main(String[] args) {
		
		try {
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\soft_link_new_fastq_location.sh";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\final_updated_name.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("-");
				String key_name = split[2] + "-" + split[3] + "-" + split[4];				
				map.put(key_name, key_name);				
				
			}
			in.close();
			
			
			String inputFileLoc = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\original_fastq_location.txt";
			fstream = new FileInputStream(inputFileLoc);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String replaceName = str.replaceAll("/rgs01/project_space/xmagrp/AMLRelapse/common/FredHutch/fastq/", "");
				String[] split_replaceName = replaceName.split("-");
				if (replaceName.contains("TARGET")) {
					if (split_replaceName[2].substring(0, 2).equals("BM")) {
						split_replaceName[3] = "14A";
					}
					if (split_replaceName[2].substring(0, 2).equals("RO")) {
						split_replaceName[3] = "14A";
					}
					String RBS = "";
					String srt = "";
					String r1or2 = "";
					if (split_replaceName[4].contains("_RBS")) {
						RBS = "_RBS";
					}
					if (split_replaceName[4].contains("_srt") || split_replaceName[4].contains(".srt")) {
						srt = "_srt";
					}
					if (str.contains("r1.fq.gz")) {
						r1or2 = "_r1.fq.gz";
					}
					if (str.contains("r2.fq.gz")) {
						r1or2 = "_r2.fq.gz";
					} 
					String new_name = split_replaceName[0] + "-" + split_replaceName[1] + "-" + split_replaceName[2] + "-" + split_replaceName[3] + "-" + split_replaceName[4].split("_")[0] + RBS + srt;
					System.out.println("ln -s " + str + " " + "/rgs01/project_space/xmagrp/AMLRelapse/common/FredHutch/fastq_softlink/" + new_name + r1or2);
					out.write("ln -s " + str + " " + "/rgs01/project_space/xmagrp/AMLRelapse/common/FredHutch/fastq_softlink/" + new_name + r1or2 + "\n");
					int i = 0;
					Iterator itr = map.keySet().iterator();
					while (itr.hasNext()) {
						String key_name = (String)itr.next();
						if (new_name.contains(key_name)) {						
							i++;
							if (i == 2) {
								System.out.println(key_name);
							}
						}
					}
					if (i == 0) {
						//System.out.println("Failed to find");
						//System.out.println(new_name + "\t" + replaceName);
					}
					if (i > 1) {
						//System.out.println("More than one");
						//System.out.println(new_name);
					} 
					//System.out.println(replaceName);
				} else {
					String target = "TARGET";
					if (split_replaceName[0].substring(0, 2).equals("BM")) {
						split_replaceName[1] = "14A";
					}
					if (split_replaceName[0].substring(0, 2).equals("RO")) {
						split_replaceName[1] = "14A";
					}
					if (split_replaceName[1].equals("14A")) {
						target += "-" + "00";
					} else {
						target += "-" + "20";
					}
					String RBS = "";
					String srt = "";
					String r1or2 = "";
					if (split_replaceName[2].contains("_RBS")) {
						RBS = "_RBS";
					}
					if (split_replaceName[2].contains("_srt") || split_replaceName[2].contains(".srt")) {
						srt = "_srt";
					}
					if (str.contains("r1.fq.gz")) {
						r1or2 = "_r1.fq.gz";
					}
					if (str.contains("r2.fq.gz")) {
						r1or2 = "_r2.fq.gz";
					} 
					String new_name = target + "-" + split_replaceName[0] + "-" + split_replaceName[1] + "-" + split_replaceName[2].split("_")[0] + RBS + srt;
					//System.out.println("ln -s " + str + " " + "/rgs01/project_space/xmagrp/AMLRelapse/common/FredHutch/fastq_softlink/" + new_name + r1or2);
					out.write("ln -s " + str + " " + "/rgs01/project_space/xmagrp/AMLRelapse/common/FredHutch/fastq_softlink/" + new_name + r1or2 + "\n");
					int i = 0;
					Iterator itr = map.keySet().iterator();
					while (itr.hasNext()) {
						String key_name = (String)itr.next();
						if (new_name.contains(key_name)) {						
							i++;
						}
					}
					if (i == 0) {
						//System.out.println("Failed to find");
						//System.out.println(new_name + "\t" + replaceName);
					}
					if (i > 1) {
						//System.out.println("More than one");
						//System.out.println(new_name);
					} 
					//System.out.println(replaceName);
				}
				
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
