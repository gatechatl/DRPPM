package WholeExonTool.Summarize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class EXONCAPBasicStats {

	public static String parameter_info() {
		return "[inputFile] [sampleFile] [mutType] [outputStatTable] [outputMutType1] [outputMutType2] [outputMutType3] [outputMutType4]";
	}
	public static void execute(String[] args) {
		
		try {
			
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\2013_SNV.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\2015_SNV_unfiltered.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\2015_SNV_final.txt";
			String inputFile = args[0];
			String sampleFile = args[1];
			String mutType = args[2];
			String outputStatTable = args[3];
			String outputMutType1 = args[4];
			String outputMutType2 = args[5];
			String outputMutType3 = args[6];
			String outputMutType4 = args[7];
			String outputMutType5 = args[8];
			
			generateBasicTable(inputFile, sampleFile, outputStatTable, outputMutType5);
			generateMutTypeFiles(inputFile, sampleFile, mutType, outputMutType1, outputMutType2, outputMutType3, outputMutType4);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void generateMutTypeFiles(String inputFile, String sampleFile, String muttype, String output1, String output2, String output3, String output4) {
		
		try {
			HashMap map = new HashMap();
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\data.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\data2.txt";
			//String sampleFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\samples.txt";
			String outputFile = output1; //"C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\output.txt";
			
			String outputFile2 = output2; //"C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\output_averageTypefrequency.txt";
			String outputFile3 = output3; //"C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\output_TsTv.txt";

			String outputFile4 = output4;
			
			FileWriter fwriter2 = new FileWriter(outputFile2);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			FileWriter fwriter3 = new FileWriter(outputFile3);
			BufferedWriter out3 = new BufferedWriter(fwriter3);
			
			FileWriter fwriter4 = new FileWriter(outputFile4);
			BufferedWriter out4 = new BufferedWriter(fwriter4);
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			

			FileInputStream fstream = new FileInputStream(sampleFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = split[1];
				String sampleName = split[0];
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
				
				String sampleName = split[2];
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
				
				if (split[1].equals("SJHQ") && (split[5].toUpperCase().equals(muttype.toUpperCase()) || muttype.toUpperCase().equals("ALL"))) {
					count_map.put(sampleName, sampleName);
					//if (split[0].equals("SJHQ") && split[5].equals("missense")) {
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
			String mutTypeList = "";
			Iterator itr3 = type.keySet().iterator();
			while (itr3.hasNext()) {
				String mutType = (String)itr3.next();				
				mutTypeList += "\t" + mutType;
			}
			out4.write("sampleName" + "\t" + "sampleType" + mutTypeList  + "\n");
			
			itr = map_sample.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				String sampleType = (String)map.get(sampleName);
				HashMap count = (HashMap)map_sample.get(sampleName);
				out4.write(sampleName + "\t" + sampleType);
				if (map.containsKey(sampleName)) {
					int total = grabNum(count);				
					String allType = "";
					Iterator itr2 = type.keySet().iterator();
					while (itr2.hasNext()) {
						String mutType = (String)itr2.next();
						double num = 0;
						if (count.containsKey(mutType)) {
							num = (Integer)count.get(mutType);
						}
						out4.write("\t" + num);
					}
				}
				out4.write("\n");
			}
			out4.close();
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
	/**
	 * 
	 * @param inputFile
	 * @param sampleFile
	 * @param outputFile
	 * @param outputChromosomeFile
	 */
	public static void generateBasicTable(String inputFile, String sampleFile, String outputFile, String outputChromosomeFile) {
		try {
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_chr = new FileWriter(outputChromosomeFile);
			BufferedWriter out_chr = new BufferedWriter(fwriter_chr);
			
			//String sampleFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\samples.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\2015_SNV_filtered.txt";
			HashMap sampleType = new HashMap();
			HashMap chr_list = new HashMap();
			HashMap map = new HashMap();
			HashMap chr_map = new HashMap();
			FileInputStream fstream = new FileInputStream(sampleFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = split[1];				
				String sampleName = split[0];
				map.put(sampleName, type);
				
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String quality = split[1];
				String sampleName = split[2];
				String type = split[5];
				String chr = split[3];
				
				if (quality.contains("SJHQ")) {
					chr_list.put(chr, chr);
					if (chr_map.containsKey(sampleName)) {
						HashMap chr_count = (HashMap)chr_map.get(sampleName);
						if (chr_count.containsKey(chr)) {
							int count = (Integer)chr_count.get(chr);
							chr_count.put(chr, count + 1);
						} else {
							chr_count.put(chr, 1);
						}
						chr_map.put(sampleName, chr_count);
					} else {
						HashMap chr_count = new HashMap();
						if (chr_count.containsKey(chr)) {
							int count = (Integer)chr_count.get(chr);
							chr_count.put(chr, count + 1);
						} else {
							chr_count.put(chr, 1);
						}
						chr_map.put(sampleName, chr_count);
					}
					if (sampleType.containsKey(sampleName)) {
						HashMap mutType = (HashMap)sampleType.get(sampleName);
						if (mutType.containsKey(type)) {
							int count = (Integer)mutType.get(type);
							mutType.put(type, count + 1);
						} else {
							mutType.put(type, 1);
						}
						sampleType.put(sampleName, mutType);
					} else {
						HashMap mutType = new HashMap();
						if (mutType.containsKey(type)) {
							int count = (Integer)mutType.get(type);
							mutType.put(type, count + 1);
						} else {
							mutType.put(type, 1);
						}
						sampleType.put(sampleName, mutType);
					}
				}
			}
			in.close();
			
			out_chr.write("sampleName");
			Iterator itr = chr_list.keySet().iterator();
			while (itr.hasNext()) {
				String chr = (String)itr.next();
				out_chr.write("\tchr" + chr);
			}
			out_chr.write("\n");
						
			itr = sampleType.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				out_chr.write(sampleName);
				HashMap chr_count = (HashMap)chr_map.get(sampleName);
				
				Iterator itr3 = chr_list.keySet().iterator();
				while (itr3.hasNext()) {
					String chr = (String)itr3.next();
					if (chr_count.containsKey(chr)) {
						int count = (Integer)chr_count.get(chr);
						out_chr.write("\t" + count);
					} else {
						out_chr.write("\t" + 0);
					}
				}
				out_chr.write("\n");
			}
			out_chr.close();
			//System.out.println("sampleName\tmissense\tsilent\tnonsense\texon\tsplice\tsplice_region\tUTR_3\tUTR_5");
			out.write("sampleName\tmissense\tsilent\tnonsense\texon\tsplice\tsplice_region\tUTR_3\tUTR_5\tTotal\n");
			itr = sampleType.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				String sampeType = (String)map.get(sampleName);
				HashMap mutType = (HashMap)sampleType.get(sampleName);
				int missense = 0;
				int silent = 0;
				int exon = 0;
				int splice = 0;
				int splice_region = 0;
				int UTR_3 = 0;
				int UTR_5 = 0;
				int nonsense = 0;
				int total = 0;
				
				if (mutType.containsKey("missense")) {
					missense = (Integer)mutType.get("missense");
				}
				if (mutType.containsKey("silent")) {
					silent = (Integer)mutType.get("silent");
				}
				if (mutType.containsKey("exon")) {
					exon = (Integer)mutType.get("exon");
				}
				if (mutType.containsKey("splice")) {
					splice = (Integer)mutType.get("splice");
				}
				if (mutType.containsKey("splice_region")) {
					splice_region = (Integer)mutType.get("splice_region");
				}
				if (mutType.containsKey("UTR_3")) {
					UTR_3 = (Integer)mutType.get("UTR_3");
				}
				if (mutType.containsKey("UTR_5")) {
					UTR_5 = (Integer)mutType.get("UTR_5");
				}
				if (mutType.containsKey("nonsense")) {
					nonsense = (Integer)mutType.get("nonsense");
				}
				total = missense + silent + exon + splice + splice_region + UTR_3 + UTR_5 + nonsense;
				out.write(sampeType + "_" + sampleName + "\t" + missense + "\t" + silent + "\t" + nonsense + "\t" + exon + "\t" + splice + "\t" + splice_region + "\t" + UTR_3 + "\t" + UTR_5 + "\t" + total + "\n");
				//System.out.println(sampeType + "_" + sampleName + "\t" + missense + "\t" + silent + "\t" + nonsense + "\t" + exon + "\t" + splice + "\t" + splice_region + "\t" + UTR_3 + "\t" + UTR_5);
			}
			
			out.close();
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
