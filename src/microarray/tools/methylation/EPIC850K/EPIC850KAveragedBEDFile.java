package microarray.tools.methylation.EPIC850K;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class EPIC850KAveragedBEDFile {

	public static String description() {
		return "Generate median BED file";
	}
	public static String type() {
		return "METHYLATION";
	}
	public static String parameter_info() {
		return "[inputFile] [refFile] [sampleNameFile] [groupName] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = args[0]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Leventaki_850K_108399_methylation_table.txt";
			String refFile = args[1]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\MethylationEPIC_v-1-0_B4.csv";
			String sampleNameFile = args[2];
			String groupName = args[3];
			String outputFile = args[4];
			LinkedList sampleName = new LinkedList();
			FileInputStream fstream = new FileInputStream(sampleNameFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				sampleName.add(str);
			}
						
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
					try {
						map.put(id, "chr" + chr + "\t" + loc + "\t" + (new Integer(loc) + 1));
					} catch (Exception ex) {
						
					}
				}
			}
			in.close();
			
			System.out.println("Finished loading refFile");
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String header1 = "track type=bedGraph name=\"";
			String header2 = "\" description=\"BedGraph format\" visibility=full color=200,100,0 altColor=0,100,200 priority=20";;
			//String[] group1 = {"SJALCL014724", "SJALCL014725"};
			//String[] group1 = {"SJALCL017844", "SJALCL017851", "SJALCL017856", "SJALCL017859", "SJALCL017863", "SJALCL045611", "SJALCL045615", "SJALCL045616", "SJALCL045620", "SJALCL045621", "SJALCL017846", "SJALCL017847", "SJALCL017858", "SJALCL045613", "SJALCL045614", "SJALCL045627", "SJALCL045629", "SJALCL045631", "SJNORM016314_G1-Thy5_34", "SJNORM016314_G3-Thy5_1a_3_48DP", "SJNORM016314_G4-Thy5_1a_3_48SP", "SJNORM016314_G2-Thy5_1a_48DP", "SJNORM016314_G5-Thy5_3_48SP", "SJNORM018231_G1-Thy9_34", "SJNORM018231_G3-Thy9_1a_3_48DP", "SJNORM018231_G4-Thy9_1a_3_48SP", "SJNORM018231_G2-Thy9_1a_48DP"};
			/*BufferedWriter[] out = new BufferedWriter[group1.length];
			
			for (int i = 0; i < group1.length; i++) {
				String outputFile = "T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\analysis\\" + group1[i] + ".bedgraph";
				FileWriter fwriter = new FileWriter(outputFile);
				out[i] = new BufferedWriter(fwriter);
				out[i].write(header1 + group1[i] + header2 + "\n");
			}*/
			
			out.write(header1 + groupName + header2 + "\n");
			String[] group1 = new String[sampleName.size()];
			int k = 0;
			Iterator itr = sampleName.iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				group1[k] = name;
				k++;
			}
			LinkedList group1_list = new LinkedList();
			
			HashMap index_conversion = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			String[] header_split = in.readLine().split("\t");
			for (int i = 0; i < header_split.length; i++) {
				int hit = -1;
				for (int j = 0; j < group1.length; j++) {
					if (header_split[i].contains(group1[j]) && header_split[i].contains("AVG_Beta")) {
						group1_list.add(i);
						index_conversion.put(j, i);
					}
				}
			}						
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String id = split[0];				
				if (map.containsKey(id)) {
					String value = (String)map.get(id);
					LinkedList value_list = new LinkedList();
					for (int j = 0; j < group1.length; j++) {						
						int index = (Integer)index_conversion.get(j);
						if (!(split[index].trim().equals("") || split[index].trim().equals("NA"))) {
							value_list.add(split[index]);
							//out[j].write(value + "\t" + split[index] + "\n");
						}
					}
					double[] values = MathTools.convertListStr2Double(value_list);
					double mean = MathTools.mean(values);
					out.write(value + "\t" + mean + "\n");
				}
			}
			in.close();
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
