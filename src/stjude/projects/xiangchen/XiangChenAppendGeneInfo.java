package stjude.projects.xiangchen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XiangChenAppendGeneInfo {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String outputFile = "\\\\gsc.stjude.org\\project_space\\chen1grp\\Wilms\\common\\2017-11-02_easton_mulder_850K_120449\\Easton_120449_sample_methylation_data_clean_append_meta_withNA.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputFile = "\\\\gsc.stjude.org\\project_space\\chen1grp\\Wilms\\common\\2017-11-02_easton_mulder_850K_120449\\Easton_120449_sample_methylation_data.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[0] + "\t" + split_header[1] + "\t" + split_header[2] + "\t" + split_header[3]);
			for (int i = 4; i < split_header.length; i++) {
				if (split_header[i].contains("AVG_Beta")) {
					out.write("\t" + split_header[i]);
				}
			}
			out.write("\tillumina_type\tcolor_channel\tchr\tloc\tstrand\trefseq_geneName\trefseq_accession\tbodyinformation\tcpgisland\tseq\tphantom4_enhancer\tphantom5_enhancer\tDMR\tOpenChromatin_NAME\ttfbs\tSNP_ID\n");
			while (in.ready()) {
				String str = in.readLine().replaceAll("\"", "");
				String[] split = str.split("\t");
				//System.out.println(split[0]);
				boolean noNA = true;
				String new_str = split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + split[3];
				for (int i = 4; i < split_header.length; i++) {
					if (split_header[i].contains("AVG_Beta")) {
						new_str += "\t" + split[i];
						//if (split[i].equals("NA") || split[i].equals(" ") || split[i].trim().equals("")) {
						//	noNA = false;
						//}
					}
				}
				if (noNA) {
					map.put(split[1], new_str);
				}
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
					
					String methyl_type = split[6];
					if (split[6].equals("II")) {
						methyl_type = "2";
					}
					if (split[6].equals("I")) {
						methyl_type = "1";
					}
					String color_type = split[8];
					String seq = split[9];
					String chr = split[11];
					String loc = split[12];
					String strand = split[14];
					String ucsc_refgenename = split[15];
					String ucsc_refseq_accession = split[16];
					String ucsc_bodyinformation = split[17];
					String cpgIsland = split[18];
					String phantom4_enhancer = split[20];
					String phantom5_enhancer = split[21];
					String DMR = split[22];
					String OpenChromatin_NAME = split[35];
					String tfbs = split[37];
					String SNP_ID = split[43];
					String SNP_freq = split[44];
					String SNP_minorallelefrequency = split[45];
					//System.out.println(id);
					if (map.containsKey(id)) {
						String line = (String)map.get(id);						
						out.write(line + "\t" + methyl_type + "\t" + color_type + "\t" + chr + "\t" + loc + "\t" + strand + "\t" + ucsc_refgenename + "\t" + ucsc_refseq_accession + "\t" + ucsc_bodyinformation + "\t" + cpgIsland + "\t" + seq + "\t" + phantom4_enhancer + "\t" + phantom5_enhancer + "\t" + DMR + "\t" + OpenChromatin_NAME + "\t" + tfbs + "\t" + SNP_ID + "\t" + SNP_freq + "\t" + SNP_minorallelefrequency + "\n");
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
