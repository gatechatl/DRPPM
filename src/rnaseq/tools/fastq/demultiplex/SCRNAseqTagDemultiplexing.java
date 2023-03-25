package rnaseq.tools.fastq.demultiplex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Custom scRNAseq demultiplexing brute force for Dorina Avram Lab.
 * @author 4472414
 *
 */
public class SCRNAseqTagDemultiplexing {

	
	public static void main(String[] args) {
		
		try {
			
			String inputFile_R1 = args[0];
			String inputFile_R2 = args[0];
			
			String barcode1 = "ACCCACCAGTAAGAC";
			String barcode2 = "GGTCGAGAGCATTCA";
			String barcode3 = "CTTGCCGCATGTCAT";
			String barcode4 = "AAAGCATTCTTCACG";
			
			String barcode1_rev = "GTCTTACTGGTGGGT";
			String barcode2_rev = "TGAATGCTCTCGACC";
			String barcode3_rev = "ATGACATGCGGCAAG";
			String barcode4_rev = "CGTGAAGAATGCTTT";
			

			FileWriter fwriter_B1R1 = new FileWriter(inputFile_R1 + "_B1R1.fastq");
			BufferedWriter out_B1R1 = new BufferedWriter(fwriter_B1R1);

			FileWriter fwriter_B1R2 = new FileWriter(inputFile_R2 + "_B1R2.fastq");
			BufferedWriter out_B1R2 = new BufferedWriter(fwriter_B1R2);

			FileWriter fwriter_B2R1 = new FileWriter(inputFile_R1 + "_B2R1.fastq");
			BufferedWriter out_B2R1 = new BufferedWriter(fwriter_B2R1);

			FileWriter fwriter_B2R2 = new FileWriter(inputFile_R2 + "_B2R2.fastq");
			BufferedWriter out_B2R2 = new BufferedWriter(fwriter_B2R2);

			FileWriter fwriter_B3R1 = new FileWriter(inputFile_R1 + "_B3R1.fastq");
			BufferedWriter out_B3R1 = new BufferedWriter(fwriter_B3R1);

			FileWriter fwriter_B3R2 = new FileWriter(inputFile_R2 + "_B3R2.fastq");
			BufferedWriter out_B3R2 = new BufferedWriter(fwriter_B3R2);

			FileWriter fwriter_B4R1 = new FileWriter(inputFile_R1 + "_B4R1.fastq");
			BufferedWriter out_B4R1 = new BufferedWriter(fwriter_B4R1);

			FileWriter fwriter_B4R2 = new FileWriter(inputFile_R2 + "_B4R2.fastq");
			BufferedWriter out_B4R2 = new BufferedWriter(fwriter_B4R2);

			FileInputStream fstream_R1 = new FileInputStream(inputFile_R1);
			DataInputStream din_R1 = new DataInputStream(fstream_R1);
			BufferedReader in_R1 = new BufferedReader(new InputStreamReader(din_R1));
			
			FileInputStream fstream_R2 = new FileInputStream(inputFile_R2);
			DataInputStream din_R2 = new DataInputStream(fstream_R2);
			BufferedReader in_R2 = new BufferedReader(new InputStreamReader(din_R2));
			
			while (in_R1.ready() && in_R2.ready()) {
				String header_R1 = in_R1.readLine();
				String fasta_R1 = in_R1.readLine();
				String plus_R1 = in_R1.readLine();
				String quality_R1 = in_R1.readLine();
				
				String header_R2 = in_R2.readLine();
				String fasta_R2 = in_R2.readLine();
				String plus_R2 = in_R2.readLine();
				String quality_R2 = in_R2.readLine();
			}
			in_R1.close();
			in_R2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
