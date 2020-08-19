package graph.figures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import constants.CONFIG;

public class HeatmapGeneration {

	public static void main(String[] args) {
		try {
			
			GangGBMGeneListDataAnalysisProtein(args);
			//GangGBMGeneListDataAnalysisPhospho(args);
			
			/*
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\HGG_EXAMPLE\\raw\\total\\Total_Proteome_GeneName.txt";
			String outputFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\HGG_EXAMPLE\\raw\\total\\Total_Proteome_GeneName.png";
			String listAFile = args[1];
			String geneSetFile = args[2];
			String h2mFile = args[4];
			String takeLogStr = args[5];
			boolean takeLog = false;
			
			if (takeLogStr.toUpperCase().equals("TRUE")) {
			
				takeLog = true;
			
			}
			
			String listA = "'p1a','p1b','P2','P3','N1','N2','N3','Ctl1','Ctl2','Ctl3'";
			String geneSet = "\"Pdgfra\", \"Kit\", \"Met\", \"Egfr\", \"Igf1r\", \"Braf\", \"Nras\", \"Kras\", \"Pik3ca\", \"Mycn\", \"Myc\", \"Mdm2\", \"Mdm4\", \"Cdk4\", \"Cdk6\", \"Ccnd2\", \"Nf1\", \"Pten\", \"Trp53\", \"Cdkn2a\", \"Cdkn2b\", \"Cdkn2c\", \"Rb1\"";
			
			HashMap h2m = human2mouse(h2mFile);
			LinkedList listA_list = readGroupList(listAFile);
			
			LinkedList geneSet_list = readGroupList(geneSetFile);
			
			LinkedList allGeneName = readGeneNames(inputFile);
						
			geneSet_list = convertH2MGene(geneSet_list, h2m);
			
			geneSet_list = filterList(geneSet_list, allGeneName);
			
			listA = list2str(listA_list);
			
			geneSet = list2str(geneSet_list);
			
			System.out.println(generateHeatmapScript(inputFile, outputFile, listA, geneSet, "Title", takeLog));
			
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void executeMADCOL(String[] args) {
		try {

			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\phospho_peptide_raw_data_GBM_Name_Collapse.txt";
			int topN = new Integer(args[1]);
			String sampleNameFile = args[2];
			String meta = args[3]; // ATMTDP1,red:Pot,blue:Lig4,green:BRCA2,green:NA,purple
			String outputFile = args[4]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\Phospho_peptide_GBM_Genes.png";
			//String geneSetFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangMouseGeneListAnalysis.txt";
			String futureParameter = ""; // this parameter contains the specification of the heatmap
			//String h2mFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			
			//String takeLogStr = args[5];
			
			String takeLogStr = "FALSE";
			
			boolean takeLog = false;
			
			if (takeLogStr.toUpperCase().equals("TRUE")) {			
				takeLog = true;			
			}
			
			String listA = ""; //"'p1a','p1b','P2','P3','N1','N2','N3','Ctl1','Ctl2','Ctl3'";
			String geneSet = ""; //"\"Pdgfra\", \"Kit\", \"Met\", \"Egfr\", \"Igf1r\", \"Braf\", \"Nras\", \"Kras\", \"Pik3ca\", \"Mycn\", \"Myc\", \"Mdm2\", \"Mdm4\", \"Cdk4\", \"Cdk6\", \"Ccnd2\", \"Nf1\", \"Pten\", \"Trp53\", \"Cdkn2a\", \"Cdkn2b\", \"Cdkn2c\", \"Rb1\"";
			
			LinkedList allGeneName = readGeneNames(inputFile);
			
			LinkedList sample_names = readGroupList(sampleNameFile);
			String sample_names_str = list2str(sample_names);
			System.out.println(generateTopMADHeatmapScript(inputFile, outputFile, sample_names_str, "Top" + topN + " MAD Heatmap", topN, meta));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}		
	}
	
	public static void executePHeatMAD(String[] args) {
		try {

			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\phospho_peptide_raw_data_GBM_Name_Collapse.txt";
			int topN = new Integer(args[1]);
			String sampleNameFile = args[2];
			String outputFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\Phospho_peptide_GBM_Genes.png";
			//String geneSetFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangMouseGeneListAnalysis.txt";
			String futureParameter = ""; // this parameter contains the specification of the heatmap
			//String h2mFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			
			//String takeLogStr = args[5];
			
			String takeLogStr = "FALSE";
			
			boolean takeLog = false;
			
			if (takeLogStr.toUpperCase().equals("TRUE")) {			
				takeLog = true;			
			}
			
			String listA = ""; //"'p1a','p1b','P2','P3','N1','N2','N3','Ctl1','Ctl2','Ctl3'";
			String geneSet = ""; //"\"Pdgfra\", \"Kit\", \"Met\", \"Egfr\", \"Igf1r\", \"Braf\", \"Nras\", \"Kras\", \"Pik3ca\", \"Mycn\", \"Myc\", \"Mdm2\", \"Mdm4\", \"Cdk4\", \"Cdk6\", \"Ccnd2\", \"Nf1\", \"Pten\", \"Trp53\", \"Cdkn2a\", \"Cdkn2b\", \"Cdkn2c\", \"Rb1\"";
			
			//HashMap h2m = human2mouse(h2mFile);
			
			//LinkedList geneSet_list = readGroupList(geneSetFile);
			
			LinkedList allGeneName = readGeneNames(inputFile);
						
			//geneSet_list = convertH2MGene(geneSet_list, h2m);
			
			LinkedList sample_names = readGroupList(sampleNameFile);
			String sample_names_str = list2str(sample_names);
			
			//geneSet_list = filterList(geneSet_list, allGeneName);
			
			
			//geneSet = list2str(geneSet_list);
			//geneSet = list2str(allGeneName);
			
			System.out.println(generatePHeatmapMAD(inputFile, outputFile, sample_names_str, "Top" + topN + " MAD Heatmap", topN));
			//System.out.println(generateHeatmapScript(inputFile, outputFile, listA, geneSet, "Title", takeLog, geneSet_list.size()));

			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}		
	}
	public static void executeMAD(String[] args) {
		try {

			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\phospho_peptide_raw_data_GBM_Name_Collapse.txt";
			int topN = new Integer(args[1]);
			String sampleNameFile = args[2];
			String outputFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\Phospho_peptide_GBM_Genes.png";
			//String geneSetFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangMouseGeneListAnalysis.txt";
			String futureParameter = ""; // this parameter contains the specification of the heatmap
			//String h2mFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			
			//String takeLogStr = args[5];
			
			String takeLogStr = "FALSE";
			
			boolean takeLog = false;
			
			if (takeLogStr.toUpperCase().equals("TRUE")) {			
				takeLog = true;			
			}
			
			String listA = ""; //"'p1a','p1b','P2','P3','N1','N2','N3','Ctl1','Ctl2','Ctl3'";
			String geneSet = ""; //"\"Pdgfra\", \"Kit\", \"Met\", \"Egfr\", \"Igf1r\", \"Braf\", \"Nras\", \"Kras\", \"Pik3ca\", \"Mycn\", \"Myc\", \"Mdm2\", \"Mdm4\", \"Cdk4\", \"Cdk6\", \"Ccnd2\", \"Nf1\", \"Pten\", \"Trp53\", \"Cdkn2a\", \"Cdkn2b\", \"Cdkn2c\", \"Rb1\"";
			
			//HashMap h2m = human2mouse(h2mFile);
			
			//LinkedList geneSet_list = readGroupList(geneSetFile);
			
			LinkedList allGeneName = readGeneNames(inputFile);
						
			//geneSet_list = convertH2MGene(geneSet_list, h2m);
			
			LinkedList sample_names = readGroupList(sampleNameFile);
			String sample_names_str = list2str(sample_names);
			
			//geneSet_list = filterList(geneSet_list, allGeneName);
			
			
			//geneSet = list2str(geneSet_list);
			//geneSet = list2str(allGeneName);
			
			System.out.println(generateTopMADHeatmapScript(inputFile, outputFile, sample_names_str, "Top" + topN + " MAD Heatmap", topN, "NA"));
			//System.out.println(generateHeatmapScript(inputFile, outputFile, listA, geneSet, "Title", takeLog, geneSet_list.size()));

			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}		
	}
	public static String PHeat_Parameter_Info() {
		return "[inputFile] [sampleName] [geneSetFile] [outputPng] [title] [rowclust] [colClust] [TakeLog] [width] [height] [rowFontSize] [colFontSize] [colorType]";
	}
	public static void executePHeat(String[] args) {
		try {

			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\phospho_peptide_raw_data_GBM_Name_Collapse.txt";
			String listAFile = args[1]; //"";
			String geneSetFile = args[2]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangMouseGeneListAnalysis.txt";
			String outputFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\Phospho_peptide_GBM_Genes.png";
			boolean row_cluster = true;
			boolean col_cluster = true;
			String width_size = "800";
			String height_size = "1200";
			String title = "";
			String takeLogStr = "FALSE";
			String row_font_size = "11";
			String col_font_size = "11";
			int colorType = 0;
			if (args.length > 4) { 			
				title = args[4];
				if (args[5].toUpperCase().equals("TRUE")) {
					row_cluster = true;
				} else {
					row_cluster = false;
				}
				if (args[6].toUpperCase().equals("TRUE")) {
					col_cluster = true;
				} else {
					col_cluster = false;
				}
				
				
				
				if (args.length > 7) {
					if (args[7].toUpperCase().equals("TRUE")) {
						takeLogStr = "TRUE";
					} else {
						takeLogStr = "FALSE";
					}
					width_size = args[8];
					height_size = args[9];
				}
				if (args.length > 10) {
					row_font_size = args[10];
					col_font_size = args[11];
				}
				if (args.length > 12) {
					colorType = new Integer(args[12]);
				}
			}
			
			String futureParameter = ""; // this parameter contains the specification of the heatmap
			//String h2mFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			
			//String takeLogStr = args[5];
			
			
			boolean takeLog = false;
			
			if (takeLogStr.toUpperCase().equals("TRUE")) {			
				takeLog = true;			
			}
			
			String listA = ""; //"'p1a','p1b','P2','P3','N1','N2','N3','Ctl1','Ctl2','Ctl3'";
			String geneSet = ""; //"\"Pdgfra\", \"Kit\", \"Met\", \"Egfr\", \"Igf1r\", \"Braf\", \"Nras\", \"Kras\", \"Pik3ca\", \"Mycn\", \"Myc\", \"Mdm2\", \"Mdm4\", \"Cdk4\", \"Cdk6\", \"Ccnd2\", \"Nf1\", \"Pten\", \"Trp53\", \"Cdkn2a\", \"Cdkn2b\", \"Cdkn2c\", \"Rb1\"";
			
			//HashMap h2m = human2mouse(h2mFile);
			
			LinkedList listA_list = new LinkedList();
			
			if (!(listAFile.equals("") || listAFile.toUpperCase().equals("NA"))) {
				listA_list = readGroupList(listAFile);
			}
			
			LinkedList geneSet_list = readGroupList(geneSetFile);
			
			LinkedList allGeneName = readGeneNames(inputFile);
						
			//geneSet_list = convertH2MGene(geneSet_list, h2m);
			
			geneSet_list = filterList(geneSet_list, allGeneName);
			
			listA = list2str(listA_list);
			
			geneSet = list2strV2(geneSet_list);
			
			System.out.println(generatePHeatmapScript(inputFile, outputFile, listA, geneSet, title, takeLog, geneSet_list.size(), col_cluster, row_cluster, width_size, height_size, row_font_size, col_font_size, colorType));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}		
	}
	
	public static String PHeat_Annotation_Parameter_Info() {
		return "[inputFile] [sampleName] [geneSetFile] [outputPng] [title] [rowclust] [colClust] [TakeLog] [width] [height] [rowFontSize] [colFontSize] [colorType] [annotation file]";
	}
	public static void executePHeatAnnotation(String[] args) {
		try {

			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\phospho_peptide_raw_data_GBM_Name_Collapse.txt";
			String listAFile = args[1]; //"";
			String geneSetFile = args[2]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangMouseGeneListAnalysis.txt";
			String outputFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\Phospho_peptide_GBM_Genes.png";
			boolean row_cluster = true;
			boolean col_cluster = true;
			String width_size = "800";
			String height_size = "1200";
			String title = "";
			String takeLogStr = "FALSE";
			String row_font_size = "11";
			String col_font_size = "11";
			String annotation = "";
			int colorType = 0;
			if (args.length > 4) { 			
				title = args[4];
				if (args[5].toUpperCase().equals("TRUE")) {
					row_cluster = true;
				} else {
					row_cluster = false;
				}
				if (args[6].toUpperCase().equals("TRUE")) {
					col_cluster = true;
				} else {
					col_cluster = false;
				}
				
				
				
				if (args.length > 7) {
					if (args[7].toUpperCase().equals("TRUE")) {
						takeLogStr = "TRUE";
					} else {
						takeLogStr = "FALSE";
					}
					width_size = args[8];
					height_size = args[9];
				}
				if (args.length > 10) {
					row_font_size = args[10];
					col_font_size = args[11];
				}
				if (args.length > 12) {
					colorType = new Integer(args[12]);
				}
				if (args.length > 13) {
					annotation = args[13];
				}
			}
			
			String futureParameter = ""; // this parameter contains the specification of the heatmap
			//String h2mFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			
			//String takeLogStr = args[5];
			
			
			boolean takeLog = false;
			
			if (takeLogStr.toUpperCase().equals("TRUE")) {			
				takeLog = true;			
			}
			
			String listA = ""; //"'p1a','p1b','P2','P3','N1','N2','N3','Ctl1','Ctl2','Ctl3'";
			String geneSet = ""; //"\"Pdgfra\", \"Kit\", \"Met\", \"Egfr\", \"Igf1r\", \"Braf\", \"Nras\", \"Kras\", \"Pik3ca\", \"Mycn\", \"Myc\", \"Mdm2\", \"Mdm4\", \"Cdk4\", \"Cdk6\", \"Ccnd2\", \"Nf1\", \"Pten\", \"Trp53\", \"Cdkn2a\", \"Cdkn2b\", \"Cdkn2c\", \"Rb1\"";
			
			//HashMap h2m = human2mouse(h2mFile);
			
			LinkedList listA_list = new LinkedList();
			
			if (!(listAFile.equals("") || listAFile.toUpperCase().equals("NA"))) {
				listA_list = readGroupList(listAFile);
			}
			
			LinkedList geneSet_list = readGroupList(geneSetFile);
			
			LinkedList allGeneName = readGeneNames(inputFile);
						
			//geneSet_list = convertH2MGene(geneSet_list, h2m);
			
			geneSet_list = filterList(geneSet_list, allGeneName);
			
			listA = list2str(listA_list);
			
			geneSet = list2strV2(geneSet_list);
			
			System.out.println(generatePHeatmapScriptAnnotation(inputFile, outputFile, listA, geneSet, title, takeLog, geneSet_list.size(), col_cluster, row_cluster, width_size, height_size, row_font_size, col_font_size, colorType, annotation));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}		
	}
	
	public static void executePHeatAnnotationNoNorm(String[] args) {
		try {

			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\phospho_peptide_raw_data_GBM_Name_Collapse.txt";
			String listAFile = args[1]; //"";
			String geneSetFile = args[2]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangMouseGeneListAnalysis.txt";
			String outputFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\Phospho_peptide_GBM_Genes.png";
			boolean row_cluster = true;
			boolean col_cluster = true;
			String width_size = "800";
			String height_size = "1200";
			String title = "";
			String takeLogStr = "FALSE";
			String row_font_size = "11";
			String col_font_size = "11";
			String annotation = "";
			int colorType = 0;
			if (args.length > 4) { 			
				title = args[4];
				if (args[5].toUpperCase().equals("TRUE")) {
					row_cluster = true;
				} else {
					row_cluster = false;
				}
				if (args[6].toUpperCase().equals("TRUE")) {
					col_cluster = true;
				} else {
					col_cluster = false;
				}
				
				
				
				if (args.length > 7) {
					if (args[7].toUpperCase().equals("TRUE")) {
						takeLogStr = "TRUE";
					} else {
						takeLogStr = "FALSE";
					}
					width_size = args[8];
					height_size = args[9];
				}
				if (args.length > 10) {
					row_font_size = args[10];
					col_font_size = args[11];
				}
				if (args.length > 12) {
					colorType = new Integer(args[12]);
				}
				if (args.length > 13) {
					annotation = args[13];
				}
			}
			
			String futureParameter = ""; // this parameter contains the specification of the heatmap
			//String h2mFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			
			//String takeLogStr = args[5];
			
			
			boolean takeLog = false;
			
			if (takeLogStr.toUpperCase().equals("TRUE")) {			
				takeLog = true;			
			}
			
			String listA = ""; //"'p1a','p1b','P2','P3','N1','N2','N3','Ctl1','Ctl2','Ctl3'";
			String geneSet = ""; //"\"Pdgfra\", \"Kit\", \"Met\", \"Egfr\", \"Igf1r\", \"Braf\", \"Nras\", \"Kras\", \"Pik3ca\", \"Mycn\", \"Myc\", \"Mdm2\", \"Mdm4\", \"Cdk4\", \"Cdk6\", \"Ccnd2\", \"Nf1\", \"Pten\", \"Trp53\", \"Cdkn2a\", \"Cdkn2b\", \"Cdkn2c\", \"Rb1\"";
			
			//HashMap h2m = human2mouse(h2mFile);
			
			LinkedList listA_list = new LinkedList();
			
			if (!(listAFile.equals("") || listAFile.toUpperCase().equals("NA"))) {
				listA_list = readGroupList(listAFile);
			}
			
			LinkedList geneSet_list = readGroupList(geneSetFile);
			
			LinkedList allGeneName = readGeneNames(inputFile);
						
			//geneSet_list = convertH2MGene(geneSet_list, h2m);
			
			geneSet_list = filterList(geneSet_list, allGeneName);
			
			listA = list2str(listA_list);
			
			geneSet = list2strV2(geneSet_list);
			
			System.out.println(generatePHeatmapScriptAnnotationNoNorm(inputFile, outputFile, listA, geneSet, title, takeLog, geneSet_list.size(), col_cluster, row_cluster, width_size, height_size, row_font_size, col_font_size, colorType, annotation));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}		
	}	
	public static void execute(String[] args) {
		try {

			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\phospho_peptide_raw_data_GBM_Name_Collapse.txt";
			String listAFile = args[1]; //"";
			String geneSetFile = args[2]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangMouseGeneListAnalysis.txt";
			String outputFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\Phospho_peptide_GBM_Genes.png";
			boolean row_cluster = true;
			boolean col_cluster = true;
			String title = "";
			if (args.length > 4) { 			
				title = args[4];
				if (args[5].toUpperCase().equals("TRUE")) {
					row_cluster = true;
				} else {
					row_cluster = false;
				}
				if (args[6].toUpperCase().equals("TRUE")) {
					col_cluster = true;
				} else {
					col_cluster = false;
				}
				
			}
			
			String futureParameter = ""; // this parameter contains the specification of the heatmap
			//String h2mFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			
			//String takeLogStr = args[5];
			
			String takeLogStr = "FALSE";
			
			boolean takeLog = false;
			
			if (takeLogStr.toUpperCase().equals("TRUE")) {			
				takeLog = true;			
			}
			
			String listA = ""; //"'p1a','p1b','P2','P3','N1','N2','N3','Ctl1','Ctl2','Ctl3'";
			String geneSet = ""; //"\"Pdgfra\", \"Kit\", \"Met\", \"Egfr\", \"Igf1r\", \"Braf\", \"Nras\", \"Kras\", \"Pik3ca\", \"Mycn\", \"Myc\", \"Mdm2\", \"Mdm4\", \"Cdk4\", \"Cdk6\", \"Ccnd2\", \"Nf1\", \"Pten\", \"Trp53\", \"Cdkn2a\", \"Cdkn2b\", \"Cdkn2c\", \"Rb1\"";
			
			//HashMap h2m = human2mouse(h2mFile);
			
			LinkedList listA_list = new LinkedList();
			
			if (!(listAFile.equals("") || listAFile.toUpperCase().equals("NA"))) {
				listA_list = readGroupList(listAFile);
			}
			
			LinkedList geneSet_list = readGroupList(geneSetFile);
			
			LinkedList allGeneName = readGeneNames(inputFile);
						
			//geneSet_list = convertH2MGene(geneSet_list, h2m);
			
			geneSet_list = filterList(geneSet_list, allGeneName);
			
			listA = list2str(listA_list);
			
			geneSet = list2strV2(geneSet_list);
			
			System.out.println(generateHeatmapScript(inputFile, outputFile, listA, geneSet, title, takeLog, geneSet_list.size(), true, true));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}		
	}
	public static void GangGBMGeneListDataAnalysisPhospho(String[] args) {
		try {

			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\phospho_peptide_raw_data_GBM_Name_Collapse.txt";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\Phospho_peptide_GBM_Genes.png";
			String listAFile = "";
			String geneSetFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangMouseGeneListAnalysis.txt";
			String h2mFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			
			//String takeLogStr = args[5];
			
			String takeLogStr = "FALSE";
			
			boolean takeLog = false;
			
			if (takeLogStr.toUpperCase().equals("TRUE")) {			
				takeLog = true;			
			}
			
			String listA = "'p1a','p1b','P2','P3','N1','N2','N3','Ctl1','Ctl2','Ctl3'";
			String geneSet = "\"Pdgfra\", \"Kit\", \"Met\", \"Egfr\", \"Igf1r\", \"Braf\", \"Nras\", \"Kras\", \"Pik3ca\", \"Mycn\", \"Myc\", \"Mdm2\", \"Mdm4\", \"Cdk4\", \"Cdk6\", \"Ccnd2\", \"Nf1\", \"Pten\", \"Trp53\", \"Cdkn2a\", \"Cdkn2b\", \"Cdkn2c\", \"Rb1\"";
			
			HashMap h2m = human2mouse(h2mFile);
			
			LinkedList listA_list = new LinkedList();
			
			if (!listAFile.equals("")) {
				listA_list = readGroupList(listAFile);
			}
			
			LinkedList geneSet_list = readGroupList(geneSetFile);
			
			LinkedList allGeneName = readGeneNames(inputFile);
						
			//geneSet_list = convertH2MGene(geneSet_list, h2m);
			
			geneSet_list = filterList(geneSet_list, allGeneName);
			
			listA = list2str(listA_list);
			
			geneSet = list2str(geneSet_list);
			
			System.out.println(generateHeatmapScript(inputFile, outputFile, listA, geneSet, "", takeLog, geneSet_list.size(), true, true));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
	}
	

	public static void GangGBMGeneListDataAnalysisProtein(String[] args) {
		try {

			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\Total_Proteome_GeneName_Collapse_GBM_Genes.txt";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\Total_Proteome_GeneName_Collapse_GBM_Genes.png";
			String listAFile = "";
			String geneSetFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangMouseGeneListAnalysis.txt";
			String h2mFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			
			//String takeLogStr = args[5];
			
			String takeLogStr = "FALSE";
			
			boolean takeLog = false;
			
			if (takeLogStr.toUpperCase().equals("TRUE")) {			
				takeLog = true;			
			}
			
			String listA = "'p1a','p1b','P2','P3','N1','N2','N3','Ctl1','Ctl2','Ctl3'";
			String geneSet = "\"Pdgfra\", \"Kit\", \"Met\", \"Egfr\", \"Igf1r\", \"Braf\", \"Nras\", \"Kras\", \"Pik3ca\", \"Mycn\", \"Myc\", \"Mdm2\", \"Mdm4\", \"Cdk4\", \"Cdk6\", \"Ccnd2\", \"Nf1\", \"Pten\", \"Trp53\", \"Cdkn2a\", \"Cdkn2b\", \"Cdkn2c\", \"Rb1\"";
			
			HashMap h2m = human2mouse(h2mFile);
			
			LinkedList listA_list = new LinkedList();
			
			if (!listAFile.equals("")) {
				listA_list = readGroupList(listAFile);
			}
			
			LinkedList geneSet_list = readGroupList(geneSetFile);
			
			LinkedList allGeneName = readGeneNames(inputFile);
						
			//geneSet_list = convertH2MGene(geneSet_list, h2m);
			
			geneSet_list = filterList(geneSet_list, allGeneName);
			
			listA = list2str(listA_list);
			
			geneSet = list2str(geneSet_list);
			
			System.out.println(generateHeatmapScript(inputFile, outputFile, listA, geneSet, "Title", takeLog, geneSet_list.size(), true, true));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
	}
	public static LinkedList filterList(LinkedList geneSet, LinkedList allGene) {
		LinkedList list = new LinkedList();
		Iterator itr = geneSet.iterator();
		while (itr.hasNext()) {
			String key = (String)itr.next();
			if (allGene.contains(key)) {
				list.add(key);
			}
		}
		return list;
	}
	public static LinkedList convertH2MGene(LinkedList list, HashMap human2mouse) {
		LinkedList newList = new LinkedList();
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String humanGene = (String)itr.next();
			if (human2mouse.containsKey(humanGene)) {
				newList.add(human2mouse.get(humanGene));
			} else {
				newList.add(humanGene);
			}
		}
		return newList;
	}
	public static HashMap human2mouse(String hs_mm_homo_r66) {
		HashMap human2mouse = new HashMap();
		try {
			
			String fileName = hs_mm_homo_r66;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				human2mouse.put(split[0], split[1]);
			}
			in.close();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return human2mouse;
	}
	public static HashMap mouse2human(String hs_mm_homo_r66) {
		HashMap mouse2human = new HashMap();
		try {
			
			String fileName = hs_mm_homo_r66;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				mouse2human.put(split[1], split[0]);
			}
			in.close();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mouse2human;
	}
	public static String list2strV2(LinkedList list) {
		LinkedList groupA_list = new LinkedList();
		//String script = "";
		StringBuffer str_buffer = new StringBuffer();
		int count = 1;
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String str = (String)itr.next();
			
			if (count == 1) {
				str_buffer.append("geneList = \"" + str + "\";\n");
				//script += "geneList = \"" + str + "\";\n";
			} else {
				str_buffer.append("geneList = c(geneList, \"" + str + "\");\n");
				//script += "geneList = c(geneList, \"" + str + "\");\n";
			}
			count++;
		}
		return str_buffer.toString();
	}
	public static String list2str(LinkedList list) {
		LinkedList groupA_list = new LinkedList();
		String groupA = "";
		boolean first = true;
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String str = (String)itr.next();
			str = str.replaceAll("-", ".");
			if (first) {
				groupA += "'" + str + "'";					
				first = false;
				
			} else {
				groupA += ",'" + str + "'";
				
			}
		}
		return groupA;
	}
	public static LinkedList readGeneNames(String inputFile) {
		LinkedList list = new LinkedList();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (!split[0].equals("null")) {
					if (!list.contains(split[0])) {
						list.add(split[0]);
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	public static LinkedList readGroupList(String groupFile) {
		LinkedList groupA_list = new LinkedList();
		String groupA = "";
		String groupOther = "";
		try {

			boolean first = true;
			FileInputStream fstream = new FileInputStream(groupFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine().trim();
				if (first) {
					groupA += "'" + str + "'";					
					first = false;
					if (!groupA_list.contains(str)) {
						groupA_list.add(str);
					}
				} else {
					groupA += ",'" + str + "'";
					if (!groupA_list.contains(str)) {
						groupA_list.add(str);
					}
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupA_list;
	}
	public static LinkedList separateGroup(String fileName, LinkedList list) {
		LinkedList groupB = new LinkedList();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String tag = in.readLine();
			String[] split = tag.split("\t");
			for (int i = 1; i < split.length; i++) {
				String str = split[i];
				if (!list.contains(str)) {
					groupB.add(str);
				}
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupB;
	}
	
	public static String grabCluster() {
		String script = "";
		script += "clust <- cbind(dataset, cluster = cutree(hm$tree_row, k = 4))\n";
		script += "cluster1 = which(clust[,\"cluster\"] == 4)\n";
		script += "dataset2 = data.matrix(selection[rownames(as.matrix(cluster1)),labels])\n";
		script += "png(file = \"SJMMNORM_Youngdon_All_20160405_pheatmap_cluster.png\", width=800,height=1400)\n";
		script += "pheatmap(dataset2, cluster_col = T, cluster_row = T, fontsize_row = 13, show_rownames = T, color=hmcols)\n";
		script += "dev.off();\n";
		script += "write.table(allDat[rownames(as.matrix(cluster1)),hm$tree_col$labels], \"Cluster1.txt\", sep = \"\\t\");\n";
		return script;
	}
	/**
	 * Generate pheatmap plots
	 */
	public static String generatePHeatmapScript(String inputFile, String outputFile, String listA, String geneSet, String title, boolean log, int size, boolean col_cluster, boolean row_cluster, String width_size, String height_size, String row_font_size, String col_font_size, int colorType) {
		String script = "";
		script += "options(bitmapType='cairo')\n";
		script += "allDat = read.table(\"" + inputFile.replaceAll("\\\\", "/") + "\", header=TRUE, row.names=1 ,sep=\"\\t\");\n";
		script += "selection = allDat;\n";
		script += "rownames = rownames(selection);\n";
		if (!listA.equals("")) {
			script += "A = c(" + listA + ")\n";
			script += "col_labels = A;\n";
		} else {
			script += "col_labels = colnames(selection);\n";
		}
		
		
		//script += "sampleLocation = col_labels;\n";
		//script += "sampleNames = col_labels[sampleLocation];\n";
		
		//script += "sampleNames = col_labels;\n";
		//script += "labels = sampleNames;\n";
		//script += "colnames(selection) = col_labels;\n";
		//script += "sampleLocation = col_labels;\n";
		//script += "sampleNames = col_labels[sampleLocation];\n";
		
		//script += "sampleNames = col_labels;\n";
		script += "labels = col_labels;\n";
		//script += "colnames(selection) = col_labels;\n";

		if (log) {
			script += "selection = log2(selection[labels] + 0.1);\n";
		}

		script += "rows = length(selection[,1]);\n";

		script += "zselection = apply(selection, 1, scale);\n";

		script += "zselection = apply(zselection, 1, rev)\n";

		script += "colnames(zselection) = names(selection)\n";
		
		
		script += "selection = as.matrix(zselection);\n";
		//script += "rownames(selection) = rownames;\n";
		script += "selection[is.na(selection)] = 0;\n";
		
		script += geneSet;
		
		script += "dataset = selection[geneList,]\n";
		
		script += "dataset = dataset[apply(dataset[,-1], 1, function(x) !all(x==0)),]\n";
		script += "library(pheatmap)\n";
		script += "minimum = -3;\n";
		script += "maximum = 3;\n";
		script += "if (abs(min(dataset)) > abs(max(dataset))) {\n";
		script += "dataset[dataset < -abs(max(dataset))] = -abs(max(dataset))\n";
		script += "} else {\n";
		script += "dataset[dataset > abs(min(dataset))] = abs(min(dataset))\n";
		script += "}\n";
		script += "bk = c(seq(minimum,minimum/2, length=100), seq(minimum/2,maximum/2,length=100),seq(maximum/2,maximum,length=100))\n";
		if (colorType == 0) {
			script += "hmcols<- colorRampPalette(c(\"dark blue\",\"blue\",\"white\",\"red\", \"dark red\"))(length(bk)-1)\n";
		} else if (colorType == 1) {
			script += "hmcols<- colorRampPalette(c(\"#34c5fd\",\"black\",\"red\"))(length(bk)-1)\n";
		} else if (colorType == 2) {
			script += "hmcols<- colorRampPalette(c(\"green\",\"black\",\"red\"))(length(bk)-1)\n";
		}
		
		script += "png(file = \"" + outputFile + "\", width=" + width_size + ",height=" + height_size + ")\n";
		String cluster_col = "T";
		String cluster_row = "T";
		if (!col_cluster) {
			cluster_col = "F";
		}
		if (!row_cluster) {
			cluster_row = "F";
		}
		// 2018/10/18 updated from complete to ward
		
		String append_show_row = "show_rownames = T";
		String append_show_col = "show_colnames = T";
		if (new Double(row_font_size) == 0.0) {
			row_font_size = "12";
			append_show_row = "show_rownames = F";
		}
		if (new Double(col_font_size) == 0.0) {
			col_font_size = "12";
			append_show_col = "show_colnames = F";
		}
		
		script += "result2 = pheatmap(dataset, cluster_col = " + cluster_col + ", cluster_row = " + cluster_row + ", fontsize_row = " + row_font_size + ", fontsize_col = " + col_font_size + ", " + append_show_row + " , " + append_show_col + ", clustering_method = \"ward\", color=hmcols)\n";
		script += "dev.off();\n";
		//script += "clust <- cbind(result2, cluster = cutree(result2$tree_row, k = 10))\n";
		script += "write.table(result2$tree_col$labels[result2$tree_col$order], file=\"" + outputFile + "._ordered_colnames.txt\")\n";
		script += "write.table(result2$tree_row$labels[result2$tree_row$order], file=\"" + outputFile + "._ordered_row.txt\")\n";
		return script;
	}
	
	/**
	 * Generate pheatmap annotation
	 */
	public static String generatePHeatmapScriptAnnotation(String inputFile, String outputFile, String listA, String geneSet, String title, boolean log, int size, boolean col_cluster, boolean row_cluster, String width_size, String height_size, String row_font_size, String col_font_size, int colorType, String annotation) {
		String script = "";
		script += "options(bitmapType='cairo')\n";
		script += "allDat = read.table(\"" + inputFile.replaceAll("\\\\", "/") + "\", header=TRUE, row.names=1 ,sep=\"\\t\");\n";
		script += "selection = allDat;\n";
		script += "rownames = rownames(selection);\n";
		if (!listA.equals("")) {
			script += "A = c(" + listA + ")\n";
			script += "col_labels = A;\n";
		} else {
			script += "col_labels = colnames(selection);\n";
		}
		
		
		//script += "sampleLocation = col_labels;\n";
		//script += "sampleNames = col_labels[sampleLocation];\n";

		//script += "sampleLocation = col_labels;\n";
		//script += "sampleNames = col_labels[sampleLocation];\n";
		
		//script += "sampleNames = col_labels;\n";
		script += "labels = col_labels;\n";
		//script += "colnames(selection) = col_labels;\n";

		if (log) {
			script += "selection = log2(selection[labels] + 0.1);\n";
		}

		script += "rows = length(selection[,1]);\n";

		script += "zselection = apply(selection, 1, scale);\n";

		script += "zselection = apply(zselection, 1, rev)\n";

		script += "colnames(zselection) = names(selection)\n";
		
		
		script += "selection = as.matrix(zselection);\n";
		//script += "rownames(selection) = rownames;\n";
		script += "selection[is.na(selection)] = 0;\n";
		
		script += geneSet;
		
		script += "dataset = selection[geneList,]\n";
		
		script += "dataset = dataset[apply(dataset[,-1], 1, function(x) !all(x==0)),]\n";
		script += "library(pheatmap)\n";
		script += "minimum = -3;\n";
		script += "maximum = 3;\n";
		script += "if (abs(min(dataset)) > abs(max(dataset))) {\n";
		script += "dataset[dataset < -abs(max(dataset))] = -abs(max(dataset))\n";
		script += "} else {\n";
		script += "dataset[dataset > abs(min(dataset))] = abs(min(dataset))\n";
		script += "}\n";
		script += "annotation = read.csv(file=\"" + annotation + "\",head=TRUE,sep=\"\\t\",stringsAsFactors=F,row.names = 1)\n";
		
		script += "bk = c(seq(minimum,minimum/2, length=100), seq(minimum/2,maximum/2,length=100),seq(maximum/2,maximum,length=100))\n";
		if (colorType == 0) {
			script += "hmcols<- colorRampPalette(c(\"dark blue\",\"blue\",\"white\",\"red\", \"dark red\"))(length(bk)-1)\n";
		} else if (colorType == 1) {
			script += "hmcols<- colorRampPalette(c(\"#34c5fd\",\"black\",\"red\"))(length(bk)-1)\n";
		} else if (colorType == 2) {
			script += "hmcols<- colorRampPalette(c(\"green\",\"black\",\"red\"))(length(bk)-1)\n";
		}
		
		script += "png(file = \"" + outputFile + "\", width=" + width_size + ",height=" + height_size + ")\n";
		String cluster_col = "T";
		String cluster_row = "T";
		if (!col_cluster) {
			cluster_col = "F";
		}
		if (!row_cluster) {
			cluster_row = "F";
		}
		// 2018/10/18 updated from complete to ward
		String append_show_row = "show_rownames = T";
		String append_show_col = "show_colnames = T";
		if (new Double(row_font_size) == 0.0) {
			row_font_size = "12";
			append_show_row = "show_rownames = F";
		}
		if (new Double(col_font_size) == 0.0) {
			col_font_size = "12";
			append_show_col = "show_colnames = F";
		}
		script += "result2 = pheatmap(dataset, cluster_col = " + cluster_col + ", cluster_row = " + cluster_row + ", fontsize_row = " + row_font_size + ", fontsize_col = " + col_font_size + ", " + append_show_row + " , " + append_show_col + " , clustering_method = \"ward\", color=hmcols, annotation_col=annotation)\n";
		script += "dev.off();\n";
		//script += "clust <- cbind(result2, cluster = cutree(result2$tree_row, k = 10))\n";
		script += "write.table(result2$tree_col$labels[result2$tree_col$order], file=\"" + outputFile + "._ordered_colnames.txt\")\n";
		script += "write.table(result2$tree_row$labels[result2$tree_row$order], file=\"" + outputFile + "._ordered_row.txt\")\n";
		return script;
	}
	
	/**
	 * Generate pheatmap annotation
	 */
	public static String generatePHeatmapScriptAnnotationNoNorm(String inputFile, String outputFile, String listA, String geneSet, String title, boolean log, int size, boolean col_cluster, boolean row_cluster, String width_size, String height_size, String row_font_size, String col_font_size, int colorType, String annotation) {
		String script = "";
		script += "options(bitmapType='cairo')\n";
		script += "allDat = read.table(\"" + inputFile.replaceAll("\\\\", "/") + "\", header=TRUE, row.names=1 ,sep=\"\\t\");\n";
		script += "selection = allDat;\n";
		script += "rownames = rownames(selection);\n";
		if (!listA.equals("")) {
			script += "A = c(" + listA + ")\n";
			script += "col_labels = A;\n";
		} else {
			script += "col_labels = colnames(selection);\n";
		}
		
		
		//script += "sampleLocation = col_labels;\n";
		//script += "sampleNames = col_labels[sampleLocation];\n";
		
		//script += "sampleNames = col_labels;\n";
		script += "labels = col_labels;\n";
		//script += "colnames(selection) = col_labels;\n";

		if (log) {
			script += "selection = log2(selection[labels] + 1.0);\n";
		}

		//script += "rows = length(selection[,1]);\n";

		//script += "zselection = apply(selection, 1, scale);\n";

		//script += "zselection = apply(zselection, 1, rev)\n";

		script += "zselection = selection\n";
		script += "colnames(zselection) = names(selection)\n";
		
		
		script += "selection = as.matrix(zselection);\n";
		//script += "rownames(selection) = rownames;\n";
		script += "selection[is.na(selection)] = 0;\n";
		
		script += geneSet;
		
		script += "dataset = selection[geneList,]\n";
		
		script += "dataset = dataset[apply(dataset[,-1], 1, function(x) !all(x==0)),]\n";
		script += "library(pheatmap)\n";
		script += "minimum = min(dataset);\n";
		script += "maximum = max(dataset);\n";
		//script += "if (abs(min(dataset)) > abs(max(dataset))) {\n";
		//script += "dataset[dataset < -abs(max(dataset))] = -abs(max(dataset))\n";
		//script += "} else {\n";
		//script += "dataset[dataset > abs(min(dataset))] = abs(min(dataset))\n";
		//script += "}\n";
		script += "annotation = read.csv(file=\"" + annotation + "\",head=TRUE,sep=\"\\t\",stringsAsFactors=F,row.names = 1)\n";
		
		script += "bk = c(seq(minimum,minimum/2, length=100), seq(minimum/2,maximum/2,length=100),seq(maximum/2,maximum,length=100))\n";
		if (colorType == 0) {
			script += "hmcols<- colorRampPalette(c(\"white\",\"blue\",\"red\"))(length(bk)-1)\n";
		} else if (colorType == 1) {
			script += "hmcols<- colorRampPalette(c(\"white\",\"#34c5fd\",\"red\"))(length(bk)-1)\n";
		} else if (colorType == 2) {
			script += "hmcols<- colorRampPalette(c(\"white\",\"green\"\"red\"))(length(bk)-1)\n";
		}
		
		script += "png(file = \"" + outputFile + "\", width=" + width_size + ",height=" + height_size + ")\n";
		String cluster_col = "T";
		String cluster_row = "T";
		if (!col_cluster) {
			cluster_col = "F";
		}
		if (!row_cluster) {
			cluster_row = "F";
		}
		// 2018/10/18 updated from complete to ward
		String append_show_row = "show_rownames = T";
		String append_show_col = "show_colnames = T";
		if (new Double(row_font_size) == 0.0) {
			row_font_size = "12";
			append_show_row = "show_rownames = F";
		}
		if (new Double(col_font_size) == 0.0) {
			col_font_size = "12";
			append_show_col = "show_colnames = F";
		}
		script += "result2 = pheatmap(dataset, cluster_col = " + cluster_col + ", cluster_row = " + cluster_row + ", fontsize_row = " + row_font_size + ", fontsize_col = " + col_font_size + ", " + append_show_row + " , " + append_show_col + " , clustering_method = \"ward\", color=hmcols, annotation_col=annotation)\n";
		script += "dev.off();\n";
		//script += "clust <- cbind(result2, cluster = cutree(result2$tree_row, k = 10))\n";
		script += "write.table(result2$tree_col$labels[result2$tree_col$order], file=\"" + outputFile + "._ordered_colnames.txt\")\n";
		script += "write.table(result2$tree_row$labels[result2$tree_row$order], file=\"" + outputFile + "._ordered_row.txt\")\n";
		return script;
	}
	/**
	 * Generate Heatmap3 plots
	 */
	public static String generateHeatmapScript(String inputFile, String outputFile, String listA, String geneSet, String title, boolean log, int size, boolean col_cluster, boolean row_cluster) {
		String script = "";
		script += "options(bitmapType='cairo')\n";
		script += "allDat = read.table(\"" + inputFile.replaceAll("\\\\", "/") + "\", header=TRUE, row.names=1,sep=\"\\t\" );\n";
		script += "selection = allDat;\n";
		if (!listA.equals("")) {
			script += "A = c(" + listA + ")\n";
			script += "col_labels = A;\n";
		} else {
			script += "col_labels = colnames(selection);\n";
		}

		script += "sampleLocation = col_labels;\n";
		//script += "sampleNames = col_labels[sampleLocation];\n";
		script += "sampleNames = col_labels;\n";
		script += "labels = sampleNames;\n";
		script += "colnames(selection) = col_labels;\n";

		if (log) {
			script += "selection = log2(selection[labels] + 0.1);\n";
		}

		script += "rows = length(selection[,1]);\n";

		script += "zselection = apply(selection, 1, scale);\n";

		script += "zselection = apply(zselection, 1, rev)\n";

		script += "colnames(zselection) = names(selection)\n";

		script += "selection = as.matrix(zselection);\n";
		
		script += geneSet;
		//script += "geneList = c(" + geneSet + ");\n";
		
		//script += "library(gplots);\n";

		
		//script += "source(\"" + CONFIG.HEATMAP3_PATH.replaceAll("\\\\", "/") + "\");\n";
		script += generate_heatmap3_script();
		
		script += "myBreaks=seq(-2.45,2.45,0.065);\n";

		script += "hmcols<- colorRampPalette(c(\"blue\", \"white\",\"red\"))(length(myBreaks)-1)\n";

		script += "color.sample.row <- function(Target) {\n";
		script += "if (length(grep(\"ATMTDP1\", Target))) {\"red\";}\n";
		script += "else if (length(grep(\"Pot\", Target))) {\"blue\";}\n";
		script += "else if (length(grep(\"Lig4\", Target))) {\"green\";}\n";
		script += "else {\"green\";}\n";
		script += "}\n";

		script += "sample_col = unlist(lapply(as.matrix(sampleNames), color.sample.row))\n";

		/*script += "ATMTDP1 = grep(\"ATMTDP1\", col_labels);\n";
		script += "Pot = grep(\"Pot\", col_labels);\n";
		script += "BRCA2 = grep(\"BRCA2\", col_labels);\n";
		script += "Lig4 = grep(\"Lig4\", col_labels);\n";*/
		
		
		script += "colSideColors = cbind(sample_col);\n";
		int height = 1000 + size * 10;
		script += "png(file = \"" + outputFile.replaceAll("\\\\", "/") + "\", width=1000,height=" + height + ")\n";
		
		String row_cluster_flag = "F";		
		String col_cluster_flag = "F";
		if (row_cluster) {
			row_cluster_flag = "T";
		} else {
			col_cluster_flag = "T";
		}
		script += "result2 = heatmap.3(selection[geneList,], col=hmcols, breaks = myBreaks, rowsep = 0, scale=\"none\", dendrogram=\"both\", Rowv = " + row_cluster_flag + ", Colv = " + col_cluster_flag + ",\n";
		script += "key=TRUE, keysize=0.5,symkey=FALSE, density.info=\"none\", margins=c(30,10), trace=\"none\", cexRow=1.2, cexCol=1.5, main=\"" + title + "\", NumColSideColors= 1.0)\n"; //ColSideColors=colSideColors
		script += "dev.off();\n";
		
		script += "write(colnames(selection[geneList,])[result2$colInd], file=\"" + outputFile.replaceAll("\\\\", "/") + "_ordered_colnames.txt\")\n";
		script += "write(rownames(selection[geneList,])[result2$rowInd], file=\"" + outputFile.replaceAll("\\\\", "/") + "_ordered_rownames.txt\")\n";
		return script;
	}
	
	public static String generateColorInfo(LinkedList list) {
		String result = "";
		String colSideColors = "colSideColors = cbind(";
		int count = 0;
		String type = "type" + count;
		
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String meta = (String)itr.next();
			
			count++;
			
			String typeTag = meta.split(":")[0].split(",")[0];
			type = typeTag + "_ID" + count;
			
			result += "color." + type + ".row <- function(Target) {\n";
			
			if (meta.equals("") || meta.equals("NA")) {
				return "";
			}
			String[] criterias = meta.split(":");
			int index = 1;
			
			for (String criteria: criterias) {
				String[] split = criteria.split(",");
				String tag = split[0];
				String color = split[1];
				if (tag.equals("NA")) {				
					if (index == 1) {
						result += "\"" + color + "\"";
					} else {
						result += "else {\"" + color + "\";}\n";
					}
				} else {				
					if (index == 1) {
						result += "if (length(grep(\"" + tag + "\", Target))) {\"" + color + "\";}\n"; 
					} else {
						result += "else if (length(grep(\"" + tag + "\", Target))) {\"" + color + "\";}\n";
					}
				}
				index++;
			}
			result += "}\n";
			result += type + "_col = unlist(lapply(as.matrix(sampleNames), color." + type + ".row))\n";
			if (count == 1) {
				colSideColors += type + "_col";
			} else {
				colSideColors += "," + type + "_col";
			}
		}
		result += colSideColors + ")\n";
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static String generateColorInfo(String meta) {
		String result = "color.sample.row <- function(Target) {\n";
		
		if (meta.equals("") || meta.equals("NA")) {
			return "";
		}
		
		
		String[] criterias = meta.split(":");
		int index = 1;
		for (String criteria: criterias) {
			String[] split = criteria.split(",");
			String tag = split[0];
			String color = split[1];
			if (tag.equals("NA")) {				
				if (index == 1) {
					result += "\"" + color + "\"";
				} else {
					result += "else {\"" + color + "\";}\n";
				}
			} else {				
				if (index == 1) {
					result += "if (length(grep(\"" + tag + "\", Target))) {\"" + color + "\";}\n"; 
				} else {
					result += "else if (length(grep(\"" + tag + "\", Target))) {\"" + color + "\";}\n";
				}
			}
			index++;
		}
		result += "}\n";
		result += "sample_col = unlist(lapply(as.matrix(sampleNames), color.sample.row))\n";
		
		result += "colSideColors = cbind(sample_col);\n";
		return result;
	}
	
	
	public static String generatePHeatmapMAD(String inputFile, String outputFile, String listA, String title, int topCount) {
		String script = "";
		script += "options(bitmapType='cairo')\n";
		script += "allDat = read.table(\"" + inputFile + "\", header=TRUE, row.names=1,sep=\"\\t\" ,);\n";
		script += "selection = allDat;\n";
		
		script += "A = c(" + listA + ")\n";
		script += "col_labels = A;\n";
		//script += "sampleLocation = grep("SJMMHGG", col_labels);\n";
		script += "sampleLocation = which(colnames(selection) == col_labels);\n";
		script += "isexpr <- rowSums(selection[,sampleLocation]>1) >= length(col_labels) / 3;\n";
		script += "selection = selection[isexpr,]\n";		
		script += "sampleNames = col_labels;\n";
		script += "geneName = rownames(selection)\n";
		script += "labels = sampleNames;\n";
		script += "colnames(selection) = col_labels;\n";
		script += "selection = log2(selection[labels] + 0.1);\n";
		script += "Mad = apply(selection, 1, mad);\n";
		script += "rows = length(selection[,1]);\n";
		script += "zselection = apply(selection, 1, scale);\n";
		script += "zselection = apply(zselection, 1, rev)\n";
		script += "colnames(zselection) = names(selection)\n";
		script += "selection = as.matrix(zselection);\n";		
		script += "selection = cbind(selection[geneName,], Mad)\n";
		script += "topNMad = head(sort(selection[,\"Mad\"],decreasing=TRUE), n = " + topCount + ")\n";
		script += "topN_Index = which(selection[,\"Mad\"] > min(topNMad))\n";
		script += "dataset = data.matrix(selection[topN_Index,labels])\n";
		
		script += "library(pheatmap)\n";
		script += "minimum = -3;\n";
		script += "maximum = 3;\n";
		
		script += "if (abs(min(dataset)) > abs(max(dataset))) {\n";
		script += "dataset[dataset < -abs(max(dataset))] = -abs(max(dataset))\n";
		script += "} else {\n";
		script += "dataset[dataset > abs(min(dataset))] = abs(min(dataset))\n";
		script += "}\n";
		
		script += "bk = c(seq(minimum,minimum/2, length=100), seq(minimum/2,maximum/2,length=100),seq(maximum/2,maximum,length=100))\n";
		script += "hmcols<- colorRampPalette(c(\"dark blue\",\"blue\",\"white\",\"red\", \"dark red\"))(length(bk)-1)\n";
		
		script += "png(file = \"" + outputFile + "\", width=1000,height=700)\n";
		script += "pheatmap(dataset, cluster_col = T, cluster_row = T, fontsize_row = 13, show_rownames = F, color=hmcols)\n";
		script += "dev.off();\n";
		return script;
	}
	/**
	 * Generate Heatmap3 plots
	 */
	public static String generateTopMADHeatmapScript(String inputFile, String outputFile, String listA, String title, int topCount, String meta) {
		String script = "";
		script += "options(bitmapType='cairo')\n";
		script += "allDat = read.table(\"" + inputFile + "\", header=TRUE, row.names=1,sep=\"\\t\" );\n";
		script += "selection = allDat;\n";
		
		script += "A = c(" + listA + ")\n";
		script += "col_labels = A;\n";
		//script += "sampleLocation = grep("SJMMHGG", col_labels);\n";
		script += "sampleLocation = which(colnames(selection) == col_labels);\n";
		script += "isexpr <- rowSums(selection[,sampleLocation]>1) >= length(col_labels) / 3;\n";
		script += "selection = selection[isexpr,]\n";		
		script += "sampleNames = col_labels;\n";
		script += "geneName = rownames(selection)\n";
		script += "labels = sampleNames;\n";
		script += "colnames(selection) = col_labels;\n";
		script += "selection = log2(selection[labels] + 0.1);\n";
		script += "Mad = apply(selection, 1, mad);\n";
		script += "rows = length(selection[,1]);\n";
		script += "zselection = apply(selection, 1, scale);\n";
		script += "zselection = apply(zselection, 1, rev)\n";
		script += "colnames(zselection) = names(selection)\n";
		script += "selection = as.matrix(zselection);\n";		
		script += "selection = cbind(selection[geneName,], Mad)\n";
		script += "topNMad = head(sort(selection[,\"Mad\"],decreasing=TRUE), n = " + topCount + ")\n";
		script += "topN_Index = which(selection[,\"Mad\"] > min(topNMad))\n";
		//script += "source(\"heatmap3.R\");\n";
		script += generate_heatmap3_script();
		
		script += "dataset = data.matrix(selection[topN_Index,labels])\n";
		script += "png(file = \"" + outputFile + "\", width=1000,height=800)\n";
		script += "myBreaks=seq(-2.45,2.45,0.065); \n";
		if (!meta.equals("NA")) {
			if (meta.contains(";")) {
				String[] split = meta.split(";");
				LinkedList list = new LinkedList();
				for (String stuff: split) {
					list.add(stuff);
				}
				script += generateColorInfo(list) + "\n";
			} else {
				script += generateColorInfo(meta) + "\n";
			}
		}
		script += "hmcols<- colorRampPalette(c(\"blue\", \"white\",\"red\"))(length(myBreaks)-1)\n";
		script += "result = heatmap.3(dataset, col=hmcols, breaks = myBreaks, rowsep = 0, scale=\"none\", labRow=c(\"\"), dendrogram=\"both\", Rowv = T, Colv = T,\n";
		if (meta.equals("NA")) {
			script += "key=TRUE, keysize=0.5,symkey=FALSE, density.info=\"none\", margins=c(30,10), trace=\"none\", cexRow=1.2, cexCol=1.5, main=\"" + title + "\", NumColSideColors= 1.0)\n";
		} else {
			script += "key=TRUE, keysize=0.5,symkey=FALSE, density.info=\"none\", margins=c(30,10), trace=\"none\", cexRow=1.2, cexCol=1.5, main=\"" + title + "\", NumColSideColors= 1.0, ColSideColors=colSideColors)\n";
		}
		
		script += "dev.off();\n";
		
		script += "write(colnames(dataset)[result$colInd], file=\"" + outputFile + "_ordered_colnames.txt\")\n";
		script += "write(rownames(dataset)[result$rowInd], file=\"" + outputFile + "_ordered_rownames.txt\")\n";
		return script;
	}
	
	public static String generate_heatmap3_script() {
		String script = "";
		script += "heatmap.3 <- function(x,\n";
		script += "Rowv = TRUE, Colv = if (symm) \"Rowv\" else TRUE,\n";
		script += "distfun = dist,\n";
		//script += "hclustfun = hclust,\n";
		script += "hclustfun = function(x) hclust( as.dist(x), method= \"ward\"),\n";
		script += "dendrogram = c(\"both\",\"row\", \"column\", \"none\"),\n";
		script += "symm = FALSE,\n";
		script += "scale = c(\"none\",\"row\", \"column\"),\n";
		script += "na.rm = TRUE,\n";
		script += "revC = identical(Colv,\"Rowv\"),\n";
		script += "add.expr,\n";
		script += "breaks,\n";
		script += "symbreaks = max(x < 0, na.rm = TRUE) || scale != \"none\",\n";
		script += "col = \"heat.colors\",\n";
		script += "colsep,\n";
		script += "rowsep,\n";
		script += "sepcolor = \"white\",\n";
		script += "sepwidth = c(0.05, 0.05),\n";
		script += "cellnote,\n";
		script += "notecex = 1,\n";
		script += "notecol = \"cyan\",\n";
		script += "na.color = par(\"bg\"),\n";
		script += "trace = c(\"none\", \"column\",\"row\", \"both\"),\n";
		script += "tracecol = \"cyan\",\n";
		script += "hline = median(breaks),\n";
		script += "vline = median(breaks),\n";
		script += "linecol = tracecol,\n";
		script += "margins = c(5,5),\n";
		script += "ColSideColors,\n";
		script += "RowSideColors,\n";
		script += "side.height.fraction=0.3,\n";
		script += "cexRow = 0.2 + 1/log10(nr),\n";
		script += "cexCol = 0.2 + 1/log10(nc),\n";
		script += "labRow = NULL,\n";
		script += "labCol = NULL,\n";
		script += "key = TRUE,\n";
		script += "keysize = 1.5,\n";
		script += "density.info = c(\"none\", \"histogram\", \"density\"),\n";
		script += "denscol = tracecol,\n";
		script += "symkey = max(x < 0, na.rm = TRUE) || symbreaks,\n";
		script += "densadj = 0.25,\n";
		script += "main = NULL,\n";
		script += "xlab = NULL,\n";
		script += "ylab = NULL,\n";
		script += "lmat = NULL,\n";
		script += "lhei = NULL,\n";
		script += "lwid = NULL,\n";
		script += "NumColSideColors = 1,\n";
		script += "NumRowSideColors = 1,\n";
		script += "KeyValueName=\"Value\",...){\n";
		script += "\n";
		script += "invalid <- function (x) {\n";
		script += "if (missing(x) || is.null(x) || length(x) == 0)\n";
		script += "return(TRUE)\n";
		script += "if (is.list(x))\n";
		script += "return(all(sapply(x, invalid)))\n";
		script += "else if (is.vector(x))\n";
		script += "return(all(is.na(x)))\n";
		script += "else return(FALSE)\n";
		script += "}\n";
		script += "\n";
		script += "x <- as.matrix(x)\n";
		script += "scale01 <- function(x, low = min(x), high = max(x)) {\n";
		script += "x <- (x - low)/(high - low)\n";
		script += "x\n";
		script += "}\n";
		script += "retval <- list()\n";
		script += "scale <- if (symm && missing(scale))\n";
		script += "\"none\"\n";
		script += "else match.arg(scale)\n";
		script += "dendrogram <- match.arg(dendrogram)\n";
		script += "trace <- match.arg(trace)\n";
		script += "density.info <- match.arg(density.info)\n";
		script += "if (length(col) == 1 && is.character(col))\n";
		script += "col <- get(col, mode = \"function\")\n";
		script += "if (!missing(breaks) && (scale != \"none\"))\n";
		script += "warning(\"Using scale=row or scale=column when breaks are specified can produce unpredictable results.\", \"Please consider using only one or the other.\")\n";
		script += "if (is.null(Rowv) || is.na(Rowv))\n";
		script += "Rowv <- FALSE\n";
		script += "if (is.null(Colv) || is.na(Colv))\n";
		script += "Colv <- FALSE\n";
		script += "else if (Colv == \"Rowv\" && !isTRUE(Rowv))\n";
		script += "Colv <- FALSE\n";
		script += "if (length(di <- dim(x)) != 2 || !is.numeric(x))\n";
		script += "stop(\"'x' must be a numeric matrix\")\n";
		script += "nr <- di[1]\n";
		script += "nc <- di[2]\n";
		script += "if (nr <= 1 || nc <= 1)\n";
		script += "stop(\"'x' must have at least 2 rows and 2 columns\")\n";
		script += "if (!is.numeric(margins) || length(margins) != 2)\n";
		script += "stop(\"'margins' must be a numeric vector of length 2\")\n";
		script += "if (missing(cellnote))\n";
		script += "cellnote <- matrix(\"\", ncol = ncol(x), nrow = nrow(x))\n";
		script += "if (!inherits(Rowv, \"dendrogram\")) {\n";
		script += "if (((!isTRUE(Rowv)) || (is.null(Rowv))) && (dendrogram %in%\n";
		script += "c(\"both\", \"row\"))) {\n";
		script += "if (is.logical(Colv) && (Colv))\n";
		script += "dendrogram <- \"column\"\n";
		script += "else dedrogram <- \"none\"\n";
		script += "warning(\"Discrepancy: Rowv is FALSE, while dendrogram is '\",\n";
		script += "dendrogram, \"'. Omitting row dendogram.\")\n";
		script += "}\n";
		script += "}\n";
		script += "if (!inherits(Colv, \"dendrogram\")) {\n";
		script += "if (((!isTRUE(Colv)) || (is.null(Colv))) && (dendrogram %in%\n";
		script += "c(\"both\", \"column\"))) {\n";
		script += "if (is.logical(Rowv) && (Rowv))\n";
		script += "dendrogram <- \"row\"\n";
		script += "else dendrogram <- \"none\"\n";
		script += "warning(\"Discrepancy: Colv is FALSE, while dendrogram is '\",\n";
		script += "dendrogram, \"'. Omitting column dendogram.\")\n";
		script += "}\n";
		script += "}\n";
		script += "if (inherits(Rowv, \"dendrogram\")) {\n";
		script += "ddr <- Rowv\n";
		script += "rowInd <- order.dendrogram(ddr)\n";
		script += "}\n";
		script += "else if (is.integer(Rowv)) {\n";
		script += "hcr <- hclustfun(distfun(x))\n";
		script += "ddr <- as.dendrogram(hcr)\n";
		script += "ddr <- reorder(ddr, Rowv)\n";
		script += "rowInd <- order.dendrogram(ddr)\n";
		script += "if (nr != length(rowInd))\n";
		script += "stop(\"row dendrogram ordering gave index of wrong length\")\n";
		script += "}\n";
		script += "else if (isTRUE(Rowv)) {\n";
		script += "Rowv <- rowMeans(x, na.rm = na.rm)\n";
		script += "hcr <- hclustfun(distfun(x))\n";
		script += "ddr <- as.dendrogram(hcr)\n";
		script += "ddr <- reorder(ddr, Rowv)\n";
		script += "rowInd <- order.dendrogram(ddr)\n";
		script += "if (nr != length(rowInd))\n";
		script += "stop(\"row dendrogram ordering gave index of wrong length\")\n";
		script += "}\n";
		script += "else {\n";
		script += "rowInd <- nr:1\n";
		script += "}\n";
		script += "if (inherits(Colv, \"dendrogram\")) {\n";
		script += "ddc <- Colv\n";
		script += "colInd <- order.dendrogram(ddc)\n";
		script += "}\n";
		script += "else if (identical(Colv, \"Rowv\")) {\n";
		script += "if (nr != nc)\n";
		script += "stop(\"Colv = 'Rowv' but nrow(x) != ncol(x)\")\n";
		script += "if (exists(\"ddr\")) {\n";
		script += "ddc <- ddr\n";
		script += "colInd <- order.dendrogram(ddc)\n";
		script += "}\n";
		script += "else colInd <- rowInd\n";
		script += "}\n";
		script += "else if (is.integer(Colv)) {\n";
		script += "hcc <- hclustfun(distfun(if (symm)\n";
		script += "x\n";
		script += "else t(x)))\n";
		script += "ddc <- as.dendrogram(hcc)\n";
		script += "ddc <- reorder(ddc, Colv)\n";
		script += "colInd <- order.dendrogram(ddc)\n";
		script += "if (nc != length(colInd))\n";
		script += "stop(\"column dendrogram ordering gave index of wrong length\")\n";
		script += "}\n";
		script += "else if (isTRUE(Colv)) {\n";
		script += "Colv <- colMeans(x, na.rm = na.rm)\n";
		script += "hcc <- hclustfun(distfun(if (symm)\n";
		script += "x\n";
		script += "else t(x)))\n";
		script += "ddc <- as.dendrogram(hcc)\n";
		script += "ddc <- reorder(ddc, Colv)\n";
		script += "colInd <- order.dendrogram(ddc)\n";
		script += "if (nc != length(colInd))\n";
		script += "stop(\"column dendrogram ordering gave index of wrong length\")\n";
		script += "}\n";
		script += "else {\n";
		script += "colInd <- 1:nc\n";
		script += "}\n";
		script += "retval$rowInd <- rowInd\n";
		script += "retval$colInd <- colInd\n";
		script += "retval$call <- match.call()\n";
		script += "x <- x[rowInd, colInd]\n";
		script += "x.unscaled <- x\n";
		script += "cellnote <- cellnote[rowInd, colInd]\n";
		script += "if (is.null(labRow))\n";
		script += "labRow <- if (is.null(rownames(x)))\n";
		script += "(1:nr)[rowInd]\n";
		script += "else rownames(x)\n";
		script += "else labRow <- labRow[rowInd]\n";
		script += "if (is.null(labCol))\n";
		script += "labCol <- if (is.null(colnames(x)))\n";
		script += "(1:nc)[colInd]\n";
		script += "else colnames(x)\n";
		script += "else labCol <- labCol[colInd]\n";
		script += "if (scale == \"row\") {\n";
		script += "retval$rowMeans <- rm <- rowMeans(x, na.rm = na.rm)\n";
		script += "x <- sweep(x, 1, rm)\n";
		script += "retval$rowSDs <- sx <- apply(x, 1, sd, na.rm = na.rm)\n";
		script += "x <- sweep(x, 1, sx, \"/\")\n";
		script += "}\n";
		script += "else if (scale == \"column\") {\n";
		script += "retval$colMeans <- rm <- colMeans(x, na.rm = na.rm)\n";
		script += "x <- sweep(x, 2, rm)\n";
		script += "retval$colSDs <- sx <- apply(x, 2, sd, na.rm = na.rm)\n";
		script += "x <- sweep(x, 2, sx, \"/\")\n";
		script += "}\n";
		script += "if (missing(breaks) || is.null(breaks) || length(breaks) < 1) {\n";
		script += "if (missing(col) || is.function(col))\n";
		script += "breaks <- 16\n";
		script += "else breaks <- length(col) + 1\n";
		script += "}\n";
		script += "if (length(breaks) == 1) {\n";
		script += "if (!symbreaks)\n";
		script += "breaks <- seq(min(x, na.rm = na.rm), max(x, na.rm = na.rm),\n";
		script += "length = breaks)\n";
		script += "else {\n";
		script += "extreme <- max(abs(x), na.rm = TRUE)\n";
		script += "breaks <- seq(-extreme, extreme, length = breaks)\n";
		script += "}\n";
		script += "}\n";
		script += "nbr <- length(breaks)\n";
		script += "ncol <- length(breaks) - 1\n";
		script += "if (class(col) == \"function\")\n";
		script += "col <- col(ncol)\n";
		script += "min.breaks <- min(breaks)\n";
		script += "max.breaks <- max(breaks)\n";
		script += "x[x < min.breaks] <- min.breaks\n";
		script += "x[x > max.breaks] <- max.breaks\n";
		script += "if (missing(lhei) || is.null(lhei))\n";
		script += "#lhei <- c(keysize, 4) #changed to 3 by cqu changing the height of col color bar\n";
		script += "lhei <- c(keysize, 2)\n";
		script += "if (missing(lwid) || is.null(lwid))\n";
		script += "lwid <- c(keysize, 4)\n";
		script += "if (missing(lmat) || is.null(lmat)) {\n";
		script += "lmat <- rbind(4:3, 2:1)\n";
		script += "\n";
		script += "if (!missing(ColSideColors)) {\n";
		script += "#if (!is.matrix(ColSideColors))\n";
		script += "#stop(\"'ColSideColors' must be a matrix\")\n";
		script += "if (!is.character(ColSideColors) || nrow(ColSideColors) != nc)\n";
		script += "stop(\"'ColSideColors' must be a matrix of nrow(x) rows\")\n";
		script += "lmat <- rbind(lmat[1, ] + 1, c(NA, 1), lmat[2, ] + 1)\n";
		script += "#lhei <- c(lhei[1], 0.2, lhei[2])\n";
		script += "lhei=c(lhei[1], side.height.fraction*NumColSideColors, lhei[2])\n";
		script += "}\n";
		script += "\n";
		script += "if (!missing(RowSideColors)) {\n";
		script += "#if (!is.matrix(RowSideColors))\n";
		script += "#stop(\"'RowSideColors' must be a matrix\")\n";
		script += "if (!is.character(RowSideColors) || ncol(RowSideColors) != nr)\n";
		script += "stop(\"'RowSideColors' must be a matrix of ncol(x) columns\")\n";
		script += "lmat <- cbind(lmat[, 1] + 1, c(rep(NA, nrow(lmat) - 1), 1), lmat[,2] + 1)\n";
		script += "#lwid <- c(lwid[1], 0.2, lwid[2])\n";
		script += "lwid <- c(lwid[1], side.height.fraction*NumRowSideColors, lwid[2])\n";
		script += "}\n";
		script += "lmat[is.na(lmat)] <- 0\n";
		script += "}\n";
		script += "\n";
		script += "if (length(lhei) != nrow(lmat))\n";
		script += "stop(\"lhei must have length = nrow(lmat) = \", nrow(lmat))\n";
		script += "if (length(lwid) != ncol(lmat))\n";
		script += "stop(\"lwid must have length = ncol(lmat) =\", ncol(lmat))\n";
		script += "op <- par(no.readonly = TRUE)\n";
		script += "on.exit(par(op))\n";
		script += "\n";
		script += "layout(lmat, widths = lwid, heights = lhei, respect = FALSE)\n";
		script += "\n";
		script += "if (!missing(RowSideColors)) {\n";
		script += "if (!is.matrix(RowSideColors)){\n";
		script += "par(mar = c(margins[1], 0, 0, 0.5))\n";
		script += "image(rbind(1:nr), col = RowSideColors[rowInd], axes = FALSE)\n";
		script += "} else {\n";
		script += "#par(mar = c(margins[1], 0, 0, 0.5))\n";
		script += "#par(mar = c(margins[1], 0, 0, 0.5)) #cqu changed chaning the width of row color bar\n";
		script += "par(mar = c(margins[1], 10, 0, 0.5))\n";
		script += "rsc = t(RowSideColors[,rowInd, drop=F])\n";
		script += "rsc.colors = matrix()\n";
		script += "rsc.names = names(table(rsc))\n";
		script += "rsc.i = 1\n";
		script += "for (rsc.name in rsc.names) {\n";
		script += "rsc.colors[rsc.i] = rsc.name\n";
		script += "rsc[rsc == rsc.name] = rsc.i\n";
		script += "rsc.i = rsc.i + 1\n";
		script += "}\n";
		script += "rsc = matrix(as.numeric(rsc), nrow = dim(rsc)[1])\n";
		script += "image(t(rsc), col = as.vector(rsc.colors), axes = FALSE)\n";
		script += "if (length(colnames(RowSideColors)) > 0) {\n";
		script += "axis(1, 0:(dim(rsc)[2] - 1)/(dim(rsc)[2] - 1), colnames(RowSideColors), las = 2, tick = FALSE)\n";
		script += "}\n";
		script += "}\n";
		script += "}\n";
		script += "\n";
		script += "if (!missing(ColSideColors)) {\n";
		script += "\n";
		script += "if (!is.matrix(ColSideColors)){\n";
		script += "par(mar = c(0.5, 0, 0, margins[2]))\n";
		script += "image(cbind(1:nc), col = ColSideColors[colInd], axes = FALSE)\n";
		script += "} else {\n";
		script += "par(mar = c(0.5, 0, 0, margins[2]))\n";
		script += "csc = ColSideColors[colInd, , drop=F]\n";
		script += "csc.colors = matrix()\n";
		script += "csc.names = names(table(csc))\n";
		script += "csc.i = 1\n";
		script += "for (csc.name in csc.names) {\n";
		script += "csc.colors[csc.i] = csc.name\n";
		script += "csc[csc == csc.name] = csc.i\n";
		script += "csc.i = csc.i + 1\n";
		script += "}\n";
		script += "csc = matrix(as.numeric(csc), nrow = dim(csc)[1])\n";
		script += "image(csc, col = as.vector(csc.colors), axes = FALSE)\n";
		script += "if (length(colnames(ColSideColors)) > 0) {\n";
		script += "axis(2, 0:(dim(csc)[2] - 1)/max(1,(dim(csc)[2] - 1)), colnames(ColSideColors), las = 2, tick = FALSE)\n";
		script += "}\n";
		script += "}\n";
		script += "}\n";
		script += "\n";
		script += "par(mar = c(margins[1], 0, 0, margins[2]))\n";
		script += "x <- t(x)\n";
		script += "cellnote <- t(cellnote)\n";
		script += "if (revC) {\n";
		script += "iy <- nr:1\n";
		script += "if (exists(\"ddr\"))\n";
		script += "ddr <- rev(ddr)\n";
		script += "x <- x[, iy]\n";
		script += "cellnote <- cellnote[, iy]\n";
		script += "}\n";
		script += "else iy <- 1:nr\n";
		script += "image(1:nc, 1:nr, x, xlim = 0.5 + c(0, nc), ylim = 0.5 + c(0, nr), axes = FALSE, xlab = \"\", ylab = \"\", col = col, breaks = breaks, ...)\n";
		script += "retval$carpet <- x\n";
		script += "if (exists(\"ddr\"))\n";
		script += "retval$rowDendrogram <- ddr\n";
		script += "if (exists(\"ddc\"))\n";
		script += "retval$colDendrogram <- ddc\n";
		script += "retval$breaks <- breaks\n";
		script += "retval$col <- col\n";
		script += "if (!invalid(na.color) & any(is.na(x))) { # load library(gplots)\n";
		script += "mmat <- ifelse(is.na(x), 1, NA)\n";
		script += "image(1:nc, 1:nr, mmat, axes = FALSE, xlab = \"\", ylab = \"\",\n";
		script += "col = na.color, add = TRUE)\n";
		script += "}\n";
		script += "axis(1, 1:nc, labels = labCol, las = 2, line = -0.5, tick = 0,\n";
		script += "cex.axis = cexCol)\n";
		script += "if (!is.null(xlab))\n";
		script += "mtext(xlab, side = 1, line = margins[1] - 1.25)\n";
		script += "axis(4, iy, labels = labRow, las = 2, line = -0.5, tick = 0,\n";
		script += "cex.axis = cexRow)\n";
		script += "if (!is.null(ylab))\n";
		script += "mtext(ylab, side = 4, line = margins[2] - 1.25)\n";
		script += "if (!missing(add.expr))\n";
		script += "eval(substitute(add.expr))\n";
		script += "if (!missing(colsep))\n";
		script += "for (csep in colsep) rect(xleft = csep + 0.5, ybottom = rep(0, length(csep)), xright = csep + 0.5 + sepwidth[1], ytop = rep(ncol(x) + 1, csep), lty = 1, lwd = 1, col = sepcolor, border = sepcolor)\n";
		script += "if (!missing(rowsep))\n";
		script += "for (rsep in rowsep) rect(xleft = 0, ybottom = (ncol(x) + 1 - rsep) - 0.5, xright = nrow(x) + 1, ytop = (ncol(x) + 1 - rsep) - 0.5 - sepwidth[2], lty = 1, lwd = 1, col = sepcolor, border = sepcolor)\n";
		script += "min.scale <- min(breaks)\n";
		script += "max.scale <- max(breaks)\n";
		script += "x.scaled <- scale01(t(x), min.scale, max.scale)\n";
		script += "if (trace %in% c(\"both\", \"column\")) {\n";
		script += "retval$vline <- vline\n";
		script += "vline.vals <- scale01(vline, min.scale, max.scale)\n";
		script += "for (i in colInd) {\n";
		script += "if (!is.null(vline)) {\n";
		script += "abline(v = i - 0.5 + vline.vals, col = linecol,\n";
		script += "lty = 2)\n";
		script += "}\n";
		script += "xv <- rep(i, nrow(x.scaled)) + x.scaled[, i] - 0.5\n";
		script += "xv <- c(xv[1], xv)\n";
		script += "yv <- 1:length(xv) - 0.5\n";
		script += "lines(x = xv, y = yv, lwd = 1, col = tracecol, type = \"s\")\n";
		script += "}\n";
		script += "}\n";
		script += "if (trace %in% c(\"both\", \"row\")) {\n";
		script += "retval$hline <- hline\n";
		script += "hline.vals <- scale01(hline, min.scale, max.scale)\n";
		script += "for (i in rowInd) {\n";
		script += "if (!is.null(hline)) {\n";
		script += "abline(h = i + hline, col = linecol, lty = 2)\n";
		script += "}\n";
		script += "yv <- rep(i, ncol(x.scaled)) + x.scaled[i, ] - 0.5\n";
		script += "yv <- rev(c(yv[1], yv))\n";
		script += "xv <- length(yv):1 - 0.5\n";
		script += "lines(x = xv, y = yv, lwd = 1, col = tracecol, type = \"s\")\n";
		script += "}\n";
		script += "}\n";
		script += "if (!missing(cellnote))\n";
		script += "text(x = c(row(cellnote)), y = c(col(cellnote)), labels = c(cellnote),\n";
		script += "col = notecol, cex = notecex)\n";
		script += "par(mar = c(margins[1], 0, 0, 0))\n";
		script += "if (dendrogram %in% c(\"both\", \"row\")) {\n";
		script += "plot(ddr, horiz = TRUE, axes = FALSE, yaxs = \"i\", leaflab = \"none\")\n";
		script += "}\n";
		script += "else plot.new()\n";
		script += "par(mar = c(0, 0, if (!is.null(main)) 5 else 0, margins[2]))\n";
		script += "if (dendrogram %in% c(\"both\", \"column\")) {\n";
		script += "plot(ddc, axes = FALSE, xaxs = \"i\", leaflab = \"none\")\n";
		script += "}\n";
		script += "else plot.new()\n";
		script += "if (!is.null(main))\n";
		script += "title(main, cex.main = 1.5 * op[[\"cex.main\"]])\n";
		script += "if (key) {\n";
		script += "par(mar = c(5, 4, 2, 1), cex = 0.75)\n";
		script += "tmpbreaks <- breaks\n";
		script += "if (symkey) {\n";
		script += "max.raw <- max(abs(c(x, breaks)), na.rm = TRUE)\n";
		script += "min.raw <- -max.raw\n";
		script += "tmpbreaks[1] <- -max(abs(x), na.rm = TRUE)\n";
		script += "tmpbreaks[length(tmpbreaks)] <- max(abs(x), na.rm = TRUE)\n";
		script += "}\n";
		script += "else {\n";
		script += "min.raw <- min(x, na.rm = TRUE)\n";
		script += "max.raw <- max(x, na.rm = TRUE)\n";
		script += "}\n";
		script += "\n";
		script += "z <- seq(min.raw, max.raw, length = length(col))\n";
		script += "image(z = matrix(z, ncol = 1), col = col, breaks = tmpbreaks,\n";
		script += "xaxt = \"n\", yaxt = \"n\")\n";
		script += "par(usr = c(0, 1, 0, 1))\n";
		script += "lv <- pretty(breaks)\n";
		script += "xv <- scale01(as.numeric(lv), min.raw, max.raw)\n";
		script += "axis(1, at = xv, labels = lv)\n";
		script += "if (scale == \"row\")\n";
		script += "mtext(side = 1, \"Row Z-Score\", line = 2)\n";
		script += "else if (scale == \"column\")\n";
		script += "mtext(side = 1, \"Column Z-Score\", line = 2)\n";
		script += "else mtext(side = 1, KeyValueName, line = 2)\n";
		script += "if (density.info == \"density\") {\n";
		script += "dens <- density(x, adjust = densadj, na.rm = TRUE)\n";
		script += "omit <- dens$x < min(breaks) | dens$x > max(breaks)\n";
		script += "dens$x <- dens$x[-omit]\n";
		script += "dens$y <- dens$y[-omit]\n";
		script += "dens$x <- scale01(dens$x, min.raw, max.raw)\n";
		script += "lines(dens$x, dens$y/max(dens$y) * 0.95, col = denscol,\n";
		script += "lwd = 1)\n";
		script += "axis(2, at = pretty(dens$y)/max(dens$y) * 0.95, pretty(dens$y))\n";
		script += "title(\"Color Key\nand Density Plot\")\n";
		script += "par(cex = 0.5)\n";
		script += "mtext(side = 2, \"Density\", line = 2)\n";
		script += "}\n";
		script += "else if (density.info == \"histogram\") {\n";
		script += "h <- hist(x, plot = FALSE, breaks = breaks)\n";
		script += "hx <- scale01(breaks, min.raw, max.raw)\n";
		script += "hy <- c(h$counts, h$counts[length(h$counts)])\n";
		script += "lines(hx, hy/max(hy) * 0.95, lwd = 1, type = \"s\",\n";
		script += "col = denscol)\n";
		script += "axis(2, at = pretty(hy)/max(hy) * 0.95, pretty(hy))\n";
		script += "title(\"Color Key\nand Histogram\")\n";
		script += "par(cex = 0.5)\n";
		script += "mtext(side = 2, \"Count\", line = 2)\n";
		script += "}\n";
		script += "else title(\"Color Key\")\n";
		script += "}\n";
		script += "else plot.new()\n";
		script += "retval$colorTable <- data.frame(low = retval$breaks[-length(retval$breaks)],\n";
		script += "high = retval$breaks[-1], color = retval$col)\n";
		script += "invisible(retval)\n";
		script += "}\n";

		return script;
	}
}


