package stjude.projects.potter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import misc.CommandLine;

/**
 * Attempt to generate fasta file for the transcripts
 * @author tshaw
 *
 */
public class PotterGrabTranscriptExonFasta {
	public static String description() {
		return "Produce the fasta file from the cufflinks result.";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[transcriptFileList: three column (transcript.gtf) (output exon sumary) (transcript list)] [dumphg19_path: /nfs_exports/apps/gnu-apps/NextGen/gwbin/dev/dumphg19]";
	}
	
	public static void execute(String[] args) {
		
		try {

			
			//String[] files = {"SJRHB010470_C6", "SJRHB010470_C7", "SJRHB010470_C8", "SJRHB010470_C9", "SJRHB010470_C10", "SJRHB010470_C11"};
			//for (String file: files) {
			
			String input_transcriptFileList = args[0];
			String dumphg19_path = args[1];
			//String output_individualExonFasta = args[2];
			//String output_transcriptFasta = args[3];
			
			
			FileInputStream fstream3 = new FileInputStream(input_transcriptFileList);
			DataInputStream din3 = new DataInputStream(fstream3);
			BufferedReader in3 = new BufferedReader(new InputStreamReader(din3));									
			while (in3.ready()) {
				String inputFileStr = in3.readLine();
				if (!inputFileStr.trim().equals("")) {
					String gtfFile = inputFileStr.split("\t")[0].trim();
					String exonInBetween = inputFileStr.split("\t")[1].trim();
					String transcriptList = inputFileStr.split("\t")[2].trim();
					
					FileWriter fwriter_exon = new FileWriter(transcriptList + ".exon.fasta");
					BufferedWriter out_exon = new BufferedWriter(fwriter_exon);			
					
					FileWriter fwriter_transcript = new FileWriter(transcriptList + ".transcript.fasta");
					BufferedWriter out_transcript = new BufferedWriter(fwriter_transcript);			
					
					HashMap transcripts = new HashMap();
					FileInputStream fstream2 = new FileInputStream(transcriptList);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));	
					in2.readLine(); // header
					while (in2.ready()) {
						String str = in2.readLine();
						String[] split = str.split("\t");
						if (split.length > 5) {
						String[] transcripts_split = split[5].split(","); 
							for (String transcripts_str: transcripts_split) {
								if (!transcripts_str.equals("")) {
									transcripts.put(transcripts_str, transcripts_str);
								}
							}
						}
					}
					in2.close();
					System.out.println("Total Transcripts: " + transcripts.size());
					HashMap transcript_fasta = new HashMap();
					HashMap exon_fasta = new HashMap();
					fstream2 = new FileInputStream(gtfFile);
					din2 = new DataInputStream(fstream2);
					in2 = new BufferedReader(new InputStreamReader(din2));									
					while (in2.ready()) {
						String str = in2.readLine();
						String[] split = str.split("\t");
						String transcript_id = split[8].split(";")[1].replaceAll("transcript_id", "").trim().replaceAll("\"", "");
						if (split[2].equals("exon")) {
							if (transcripts.containsKey(transcript_id)) {
								String chr = split[0];
								String start = split[3];
								String end = split[4];
								String buffer = UUID.randomUUID().toString();
								String script = dumphg19_path + " " + chr + " " + start + " " + end + " > " + buffer;
								StringBuffer buffer_str = new StringBuffer();
								CommandLine.executeCommand(script);
								FileInputStream fstream4 = new FileInputStream(buffer);
								DataInputStream din4 = new DataInputStream(fstream4);
								BufferedReader in4 = new BufferedReader(new InputStreamReader(din4));	
								while (in4.ready()) {
									String line = (String)in4.readLine();
									buffer_str.append(line.toUpperCase());
								}
								in4.close();
								File f = new File(buffer);
								if (f.exists()) {
									f.delete();
								}
								
								if (transcript_fasta.containsKey(transcript_id)) {
									StringBuffer prev_buffer_str = (StringBuffer)transcript_fasta.get(transcript_id);
									prev_buffer_str.append(buffer_str.toString());
									transcript_fasta.put(transcript_id, prev_buffer_str);
								} else {
									transcript_fasta.put(transcript_id, buffer_str);
								}
								
								String exon_id = transcript_id + ":" + chr + ":" + start + ":" + end;
								out_exon.write(">" + str.replaceAll("\t", ":") + "\n");
								out_exon.write(buffer_str.toString() + "\n");
								/*if (exon_fasta.containsKey(transcript_id)) {
									StringBuffer prev_buffer_str = (StringBuffer)exon_fasta.get(transcript_id);
									prev_buffer_str.append(buffer_str.toString());
									exon_fasta.put(transcript_id, prev_buffer_str);
								} else {
									exon_fasta.put(transcript_id, buffer_str);
								}*/
								
							}
						}						
					}
					in2.close();
					
					Iterator itr = transcript_fasta.keySet().iterator();
					while (itr.hasNext()) {
						String transcript_id = (String)itr.next();
						StringBuffer string_buffer = (StringBuffer)transcript_fasta.get(transcript_id);
						out_transcript.write(">" + transcript_id + "\n");
						out_transcript.write(string_buffer.toString() + "\n");
					}
					out_exon.close();
					out_transcript.close();
					
				}
			}
			in3.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
