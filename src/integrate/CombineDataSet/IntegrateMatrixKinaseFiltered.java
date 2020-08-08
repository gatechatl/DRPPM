package integrate.CombineDataSet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * We need to filter the kinase list based on anova's fdr
 * @author tshaw
 *
 */
public class IntegrateMatrixKinaseFiltered {

	public static void main(String[] args) {
		
		try {

			HashMap goodKinase = new HashMap();
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\KinaseInformationFiltered_v2_20160414.txt";
		
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String anova_filtered = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\Kinase_IKAP_activity_change_along_Tcell_activation.txt";
			FileInputStream fstream = new FileInputStream(anova_filtered);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				goodKinase.put(split[0], split[split.length - 2] + "\t" + split[split.length - 1]);
			}
			String kinaseListFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\KinaseInformation.txt";
			fstream = new FileInputStream(kinaseListFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			//out.write(in.readLine() + "\tAno_Pval\tAno_FDR\n");
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (goodKinase.containsKey(split[0])) {
					String line = (String)goodKinase.get(split[0]);
					out.write(str + "\t" + line + "\n");
				}
			}
			in.close();									
			out.close();
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
