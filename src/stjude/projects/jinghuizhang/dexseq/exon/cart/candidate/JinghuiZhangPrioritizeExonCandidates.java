package stjude.projects.jinghuizhang.dexseq.exon.cart.candidate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class JinghuiZhangPrioritizeExonCandidates {

	public static String description() {
		return "Prioritize the exon candidate list";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[pcgp_inputFile] [target_inputFile] [gtex_inputFile_0] [outputFile_all] [outputFile_candidate] [outputFile_bed]";
	}
	public static void execute(String[] args) {
		
		try {
			

			String pcgp_inputFile = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\PCGP_905_FPKM_filtcol_truefalse_MedianDiseaseType_filterNAs.txt";
			String target_inputFile = args[1]; // "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\TARGET_1054_FPKM_filtcol_truefalse_MedianDiseaseType_filterNAs.txt";
			String gtex_inputFile_0 = args[2]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\GTEx_7526_FPKM_truefalse_MedianHistology_filterNAs.txt_0";;
			String outputFile = args[3]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\PCGP_TARGET_GTEx_Candidates.txt";
			String outputFile_candidate = args[4];
			String outputFile_bed = args[5]; 
			int chr_index = -1;
			int start_index = -1;
			int end_index = -1;
			int direction_index = -1;
			FileInputStream fstream = new FileInputStream(pcgp_inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			HashMap pcgp_index2name = new HashMap();
			for (int i = 1; i < split_header.length; i++) {
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation")  || split_header[i].equals("ExonID") || split_header[i].equals("ExonName") || split_header[i].equals("Type") || split_header[i].equals("Chr") || split_header[i].equals("Start") || split_header[i].equals("End") || split_header[i].equals("Strand"))) {
					pcgp_index2name.put(i, split_header[i]);
				}
				if (split_header[i].equals("Chr") || split_header[i].equals("chr")) {
					chr_index = i;
				}
				if (split_header[i].equals("Start") || split_header[i].equals("start")) {
					start_index = i;
				}
				if (split_header[i].equals("End") || split_header[i].equals("end")) {
					end_index = i;
				}
				if (split_header[i].equals("Strand") || split_header[i].equals("strand")) {
					direction_index = i;
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA")) {
						if (pcgp_index2name.containsKey(i)) {
							pcgp_index2name.remove(i);
						}
					}
				}				
			}
			in.close();
			
			
			HashMap pcgp_avg_result = new HashMap();
			HashMap pcgp_hit = new HashMap();
			fstream = new FileInputStream(pcgp_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				LinkedList values = new LinkedList();
				//System.out.println(str);;
				Iterator itr = pcgp_index2name.keySet().iterator();
				while (itr.hasNext()) {
					int index = (Integer)itr.next();
					String index_name = (String)pcgp_index2name.get(index);
					
					values.add(new Double(split[index]));
					if (new Double(split[index]) >= 3) {
						if (pcgp_hit.containsKey(split[0])) {
							String line = (String)pcgp_hit.get(split[0]) + ","  + index_name;
							pcgp_hit.put(split[0], line);
						} else {
							pcgp_hit.put(split[0], index_name);
						}
					}
				}
				double avg = MathTools.mean(MathTools.convertListDouble2Double(values));
				pcgp_avg_result.put(split[0], avg);
				
			}
			in.close();

			
			
			fstream = new FileInputStream(target_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			HashMap target_index2name = new HashMap();
			for (int i = 1; i < split_header.length; i++) {
				//if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation"))) {
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation")  || split_header[i].equals("ExonID") || split_header[i].equals("ExonName") || split_header[i].equals("Type") || split_header[i].equals("Chr") || split_header[i].equals("Start") || split_header[i].equals("End") || split_header[i].equals("Strand"))) {
					target_index2name.put(i, split_header[i]);
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA")) {
						if (target_index2name.containsKey(i)) {
							target_index2name.remove(i);
						}
					}
				}				
			}
			in.close();
			
			HashMap target_data = new HashMap();
			HashMap target_avg_result = new HashMap();
			HashMap target_hit = new HashMap();
			fstream = new FileInputStream(target_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String target_header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				target_data.put(split[0],  str);
				LinkedList values = new LinkedList();
				//System.out.println(str);;
				Iterator itr = target_index2name.keySet().iterator();
				while (itr.hasNext()) {
					int index = (Integer)itr.next();
					String index_name = (String)target_index2name.get(index);
					
					values.add(new Double(split[index]));
					
					if (new Double(split[index]) >= 3) {
						if (target_hit.containsKey(split[0])) {
							String line = (String)target_hit.get(split[0]) + ","  + index_name;
							target_hit.put(split[0], line);
						} else {
							target_hit.put(split[0], index_name);
						}
					}
				}
				double avg = MathTools.mean(MathTools.convertListDouble2Double(values));
				target_avg_result.put(split[0], avg);
				
			}
			in.close();

			HashMap gtex_data_0 = new HashMap();
			HashMap good_gtex_0 = new HashMap();
			HashMap gtex_avg_0 = new HashMap();
			HashMap gtex_data_1 = new HashMap();
			HashMap good_gtex_1 = new HashMap();
			HashMap gtex_avg_1 = new HashMap();
			HashMap gtex_data_2 = new HashMap();
			HashMap good_gtex_2 = new HashMap();
			HashMap gtex_avg_2 = new HashMap();
			HashMap gtex_data_3 = new HashMap();
			HashMap good_gtex_3 = new HashMap();
			HashMap gtex_avg_3 = new HashMap();
			HashMap gtex_data_4 = new HashMap();
			HashMap good_gtex_4 = new HashMap();
			HashMap gtex_avg_4 = new HashMap();
			HashMap gtex_data_5 = new HashMap();
			HashMap good_gtex_5 = new HashMap();
			HashMap gtex_avg_5 = new HashMap();
			HashMap gtex_data_6 = new HashMap();
			HashMap good_gtex_6 = new HashMap();
			HashMap gtex_avg_6 = new HashMap();
			HashMap gtex_data_7 = new HashMap();
			HashMap good_gtex_7 = new HashMap();
			HashMap gtex_avg_7 = new HashMap();
						
			
			fstream = new FileInputStream(gtex_inputFile_0);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			HashMap gtex_index2name = new HashMap();
			for (int i = 1; i < split_header.length; i++) {
				//if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("histology") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation") || split_header[i].equals("geneID") || split_header[i].equals("geneName"))) {
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation")  || split_header[i].equals("ExonID") || split_header[i].equals("ExonName") || split_header[i].equals("Type") || split_header[i].equals("Chr") || split_header[i].equals("Start") || split_header[i].equals("End") || split_header[i].equals("Strand"))) {
					gtex_index2name.put(i, split_header[i]);
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gtex_data_0.put(split[0], str);
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA")) {
						if (gtex_index2name.containsKey(i)) {
							gtex_index2name.remove(i);
						}
					}
				}				
			}
			in.close();
			
			fstream = new FileInputStream(gtex_inputFile_0);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String gtex_header_0 = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				LinkedList values = new LinkedList();
				Iterator itr = gtex_index2name.keySet().iterator();
				while (itr.hasNext()) {
					int index = (Integer)itr.next();
					String index_name = (String)gtex_index2name.get(index);
					values.add(new Double(split[index]));					
				}
				double avg = MathTools.mean(MathTools.convertListDouble2Double(values));
				gtex_avg_0.put(split[0], avg);
				if (avg < 1.0) {
					good_gtex_0.put(split[0], split[0]);
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			FileWriter fwriter_candidate = new FileWriter(outputFile_candidate);
			BufferedWriter out_candidate = new BufferedWriter(fwriter_candidate);

			FileWriter fwriter_bed = new FileWriter(outputFile_bed);
			BufferedWriter out_bed = new BufferedWriter(fwriter_bed);

			fstream = new FileInputStream(pcgp_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();			
			out.write(header + "\tCandidateFlag\t" + target_header + "\tPCGP_avg\tPCGP_With_High_Expr\tTARGET_avg\tTARGET_With_High_Expr\tGTEX_avg_0\tGTEx_With_Low_Expr_0\t" + gtex_header_0 + "\n");
			out_candidate.write(header + "\tCandidateFlag\t" + target_header + "\tPCGP_avg\tPCGP_With_High_Expr\tTARGET_avg\tTARGET_With_High_Expr\tGTEX_avg_0\tGTEx_With_Low_Expr_0\t" + gtex_header_0 + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				LinkedList values = new LinkedList();
				Iterator itr = pcgp_index2name.keySet().iterator();
				while (itr.hasNext()) {
					int index = (Integer)itr.next();
					String index_name = (String)pcgp_index2name.get(index);
					values.add(new Double(split[index]));					
				}
				double avg = MathTools.mean(MathTools.convertListDouble2Double(values));
				boolean gtex_low_expr_0 = false;
				if (good_gtex_0.containsKey(name)) {
					gtex_low_expr_0 = true;
				}

				boolean pcgp_high_expr = false;
				if (pcgp_hit.containsKey(name)) {
					pcgp_high_expr = true;
				}
				boolean target_high_expr = false;
				if (target_hit.containsKey(name)) {
					target_high_expr = true;
				}
				boolean candidate = false;
				if ((pcgp_high_expr || target_high_expr) && gtex_low_expr_0) {
					candidate = true;
				}
				out.write(str + "\t" + candidate + "\t" + target_data.get(split[0]) + "\t" + avg + "\t" + pcgp_high_expr + "\t" + target_avg_result.get(split[0]) + "\t" + target_high_expr + "\t" +  gtex_avg_0.get(split[0]) + "\t" + gtex_low_expr_0 + "\t" +  gtex_data_0.get(split[0]) + "\n");
				if (candidate) {
					out_candidate.write(str + "\t" + candidate + "\t" + target_data.get(split[0]) + "\t" + avg + "\t" + pcgp_high_expr + "\t" + target_avg_result.get(split[0]) + "\t" + target_high_expr + "\t" +  gtex_avg_0.get(split[0]) + "\t" + gtex_low_expr_0 + "\t" +  gtex_data_0.get(split[0]) + "\n");
					String tag = split[0];
					
					String chr = "NA"; 
					if (chr_index == -1) {
						chr = tag.split("\\|")[1];
					} else {
						chr = split[chr_index];
					}
					String start = "NA";
					if (start_index == -1) {
						start = tag.split("\\|")[2];
					} else {
						start = split[start_index];
					}
					String end = "NA";
					if (end_index == -1) {
						end = tag.split("\\|")[3];
					} else {
						end = split[end_index];
					}
					
					String direction = "NA";
					if (direction_index == -1) {
						direction = tag.split("\\|")[4];
					} else {
						direction = split[direction_index];
					}
					
					
					/*String chr = tag.split("\\|")[1];
					String start = tag.split("\\|")[2];
					String end = tag.split("\\|")[3];
					String direction = tag.split("\\|")[5];*/
					if (direction.equals("F")) {
						direction = "+";
					}
					if (direction.equals("R")) {
						direction = "-";
					}
					String type = "Other";
					if (str.contains("PanCan")) {
						type = "PanCan";
					}
					if (str.contains("Novel_ECM")) {
						type = "Novel_ECM";
					}
					if (str.contains("Novel_PanCan")) {
						type = "Novel_PanCan";
					}
					if (str.contains("KnownECM")) {
						type = "KnownECM";
					}
					out_bed.write(chr + "\t" + start + "\t" + end + "\t" + tag + "_" + type + "\t" + 1 + "\t" + direction + "\n");
				}
			}
			in.close();
			out.close();			
			out_candidate.close();
			out_bed.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
