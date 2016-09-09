package PeptidePeaksIonDrawerPipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.UUID;

public class GenerateDisplayIonHTMLImgSimple {


	public static String parameter_info() {

		return "[inputFile] [ipaddress] [outputIMG] [outputCSV] [outputHTML]";
	}
	public static void execute(String[] args) {
		try {
			//String outputFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\final_raw_scan_new_jumpf_removed_link.txt";
        	//FileWriter fwriter = new FileWriter(outputFile);
            //BufferedWriter out = new BufferedWriter(fwriter);
            
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\raw_scan.txt";
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\final_raw_scan_new_jumpf_removed.txt";
			String ipaddress = args[1];
			/*int name_idx = new Integer(args[2]);
			int scan_number_idx = new Integer(args[3]);
			int charge_idx = new Integer(args[4]);
			int peptide_idx = new Integer(args[5]);
			int uniprot_idx = new Integer(args[6]);*/
						
			String outputFolderImg = args[2];
			String outputFolderCsv = args[3];
			String outputFolderHtml = args[4];
			String backtrack_txt = args[5];
			String TMT_flag_str = args[6];
			
			FileWriter fwriter = new FileWriter(backtrack_txt);
            BufferedWriter out = new BufferedWriter(fwriter);
            
/*			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			//in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String peptide = split[1].split("\\.")[1];
				String path = split[2];
				String fileName = path.split("/")[path.split("/").length - 1];
	*/
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				String peptide = split[1];
				String id = split[2];
				String final_path = split[3];
				
				//String name = split[name_idx];
				//String scan_number = split[scan_number_idx];
				//String charge = split[charge_idx];
				//String id = name + "." + scan_number + "." + 1 + "." + charge;
				//String peptide = split[peptide_idx];
				
				//String fileName = split[uniprot_idx].replaceAll("\\|", "_") + "_" + peptide.replaceAll("\\*", "");
				String fileName = name.replaceAll("\\|", "_") + "_" + peptide.replaceAll("\\*", "");
				
				//String final_path = path + "/" + id;
				//out.write("=HYPERLINK(\"" + generateScript(final_path, peptide) + "\")\n");
				//System.out.println(generateScript(final_path, peptide, ipaddress));
				boolean tmt_flag = false;
				if (TMT_flag_str.equals("yes")) {
					tmt_flag = true;
				}
				executeCommand("wget \"" + generateScript(final_path, peptide, ipaddress, tmt_flag) + "\" -O " + outputFolderHtml + "/" + id + ".html");
				out.write(peptide + "\t" + name + "\t" + id + "\t" + generateScript(final_path, peptide, ipaddress, tmt_flag) + "\n");
				String outputFile2 = outputFolderCsv + "/" + id + ".csv";
				FileWriter fwriter2 = new FileWriter(outputFile2);
	            BufferedWriter out2 = new BufferedWriter(fwriter2);
	            
	            boolean startWrite = false;
				FileInputStream fstream2 = new FileInputStream(outputFolderHtml + "/" + id + ".html");
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					if (str2.contains("Seq")) {
						startWrite = true;
						in2.readLine();						
					}
					if (startWrite) {
						str2 = str2.replaceAll("<B><FONT COLOR=\"FF0000\">", "");
						str2 = str2.replaceAll("</FONT></B>", "*");
						str2 = str2.replaceAll("<B><FONT COLOR=\"0000FF\">", "");
						str2 = str2.replaceAll("<FONT COLOR=\"#FF7F00\"><B>", "");
						str2 = str2.replaceAll("</B></FONT>", "");
						str2 = str2.replaceAll("\\*S", "#S");
						str2 = str2.replaceAll("\\*T", "#T");
						str2 = str2.replaceAll("\\*Y", "#Y");
						out2.write(cleanStr(str2).replaceAll(" ", ",") + "\n");
						
					}
					if (str2.trim().equals("")) {
						startWrite = false;
					}
					if (str2.contains("IMG SRC")) {
						String gifName = str2.split("/")[2].split("gif")[0] + "gif";
						executeCommand("cp /tmp/" + gifName + " " + outputFolderImg + "/" + id + ".gif"); 
					}
				}
				in2.close();
				out2.close();
			}
			in.close();
			out.close();
			//Dta=/www/gygi.med.harvard.edu/data/trim/03727.2043.2043.2.dta&MassType=1&NumAxis=4&Pep=YLPAMALIDLAM&DMass1=79.97&DMass2=79.97&DMass3=15.99&MassC=160.03&DSite=200000000003&IgnorePrecursor=1&ScaleV=1.0
			//out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String cleanStr(String str) {
		String result = str;
		while (result.contains("  ")) {
			result = result.replaceAll("  ", " ");
		}
		
