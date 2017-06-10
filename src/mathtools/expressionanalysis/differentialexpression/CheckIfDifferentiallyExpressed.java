package mathtools.expressionanalysis.differentialexpression;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Check whether a list of differentially expressed 
 * gene list is truly differentially expressed
 * @author tshaw
 *
 */
public class CheckIfDifferentiallyExpressed {
	public static void execute(String[] args) {
		
		try {

			HashMap filterList = new HashMap();
			String gene_list_file = args[1];
			FileInputStream fstream = new FileInputStream(gene_list_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				filterList.put(str, str);
			}
			in.close();
			
			HashMap DE_genes = new HashMap();
			String DE_Gene_File = args[0];
			fstream = new FileInputStream(DE_Gene_File);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				DE_genes.put(split[0], split[0]);
				String gene = split[0].replaceAll("\"",  "");
				if (filterList.containsKey(gene)) {
					System.out.println(gene);
				}
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
