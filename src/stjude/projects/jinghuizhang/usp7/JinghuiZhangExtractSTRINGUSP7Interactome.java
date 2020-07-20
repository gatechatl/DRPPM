package stjude.projects.jinghuizhang.usp7;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JinghuiZhangExtractSTRINGUSP7Interactome {

	
	public static void main(String[] args) {
		
		try {
			
			String usp7 = "9606.ENSP00000343535";
			
			HashMap map = new HashMap();
			HashMap genes = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\resgen\\dev\\wc\\tshaw\\REFERENCE\\PROTEOMICS\\PPI\\String\\9606.protein.links.detailed.v11.0.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(" ");
				if (split[0].equals(usp7) || split[1].equals(usp7)) {
					if (new Integer(split[6]) > 0) {
						System.out.println(str);
						if (split[0].equals(usp7)) {
							map.put(split[0], split[6]);
						}
						if (split[1].equals(usp7)) {
							map.put(split[1], split[6]);
						}
						genes.put(split[0], split[0]);
						genes.put(split[1], split[1]);
					}
				}
			}
			in.close();

			HashMap interactions = new HashMap();
			inputFile = "\\\\gsc.stjude.org\\resgen\\dev\\wc\\tshaw\\REFERENCE\\PROTEOMICS\\PPI\\String\\9606.protein.aliases.v11.0.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[2].contains("Ensembl_HGNC_trans_name")) {
					if (genes.containsKey(split[0])) {
						String score = (String)map.get(split[0]);
						interactions.put(split[1].split("-")[0], score);
					}
				}
			}
			in.close();
			
			Iterator itr = interactions.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				String score = (String)interactions.get(gene);
				System.out.println(gene + "\t" + score);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
