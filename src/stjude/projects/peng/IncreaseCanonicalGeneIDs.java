package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Some of the UCSC gene IDs might be replaced by the new ones in the canonical gene list. Adding the old UCSC gene ids to the canonical gene list 
 * @author tshaw
 *
 */
public class IncreaseCanonicalGeneIDs {
	public static String description() {
		return "Some of the UCSC gene IDs might be replaced by the new ones in the canonical gene list. Adding the old UCSC gene ids to the canonical gene list";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[canonicalGeneSet] [kgXrefFile]";
	}
	
	public static void execute(String[] args) {
		try {

			HashMap geneName2ucscGeneID = new HashMap();
			String canonicalGeneSet = args[0];
			String kgXrefFile = args[1];
			FileInputStream fstream = new FileInputStream(kgXrefFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[4].equals("")) {
					geneName2ucscGeneID.put(split[4], split[0]);
				}
			}
			in.close();
			
			HashMap missing = new HashMap();
			fstream = new FileInputStream(canonicalGeneSet);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				String ucscID = split[1];
				System.out.println(str);
				if (geneName2ucscGeneID.containsKey(geneName)) {
					String kgXref_ucscID = (String)geneName2ucscGeneID.get(geneName);
					String[] split_ucscID = kgXref_ucscID.split("\\.");
					if (!split_ucscID[0].equals(ucscID)) {
						System.out.println(geneName + "\t" + split_ucscID[0] + "\t" + split_ucscID[1]);
					}
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
