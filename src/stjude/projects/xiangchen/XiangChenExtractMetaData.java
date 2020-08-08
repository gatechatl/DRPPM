package stjude.projects.xiangchen;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.xmlbeans.SchemaStringEnumEntry;

public class XiangChenExtractMetaData {

	public static String description() {
		return "Xiang Chen, grab the metainformation for all the top variable genes.";
	}
	public static String type() {
		return "XIANGCHEN";
	}
	public static String parameter_info() {
		return "[inputTopVariableGenes] [metaInformationFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputTopVariableGenes = args[0]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Leventaki_850K_108399_methylation_table.txt";
			String metaInformationFile = args[1];
			HashMap id2result = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputTopVariableGenes);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				id2result.put(str.trim(), "");
				
			}
			in.close();
			fstream = new FileInputStream(metaInformationFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine().replaceAll("," , "\t");
			System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(",");
				if (id2result.containsKey(split[0])) {
					System.out.println(str.replaceAll(",", "\t"));
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}
}
