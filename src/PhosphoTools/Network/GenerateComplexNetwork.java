package PhosphoTools.Network;

import idconversion.tools.Uniprot2GeneID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Generate a complex network given the complex file downloaded from
 * A Census of Human Soluble Protein Complexes
 * @author tshaw
 *
 */
public class GenerateComplexNetwork {

	public static void execute(String[] args) {
		
		try {
			
			String complexFile = args[0];
			String uniprot2geneIDFile = args[1];
			String outputFile = args[2];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap uniprot2geneID = Uniprot2GeneID.uniprot2geneID(uniprot2geneIDFile);
			
			HashMap map = new HashMap();	
			FileInputStream fstream = new FileInputStream(complexFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();	
				String[] split = str.split("\t");
							
				if (uniprot2geneID.containsKey(split[2])) {
					if (map.containsKey(split[0])) {
						String geneName = (String)uniprot2geneID.get(split[2]);
						String geneList = (String)map.get(split[0]);
						map.put(split[0], geneList + "," + geneName);
					} else {
						String geneName = (String)uniprot2geneID.get(split[2]);
						map.put(split[0], geneName);						
					}
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String complexID = (String)itr.next();
				String geneListStr = (String)map.get(complexID);
				String[] geneList = geneListStr.split(",");
				
				for (int i = 0; i < geneList.length; i++) {
					for (int j = i + 1; j < geneList.length ; j++) {
						out.write("EntrezID\t" + geneList[i] + "\tEntrezID\t" + geneList[j] + "\n");
					}
				}
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
