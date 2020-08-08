package rnaseq.splicing.juncsalvager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

<<<<<<< HEAD
import expression.matrix.tools.FilterMatrixExpression;
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
import misc.CommandLine;
import misc.RemoveQuotations;

/**
 * Input should include each disease compared to a reference group.
 * For each reference group, grab the sample and the exon's values.
 * Generate the rank for each matrix 
 * @author tshaw
 *
 */
public class JuncSalvagerSplitMatrixCandidates {


	public static String description() {
		return "Split the matrix based on their annotation";
	}
	public static String type() {
		return "DATAMATRIX";
	}
	public static String parameter_info() {
		return "[inputPCGPMatrix] [inputPCGPAnnotationFile] [outputFolderPCGP]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputPCGPMatrix = args[0]; // PCGP_903_FPKM_ECM_filtcol.txt
			String inputPCGPAnnotationFile = args[1]; // /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/PCGP_TARGET_RNAseq_Analysis/PCGP_TARGET_Sample2DiseaseType.txt
			//String inputGTExMatrix = args[2]; // GTEx_7526_FPKM_ECM_filtcol.txt
			//String inputGTExAnnotationFile = args[3]; // /research/rgs01/project_space/zhanggrp/MethodDevelopment/common/ExonLevelQuantificationPipeline/Reference/ExonLevelGTF/GTEx_Annotation/GTEx_SampleID2Histology.txt
			
			String outputFolderPCGP = args[2];
			//String outputFolderGTEx = args[5];
			//String outputFile = args[6]; // write Disease \t GTEx_Tissue \t wilcox_pvalue \t 
			
			HashMap pcgp_annotation = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputPCGPAnnotationFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[1] = split[1].replaceAll(" ", "_");
				if (pcgp_annotation.containsKey(split[1])) {
					LinkedList list = (LinkedList)pcgp_annotation.get(split[1]);
					list.add(split[0]);
					pcgp_annotation.put(split[1], list);
				} else {
					LinkedList list = new LinkedList();
					list.add(split[0]);
					pcgp_annotation.put(split[1], list);
				}
			}
			in.close();
			
			/*
			HashMap gtex_annotation = new HashMap();
			fstream = new FileInputStream(inputGTExAnnotationFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (gtex_annotation.containsKey(split[1])) {
					LinkedList list = (LinkedList)gtex_annotation.get(split[1]);
					list.add(split[0]);
					gtex_annotation.put(split[1], list);
				} else {
					LinkedList list = new LinkedList();
					list.add(split[0]);
					gtex_annotation.put(split[1], list);
				}
			}
			in.close();
			*/
			
