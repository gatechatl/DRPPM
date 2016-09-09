package PhosphoTools.Enrichment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class GenerateEnrichmentFileInput {

	public static String parameter_info() {
		return "[upRegulatedFile] [dnRegulatedFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String upRegFile = args[0];
			String dnRegFile = args[1];
			String outputFile = args[2];
			LinkedList up = new LinkedList();
			LinkedList dn = new LinkedList();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			FileInputStream fstream = new FileInputStream(upRegFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String site = split[0].replaceAll("\"", "");
				if (!up.contains(site)) {
					up.add(site);
				}				
			}
			in.close();
			
			fstream = new FileInputStream(dnRegFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String site = split[0].replaceAll("\"", "");
				if (!dn.contains(site)) {
					dn.add(site);
				}				
			}
			in.close();
			
			out.write("UpRegList\t");
			Iterator itr = up.iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				out.write(gene + ",");
			}
			out.write("\n");
			out.write("DnRegList\t");
			itr = dn.iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				out.write(gene + ",");
			}
			out.write("");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
