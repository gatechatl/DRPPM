package rnaseq.splicing.cseminer.exonannotation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Not used in the paper
 * @author 4472414
 *
 */
public class CSEMinerAnnotateGeneWithNormalSampleExonExpression {

	
	public static void main(String[] args) {
		
		try {
			
			String inputFile = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/Exon_Pediatric_GTEx_TissueEnrichment_20230314.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
