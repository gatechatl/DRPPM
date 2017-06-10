package stjude.projects.peng;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XushengAddHumanMouseGeneName {

	public static void main(String[] args) {
		
		try {

			
			String inputFile = args[0];
			String human_gtfFile = args[1];
			String mouse_gtfFile = args[2];
			String human_uniprotFile = args[3];
			String mouse_uniprotFile = args[4];
			String outputFile = args[5];
			HashMap map = new HashMap();
			GTFFile human_gtf = new GTFFile();
			human_gtf.initialize(human_gtfFile);
			GTFFile mouse_gtf = new GTFFile();
			mouse_gtf.initialize(mouse_gtfFile);
			
			HashMap uniprot2entrezID = new HashMap();
			FileInputStream fstream = new FileInputStream(human_uniprotFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String ensembl = "";
				String uniprot = "";
				if (split[1].equals("Ensembl")) {
					ensembl = split[2];
					uniprot2entrezID.put(uniprot, ensembl);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
}
