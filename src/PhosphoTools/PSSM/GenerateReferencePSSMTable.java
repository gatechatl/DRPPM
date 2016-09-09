package PhosphoTools.PSSM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * From the PSSM Table generate background PSSM Table
 * @author tshaw
 *
 */
public class GenerateReferencePSSMTable {

	public static String type() {
		return "PSSM";
	}
	public static String description() {
		return "From the PSSM Table generate background PSSM Table";
	}
	public static String parameter_info() {
		return "[inputFolder] [PSSMTable] [outputFolder] [outputKinaseMotifIndexFile]";
	}
	public static void execute(String[] args) {
		
		try {

			String inputFolder = args[0];
			String pssmTableFile = args[1];
			String outputFolder = args[2];
			String outputKinaseMotifIndexFile = args[3];
			HashMap map = new HashMap();
			
			HashMap pssm_table = new HashMap();
			
			int index = 0;			
			FileInputStream fstream2 = new FileInputStream(pssmTableFile);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));				
			while (in2.ready()) {
				String str = in2.readLine().trim();
				String[] split = str.split("\t");
				pssm_table.put(split[0], index);
				index++;
			}
			in2.close();
			
			FileWriter fwriter2 = new FileWriter(outputKinaseMotifIndexFile);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			//(outputFolder + "/" + kinaseMotifName + ".txt");
			FileWriter[] fwriter = new FileWriter[pssm_table.size()];
			BufferedWriter[] out = new BufferedWriter[pssm_table.size()];
			Iterator itr = pssm_table.keySet().iterator();
			while (itr.hasNext()) {
				String kinaseMotifName = (String)itr.next();
				index = (Integer)pssm_table.get(kinaseMotifName);
				fwriter[index] = new FileWriter(outputFolder + "/" + kinaseMotifName + ".txt");
				out[index] = new BufferedWriter(fwriter[index]);
				out2.write(outputFolder + "/" + kinaseMotifName + ".txt\n");
			}
			
			out2.close();
			int k = 0;
			File file = new File(inputFolder);
			for (File f: file.listFiles()) {
				String inputFile = f.getPath();
			
				k++;
				System.out.println(k);
				HashMap type = new HashMap();
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				String header = in.readLine();
				String[] split_header = header.split("\t");				
				while (in.ready()) {
					String str = in.readLine().trim();
					String[] split = str.split("\t");
					String site = split[0];
					String peptide = split[1].substring(7, 8);
					for (int i = 2; i < split.length; i++) {
						String kinaseMotifName = split_header[i];
						index = (Integer)pssm_table.get(kinaseMotifName);
						
						if (kinaseMotifName.split("_")[1].contains(peptide)) {
							out[index].write(site + "\t" + split[i] + "\t" + split[1] + "\n");
							out[index].flush();
							/*if (map.containsKey(kinaseMotifName)) {
								HashMap location = (HashMap)map.get(kinaseMotifName);
								location.put(site, new Double(split[i]));
								map.put(split_header[i], location);
							} else {
								HashMap location = new HashMap();
								location.put(site, new Double(split[i]));
								map.put(split_header[i], location);
							}*/
						}
					}
				}
				in.close();

			}
			
			
			/*itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String kinaseMotifName = (String)itr.next();
				out2.write(outputFolder + "/" + kinaseMotifName + ".txt\n");
				FileWriter fwriter = new FileWriter(outputFolder + "/" + kinaseMotifName + ".txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				
				HashMap location = (HashMap)map.get(kinaseMotifName);
				Iterator itr2 = location.keySet().iterator();
				while (itr2.hasNext()) {
					String site = (String)itr2.next();
					double value = (Double)location.get(site);
					out.write(site + "\t" + value + "\n");
				}
				out.close();
			}*/
			
			
			itr = pssm_table.keySet().iterator();
			while (itr.hasNext()) {
				String kinaseName = (String)itr.next();
				index = (Integer)pssm_table.get(kinaseName);
				out[index].close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
