package stjude.projects.jinghuizhang.dexseq.exon.annotation.gtex;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class JinghuiZhangAppendGTExSampleInformation {

	
	public static void main(String[] args) {
		
		
		try {
			
			String inputGTExSampleInfo = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FeatureIntegration\\PanTARGET\\temp_patient_id_conversion.txt";
			FileInputStream fstream = new FileInputStream(inputGTExSampleInfo);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			//String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
