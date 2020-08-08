package stjude.projects.jinghuizhang.dexseq.exon.cart.amlfredhutch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Annotate the fusion in AML
 * @author tshaw
 *
 */
public class JinghuiZhangReannotateAMLSampleInformation {

	
	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String inputFusionAnnotation = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FredHutch_RNAseq\\ExonLevelExpression_Tian\\FPKM_TPM\\CAR-T_Summary_ECM\\AML_Fusion_annotation.txt";
			FileInputStream fstream = new FileInputStream(inputFusionAnnotation);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				System.out.println(str);
				map.put(split[0], split[1]);
			}
			in.close();
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FredHutch_RNAseq\\ExonLevelExpression_Tian\\FPKM_TPM\\CAR-T_Summary_ECM\\AML_sample_annotation_fusion.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputSampleAnnotation = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FredHutch_RNAseq\\ExonLevelExpression_Tian\\FPKM_TPM\\CAR-T_Summary_ECM\\AML_sample_annotation.txt";
			fstream = new FileInputStream(inputSampleAnnotation);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sample = split[0];
				String type = split[1];
				String note = "";
				Iterator itr = map.keySet().iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					if (sample.contains(sampleName)) {
						note = (String)map.get(sampleName);
					}
				}
				if (note.equals("")) {
					note = "OTHER";
				}
				out.write(sample + "\t" + type + "_" + note + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
