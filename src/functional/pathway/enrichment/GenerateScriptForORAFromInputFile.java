package functional.pathway.enrichment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Generate the script for over-representation analysis given a list of pathways to test for
 * @author tshaw
 *
 */
public class GenerateScriptForORAFromInputFile {


	public static String description() {
		return "Generate script for performing over representation analysis and html summary.";
	}
	public static String type() {
		return "ENRICHMENT";
	}
	public static String parameter_info() {
		return "[input_file] [limma_all_file] [prefix] [upregKeyWord] [dnregKeyWord] [pathwayListFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			//String upregulated_file = args[0];
			//String dnregulated_file = args[1];
			//String all_file = args[2];
			String inputFile = args[0];
			String all_file = args[1];
			String prefix = args[2];		
			String upregKey = args[3];
			String dnregKey = args[4];
			String pathwayListFile = args[5];
			
			FileInputStream fstream = new FileInputStream(pathwayListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String pathwayGeneListFile = in.readLine();
				if (!pathwayGeneListFile.equals("")) {
					String[] split = pathwayGeneListFile.split("\t");
					String pathwayName = split[0];
					String pathwayFile = split[1];
					//System.out.println("drppm -GenerateEnrichmentFileInput " + upregulated_file + " " + dnregulated_file + " " + prefix + "_ENRICH_INPUT.txt");
					System.out.println("drppm -OverRepresentationAnalysis " + inputFile + " " + pathwayFile + " " + all_file + " false " + prefix + "_" + pathwayName + ".txt");
					System.out.println("drppm -OverRepresentationAnalysisFDR " + prefix + "_" + pathwayName + ".txt " + pathwayFile + " " + prefix + "_" + pathwayName + "_FDR.txt");
					System.out.println("drppm -FilterORAResultsFlex " + prefix + "_" + pathwayName + "_FDR.txt 0.05 0.05 " + prefix + "_" + pathwayName + "_final_filtered.txt " + upregKey + " " + dnregKey + " " + prefix + "_" + pathwayName + "_FDR_filtered_UpReg.txt " + prefix + "_" + pathwayName + "_FDR_filtered_DnReg.txt");
					System.out.println("drppm -GenerateHorizontalBarPlotJavaScript " + prefix + "_" + pathwayName + "_FDR_filtered_UpReg.txt 3 2 1 4 " + prefix + "_" + upregKey + "_" + pathwayName + "_UpRegGenes" + " Enrichment_Score true > " + prefix + "_" + upregKey + "_" + pathwayName + "_FDR_filtered_UpReg.txt.html");
					System.out.println("drppm -GenerateHorizontalBarPlotJavaScript " + prefix + "_" + pathwayName + "_FDR_filtered_DnReg.txt 3 2 1 4 " + prefix + "_" + dnregKey + "_" + pathwayName + "_DnRegGenes" + " Enrichment_Score true > " + prefix + "_" + dnregKey + "_" + pathwayName + "_FDR_filtered_DnReg.txt.html");
					
				}
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
