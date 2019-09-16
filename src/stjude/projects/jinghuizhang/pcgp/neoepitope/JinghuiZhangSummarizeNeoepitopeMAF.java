package stjude.projects.jinghuizhang.pcgp.neoepitope;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;


public class JinghuiZhangSummarizeNeoepitopeMAF {



	public static void main(String[] args) {
		
		try {
			
			String inputFolder = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\prev_epitope_info\\";
			String bamList = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\variant2matrix\\bam.lst";		
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\variant2matrix\\PCGP_Subset_RNA_DNA_MAF_Neoepitope.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap bam_map = new HashMap();
			FileInputStream fstream = new FileInputStream(bamList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\\/");
				bam_map.put(split[split.length - 1].split("-")[0], str);
			}
			in.close();
			boolean readOnce = true;
			File folder = new File(inputFolder);
			for (File f: folder.listFiles()) {
				if (f.getName().contains("exp.out")) {
					HashMap map = new HashMap();
					String sampleName = "NA";
					fstream = new FileInputStream(f.getPath());
					din = new DataInputStream(fstream);
					in = new BufferedReader(new InputStreamReader(din));
					String neoepitope_header = in.readLine();
					if (readOnce) {
						out.write(neoepitope_header + "\tbam\tvariant\tRNA_reference_count\tRNA_variant_count\n");
						readOnce = false;
					}
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						if (split.length > 6) {
							String name = split[2] + "." + split[3] + "." + split[5] + "." + split[6];
							double nm = new Double(split[12]);
							if (map.containsKey(name)) {
								String prev_line = (String)map.get(name);
								String[] split_prev_line = prev_line.split("\t");
								if (new Double(split_prev_line[12]) > nm) {
									map.put(name, str);
								}
							} else {
								map.put(name, str);
							}
							sampleName = split[1];
						}
					}
					in.close();
					
					
					if (!sampleName.equals("NA") && sampleName.contains("SJ")) {
						if (bam_map.containsKey(sampleName)) {
							HashMap rna_maf = new HashMap();
							String rna_maf_file = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\variant2matrix\\" + sampleName + "_simple.tab";
							File f2 = new File(rna_maf_file);
							if (f2.exists()) {
								//System.out.println("File exists?");
								fstream = new FileInputStream(rna_maf_file);
								din = new DataInputStream(fstream);
								in = new BufferedReader(new InputStreamReader(din));
								in.readLine();
								while (in.ready()) {
									String str = in.readLine();
									String[] split = str.split("\t");
									if (!rna_maf.containsKey(split[1])) {
										rna_maf.put(split[1], str);									
										
									} else {
										System.out.println("Duplicate");
									}
								}
								in.close();
								
								Iterator itr = map.keySet().iterator();
								while (itr.hasNext()) {
									String name = (String)itr.next();
									String rna_line = (String)rna_maf.get(name);
									out.write(map.get(name) + "\t" + rna_line + "\n");
								}
							}
						} 
					}
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
