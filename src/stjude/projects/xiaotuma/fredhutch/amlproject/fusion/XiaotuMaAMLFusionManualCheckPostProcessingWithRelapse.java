package stjude.projects.xiaotuma.fredhutch.amlproject.fusion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class XiaotuMaAMLFusionManualCheckPostProcessingWithRelapse {

	public static void main(String[] args) {
		
		try {
			
			LinkedList list = new LinkedList();
			HashMap map = new HashMap();
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\AML_CICERO_Manual_Check_List_Relapse_Clean_Fusion_20200413.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			// out.write(sampleName + "\t" + major_gene + "\t" + flt3 + "\t" + major_event + "\t" + secondary_gene + "\t" + fusion + "\t" + indel + "\n");
			out.write("SampleName\tMajor_Gene\tFLT3\tMajor_Event\tSecondary_Event\tIndel\tAllEvents\n");
			
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\AML_CICERO_Manual_Check_List_Relapse_20200413.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				
				String fusion = split[24] + "_" + split[26];
				fusion = fusion.replaceAll("9-Sep", "SEPT9");
				fusion = fusion.replaceAll("6-Sep", "SEPT6");
				fusion = fusion.replaceAll("2-Sep", "SEPT2");
				fusion = fusion.replaceAll("8-Mar", "MARCH8");
				fusion = fusion.replaceAll("8-Mar", "MARCH8");
				fusion = fusion.replaceAll("SETP6", "SEPT6");
				
				if (map.containsKey(sampleName)) {
					String prev_fusion = (String)map.get(sampleName);
					if (!prev_fusion.contains(fusion)) {
						String new_fusion = prev_fusion + "," + fusion;
						map.put(sampleName, new_fusion);
					}
				} else {
					map.put(sampleName, fusion);
				}
			}
			in.close();			
			/*
			String inputRNAindelFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rna_indel\\Kohei_NPM1_CEBPA_KIT_SJID.txt";
			fstream = new FileInputStream(inputRNAindelFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String indel = split[2] + "_INDEL";
				if (map.containsKey(sampleName)) {
					String prev_fusion = (String)map.get(sampleName);
					if (!prev_fusion.contains(indel)) {
						String new_fusion = prev_fusion + "," + indel;
						map.put(sampleName, new_fusion);
					}
				} else {
					map.put(sampleName, indel);
				}
			}
			in.close();			
			
				*/		
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				String fusion = (String)map.get(sampleName);
				String major_event = "";
				String major_gene = "";
				String secondary_gene = "";
				String novel_event = "NA";
				String flt3 = "NA";
				String indel = "";
				if (fusion.contains("KIT_INDEL")) {	
					indel += "KIT_INDEL,";
					major_gene += "KIT,";
				}
				if (fusion.contains("NPM1_INDEL")) {
					indel += "NPM1_INDEL,";
					major_gene += "NPM1,";
				}
				if (fusion.contains("CEBPA_INDEL")) {
					major_event += "CEBPA_INDEL,";
					indel += "CEBPA_INDEL,";
					major_gene += "CEBPA,";
				}
				if (fusion.contains("FLT3")) {
					flt3 = "FLT3-ITD";
					major_gene += "FLT3,";
				}
				if (fusion.contains("BPTF_NUP98")) {
					major_event += "BPTF_NUP98,";
					major_gene += "NUP98,";
					novel_event = "yes";
				} else if (fusion.contains("NUP98_HOXD13")) {
					major_event += "NUP98_HOXD13,";
					major_gene += "NUP98,";
				} else if (fusion.contains("NSD1_NUP98")) {
					major_event += "NUP98_NSD1,";
					major_gene += "NUP98,";
				} else if (fusion.contains("NUP98_NSD1")) {
					major_event += "NUP98_NSD1,";
					major_gene += "NUP98,";
				} else if (fusion.contains("NUP98_KDM5A")) {
					major_event += "NUP98_KDM5A,";
					major_gene += "NUP98,";
				} else if (fusion.contains("NUP98_KDM5A")) {
					major_event += "NUP98_KDM5A,";
					major_gene += "NUP98,";
				} else if (fusion.contains("KDM5A_NUP98")) {
					major_event += "NUP98_KDM5A,";
					major_gene += "NUP98,";
				} else if (fusion.contains("NUP98_DDX10")) {
					major_event += "NUP98_DDX10,";
					major_gene += "NUP98,";
				} else if (fusion.contains("NUP98_FLI1")) {
					major_event += "NUP98_FLI1,";
					major_gene += "NUP98,";
				} else if (fusion.contains("NUP98_HOXA13")) {
					major_event += "NUP98_HOXA13,";
					major_gene += "NUP98,";
				} else if (fusion.contains("NUP98_HOXA9")) {
					major_event += "NUP98_HOXA9,";
					major_gene += "NUP98,";
				} else if (fusion.contains("NUP98_JADE2")) {
					major_event += "NUP98_JADE2,";
					major_gene += "NUP98,";
				} else if (fusion.contains("NUP98_PHF23")) {
					major_event += "NUP98_PHF23,";
					major_gene += "NUP98,";
				} else if (fusion.contains("NUP98_PRRX1")) {
					major_event += "NUP98_PRRX1,";
					major_gene += "NUP98,";
				}

				if (fusion.contains("ARAP1_KMT2A")) {
					major_event += "KMT2A_ARAP1,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_MLLT10")) {
					major_event += "KMT2A_MLLT10,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_MLLT1")) {
					major_event += "KMT2A_MLLT1,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("DNAJC1_KMT2A")) {
					major_event += "KMT2A_DNAJC1,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("ELL_KMT2A")) {
					major_event += "KMT2A_ELL,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_ELL")) {
					major_event += "KMT2A_ELL,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_SEPT6")) { 
					major_event += "KMT2A_SEPT6,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_SEPT2")) {
					major_event += "KMT2A_SEPT2,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_SEPT9")) {
					major_event += "KMT2A_SEPT9,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_MLLT3")) {
					major_event += "KMT2A_MLLT3,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_MLLT4")) {
					major_event += "KMT2A_MLLT4,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("HINFP_KMT2A,CBL_PHLDB1,MIR6716,MLLT10_UVRAG")) {
					major_event += "KMT2A,MLLT10,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_ABI1")) {
					major_event += "KMT2A_ABI1,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_AFF1")) {
					major_event += "KMT2A_AFF1,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_AFF3")) {
					major_event += "KMT2A_AFF3,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_ARHGEF37")) {
					major_event += "KMT2A_ARHGEF37,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_DCP1A")) {
					major_event += "KMT2A_DCP1A,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_DNAJC1,PIP4K2A_KMT2A,MLLT10_PIP4K2A")) {
					major_event += "KMT2A,MLLT10,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("MLLT10_AMBRA1,TRIM44_KMT2A")) {
					major_event += "KMT2A,MLLT10,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_FNBP1")) {
					major_event += "KMT2A_FNBP1,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_LASP1")) {
					major_event += "KMT2A_LASP1,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_LYN")) {
					major_event += "KMT2A_LYN,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_LYN")) {
					major_event += "KMT2A_LYN,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("LASP1_KMT2A")) {
					major_event += "KMT2A_LASP1,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("MLLT10_KMT2A")) {
					major_event += "KMT2A_MLLT10,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("MLLT1_KMT2A")) {
					major_event += "KMT2A_MLLT1,";
					major_gene += "KMT2A,";					
				} else if (fusion.contains("KMT2A_MLLT11")) {
					major_event += "KMT2A_MLLT11,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_MLLT6")) {
					major_event += "KMT2A_MLLT6,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_MYO1F")) {
					major_event += "KMT2A_MYO1F,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_MYOCD")) {
					major_event += "KMT2A_MYOCD,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("KMT2A_USP2")) {
					major_event += "KMT2A_USP2,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("MLLT3_KMT2A")) {
					major_event += "KMT2A_MLLT3,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("AKAP2_KMT2A")) {
					major_event += "KMT2A_AKAP2,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("PHLPP1_KMT2A")) {
					major_event += "KMT2A_PHLPP1,";
					major_gene += "KMT2A,";
				} else if (fusion.contains("ZNF146_KMT2A")) {
					major_event += "KMT2A_ZNF146,";
					major_gene += "KMT2A,";
				} 
				if (fusion.contains("RUNX1_RUNX1T1")) {
					major_event += "RUNX1_RUNX1T1,";
					major_gene += "RUNX1,";
				} else if (fusion.contains("RUNX1_CBFA2T3")) {
					major_event += "RUNX1_CBFA2T3,";
					major_gene += "RUNX1,";
				} else if (fusion.contains("GEMIN7_RUNX1")) {
					major_event += "GEMIN7_RUNX1,";
					major_gene += "RUNX1,";
				} else if (fusion.contains("RUNX1_STK3")) {
					major_event += "RUNX1_STK3,";
					major_gene += "RUNX1,";
				} else if (fusion.contains("RUNX1_USP42")) {
					major_event += "RUNX1_USP42,";
					major_gene += "RUNX1,";
				} else if (fusion.contains("RUNX1_ZFPM2")) {
					major_event += "RUNX1_ZFPM2,";
					major_gene += "RUNX1,";
				} else if (fusion.contains("RUNX1_GATA2")) {
					major_event += "RUNX1_GATA2,";
					major_gene += "RUNX1,";
				} else if (fusion.contains("RUNX1_MECOM")) {
					major_event += "RUNX1_MECOM,";
					major_gene += "RUNX1,MECOM,";
				} else if (fusion.contains("RUNX1_EVX1")) {
					major_event += "RUNX1_EVX1,";
					major_gene += "RUNX1,";					
				}
				if (fusion.contains("CBFA2T3_GLIS2")) {
					major_event += "CBFA2T3_GLIS2,";
					major_gene += "CBFA2T3,";
				} else if (fusion.contains("CBFA2T3_GLIS3")) {
					major_event += "CBFA2T3_GLIS3,";
					major_gene += "CBFA2T3,";
				} else if (fusion.contains("CBFA2T3_SLC7A5")) {
					major_event += "CBFA2T3_SLC7A5,";
					major_gene += "CBFA2T3,";
				} else if (fusion.contains("SLC7A5_CBFA2T3")) {
					major_event += "CBFA2T3_SLC7A5,";
					major_gene += "CBFA2T3,";
				} else if (fusion.contains("MIR6775_CBFA2T3")) {
					major_event += "CBFA2T3_MIR6775,";
					major_gene += "CBFA2T3,";
				}
				
				if (fusion.contains("CBFB_MYH11")) {
					major_event += "CBFB_MYH11,";
					major_gene += "CBFB,";
				} else if (fusion.contains("MIR484_CBFB")) {
					major_event += "CBFB_MIR484,";
					major_gene += "CBFB,";
				} else if (fusion.contains("CBFB_MYH9")) {
					major_event += "CBFB_MYH9,";
					major_gene += "CBFB,";
				} else if (fusion.contains("FHOD1_CBFB")) {
					major_event += "CBFB_FHOD1,";
					major_gene += "CBFB,";
				} else if (fusion.contains("FHOD1_CBFB")) {
					major_event += "CBFB_FHOD1,";
					major_gene += "CBFB,";
				}
				
				if (fusion.contains("CBL_CBL")) {
					major_event += "CBL_ITD,";
					major_gene += "CBL,";
				} else if (fusion.contains("CBL_PHLDB1")){
					major_gene += "CBL,";
				}
				
				if (fusion.contains("CEBPA_CEBPA")) {
					major_event += "CEBPA_ITD,";
					major_gene += "CEBPA";
				}
				if (fusion.contains("CLASP1_MLLT3")) {
					major_event += "CLASP1_MLLT3,";
					major_gene += "MLLT3,";
				}
				if (fusion.contains("CLASP1_MLLT3")) {
					major_event += "CLASP1_MLLT3,";
					major_gene += "MLLT3,";
				}
				if (fusion.contains("DDX3X_MLLT10")) {
					major_event += "MLLT10_DDX3X,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("HNRNPUL1_MLLT10")) {
					major_event += "HNRNPUL1_MLLT10,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("MLLT10_DDX10")) {
					major_event += "MLLT10_DDX10,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("MLLT10_DNAJC1")) {
					major_event += "MLLT10_DNAJC1,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("MLLT10_LUC7L2")) {
					major_event += "MLLT10_LUC7L2,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("MLLT10_MPL")) {
					major_event += "MLLT10_MPL,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("MLLT10_NAP1L1")) {
					major_event += "MLLT10_NAP1L1,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("MLLT10_TENM4")) {
					major_event += "MLLT10_TENM4,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("MLLT10_TREH")) {
					major_event += "MLLT10_TREH,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("MLLT10_ZBTB16")) {
					major_event += "MLLT10_ZBTB16,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("PICALM_MLLT10")) {
					major_event += "MLLT10_PICALM,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("TEC_MLLT10")) {
					major_event += "MLLT10_TEC,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("XPO1_MLLT10")) {
					major_event += "MLLT10_XPO1,";
					major_gene += "MLLT10,";
				} else if (fusion.contains("MLLT10_NYX")) {
					major_event += "MLLT10_NYX,";
					major_gene += "MLLT10,";
				}
				if (fusion.contains("DEK_NUP214")) {
					major_event += "DEK_NUP214,";
					major_gene += "NUP214,";
				} else if (fusion.contains("NUP214_ABL1")) {
					major_event += "NUP214_ABL1,";
					major_gene += "NUP214,";
				} else if (fusion.contains("NUP214_PSIP1")) {
					major_event += "NUP214_PSIP1,";
					major_gene += "NUP214,";
				} else if (fusion.contains("SET_NUP214")) {
					major_event += "SET_NUP214,";
					major_gene += "NUP214,";
				}  
				
				if (fusion.contains("ETV6_ABL2")) {
					major_event += "ETV6_ABL2,";
					major_gene = "ETV6,ABL2,";
				} else if (fusion.contains("EP300_ETV6")) {
					major_event += "EP300_ETV6,";
					major_gene = "ETV6,EP300,";					
				} else if (fusion.contains("ETV6_ABL1")) {
					major_event += "ETV6_ABL1,";
					major_gene = "ETV6,ABL1,";					
				} else if (fusion.contains("ETV6_FOXO1")) {
					major_event += "ETV6_FOXO1,";
					major_gene = "ETV6,FOXO1,";					
				} else if (fusion.contains("ETV6_GRIN2B")) {
					major_event += "ETV6_GRIN2B,";
					major_gene = "ETV6,";					
				} else if (fusion.contains("ETV6_MN1")) {
					major_event += "ETV6_MN1,";
					major_gene = "ETV6,";					
				} else if (fusion.contains("ETV6_RERE")) {
					major_event += "ETV6_RERE,";
					major_gene = "ETV6,";					
				} else if (fusion.contains("ETV6_NSD1")) {
					major_event += "ETV6_NSD1,";
					major_gene = "ETV6,";					
				} else if (fusion.contains("ETV6_NSD1")) {
					major_event += "ETV6_NSD1,";
					major_gene = "ETV6,";					
				} else if (fusion.contains("MNX1_ETV6")) {
					major_event += "MNX1_ETV6,";
					major_gene = "ETV6,";	
				} else if (fusion.contains("NIPBL_ETV6")) {
					major_event += "NIPBL_ETV6,";
					major_gene = "NIPBL,ETV6,";	
				} 
				
				
				if (fusion.contains("KMT2C_ETV6")) {
					major_event += "KMT2C_ETV6,";
					major_gene += "KMT2C,";
					
				}
				if (fusion.contains("KMT2C_PUS7")) {
					major_event += "KMT2C_PUS7,";
					major_gene += "KMT2C,";
				}
				
				if (fusion.contains("KMT2C_RBM28")) {
					major_event += "KMT2C_RBM28,";
					major_gene += "KMT2C,";
				}
				if (fusion.contains("EP300_KAT6A")) {
					major_event += "EP300_KAT6A,";					
					major_gene += "KAT6A,EP300,";
				} else if (fusion.contains("KAT6A_CREBBP")) {
					major_event += "KAT6A_CREBBP,";					
					major_gene += "KAT6A,CREBBP,";
				} else if (fusion.contains("KAT6A_NCOA2")) {
					major_event += "KAT6A_NCOA2,";					
					major_gene += "KAT6A,";
				}
				if (fusion.contains("ERG_FUS")) {
					major_event += "FUS_ERG,";					
					major_gene += "ERG,";
				} else if (fusion.contains("FUS_ERG")) {
					major_event += "FUS_ERG,";					
					major_gene += "ERG,";
				} else if (fusion.contains("ERG_HNRNPH1")) {
					major_event += "HNRNPH1_ERG,";					
					major_gene += "ERG,";
				} else if (fusion.contains("HNRNPH1_ERG")) {
					major_event += "HNRNPH1_ERG,";					
					major_gene += "ERG,";
				} else if (fusion.contains("GATA2_ERG")) {
					major_event += "GATA2_ERG,";					
					major_gene += "ERG,";
				}
				
				
				if (fusion.contains("MED14_HOXA9")) {
					major_event += "MED14_HOXA9,";
					secondary_gene += "MED14,HOXA9";
				} else if (fusion.contains("NIPBL_HOXB9")) {
					major_event += "NIPBL_HOXB9,";
					secondary_gene += "HOXB9";
					major_gene += "NIPBL,";
				} else if (fusion.contains("NIPBL_HOXB7")) {
					major_event += "NIPBL_HOXB7,";
					secondary_gene += "NIPBL,HOXB7";
				} else if (fusion.contains("NIPBL_HOXB7")) {
					major_event += "NIPBL_HOXB7,";
					secondary_gene += "NIPBL,HOXB7";
				}
				
				if (fusion.contains("EWSR1_FEV")) {
					major_event += "EWSR1_FEV,";
					secondary_gene += "EWSR1,REV";
				}
				if (fusion.contains("ANP32A_KLF6")) {
					major_event += "ANP32A_KLF6,";
					secondary_gene += "KLF6";
				} else if (fusion.contains("CPSF6_KLF11")) {
					major_event += "CPSF6_KLF11,";
					secondary_gene += "KLF11";
				} else if (fusion.contains("FOSB_KLF6")) {
					major_event += "FOSB_KLF6,";
					secondary_gene += "KLF6";
				} else if (fusion.contains("KLF2_PTBP1")) {
					major_event += "KLF2_PTBP1,";
					secondary_gene += "KLF2";
				} else if (fusion.contains("ZFP36_KLF6")) {
					major_event += "ZFP36_KLF6,";
					secondary_gene += "KLF6";
				}  
				
				if (fusion.contains("C3orf27_MECOM")) {
					major_event += "C3orf27_MECOM,";
					major_gene += "MECOM,";
				} else if (fusion.contains("C3orf27_MECOM")) {
					major_event += "C3orf27_MECOM,";
					major_gene += "MECOM,";
				} else if (fusion.contains("TBL1XR1_MECOM")) {
					major_event += "TBL1XR1_MECOM,";
					major_gene += "MECOM,";					
				}
				
				if (fusion.contains("FBXL14_TPM3")) {
					major_event += "FBXL14_TPM3,";
					secondary_gene += "TPM3,";
				}
				
				if (fusion.contains("PSPC1_ZFP36L1")) {
					major_event += "PSPC1_ZFP36L1,";
					secondary_gene += "PSPC1,";
				}
				
				if (fusion.contains("UNKL_SKI")) {
					major_event += "UNKL_SKI,";
					secondary_gene += "PSPC1,";
				}
				if (fusion.contains("GATA2_GATA2")) {
					major_event += "GATA2_ITD,";
					secondary_gene += "GATA2,";
				}
				if (fusion.contains("HBS1L_MYB")) {
					major_event += "HBS1L_MYB,";
					major_gene += "MYB,";
				}
				if (fusion.contains("MAP3K1_ATM")) {
					major_event += "MAP3K1_ATM,";
					secondary_gene += "ATM,MAP3K1,";
				}
				if (fusion.contains("ZMYND11_MBTD1")) {
					major_event += "ZMYND11_MBTD1,";
					secondary_gene += "ZMYND11,MBTD1,";
				}
				
				if (fusion.contains("MIR31HG_SETD2")) {
					major_event += "MIR31HG_SETD2,";
					secondary_gene += "SETD2,";
				}

				if (fusion.contains("MKL1_FAM76A")) {
					major_event += "MKL1_FAM76A,";
					major_gene += "MKL1,";
				} else if (fusion.contains("RBM15_MKL1")) {
					major_event += "RBM15_MKL1,";
					major_gene += "MKL1,";
				} else if (fusion.contains("MKL1_RBM15")) {
					major_event += "RBM15_MKL1,";
					major_gene += "MKL1,";
				}

				if (fusion.contains("MYB_CLINT1")) {
					major_event += "MYB_CLINT1,";
					major_gene += "MYB,";
				}
				if (fusion.contains("MYB_GATA1")) {
					major_event += "MYB_GATA1,";
					major_gene += "MYB,GATA1,";
				}
				if (fusion.contains("MYB_ZFAT")) {
					major_event += "MYB_ZFAT,";
					major_gene += "MYB,";
				}
				
				if (fusion.contains("NPM1_CCDC28A")) {
					major_event += "NPM1_CCDC28A,";
					major_gene += "NPM1,";
				} else if (fusion.contains("NPM1_MLF1")) {
					major_event += "NPM1_MLF1,";
					major_gene += "NPM1,";
					secondary_gene += "MLF1,";
				} else if (fusion.contains("NPM1_MLF1")) {
					major_event += "NPM1_MLF1,";
					major_gene += "NPM1,";
					secondary_gene += "MLF1,";
				}
				
				if (fusion.contains("RANBP2_ALK")) {
					major_event += "RANBP2_ALK,";
					major_gene += "ALK,";
				} else if (fusion.contains("SPTBN1_ALK")) {
					major_event += "SPTBN1_ALK,";
					major_gene += "ALK,";
				}  
				
				if (fusion.contains("SFPQ_ZFP36L2")) {
					major_event += "SFPQ_ZFP36L2,";
					secondary_gene += "SFPQ,ZFP36L2,";
				}

				if (fusion.contains("STAG2_TMEM237")) {
					major_event += "STAG2_TMEM237,";
					secondary_gene += "STAG2,";
				}
				if (fusion.contains("TPT1,SNORA31_STAG2")) {
					major_event += "SNORA31_STAG2,";
					secondary_gene += "STAG2,";
				}
				if (fusion.contains("TPT1,SNORA31_STAG2")) {
					major_event += "SNORA31_STAG2,";
					secondary_gene += "STAG2,";
				}
				
				if (fusion.contains("PICALM_AFF2,TENM1_STAG2")) {
					major_event += "TENM1_STAG2,";
					secondary_gene += "STAG2,";
				}
				if (fusion.contains("ZBTB7A_NUTM1")) {
					major_event += "ZBTB7A_NUTM1,";
					secondary_gene += "ZBTB7A,NUTM1,";
				}
				if (fusion.contains("CRTC1_SKI")) {
					major_event += "CRTC1_SKI,";
					secondary_gene += "SKI,";
				}
				if (fusion.contains("UNKL_SKI")) {
					major_event += "UNKL_SKI,";
					secondary_gene += "SKI,";
				}
				
				if (fusion.contains("MYC_MYC")) {
					major_event += "MYC_ITD,";
					major_gene += "MYC,";
				}
				if (fusion.contains("TET_TET")) {
					major_event += "TET_ITD,";
					major_gene += "TET,";
				}
				if (indel.equals("")) {
					indel = "NA";
				}
				out.write(sampleName + "\t" + major_gene + "\t" + flt3 + "\t" + major_event + "\t" + secondary_gene + "\t" + indel + "\t" + fusion + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
