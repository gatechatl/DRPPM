package stjude.projects.suzannebaker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class StemLineagePlot {

	/**
	 * Take the normalized values for stemness and input from 
	 * @param args
	 */
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			double[] ES = new double[split_header.length - 1];
			double[] OL = new double[split_header.length - 1];
			double[] OPC = new double[split_header.length - 1];
			double[] ASTRO = new double[split_header.length - 1];
			
			double[] olig_signature = new double[split_header.length - 1];
			double[] olig_or_astro = new double[split_header.length - 1];
			double[] lineage_score = new double[split_header.length - 1];
			double[] correct_lineage_score = new double[split_header.length - 1];
			double[] lineage_score_signed = new double[split_header.length - 1];
			double[] final_stem_score = new double[split_header.length - 1];
			
			
			//while (in.ready()) {
			String str = in.readLine();
			String[] split = str.split("\t");
			for (int i = 1; i < split.length; i++) {
				ES[i - 1] = new Double(split[i]);
			}
			
			str = in.readLine();
			split = str.split("\t");
			for (int i = 1; i < split.length; i++) {
				OL[i - 1] = new Double(split[i]);
			}
			
			str = in.readLine();
			split = str.split("\t");
			for (int i = 1; i < split.length; i++) {
				OPC[i - 1] = new Double(split[i]);
			}
			
			str = in.readLine();
			split = str.split("\t");
			for (int i = 1; i < split.length; i++) {
				ASTRO[i - 1] = new Double(split[i]);
			}
			//}
			// check whether OL or OPC is larger
			for (int i = 0; i < split.length - 1; i++) {
				if (OL[i] > OPC[i]) {
					olig_signature[i] = OL[i];
				} else {
					olig_signature[i] = OPC[i];
				}
			}
			
			// check whether oligo is larger than astro
			for (int i = 0; i < split.length - 1; i++) {
				if (olig_signature[i] > ASTRO[i]) {
					olig_or_astro[i] = 1;
					lineage_score[i] = olig_signature[i];
				} else {
					olig_or_astro[i] = -1;
					lineage_score[i] = ASTRO[i];
				}
			}
			// calculate stem_score 
			
			// correct the lineage score
			//for (int i = 0; i < split.length - 1; i++) {
			//	if (lineage_score[i] < 0 || stem_score)
			//}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
