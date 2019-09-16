package stjude.projects.jinghuizhang.fusion2expression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Convert the TARGET SJ sample name to target id
 * @author tshaw
 *
 */
public class JinghuiZhangConvertSampleName {

	public static void main(String[] args) {
		
		try {
			
			HashMap sjid2targetid = new HashMap();
			
			String inputConversionFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FeatureIntegration\\PanTARGET\\temp_patient_id_conversion.txt";
			FileInputStream fstream = new FileInputStream(inputConversionFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			//String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sjid2targetid.put(split[2], split[1]);
				
			}
			in.close();
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FeatureIntegration\\PanTARGET\\TARGET.combined.fpkm.geneNameOnly.targetid.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			String inputTARGETFPKMFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TARGET_RNAseq\\from_strongarm_mapping\\htseq\\TARGET.combined.fpkm.geneNameOnly.txt";
			fstream = new FileInputStream(inputTARGETFPKMFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[0]);
			for (int i = 1;i < split_header.length; i++) {
				if (sjid2targetid.containsKey(split_header[i])) {
					String targetid = (String)sjid2targetid.get(split_header[i]);
					targetid = targetid.substring(0, targetid.length() - 1) + "D";
					out.write("\t" + targetid);
				} else {
					out.write("\t" + split_header[i]);
				}
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				out.write(str + "\n");				
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
