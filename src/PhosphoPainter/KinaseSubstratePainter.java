package PhosphoPainter;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class KinaseSubstratePainter {

	public static void main(String[] args) {
		
		try {
			
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\pdgfra_idsum_peptides.out";
			HashMap phosphoMap = readPhosphoFile(inputFile);
			String gffFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\PROTEINREFERENCE\\Mouse_UNIPROT_GFF_Downloaded_20141111.txt";
			HashMap gffMap = readUniprotGFF(gffFile);
			
			String geneName = "";
			String geneFileName = args[2];
			FileInputStream fstream = new FileInputStream(geneFileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();			
				geneName = str.toUpperCase();
			}
			String outputFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\pdgra_png.png";
			//System.out.println("Finished Reading Files");

			String genePhosphoStr = (String)phosphoMap.get(geneName);
			String geneUniprot = genePhosphoStr.split("\t")[1];
			
			String phosphoRefFileName = args[4]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\pdgfra_uniprot.txt";
			String phosphoRefStr = "";
			fstream = new FileInputStream(phosphoRefFileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {				
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].toUpperCase().equals(geneUniprot.toUpperCase())) {
					phosphoRefStr = split[1];
				}
			}
			in.close();
			
			LinkedList phosphoListRef = createPhospho(phosphoRefStr, 0);
			

			UniprotGFF geneGff = (UniprotGFF)gffMap.get(geneUniprot);
			//System.out.println(geneUniprot);
			LinkedList phosphoList = createPhospho(genePhosphoStr, 4);
			
			LinkedList finalPhosphoList = mergePhosphoList(phosphoList, phosphoListRef);
			//System.out.println("Creating Script");
			
			//System.out.println(finalPhosphoList.size());
			
			System.out.println(generatePaintScript(geneGff, finalPhosphoList, phosphoListRef, outputFile, true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void pdgfra_version(String[] args) {
		
		try {
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\pdgfra_idsum_peptides.out";
			HashMap phosphoMap = readPhosphoFile(inputFile);
			String gffFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\PROTEINREFERENCE\\Mouse_UNIPROT_GFF_Downloaded_20141111.txt";
			HashMap gffMap = readUniprotGFF(gffFile);
			
			String geneName = "Pdgfra";
			/*String geneFileName = args[2];
			FileInputStream fstream = new FileInputStream(geneFileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();			
				geneName = str;
			}*/
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\pdgra_png.png";
			//System.out.println("Finished Reading Files");
			
			String phosphoRefFileName = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\pdgfra_uniprot.txt";
			String phosphoRefStr = "";
			FileInputStream fstream = new FileInputStream(phosphoRefFileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				phosphoRefStr = in.readLine();			
			}
			in.close();
			
			LinkedList phosphoListRef = createPhospho(phosphoRefStr, 0);
			
			String genePhosphoStr = (String)phosphoMap.get(geneName);
			String geneUniprot = genePhosphoStr.split("\t")[1];
			UniprotGFF geneGff = (UniprotGFF)gffMap.get(geneUniprot);
			//System.out.println(geneUniprot);
			LinkedList phosphoList = createPhospho(genePhosphoStr, 4);
			
			LinkedList finalPhosphoList = mergePhosphoList(phosphoList, phosphoListRef);
			//System.out.println("Creating Script");
			
			//System.out.println(finalPhosphoList.size());
			
			System.out.println(generatePaintScript(geneGff, finalPhosphoList, phosphoListRef, outputFile, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void standard_execution(String[] args) {
		
		try {
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\MOUSE_TTEST_COORD.txt";
			HashMap phosphoMap = readPhosphoFile(inputFile);
			String gffFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\PROTEINREFERENCE\\Mouse_UNIPROT_GFF_Downloaded_20141111.txt";
			HashMap gffMap = readUniprotGFF(gffFile);
			
			String geneName = "";
			String geneFileName = args[2];
			FileInputStream fstream = new FileInputStream(geneFileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();			
				geneName = str;
			}
			String outputFile = args[3];
			//System.out.println("Finished Reading Files");
			String genePhosphoStr = (String)phosphoMap.get(geneName);
			String geneUniprot = genePhosphoStr.split("\t")[1];
			UniprotGFF geneGff = (UniprotGFF)gffMap.get(geneUniprot);
			//System.out.println(geneUniprot);
			LinkedList phosphoList = createPhospho(genePhosphoStr, 4);
			//System.out.println("Creating Script");
			
			System.out.println(generatePaintScript(geneGff, phosphoList, new LinkedList(), outputFile, true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	public static void execute(String[] args) {
		try {
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\MOUSE_TTEST_COORD.txt";
			HashMap phosphoMap = readPhosphoFile(inputFile);
			String gffFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\PROTEINREFERENCE\\Mouse_UNIPROT_GFF_Downloaded_20141111.txt";
			HashMap gffMap = readUniprotGFF(gffFile);
			String geneName = args[2];
			String outputFile = args[3];
			//System.out.println("Finished Reading Files");
			String genePhosphoStr = (String)phosphoMap.get(geneName);
			String geneUniprot = genePhosphoStr.split("\t")[1];
			UniprotGFF geneGff = (UniprotGFF)gffMap.get(geneUniprot);
			//System.out.println(geneUniprot);
			LinkedList phosphoList = createPhospho(genePhosphoStr, 4);
			//System.out.println("Creating Script");
			
			System.out.println(generatePaintScript(geneGff, phosphoList, new LinkedList(), outputFile, true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static LinkedList createPhospho(String str, int phosphoIDCol) {
		String[] split = str.split("\t");
		String[] split2 = split[phosphoIDCol].split(",");

		HashMap map = new HashMap();
		int[] array = new int[split2.length];
		for (int i = 0; i < split2.length; i++) {
			if (!split2[i].equals("")) {
				//System.out.println(split2[i]);
				int num = new Integer(split2[i].substring(0, split2[i].length() - 1));
				Phospho pho = new Phospho();
				pho.name = split2[i];
				pho.loc = num;
				map.put(num, pho);
				array[i] = num;
				
			}
		}
		Arrays.sort(array);
		
		LinkedList list = new LinkedList();
		for (int num: array) {
			list.add(map.get(num));
		}
		return list;
	}
	
	public static LinkedList mergePhosphoList(LinkedList listExp, LinkedList listRef) {

		HashMap mapAll = new HashMap();
		Iterator itr = listExp.iterator();
		while (itr.hasNext()) {
			Phospho pho = (Phospho)itr.next();
			pho.id_stat = "NOVEL";
			mapAll.put(pho.loc, pho);
		
		}
		
		itr = listRef.iterator();
		while (itr.hasNext()) {
			Phospho pho = (Phospho)itr.next();
			if (mapAll.containsKey(pho.loc)) {
				pho.id_stat = "FOUND"; 
				//System.out.println(pho.loc + "\t" + "FOUND");
				mapAll.put(pho.loc, pho);
			} else {
				pho.id_stat = "MISSED";
				//System.out.println("MISSED");
			}			
					
		}		
		//System.exit(0);
		int[] array = new int[mapAll.size()];
		int i = 0;
		itr = mapAll.keySet().iterator();
		while (itr.hasNext()) {			
			int num = (Integer)itr.next();
			Phospho pho = (Phospho)mapAll.get(num);			
			array[i] = num;						
			i++;
		}
		Arrays.sort(array);
		
		LinkedList list = new LinkedList();
		for (int num: array) {
			list.add(mapAll.get(num));
			//System.out.println(num + "\t" + ((Phospho)(mapAll.get(num))).id_stat);
		}
		//System.exit(0);
		return list;
	}
	public static HashMap readPhosphoFile(String inputFile) {
		HashMap map = new HashMap();
		try {
			String fileName = inputFile;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();			
				String[] split = str.split("\t");
				String geneName = split[0].toUpperCase();
				if (map.containsKey(geneName)) {
					String str2 = (String)map.get(geneName);
					if (str2.length() < str.length()) {
						map.put(geneName, str);
					}					
				} else {
					map.put(geneName, str);
				}
				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static HashMap readUniprotGFF(String inputFile) {
		HashMap gff_map = new HashMap();
		try {
			UniprotGFF gff = new UniprotGFF();
			
			String fileName = inputFile;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains("##sequence-region ")) {
					if (!gff.geneName.equals("")) {
						gff_map.put(gff.geneName, gff);
						gff = new UniprotGFF();
					}
					
					
					String[] split = str.split(" ");
					gff.geneName = split[1];
					gff.start = new Integer(split[2]);
					gff.end = new Integer(split[3]);
					
				}
				if (str.contains("\tDomain\t")) {
					String[] split = str.split("\t");
					Domain domain = new Domain();
					domain.start = new Integer(split[3]);
					domain.end = new Integer(split[4]);
					domain.note = parseOutNote(split[8]); 
					gff.domains.add(domain);
				}
			}
			in.close();
			gff_map.put(gff.geneName, gff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gff_map;
	}
	public static String parseOutNote(String str) {
		String result = "";
		if (str.contains("Note=")) {
			result = str.replaceAll("Note=", "");
			result = result.split(";")[0];
		}
		return result;
	}
	public static String generatePaintScript(UniprotGFF gff, LinkedList phospho, LinkedList reference_phospho, String outputFile, boolean show_label) {
		
		
		String geneCoord = gff.geneName;
		int min = gff.start;
		int max = gff.end;
		int ballsize = (max - min) / 80;
		String script = "";
		script += "require(plotrix)\n";
		script += "png(file = \"" + outputFile.replaceAll("\\\\", "/") + "\", width=1000,height=400)\n";
		script += "min = " + min + ";\n";
		script += "max = " + max + ";\n";
		script += "buffer = max * 0.03;\n";
		script += "leftbuffer = buffer * 12;\n";
		script += "plot(0,0,col=\"white\",xlim=c(-(leftbuffer),max + buffer), ylim=c(0,700),axes=F,xlab=\"\", ylab=\"\");\n";
		String atscript = "at=c(0";
		for (int i = 100; i < max; i=i+100) {
			atscript += "," + i;
		}
		atscript += "," + max + ")";
		script += "axis(1, " + atscript + ", pos=0)\n";
		int colnum = 2;
		//System.out.println("domains: " + gff.domains.size());
		Iterator itr = gff.domains.iterator();
		while (itr.hasNext()) {
			Domain domain = (Domain)itr.next();
			script += "rect(" + domain.start + ",150," + domain.end + ",250, lwd=2,col=" + colnum + ")\n";
			colnum++;
		}
		
		colnum = 2;
		int legendY = 690;
		itr = gff.domains.iterator();
		while (itr.hasNext()) {
			Domain domain = (Domain)itr.next();
			
			script += "rect(-leftbuffer + 40," + legendY + ",-leftbuffer + 60," + (legendY - 20) + ",lwd=1,col=" + colnum + ")\n";
			script += "text(-leftbuffer + 70," + (legendY - 10) + ", \"" + domain.note + "\",adj=c(0,NA))\n";
			legendY = legendY - 30;
			colnum++;
		}
		script += "rect(min,150,max,250, lwd=3)\n";
		script += "text(" + max + ",190, \"" + max + "\",adj=c(0,NA))\n";
		itr = phospho.iterator();
		while (itr.hasNext()) {
			Phospho pho = (Phospho)itr.next();
			
			
			String ballColor = "lightblue";
			if (pho.id_stat.equals("MISSED")) {
				ballColor = "red";
			} else if (pho.id_stat.equals("NOVEL")) {
				ballColor = "green";				
			} else if (pho.id_stat.equals("FOUND")) {
				ballColor = "lightblue";
			}
			script += "segments(" + pho.loc + ", 250, " + pho.loc + ",300, lwd=2);\n";
			script += "draw.circle(" + pho.loc + ",300," + ballsize + ",col=\"" + ballColor + "\",lwd=2);\n";
			if (itr.hasNext()) {
				pho = (Phospho)itr.next();
				ballColor = "lightblue";
				if (pho.id_stat.equals("MISSED")) {
					ballColor = "red";
				} else if (pho.id_stat.equals("NOVEL")) {
					ballColor = "green";				
				} else if (pho.id_stat.equals("FOUND")) {
					ballColor = "lightblue";
				}
				script += "segments(" + pho.loc + ", 250, " + pho.loc + ",450, lwd=2);\n";
				script += "draw.circle(" + pho.loc + ",450," + ballsize + ",col=\"" + ballColor + "\",lwd=2);\n";
				
				if (itr.hasNext()) {
					pho = (Phospho)itr.next();
					ballColor = "lightblue";
					if (pho.id_stat.equals("MISSED")) {
						ballColor = "red";
					} else if (pho.id_stat.equals("NOVEL")) {
						ballColor = "green";				
					} else if (pho.id_stat.equals("FOUND")) {
						ballColor = "lightblue";
					}
					script += "segments(" + pho.loc + ", 250, " + pho.loc + ",600, lwd=2);\n";
					script += "draw.circle(" + pho.loc + ",600," + ballsize + ",col=\"" + ballColor + "\",lwd=2);\n";
				}
			}
		}
		
		itr = reference_phospho.iterator();
		while (itr.hasNext()) {
			Phospho pho = (Phospho)itr.next();
						
			String ballColor = "red";
			
			script += "segments(" + pho.loc + ", 150, " + pho.loc + ",100, lwd=2);\n";
			script += "draw.circle(" + pho.loc + ",100," + ballsize + ",col=\"" + ballColor + "\",lwd=2);\n";
			
		}
		
		
		
		
		if (show_label) {
			itr = phospho.iterator();
			while (itr.hasNext()) {
				Phospho pho = (Phospho)itr.next();
				
				script += "text(" + pho.loc + ",400, \"" + pho.name + "\", pos = 1, srt = 50, adj=c(0),col=\"blue\");\n";			
				if (itr.hasNext()) {
					pho = (Phospho)itr.next();
					script += "text(" + pho.loc + ",550, \"" + pho.name + "\", pos = 1, srt = 50, adj=c(0),col=\"blue\");\n";
					if (itr.hasNext()) {
						pho = (Phospho)itr.next();
						script += "text(" + pho.loc + ",700, \"" + pho.name + "\", pos = 1, srt = 50, adj=c(0),col=\"blue\");\n";				
					}
				}
			}
		}
		script += "dev.off()\n";
		return script;
	}
	
	public static class UniprotGFF {
		public String geneName = "";
		public int start = -1;
		public int end = -1;			
		public LinkedList domains = new LinkedList();
	}
	public static class Domain {
		public int start = -1;
		public int end = -1;
		public String note = "";
	}
	public static class Phospho {
		public String name = "";
		public int loc = -1;		
		public String id_stat = "MISSED"; // can be of FOUND NOVEL MISSED
		
	}
}
