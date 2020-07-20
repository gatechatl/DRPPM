package stjude.projects.junminpeng.excretome.phospho;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JunminPengCheckFAM20CPhosphoScore {


	public static String description() {
		return "Generate FAM20C phosphorylation score.";
	}
	public static String type() {
		return "JunminPeng";
	}
	public static String parameter_info() {
		return "[inputFile: Kaushik's file] [pssm_folder] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String pssm_folder = args[1];
			String outputFile = args[2];

        	FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tPos\tPeptide\tFAM20C_Score\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[2].split("\\|")[1];
				String[] pos_split = split[14].split(",");
				String peptide = "NA";
				double score = Double.NaN;
				File f = new File(pssm_folder + "/" + accession + ".txt");
				for (String pos: pos_split) {
					pos = pos.trim();
					int pos_num = new Integer(pos.replaceAll("S", "").replaceAll("T", "").replaceAll("Y", ""));
					if (f.exists()) {										
						FileInputStream fstream2 = new FileInputStream(f.getPath());
						DataInputStream din2 = new DataInputStream(fstream2);
						BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
						while (in2.ready()) {
							String str2 = in2.readLine();
							String[] split2 = str2.split("\t");
							if (split2[0].equals(accession + "_" + pos_num)) {
								score = new Double(split2[2]);
								peptide = split2[1];
							}
						}
						in2.close();
					}
					out.write(str + "\t" + pos + "\t" + peptide + "\t" + score + "\n");
				}
				
			}
			in.close();
			out.close();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