		return result.trim();
	}
	public static String generateScript(String path, String peptide, String ipaddress, boolean tmt_flag) {
		String result = "";
		peptide = peptide.replaceAll("\\@", "\\*");
		peptide = peptide.replaceAll("\\#", "\\*");
		peptide = peptide.replaceAll("\\!", "\\*");
		peptide = peptide.replaceAll("\\&", "\\*");
		peptide = peptide.replaceAll("\\%", "\\*");
		peptide = peptide.replaceAll("\\$", "\\*");
		//System.out.println(peptide);
		String clean_peptide = peptide.replaceAll("\\*", "");
		String dsite = "";
		for (int i = 0; i < clean_peptide.length(); i++) {
			dsite += "0";
		}
		int index = 0;
		for (int i = 0; i < peptide.length() - 1; i++) {
			
			if (peptide.substring(i, i + 2).equals("S*") || peptide.substring(i, i + 2).equals("T*") || peptide.substring(i, i + 2).equals("Y*")) {
				dsite = dsite.substring(0, index) + "2" + dsite.substring(index + 1, dsite.length());
			}
			if (peptide.substring(i, i + 2).equals("M*") ) {
				dsite = dsite.substring(0, index) + "1" + dsite.substring(index + 1, dsite.length());
			}
			if (peptide.substring(i, i + 2).contains("*")) {
				i++;
			}
			index++;
		}
		/*if (peptide.contains("M\\*")) {
			//result = "http://" + ipaddress + "/cgi-bin/displayions_html5?Dta=" + path + "&MassType=1&NumAxis=1&Pep=" + peptide.replaceAll("\\*", "") + "&Nterm=229.16&MassC=160.0306&MassK=357.2579&MassM=147.0354";
			result = "http://" + ipaddress + "/cgi-bin/displayions_html5?Dta=" + path + "&MassType=1&NumAxis=1&Pep=" + peptide.replaceAll("\\*", "") + "&Nterm=229.16&MassK=357.2579&MassM=147.0354";
		} else {
			//result = "http://" + ipaddress + "/cgi-bin/displayions_html5?Dta=" + path + "&MassType=1&NumAxis=1&Pep=" + peptide.replaceAll("\\*", "") + "&Nterm=229.16&MassC=160.0306&MassK=357.2579";
			result = "http://" + ipaddress + "/cgi-bin/displayions_html5?Dta=" + path + "&MassType=1&NumAxis=1&Pep=" + peptide.replaceAll("\\*", "") + "&Nterm=229.16&MassK=357.2579";
		}*/ //DMass1=15.99
		
		if (tmt_flag) {
			result = "http://" + ipaddress + "/cgi-bin/displayions_html5?Dta=" + path + "&MassType=1&NumAxis=1&Pep=" + peptide.replaceAll("\\*", "") + "&Nterm=229.16&MassK=357.2579&DMass1=15.99&DMass2=79.97&DSite=" + dsite;
		} else {
			//result = "http://" + ipaddress + "/cgi-bin/displayions_html5?Dta=" + path + "&MassType=1&NumAxis=1&Pep=" + peptide.replaceAll("\\*", "") + "&MassK=357.2579&DMass1=15.99&DMass2=79.97&DSite=" + dsite;
			result = "http://" + ipaddress + "/cgi-bin/displayions_html5?Dta=" + path + "&MassType=1&NumAxis=1&Pep=" + peptide.replaceAll("\\*", "") + "&DMass1=15.99&DMass2=79.97&DSite=" + dsite;
		}
		return result;
	}

	public static void executeCommand(String executeThis) {
    	try {
    		
    		String buffer = UUID.randomUUID().toString();
        	writeFile(buffer + "tempexecuteCommand.sh", executeThis);
        	String[] command = {"sh", buffer + "tempexecuteCommand.sh"};
        	Process p1 = Runtime.getRuntime().exec(command);
        	BufferedReader inputn = new BufferedReader(new InputStreamReader(p1.getInputStream()));
        	String line=null;
        	while((line=inputn.readLine()) != null) {}
        	inputn.close();
        	p1.destroy();
        	File f = new File(buffer + "tempexecuteCommand.sh");
        	f.delete();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	public static void writeFile(String fileName, String command) {
    	try {
        	FileWriter fwriter2 = new FileWriter(fileName);
            BufferedWriter out2 = new BufferedWriter(fwriter2);
            out2.write(command + "\n");
            out2.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
}



