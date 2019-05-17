package rnaseq.tools.singlecell.tenxgenomics.cellranger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Merge dataset together
 * @author tshaw
 *
 */
public class CombineSeuratObjectFromCellRanger {

	public static void execute(String[] args) {
		
		try {
			
			String inputFileMeta = args[0];
			String outputFolder = args[1];
			
			
			FileInputStream fstream = new FileInputStream(inputFileMeta);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String folderPath = split[0];
				String prefix = split[1];
				String barcodes = folderPath + "/" + "barcodes.tsv";
				FileInputStream fstream2 = new FileInputStream(inputFileMeta);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
