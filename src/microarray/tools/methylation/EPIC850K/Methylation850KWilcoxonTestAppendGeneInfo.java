package microarray.tools.methylation.EPIC850K;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;




public class Methylation850KWilcoxonTestAppendGeneInfo {

	public static String description() {
		return "Append EPIC Methyl 850K Annoation information.";
	}
	public static String type() {
		return "METHYLATION";
	}
	public static String parameter_info() {
		return "[inputFile] [MethylationEPIC_v-1-0_B4_clean.csv] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String outputFile = args[2]; //"\\\\gsc.stjude.org\\project_space\\chen1grp\\Wilms\\common\\2017-11-02_easton_mulder_850K_120449\\Easton_120449_sample_methylation_data_clean_append_meta_withNA.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputFile = args[0]; //"\\\\gsc.stjude.org\\project_space\\chen1grp\\Wilms\\common\\2017-11-02_easton_mulder_850K_120449\\Easton_120449_sample_methylation_data.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(header);
			/*for (int i = 1; i < split_header.length; i++) {
				if (split_header[i].contains("AVG_Beta")) {
					out.write("\t" + split_header[i]);
				}
			}*/
			out.write("\tillumina_type\tcolor_channel\tchr\tloc\tstrand\trefseq_geneName\trefseq_accession\tbodyinformation\tcpgisland\tseq\tphantom4_enhancer\tphantom5_enhancer\tDMR\tOpenChromatin_NAME\ttfbs\tSNP_ID\tSNP_freq\tSNP_minorallelefrequency\n");
			while (in.ready()) {
				String str = in.readLine().replaceAll("\"", "");
				String[] split = str.split("\t");					
				map.put(split[0], str);
				
			}
			//String refFile = "T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\HumanMethylation450_15017482_v1-2.csv";
			String refFile = args[1]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\MethylationEPIC_v-1-0_B4.csv";
			fstream = new FileInputStream(refFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(",");
				
				if (split.length > 7) {
					String id = split[0];
					
					String methyl_type = split[6];
					if (split[6].equals("II")) {
						methyl_type = "2";
					}
					if (split[6].equals("I")) {
						methyl_type = "1";
					}
					
					String color_type = "";
					if (split.length > 8) {
						color_type = split[8];
					}
					String seq = "";
					if (split.length > 9) {
						seq = split[9];
					}
					
					String chr = "";
					if (split.length > 11) {
						chr = split[11];
					}
					String loc = "";
					if (split.length > 12) {
						loc = split[12];
					}
					String strand = "";
					if (split.length > 14) {
						strand = split[14];
					}
					String ucsc_refgenename = "";
					if (split.length > 15) {
						ucsc_refgenename = split[15];
					}
					String ucsc_refseq_accession = "";
					if (split.length > 16) {
						ucsc_refseq_accession = split[16];
					}
					String ucsc_bodyinformation = "";
					if (split.length > 17) {
						ucsc_bodyinformation = split[17];
					}
					String cpgIsland = "";
					if (split.length > 18) {
						cpgIsland = split[18];
					}
					String phantom4_enhancer = "";
					if (split.length > 20) {
						phantom4_enhancer = split[20];
					}
					String phantom5_enhancer = "";
					if (split.length > 21) {
						phantom5_enhancer = split[21];
					}
					String DMR = "";
					if (split.length > 22) {
						DMR = split[22];
					}
					String OpenChromatin_NAME = "";
					if (split.length > 35) {
						OpenChromatin_NAME = split[35];
					}
					String tfbs = "";
					if (split.length > 37) {
						tfbs = split[37];
					}
					String SNP_ID = "";
					if (split.length > 43) {
						SNP_ID = split[43];
					}
					String SNP_freq = "";
					if (split.length > 44) {
						SNP_freq = split[44];
					}
					String SNP_minorallelefrequency = "";
					if (split.length > 45) {
						SNP_minorallelefrequency = split[45];
					}
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
