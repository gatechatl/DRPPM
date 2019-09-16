package stjude.projects.jinghuizhang.panball;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiZhangReplaceMeta {

	public static void main(String[] args) {
		
		try {
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\BALL_Mullighan\\tpm\\Combat\\BALL_meta_updated.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String listOfCandidate = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\BALL_Mullighan\\tpm\\Combat\\BALL_meta.txt";
			FileInputStream fstream = new FileInputStream(listOfCandidate);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				int rename = 1;
				if (split[1].equals("Stranded_total_RNA_PE100bp")) {
					rename = 1;
				} else if (split[1].equals("Stranded_total_RNA_PE75bp")) {
					rename = 2;
				} else if (split[1].equals("Unstranded_mRNA_PE100bp")) {
					rename = 3;
				} else if (split[1].equals("Unstranded_mRNA_PE50bp")) {
					rename = 4;
				} else if (split[1].equals("Unstranded_mRNA_PE75bp")) {
					rename = 5;
				} else {
					rename = 6;
				}
				out.write(split[0] + "\t" + rename + "\t" + split[2] + "\t" + split[3] + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
