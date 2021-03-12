package rnaseq.splicing.csiminer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Extract the gene symbol and generate interaction network
 * @author tshaw
 *
 */
public class CSIMinerAnnotatePrioritizedExons {


	public static String description() {
		return "Append the exon annotation information for each candidate.";
	}
	public static String type() {
		return "DATAMATRIX";
	}
	public static String parameter_info() {
		return "[CSIminer_Exons_inputFile] [inputMatrixCoreFile] [inputTherapeuticFile] [inputSurfaceomeFile] [inputGTExAnnotationFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String meta_analysis_inputFile = args[0]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_full_exon_analysis\\exon_meta_analysis_20200720.txt";
			
			String inputMatrixCoreFile = args[1]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\references\\extra_cellular_matrix\\matrixdb_CORE.tab";
			String inputTherapeuticFile = args[2]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\references\\therapeutic_targets\\therpeutic_target.txt";
			String inputSurfaceomeFile = args[3]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\references\\surfaceome\\pnas\\experiment.txt";			
			
			String outputFile = args[4]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_full_exon_analysis\\exon_meta_analysis_matrixDB_20200720.txt";
			
			File f = new File(outputFile);
			if (f.exists()) {
				System.out.println("Please delete the output file");
				//System.exit(0);
			}
			FileInputStream fstream = new FileInputStream(inputMatrixCoreFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName1 = "";
				String geneName2 = "";
				if (split[5].contains("uniprotkb:")) {
					geneName2 = split[5].replaceAll("uniprotkb:\"", "").replaceAll("\"\\(gene name\\)", "");
				}
				if (split[4].contains("uniprotkb:")) {
					geneName1 = split[4].replaceAll("uniprotkb:\"", "").replaceAll("\"\\(gene name\\)", "");
				}
				if (!geneName2.equals("")) {
					map.put(geneName2.toUpperCase(), geneName2.toUpperCase());
				}
				if (!geneName1.equals("")) {
					map.put(geneName1.toUpperCase(), geneName1.toUpperCase());
				}
				
				//System.out.println(geneName1 + "\t" + geneName2);
			}
			in.close();

			HashMap map_therapeutic = new HashMap();
			
			fstream = new FileInputStream(inputTherapeuticFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneNames = split[4];
				for (String geneName: geneNames.split(";")) {
					if (map_therapeutic.containsKey(geneName)) {
						String antibody_name = (String)map_therapeutic.get(geneName);
						if (!antibody_name.contains(split[0])) {
							antibody_name += "," + split[0];
						}
						map_therapeutic.put(geneName, antibody_name);
					} else {
						map_therapeutic.put(geneName, split[0]);
					}
				}
								
			}
			in.close();


			HashMap map_surfaceome = new HashMap();
			
			fstream = new FileInputStream(inputSurfaceomeFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneNames = split[5];
				map_surfaceome.put(geneNames.toUpperCase(), geneNames.toUpperCase());
				
								
			}
			in.close();


			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			
			
			fstream = new FileInputStream(meta_analysis_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tMatrixDB\tTherapeuticTarget\tTherapeuticName\tDiseaseHit\tSurfaceome\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split("_")[0];
				boolean matrixdb = false;
				boolean therapeutic = false;
				boolean disease_hit = false;
				boolean surfaceome = false;
				String known_therpeutic_name = "";
				if (map.containsKey(geneName)) {
					matrixdb = true;
				}
				if (map_therapeutic.containsKey(geneName)) {
					therapeutic = true;
					known_therpeutic_name = (String)map_therapeutic.get(geneName);
					
				}
				if (map_surfaceome.containsKey(geneName)) {
					surfaceome = true;
				}
				if (split.length > 3) {
					if (!split[3].equals("") ) {
						disease_hit = true; 
					}
				}
				if (split.length > 4) {
					if (!split[4].equals("") ) {
						disease_hit = true; 
					}
				}
				out.write(str + "\t" + matrixdb + "\t" + therapeutic + "\t" + known_therpeutic_name + "\t" + disease_hit + "\t" + surfaceome + "\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
