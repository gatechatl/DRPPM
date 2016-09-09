package WholeExonTool.Summarize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class McKinnonAddSampleTypeInfo {

	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\data.txt";
			String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\data2.txt";
			String sampleFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\samples.txt";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\output.txt";
			
			String outputFile2 = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\output_averageTypefrequency.txt";
			String outputFile3 = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\output_TsTv.txt";

			FileWriter fwriter2 = new FileWriter(outputFile2);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			FileWriter fwriter3 = new FileWriter(outputFile3);
			BufferedWriter out3 = new BufferedWriter(fwriter3);
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(sampleFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String type = str.split("-")[1];
				String sampleName = str.split("_")[0];
				map.put(sampleName, type);
			}
			in.close();
			
			HashMap count_map = new HashMap();
			HashMap type = new HashMap();
			HashMap map_sample = new HashMap();
			HashMap map_sample_transition = new HashMap();
			HashMap map_sample_transversion = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\tMutType\tSampleType\tTsOrTv\tSampleNameType" + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String sampleName = split[1];
				String ref_allele = split[13];
				String mut_allele = split[14];
				
				String mut_type = ref_allele + "->" + mut_allele;
				boolean isTransition = true; 
				if (mut_type.equals("G->C")) {
					mut_type = "G->C";
				}
				if (mut_type.equals("C->G")) {
					mut_type = "G->C";
				}
				if (mut_type.equals("G->A")) {
					mut_type = "G->A";
				}
				if (mut_type.equals("C->T")) {
					mut_type = "G->A";
				}
				if (mut_type.equals("G->T")) {
					mut_type = "G->T";
				}
				if (mut_type.equals("C->A")) {
					mut_type = "G->T";
				}
				if (mut_type.equals("T->G")) {
					mut_type = "A->C";
				}
				if (mut_type.equals("T->A")) {
					mut_type = "A->T";
				}
				if (mut_type.equals("T->C")) {
					mut_type = "A->G";
				}
				if (mut_type.equals("A->C")) {
					mut_type = "A->C";
				}
				if (mut_type.equals("A->T")) {
					mut_type = "A->T";
				}
				if (mut_type.equals("A->G")) {
					mut_type = "A->G";
				}
				
				if (mut_type.equals("G->A") || mut_type.equals("T->C")) {
					isTransition = true;
				} else {
					isTransition = false;
				}
				type.put(mut_type, mut_type);
				//if (split[0].equals("SJHQ") && split[4].equals("silent")) {
				count_map.put(sampleName, sampleName);
				if (split[0].equals("SJHQ") && split[4].equals("missense")) {
					if (map_sample.containsKey(sampleName)) {
						HashMap count = (HashMap)map_sample.get(sampleName);
						if (count.containsKey(mut_type)) {
							int num = (Integer)count.get(mut_type);
							num = num + 1;
							count.put(mut_type, num);
							map_sample.put(sampleName, count);
						} else {
							count.put(mut_type, 1);
							map_sample.put(sampleName, count);
						}
					} else {
						HashMap count = new HashMap();
						count.put(mut_type, 1);
						map_sample.put(sampleName, count);
					}									
					if (isTransition) {
						if (map_sample_transition.containsKey(sampleName)) {
							int count = (Integer)map_sample_transition.get(sampleName);
							map_sample_transition.put(sampleName, count + 1);
							
						} else {														
							map_sample_transition.put(sampleName, 1);
						}					
					} else {
						if (map_sample_transversion.containsKey(sampleName)) {
							int count = (Integer)map_sample_transversion.get(sampleName);
							map_sample_transversion.put(sampleName, count + 1);
							
						} else {														
							map_sample_transversion.put(sampleName, 1);
						}
					}
					
					if (map.containsKey(sampleName)) {
						String sampleType = (String)map.get(sampleName);
						String transition_or_transversion = "Transversions";
						if (isTransition) {
							transition_or_transversion = "Transitions";
						}
						
						
						out.write(str + "\t" + mut_type + "\t" + sampleType + "\t" + transition_or_transversion + "\t" + sampleType + "_" + split[1] + "\n");
						
					} else {
						//System.out.println(sampleName);
					}
				}
				
				
			}
			in.close();
			out.close();
			
			System.out.println("CountMap: " + count_map.size());
			out2.write("SampleName\tSampleType\tMutType\tFrequency\n");
			out3.write("SampleName\tSampleType\tTsTv\tTransition\tTransversion\n");
			
			/*Iterator itr2 = type.keySet().iterator();
			while (itr2.hasNext()) {
				String mutType = (String)itr2.next();
				out2.write("\t" + mutType);
			}*/

			// calculate Transition vs Transversion
			Iterator itr = map_sample.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (map.containsKey(sampleName)) {
					String sampleType = (String)map.get(sampleName);
					double count_transition = 0;
					if (map_sample_transition.containsKey(sampleName)) {
						count_transition = (Integer)map_sample_transition.get(sampleName);
					}
					double count_transversion = 0;
					if (map_sample_transversion.containsKey(sampleName)) {
						count_transversion = (Integer)map_sample_transversion.get(sampleName);					
					}
					if ((count_transition + count_transversion) > 0 && count_transversion > 0) {
						out3.write(sampleName + "\t" + sampleType + "\t" + (count_transition / count_transversion) + "\t" + count_transition + "\t" + count_transversion + "\n");
					}
				}
			}
			
			
			itr = map_sample.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				String sampleType = (String)map.get(sampleName);
				HashMap count = (HashMap)map_sample.get(sampleName);
				
				if (map.containsKey(sampleName)) {
					int total = grabNum(count);				
					String allType = "";
					Iterator itr2 = type.keySet().iterator();
					while (itr2.hasNext()) {
						String mutType = (String)itr2.next();
						if (count.containsKey(mutType)) {
							
							double num = (Integer)count.get(mutType);
							double transition = 0.0000001;
							double transversion = 0.0000001;
							
							out2.write(sampleName + "\t" + sampleType + "\t" + mutType + "\t" + num / total  + "\n");
							
						} else {
							double num = 0;
							out2.write(sampleName + "\t" + sampleType + "\t" + mutType + "\t" + num / total + "\n");
						}
					}
				}
				//out2.write(sampleName + "\t" + sampleType + allType + "\n");
			}
			out2.close();
			out3.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int grabNum(HashMap map) {
		int total = 0;
		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
			String mutType = (String)itr.next();
			int count = (Integer)map.get(mutType);
			total += count;
		}
		return total;
	}
}
