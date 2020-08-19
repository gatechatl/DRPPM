package rnaseq.splicing.juncsalvager;

import graph.figures.HeatmapGeneration;
import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import misc.CommandLine;
import statistics.general.MathTools;

/**
 * Find a specific gene from the exon matrix to extract.
 * @author gatechatl
 *
 */
public class JuncSalvagerExtractGeneMatrix {


	public static String description() {
		return "Extract the gene from the exon matrix. Use the gtf file and create a matrix for each available transcript.";
	}
	public static String type() {
		return "JUNCSALVAGER";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [inputGeneName] [gtfFile] [annotationFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			String inputMatrixFile = args[0];
			String inputGeneName = args[1];
			String gtfFile = args[2];
			String annotationFile = args[3];
			boolean show_rowname = new Boolean(args[4]);
			boolean show_colname = new Boolean(args[5]);
			String outputFile = args[6];
			
			GTFFile gtf = new GTFFile();
			gtf.initialize(gtfFile);
			
			LinkedList gene_id_list = (LinkedList)gtf.geneName2geneID.get(inputGeneName);
			Iterator gene_id_itr = gene_id_list.iterator();
			while (gene_id_itr.hasNext()) {				
				String gene_id = (String)gene_id_itr.next();
				String transcripts = (String)gtf.gene2transcript.get(gene_id);
				
				FileWriter fwriter = new FileWriter(outputFile + "_" + gene_id + ".txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				

				FileWriter fwriter2 = new FileWriter(outputFile + "_sampleName.txt");
				BufferedWriter out2 = new BufferedWriter(fwriter2);
				
				HashMap exons = new HashMap();
				FileInputStream fstream = new FileInputStream(inputMatrixFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				String header = in.readLine();
				out.write(header + "\n");
				String[] split_header = header.split("\t");
				for (int i = 1; i < split_header.length; i++) {
					out2.write(split_header[i] + "\n");
				}
				out2.close();
				
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					String geneSymbol = split[0].split("_")[0];
					if (geneSymbol.equals(inputGeneName)) {
						out.write(str + "\n");
						exons.put(split[0].replaceAll("_ECM", ""), str);
					}
				}
				in.close();
				out.close();
				
				for (String transcript_id: transcripts.split(",")) {
					if (!transcript_id.equals("")) {
						fwriter = new FileWriter(outputFile + "_" + transcript_id + ".txt");
						out = new BufferedWriter(fwriter);
						out.write(header + "\n");
						

						fwriter2 = new FileWriter(outputFile + "_" + transcript_id + "_geneName.txt");
						out2 = new BufferedWriter(fwriter2);
						out2.write(">Exons\n");
						String exon_str = (String)gtf.transcript2exon.get(transcript_id);
						String[] exon_split = exon_str.split(",");
						
						// String new_exon = "CHR" + chr + ":" + start + "-" + end + ":" + direct + ":" + exon_number;
						for (String exon: exon_split) {
							//System.out.println("compared to: " + exon);
							String exon_chr = exon.split(":")[0].toUpperCase();
							exon_chr = exon_chr.replaceAll("CHRCHR", "CHR");
							String exon_start = exon.split(":")[1].split("-")[0];
							String exon_end = exon.split(":")[1].split("-")[1];
							String exon_direction = exon.split(":")[2]; 
							Iterator itr_exons = exons.keySet().iterator();
							while (itr_exons.hasNext()) {
								String exon_info = (String)itr_exons.next();
								//System.out.println("exon_info: " + exon_info);							
								String chr = exon_info.split("_")[1].toUpperCase();
								String start = exon_info.split("_")[2];
								String end = exon_info.split("_")[3];
								String direction = exon_info.split("_")[4];
								
								//System.out.println(exon_chr + "\t" + chr);
								//System.out.println(exon_start + "\t" + start);
								//System.out.println(exon_end + "\t" + end);
								//System.out.println(exon_direction + "\t" + direction);
								
								if (chr.equals(exon_chr) && start.equals(exon_start) && end.equals(exon_end) && direction.equals(exon_direction)) {
									String line = (String)exons.get(exon_info);
									String[] split_line = line.split("\t");
									out.write(split_line[0]);
									out2.write(split_line[0] + "\n");
									for (int i = 1; i < split_line.length; i++) {
										out.write("\t" + MathTools.log2(new Double(split_line[i]) + 1.0));
									}
									out.write("\n");
								}												
							}
						}
						out.close();
						out2.close();
						String arg1 = outputFile + "_" + transcript_id + ".txt";
						String arg2 = outputFile + "_sampleName.txt";
						String arg3 = outputFile + "_" + transcript_id + "_geneName.txt";
						String arg4 = outputFile + "_" + transcript_id + ".txt.png";
						String arg5 = outputFile + "_" + transcript_id + ".r";
						String height = "550";
						if (exon_split.length > 40) {
							height = "550";
						}
						if (25 < exon_split.length && exon_split.length <= 40) {
							height = "500";
						}
						if (15 < exon_split.length && exon_split.length <= 25) {
							height = "450";
						}
						if (exon_split.length <= 15) {
							height = "400";
						}
			
						int row_size = 10;
						int col_size = 10;
						if (!show_rowname) {
							row_size = 0;
						}
						if (!show_colname) {
							col_size = 0;
						}
						String parameters = arg1 + " " + arg2 + " " + arg3 + " " + arg4 + " Heatmap false false false 1200 " + height + " " + row_size + " " + col_size + " 0 " + annotationFile + " > " + arg5;
						CommandLine.executeCommand("drppm -plotPHeatMapAnnotationNoNorm " + parameters);
						CommandLine.executeCommand("R --vanilla < " + arg5);
						
					}
				}
			
			}
		} catch (Exception e ){
			e.printStackTrace();
		}
	}
}
