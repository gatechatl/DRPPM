package TranscriptionFactorTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import mathtools.expressionanalysis.differentialexpression.AddAnnotation2DiffFisher;

/**
 * Input list of gene and output list of transcription factors
 * @author tshaw
 *
 */
public class TFRegulatedGenes {

	public static void execute(String[] args) {
		
		try {
			
			HashMap query = new HashMap();
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				query.put(str, str);
			}
			
			String outputFile = args[2];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			HashMap map = AddAnnotation2DiffFisher.generateHashMapList(args[1]);
			Iterator itr = map.keySet().iterator();
			String additional_header = "";
			LinkedList tags = new LinkedList();
			while (itr.hasNext()) {
				String tag = (String)itr.next();
				int found = 0;
				String geneHits = "";
				HashMap geneList = (HashMap)map.get(tag);
				Iterator itr2 = geneList.keySet().iterator();
				while (itr2.hasNext()) {
					String gene = (String)itr2.next();
					if (query.containsKey(gene)) {
						found++;
						if (geneHits.equals("")) {
							geneHits += gene;
						} else {
							geneHits += "," + gene;
						}
					}
				}
				if (found > 0) {
					out.write(tag + "\t" + found + "\t" + geneHits + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
