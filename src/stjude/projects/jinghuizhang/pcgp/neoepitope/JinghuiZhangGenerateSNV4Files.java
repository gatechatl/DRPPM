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

/**
 * Custom script for generating the snv4 files from the neo-epitope results
 * @author tshaw
 *
 */
public class JinghuiZhangGenerateSNV4Files {

	public static void main(String[] args) {
		
		try {
			
			String inputFolder = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\prev_epitope_info\\";
			String outputFolder = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\variant2matrix\\snv4";
			String bamList = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\variant2matrix\\bam.lst";
			
			String runAllScript = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\variant2matrix\\runAll.sh";
			FileWriter fwriter_run = new FileWriter(runAllScript);
			BufferedWriter out_run = new BufferedWriter(fwriter_run);
			
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
			
			File folder = new File(inputFolder);
			for (File f: folder.listFiles()) {
				if (f.getName().contains("exp.out")) {
					HashMap map = new HashMap();
					String sampleName = "NA";
					fstream = new FileInputStream(f.getPath());
					din = new DataInputStream(fstream);
					in = new BufferedReader(new InputStreamReader(din));
					in.readLine();
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						if (split.length > 6) {
							String name = split[2] + "." + split[3] + "." + split[5] + "." + split[6];
							map.put(name, name);
							sampleName = split[1];
						}
					}
					in.close();
					if (!sampleName.equals("NA") && sampleName.contains("SJ")) {
						if (bam_map.containsKey(sampleName)) {
							String outputFile = outputFolder + "\\" + sampleName + ".snv4";
							//System.out.println(outputFile);
							FileWriter fwriter = new FileWriter(outputFile);
							BufferedWriter out = new BufferedWriter(fwriter);
							
							String outputFile_bamlst = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\variant2matrix\\bamlst\\" + sampleName + ".bam.lst";
							FileWriter fwriter_bamlst = new FileWriter(outputFile_bamlst);
							BufferedWriter out_bamlst = new BufferedWriter(fwriter_bamlst);
							out_bamlst.write((String)bam_map.get(sampleName));
							out_bamlst.close();
							
							Iterator itr = map.keySet().iterator();
							while (itr.hasNext()) {
								String name = (String)itr.next();
								out.write(name + "\n");
							}
							out.close();
							
							out_run.write("variants2matrix -bam-list bamlst/" + sampleName + ".bam.lst -variant-file snv4/" + sampleName + ".snv4 -all -snv4 -now; mv matrix_combined_matrix_simple.tab " + sampleName + "_simple.tab;\n");
						} else {
							//System.out.println(sampleName);
						}
					}
				}
			}
			out_run.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
