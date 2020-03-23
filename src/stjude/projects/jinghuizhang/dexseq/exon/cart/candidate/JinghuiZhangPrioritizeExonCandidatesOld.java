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

public class JinghuiZhangPrioritizeExonCandidatesOld {

	public static void main(String[] args) {
		
		try {
			

			String pcgp_inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\PCGP_905_FPKM_filtcol_truefalse_MedianDiseaseType_filterNAs.txt";
			String target_inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\TARGET_1054_FPKM_filtcol_truefalse_MedianDiseaseType_filterNAs.txt";
			String gtex_inputFile_0 = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\GTEx_7526_FPKM_truefalse_MedianHistology_filterNAs.txt_0";;
			String gtex_inputFile_1 = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\GTEx_7526_FPKM_truefalse_MedianHistology_filterNAs.txt_1";;
			String gtex_inputFile_2 = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\GTEx_7526_FPKM_truefalse_MedianHistology_filterNAs.txt_2";;
			String gtex_inputFile_3 = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\GTEx_7526_FPKM_truefalse_MedianHistology_filterNAs.txt_3";;
			String gtex_inputFile_4 = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\GTEx_7526_FPKM_truefalse_MedianHistology_filterNAs.txt_4";;
			String gtex_inputFile_5 = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\GTEx_7526_FPKM_truefalse_MedianHistology_filterNAs.txt_5";;
			String gtex_inputFile_6 = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\GTEx_7526_FPKM_truefalse_MedianHistology_filterNAs.txt_6";;
			String gtex_inputFile_7 = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\GTEx_7526_FPKM_truefalse_MedianHistology_filterNAs.txt_7";;
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\PCGP_TARGET_GTEx_Candidates.txt";
			
			FileInputStream fstream = new FileInputStream(pcgp_inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			HashMap pcgp_index2name = new HashMap();
			for (int i = 1; i < split_header.length; i++) {
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation"))) {
					pcgp_index2name.put(i, split_header[i]);
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
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation"))) {
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
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("histology") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation") || split_header[i].equals("geneID") || split_header[i].equals("geneName"))) {
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

			
			fstream = new FileInputStream(gtex_inputFile_1);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			gtex_index2name = new HashMap();
			for (int i = 1; i < split_header.length; i++) {
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("histology") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation") || split_header[i].equals("geneID") || split_header[i].equals("geneName"))) {
					gtex_index2name.put(i, split_header[i]);
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gtex_data_1.put(split[0], str);
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA")) {
						if (gtex_index2name.containsKey(i)) {
							gtex_index2name.remove(i);
						}
					}
				}				
			}
			in.close();
			
			fstream = new FileInputStream(gtex_inputFile_1);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String gtex_header_1 = in.readLine();			
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
				gtex_avg_1.put(split[0], avg);
				if (avg < 1.0) {
					good_gtex_1.put(split[0], split[0]);
				}
			}
			in.close();


			
			fstream = new FileInputStream(gtex_inputFile_2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			gtex_index2name = new HashMap();
			for (int i = 1; i < split_header.length; i++) {
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("histology") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation") || split_header[i].equals("geneID") || split_header[i].equals("geneName"))) {
					gtex_index2name.put(i, split_header[i]);
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gtex_data_2.put(split[0], str);
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA")) {
						if (gtex_index2name.containsKey(i)) {
							gtex_index2name.remove(i);
						}
					}
				}				
			}
			in.close();
			
