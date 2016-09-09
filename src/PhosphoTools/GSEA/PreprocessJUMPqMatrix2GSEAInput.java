package PhosphoTools.GSEA;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This version is primarily for Hongbo's dataset
 * This dataset is not site specific
 * @author tshaw
 *
 */
public class PreprocessJUMPqMatrix2GSEAInput {

	
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			/**
			 * sampleInfo should have three columns
			 * sampleName, SampleAlias, WTvsKO id
			 */
			String sampleInfo = args[1];
			
			String outputGCT = args[2]; // matrix containing gene expression
			String outputCLS = args[3];
			
			LinkedList sampleNameList = new LinkedList();
			LinkedList sampleNameType = new LinkedList();
			FileInputStream fstream = new FileInputStream(sampleInfo);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sampleNameList.add(split[0]);
				sampleNameType.add(split[1]);
			}
			in.close();
			
			HashMap map = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0].split("\\|")[1] + "_" + split[1].split(",")[0].replaceAll("*", "");
				String motif = split[5];
				if (map.containsKey(motif)) {
					LinkedList list = (LinkedList)map.get(motif);
					if (!list.contains(name)) {
						list.add(name);
					}
					map.put(motif, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(name);
					map.put(motif, list);
				}
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
