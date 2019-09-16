package rnaseq.splicing.rnapeg.juncsalvager;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Comprehensive pipeline to perform alternative splicing analysis.
 * Please use absolute path as the input...
 * @author tshaw
 *
 */
public class JuncSalvagerPipeline {

	public static String type() {
		return "RNApeg";
	}
	public static String description() {
		return "Generate the comprehensive pipeline to perform RNApeg based alternative splicing analysis.\n";
	}
	public static String parameter_info() {
		return "[fileList: sampleName[tab]bam[tab]RNApeg] [geneNameFile] [gtf_file] [parameterFile] [outputFolder] [outputShellScript] [outputNovelFile] [outputAltSpliceFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String fileList = args[0]; // table listing the sampleName bam file and rnapeg result along with. File path needs to be the complete path
			String geneNameFile = args[1];
			String gtfFile = args[2];
			String parameterFile = args[3]; // will need to create this parameter file
			String outputFolder = args[4];
			String outputShellScript = args[5];
			String outputNovelFile = args[6]; // file containing the path to all the novel exon files
			String outputAltSpliceFile = args[7]; // file containing the path to all the alt start site files			
			
			
			LinkedList sample_stuff_listt = new LinkedList();
			FileInputStream fstream = new FileInputStream(fileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sample_stuff_listt.add(str);
			}
			in.close();

			HashMap gene_name_map = new HashMap();
			fstream = new FileInputStream(geneNameFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gene_name_map.put(split[0], split[0]);
			}
			in.close();

			HashMap gene_name_coord_map = new HashMap();
			// obtain the coordinates for each gene
			fstream = new FileInputStream(gtfFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 8) {
					String name = GTFFile.grabMeta(split[8], "gene_name");
					
					String chr_name = split[0];
					int start = new Integer(split[3]);
					int end = new Integer(split[4]);
					if (gene_name_coord_map.containsKey(name)) {
						String prev_position = (String)gene_name_coord_map.get(name);
						int prev_start = new Integer(prev_position.split(":")[1].split("-")[0]);
						int prev_end = new Integer(prev_position.split(":")[1].split("-")[1]);
						int new_start = start;
						int new_end = end;
						if (prev_start <= start) {
							new_start = prev_start;
						}
						if (prev_end >= end) {
							new_end = prev_end;
						}
						String position = chr_name + ":" + new_start + "-" + new_end;
						gene_name_coord_map.put(name, position);
					} else {
						String position = chr_name + ":" + start + "-" + end;
						gene_name_coord_map.put(name, position);
					}
				}
			}
			in.close();
			
			FileWriter fwriter_shell = new FileWriter(outputShellScript);
			BufferedWriter out_shell = new BufferedWriter(fwriter_shell);
			
			FileWriter fwriter_novel_exon = new FileWriter(outputNovelFile);
			BufferedWriter out_novel_exon = new BufferedWriter(fwriter_novel_exon);

			FileWriter fwriter_alt_start = new FileWriter(outputAltSpliceFile);
			BufferedWriter out_alt_start = new BufferedWriter(fwriter_alt_start);

			File outputFolderFile = new File(outputFolder);
			if (!outputFolderFile.isDirectory()) {
				outputFolderFile.mkdir();
			}
			Iterator itr = sample_stuff_listt.iterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				String[] split = line.split("\t");
				String sampleName = split[0];
				String bamFile = split[1];
				String rnapegFile = split[2];
				File file = new File(outputFolder + "/" + sampleName);
				if (!file.isDirectory()) {
					file.mkdir();
				}
				String outputSampleShell = outputFolder + "/" + sampleName + "/sample_junc_salvager_pipeline.sh";
				
				FileWriter fwriter_sample_shell = new FileWriter(outputSampleShell);
				BufferedWriter out_sample_shell = new BufferedWriter(fwriter_sample_shell);
				
				out_shell.write("sh " + outputSampleShell + "\n");
				Iterator itr2 = gene_name_map.keySet().iterator();
				while (itr2.hasNext()) {
					String geneName = (String)itr2.next();
					
					if (gene_name_coord_map.containsKey(geneName)) {
						String prev_position = (String)gene_name_coord_map.get(geneName);
						String prev_chr = prev_position.split(":")[0].replaceAll("chr", "");
						int prev_start = new Integer(prev_position.split(":")[1].split("-")[0]);
						int prev_end = new Integer(prev_position.split(":")[1].split("-")[1]);						
						String position = prev_chr + ":" + (prev_start - 1000) + "-" + (prev_end + 1000);
						String sample_gene_folder = outputFolder + "/" + sampleName + "/" + geneName;
						File file2 = new File(sample_gene_folder);
						if (!file2.isDirectory()) {
							file2.mkdir();
						}
						
						String bedGraphFile = sample_gene_folder + "/" + sampleName + ".bedGraph";
						
						String outputSampleGeneShell = sample_gene_folder + "/sample_gene_junc_salvager_pipeline.sh";
						
						out_sample_shell.write("sh " + outputSampleGeneShell + "\n"); // shell file where we will run everything
						
						out_alt_start.write(sampleName + "\t" + sample_gene_folder + "/AlternativeStartSite.txt\n");
						out_novel_exon.write(sampleName + "\t" + sample_gene_folder + "/NovelExons.txt\n");
						
						FileWriter fwriter_sample_gene_shell = new FileWriter(outputSampleGeneShell);
						BufferedWriter out_sample_gene_shell = new BufferedWriter(fwriter_sample_gene_shell);
						out_sample_gene_shell.write("drppm -RNApegPostProcessingMatrix " + rnapegFile + " 5 " + gtfFile + " " + geneName + " " + sample_gene_folder + "\n");
						out_sample_gene_shell.write("bamCoverage --bam " + bamFile + " --binSize 1 --outFileFormat bedgraph --region " + position + " -o " + bedGraphFile + "\n");
						out_sample_gene_shell.write("drppm -RNApegDefineExonBasedoOnBW " + sample_gene_folder + "/ExonList.txt " + bedGraphFile + " " + gtfFile + " " + geneName + " 8 0.1 " + sample_gene_folder + "\n");
						out_sample_gene_shell.write("\n");
						out_sample_gene_shell.close();
					}
				}
				out_sample_shell.close();
			}

			out_novel_exon.close();
			out_alt_start.close();
			out_shell.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