			fstream = new FileInputStream(gtex_inputFile_2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String gtex_header_2 = in.readLine();			
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
				gtex_avg_2.put(split[0], avg);
				if (avg < 1.0) {
					good_gtex_2.put(split[0], split[0]);
				}
			}
			in.close();

			
			fstream = new FileInputStream(gtex_inputFile_3);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			gtex_index2name = new HashMap();
			for (int i = 1; i < split_header.length; i++) {
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("histology") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation") || split_header[i].equals("geneID") || split_header[i].equals("geneName"))) {
					gtex_index2name.put(i, split_header[i]);
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gtex_data_3.put(split[0], str);
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA")) {
						if (gtex_index2name.containsKey(i)) {
							gtex_index2name.remove(i);
						}
					}
				}				
			}
			in.close();
			
			fstream = new FileInputStream(gtex_inputFile_3);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String gtex_header_3 = in.readLine();			
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
				gtex_avg_3.put(split[0], avg);
				if (avg < 1.0) {
					good_gtex_3.put(split[0], split[0]);
				}
			}
			in.close();
			
			
			fstream = new FileInputStream(gtex_inputFile_4);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			gtex_index2name = new HashMap();
			for (int i = 1; i < split_header.length; i++) {
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("histology") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation") || split_header[i].equals("geneID") || split_header[i].equals("geneName"))) {
					gtex_index2name.put(i, split_header[i]);
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gtex_data_4.put(split[0], str);
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA")) {
						if (gtex_index2name.containsKey(i)) {
							gtex_index2name.remove(i);
						}
					}
				}				
			}
			in.close();
			
			fstream = new FileInputStream(gtex_inputFile_4);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String gtex_header_4 = in.readLine();			
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
				gtex_avg_4.put(split[0], avg);
				if (avg < 1.0) {
					good_gtex_4.put(split[0], split[0]);
				}
			}
			in.close();

			
			fstream = new FileInputStream(gtex_inputFile_5);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			gtex_index2name = new HashMap();
			for (int i = 1; i < split_header.length; i++) {
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("histology") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation") || split_header[i].equals("geneID") || split_header[i].equals("geneName"))) {
					gtex_index2name.put(i, split_header[i]);
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gtex_data_5.put(split[0], str);
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA")) {
						if (gtex_index2name.containsKey(i)) {
							gtex_index2name.remove(i);
						}
					}
				}				
			}
			in.close();
			
			fstream = new FileInputStream(gtex_inputFile_5);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String gtex_header_5 = in.readLine();			
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
				gtex_avg_5.put(split[0], avg);
				if (avg < 1.0) {
					good_gtex_5.put(split[0], split[0]);
				}
			}
			in.close();

			
			fstream = new FileInputStream(gtex_inputFile_6);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			gtex_index2name = new HashMap();
			for (int i = 1; i < split_header.length; i++) {
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("histology") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation") || split_header[i].equals("geneID") || split_header[i].equals("geneName"))) {
					gtex_index2name.put(i, split_header[i]);
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gtex_data_6.put(split[0], str);
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA")) {
						if (gtex_index2name.containsKey(i)) {
							gtex_index2name.remove(i);
						}
					}
				}				
			}
			in.close();
			
			fstream = new FileInputStream(gtex_inputFile_6);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String gtex_header_6 = in.readLine();			
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
				gtex_avg_6.put(split[0], avg);
				if (avg < 1.0) {
					good_gtex_6.put(split[0], split[0]);
				}
			}
			in.close();

			
			fstream = new FileInputStream(gtex_inputFile_7);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			gtex_index2name = new HashMap();
			for (int i = 1; i < split_header.length; i++) {
				if (!(split_header[i].equals("diagnosis_short") || split_header[i].equals("histology") || split_header[i].equals("geneID") || split_header[i].equals("geneName") || split_header[i].equals("type") || split_header[i].equals("status") || split_header[i].equals("chr") || split_header[i].equals("start") || split_header[i].equals("end")  || split_header[i].equals("direction")   || split_header[i].equals("Annotation") || split_header[i].equals("geneID") || split_header[i].equals("geneName"))) {
					gtex_index2name.put(i, split_header[i]);
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gtex_data_7.put(split[0], str);
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA")) {
						if (gtex_index2name.containsKey(i)) {
							gtex_index2name.remove(i);
						}
					}
				}				
			}
			in.close();
			
			fstream = new FileInputStream(gtex_inputFile_7);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String gtex_header_7 = in.readLine();			
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
				gtex_avg_7.put(split[0], avg);
				if (avg < 1.0) {
					good_gtex_7.put(split[0], split[0]);
				}
			}
			in.close();

			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			fstream = new FileInputStream(pcgp_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();			
			out.write(header + "\tCandidateFlag\t" + target_header + "\tPCGP_avg\tPCGP_With_High_Expr\tTARGET_avg\tTARGET_With_High_Expr\tGTEX_avg_0\tGTEx_With_Low_Expr_0\tGTEX_avg_1\tGTEx_With_Low_Expr_1\tGTEX_avg_2\tGTEx_With_Low_Expr_2\tGTEX_avg_3\tGTEx_With_Low_Expr_3\tGTEX_avg_4\tGTEx_With_Low_Expr_4\tGTEX_avg_5\tGTEx_With_Low_Expr_5\tGTEX_avg_6\tGTEx_With_Low_Expr_6\tGTEX_avg_7\tGTEx_With_Low_Expr_7\t" + gtex_header_0 + "\t" + gtex_header_1 + "\t" + gtex_header_2 + "\t" + gtex_header_3 + "\t" + gtex_header_4 + "\t" + gtex_header_5 + "\t" + gtex_header_6 + "\t" + gtex_header_7 + "\n");
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
				boolean gtex_low_expr_1 = false;
				if (good_gtex_1.containsKey(name)) {
					gtex_low_expr_1 = true;
				}
				boolean gtex_low_expr_2 = false;
				if (good_gtex_2.containsKey(name)) {
					gtex_low_expr_2 = true;
				}
				boolean gtex_low_expr_3 = false;
				if (good_gtex_3.containsKey(name)) {
					gtex_low_expr_3 = true;
				}
				boolean gtex_low_expr_4 = false;
				if (good_gtex_4.containsKey(name)) {
					gtex_low_expr_4 = true;
				}
				boolean gtex_low_expr_5 = false;
				if (good_gtex_5.containsKey(name)) {
					gtex_low_expr_5 = true;
				}
				boolean gtex_low_expr_6 = false;
				if (good_gtex_6.containsKey(name)) {
					gtex_low_expr_6 = true;
				}
				boolean gtex_low_expr_7 = false;
				if (good_gtex_7.containsKey(name)) {
					gtex_low_expr_7 = true;
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
				if ((pcgp_high_expr || target_high_expr) && gtex_low_expr_0 && gtex_low_expr_1 && gtex_low_expr_2 && gtex_low_expr_3 && gtex_low_expr_4 && gtex_low_expr_5 && gtex_low_expr_6) {
					candidate = true;
				}
				out.write(str + "\t" + candidate + "\t" + target_data.get(split[0]) + "\t" + avg + "\t" + pcgp_high_expr + "\t" + target_avg_result.get(split[0]) + "\t" + target_high_expr + "\t" +  gtex_avg_0.get(split[0]) + "\t" + gtex_low_expr_0 + "\t" +  gtex_avg_1.get(split[0]) + "\t" + gtex_low_expr_1 + "\t" + gtex_avg_2.get(split[0]) + "\t" + gtex_low_expr_2 + "\t" + gtex_avg_3.get(split[0]) + "\t" + gtex_low_expr_3 + "\t" + gtex_avg_4.get(split[0]) + "\t" + gtex_low_expr_4 + "\t" +  gtex_avg_5.get(split[0]) + "\t" + gtex_low_expr_5 + "\t" +  gtex_avg_6.get(split[0]) + "\t" + gtex_low_expr_6 + "\t" +  gtex_avg_7.get(split[0]) + "\t" + gtex_low_expr_7 + "\t" + gtex_data_0.get(split[0]) + "\t" + gtex_data_1.get(split[0]) + "\t" + gtex_data_2.get(split[0]) + "\t" + gtex_data_3.get(split[0]) + "\t" + gtex_data_4.get(split[0]) + "\t" + gtex_data_5.get(split[0]) + "\t" + gtex_data_6.get(split[0]) + "\t" + gtex_data_7.get(split[0]) + "\n");				
			}
			in.close();
			out.close();			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
