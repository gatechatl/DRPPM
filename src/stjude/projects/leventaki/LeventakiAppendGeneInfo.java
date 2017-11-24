package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LeventakiAppendGeneInfo {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String outputFile = "T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Leventaki_850K_methylation_table_WilcoxResult_CandidateTable_20170926.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputFile = "T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Leventaki_850K_methylation_table_WilcoxResult_0.01_cutoff_20170926.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			out.write(header + "\tcolor_channel\tchr\tloc\tstrand\trefseq_geneName\trefseq_accession\tbodyinformation\tcpgisland\tseq\n");
			while (in.ready()) {
				String str = in.readLine().replaceAll("\"", "");
				String[] split = str.split("\t");
				//System.out.println(split[0]);
				map.put(split[0], str);
			}
			//String refFile = "T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\HumanMethylation450_15017482_v1-2.csv";
			String refFile = "T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\MethylationEPIC_v-1-0_B4.csv";
			fstream = new FileInputStream(refFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(",");
				if (split.length > 30) {
					String id = split[0];
					String type = split[8];
					String chr = split[11];
					String loc = split[12];
					String strand = split[14];
					String ucsc_refgenename = split[15];
					String ucsc_refseq_accession = split[16];
					String ucsc_bodyinformation = split[17];
					String cpgIsland = split[18];
					String seq = split[9];
					//System.out.println(id);
					if (map.containsKey(id)) {
						String line = (String)map.get(id);
						out.write(line + "\t" + type + "\t" + chr + "\t" + loc + "\t" + strand + "\t" + ucsc_refgenename + "\t" + ucsc_refseq_accession + "\t" + ucsc_bodyinformation + "\t" + cpgIsland + "\t" + seq + "\n");
					}
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