			Iterator itr = pcgp_annotation.keySet().iterator();
			while (itr.hasNext()) {
				String disease = (String)itr.next();
				FileWriter fwriter = new FileWriter(outputFolderPCGP + "/" + disease + ".txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write("ExonID");
				LinkedList samples = (LinkedList)pcgp_annotation.get(disease);
				LinkedList ids = new LinkedList();
				
				fstream = new FileInputStream(inputPCGPMatrix);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				header = in.readLine();
				String[] split_header = header.split("\t");
				for (int i = 2; i < split_header.length; i++) {
					if (samples.contains(split_header[i])) {
						ids.add(i);
						out.write("\t" + split_header[i]);
					}
				}
				out.write("\n");
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					out.write(split[0]);
					Iterator itr2 = ids.iterator();
					while (itr2.hasNext()) {
						int id = (Integer)itr2.next();
						out.write("\t" + split[id]);
					}
					out.write("\n");
				}
				in.close();
				out.close();
				rank(outputFolderPCGP + "/" + disease + ".txt", outputFolderPCGP + "/" + disease + "_rank_tmp.txt");
				String[] arguments = {(outputFolderPCGP + "/" + disease + "_rank_tmp.txt"), (outputFolderPCGP + "/" + disease + "_rank.txt")};
				RemoveQuotations.execute(arguments);
				File f = new File(outputFolderPCGP + "/" + disease + "_rank_tmp.txt");
				f.delete();
<<<<<<< HEAD
				
				String[] arguments2 = {(outputFolderPCGP + "/" + disease + ".txt"), "1.0", "0.1", (outputFolderPCGP + "/" + disease + "_1FPKM.txt")};
				FilterMatrixExpression.execute(arguments2);
				rank_norm(outputFolderPCGP + "/" + disease + "_1FPKM.txt", outputFolderPCGP + "/" + disease + "_rank_1FPKM_tmp.txt", outputFolderPCGP + "/" + disease + "_rank_1FPKM_median_tmp.txt");
				
				String[] arguments3 = {(outputFolderPCGP + "/" + disease + "_rank_1FPKM_tmp.txt"), (outputFolderPCGP + "/" + disease + "_rank_1FPKM.txt")};
				RemoveQuotations.execute(arguments3);
				
				String[] arguments4 = {outputFolderPCGP + "/" + disease + "_rank_1FPKM_median_tmp.txt", outputFolderPCGP + "/" + disease + "_rank_1FPKM_median.txt"};
				RemoveQuotations.execute(arguments4);				
				
				f = new File(outputFolderPCGP + "/" + disease + "_rank_1FPKM_tmp.txt");
				f.delete();
				
				f = new File(outputFolderPCGP + "/" + disease + "_rank_1FPKM_median_tmp.txt");
				f.delete();
				
			}
			
			
			
=======
			}
			
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
			/*
			itr = gtex_annotation.keySet().iterator();
			while (itr.hasNext()) {
				String histology = (String)itr.next();
				FileWriter fwriter = new FileWriter(outputFolderGTEx + "/" + histology + ".txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write("ExonID");
				LinkedList samples = (LinkedList)gtex_annotation.get(histology);
				LinkedList ids = new LinkedList();
				
				fstream = new FileInputStream(inputGTExMatrix);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				header = in.readLine();
				String[] split_header = header.split("\t");
				for (int i = 2; i < split_header.length; i++) {
					if (samples.contains(split_header[i])) {
						ids.add(i);
						out.write("\t" + split_header[i]);
					}
				}
				out.write("\n");
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					out.write(split[0]);
					Iterator itr2 = ids.iterator();
					while (itr2.hasNext()) {
						int id = (Integer)itr2.next();
						out.write("\t" + split[id]);
					}
					out.write("\n");
				}
				in.close();
				out.close();
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void rank(String inputFile, String outputFile) {
		
		try {
			
				
			String script = "";
			//script += "library(limma);\n";
			//script += "#library(edgeR)\n";
			script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=T, as.is=T);\n";
			script += "gene=data[,1]\n";
			script += "allDat = data;\n";
			script += "selection = allDat;\n";
			script += "genenames = selection[,1];\n";
			script += "col_labels = colnames(allDat[1,]);\n";
			script += "sampleNames = col_labels[2:length(col_labels)];\n";
			script += "colnames(selection) = col_labels;\n";
			script += "rownames(selection) = genenames;\n";
			script += "mat = as.matrix(selection[, sampleNames]);\n";
			//script += "numTop = 50;\n";
			script += "rownames(mat)=genenames\n";
			script += "mat <- apply(-mat, 2, rank, ties.method= \"first\");\n";			
			script += "write.table(mat, file=\"" + outputFile + "\", sep=\"\t\");\n";
			CommandLine.writeFile(inputFile + ".r", script);
			CommandLine.executeCommand("R --vanilla < " + inputFile + ".r");
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
<<<<<<< HEAD
	
	public static void rank_norm(String inputFile, String outputFile, String outputMedianFile) {
		
		try {
			
				
			String script = "";
			//script += "library(limma);\n";
			//script += "#library(edgeR)\n";
			script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=T, as.is=T);\n";
			script += "gene=data[,1]\n";
			script += "allDat = data;\n";
			script += "selection = allDat;\n";
			script += "genenames = selection[,1];\n";
			script += "col_labels = colnames(allDat[1,]);\n";
			script += "sampleNames = col_labels[2:length(col_labels)];\n";
			script += "colnames(selection) = col_labels;\n";
			script += "rownames(selection) = genenames;\n";
			script += "mat = as.matrix(selection[, sampleNames]);\n";
			//script += "numTop = 50;\n";
			script += "rownames(mat)=genenames\n";
			script += "mat <- apply(-mat, 2, rank, ties.method= \"first\");\n";		
			script += "new_mat = 1 - (mat / length(mat[,1]));\n";
			script += "write.table(new_mat, file=\"" + outputFile + "\", sep=\"\t\");\n";
			script += "median_mat = apply(new_mat, 1, median);\n";
			script += "write.table(median_mat, file=\"" + outputMedianFile + "\", sep=\"\t\");\n";
			CommandLine.writeFile(inputFile + ".r", script);
			CommandLine.executeCommand("R --vanilla < " + inputFile + ".r");
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
=======
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
}
