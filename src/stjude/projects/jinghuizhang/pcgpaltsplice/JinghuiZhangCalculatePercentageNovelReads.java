package stjude.projects.jinghuizhang.pcgpaltsplice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiZhangCalculatePercentageNovelReads {

	
	public static void main(String[] args) {
		
		try {
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\RNApeg\\Novel_percentage.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			String filePath = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\RNApeg\\";
			File folder = new File(filePath);
			for (File f: folder.listFiles()) {
				;
				if (f.getName().contains("annotated.tab")) {
					if (f.getName().substring(f.getName().length() - 13, f.getName().length()).equals("annotated.tab")) {
						String type = f.getName().split("0")[0].split("1")[0].split("2")[0].split("3")[0].split("4")[0].split("5")[0].split("6")[0].split("7")[0].split("8")[0].split("9")[0];
						double novel_count = 0.0;
						double known_count = 0.0;
						FileInputStream fstream = new FileInputStream(f.getPath());
						DataInputStream din = new DataInputStream(fstream);
						BufferedReader in = new BufferedReader(new InputStreamReader(din));
						String header = in.readLine();
						while (in.ready()) {
							String str = in.readLine();
							String[] split = str.split("\t");
							if (split[2].equals("novel")) {
								novel_count += new Double(split[1]);
							} else {
								known_count += new Double(split[1]);
							}
						}
						in.close();
						out.write(f.getName() + "\t" + type + "\t" + (novel_count / (novel_count + known_count)) + "\t" + novel_count + "\t" + known_count + "\n");
						out.flush();
						System.out.println(f.getName() + "\t" + type + "\t" + (novel_count / (novel_count + known_count)) + "\t" + novel_count + "\t" + known_count + "\n");
					}
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
