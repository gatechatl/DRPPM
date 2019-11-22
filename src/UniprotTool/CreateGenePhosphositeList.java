package UniprotTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import proteomics.phospho.tools.painter.KinaseSubstratePainter.Domain;
import proteomics.phospho.tools.painter.KinaseSubstratePainter.UniprotGFF;

/**
 * Based on the uniprot gff file create a output file showing the gene and phosphosite list
 * @author tshaw
 *
 */
public class CreateGenePhosphositeList {

	public static void main(String[] args) {
		try {
			
			String outputFile = "C:\\Users\\tshaw\\Desktop\\INTEGRATION\\Uniprot\\Mouse_UNIPROT_GFF_Phospho_Ref.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = readUniprotGFF("C:\\Users\\tshaw\\Desktop\\INTEGRATION\\Uniprot\\Mouse_UNIPROT_GFF_Downloaded_20141111.txt");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				out.write(geneName + "\t" + map.get(geneName) + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap readUniprotGFF(String inputFile) {
		HashMap gff_mod = new HashMap();
		try {
			UniprotGFF gff = new UniprotGFF();
			
			String fileName = inputFile;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String result = "";
				if (split.length > 8) {
					if (split[2].contains("Modified residue")) {
						int location = new Integer(split[3]);
						if (split[8].contains("Phosphothreonine")) {
							
							result = location + "T";
						}
						if (split[8].contains("Phosphoserine")) {
							result = location + "S";
						}
						
						if (split[8].contains("Phosphotyrosine")) {
							result = location + "Y";
						}
	
						if (!result.equals("")) {
							if (gff_mod.containsKey(split[0])) {
								String prev = (String)gff_mod.get(split[0]);
								prev += result;
								gff_mod.put(split[0], prev + ",");
							} else {
								gff_mod.put(split[0], result + ",");
							}
						}
					}
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gff_mod;
	}
	public static String parseOutNote(String str) {
		String result = "";
		if (str.contains("Note=")) {
			result = str.replaceAll("Note=", "");
			result = result.split(";")[0];
		}
		return result;
	}
}
