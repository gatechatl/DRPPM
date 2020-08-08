package stjude.projects.jinghuizhang.usp7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class JinghuiZhangLoadText {

	public static void main(String[] args) {
		
		try {
			
			LinkedList id = new LinkedList();
			LinkedList geneSymbol = new LinkedList();
			LinkedList id2 = new LinkedList();
			LinkedList non = new LinkedList();
			LinkedList psel = new LinkedList();
			LinkedList nsel = new LinkedList();
			LinkedList non_psel = new LinkedList();
			
			String inputFile = "C:\\Users\\tshaw\\Downloads\\SuppFigure1.txt";
			String outputFile = "C:\\Users\\tshaw\\Downloads\\SuppFigure1_convert.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				for (int i = 0; i < 50; i++) {				
					id.add(str);
					str = in.readLine();
				}
				for (int i = 0; i < 50; i++) {
					geneSymbol.add(str);
					str = in.readLine();
				}
				for (int i = 0; i < 50; i++) {
					id2.add(str);
					str = in.readLine();
				}
				for (int i = 0; i < 50; i++) {
					non.add(str);
					str = in.readLine();
				}
				for (int i = 0; i < 50; i++) {
					psel.add(str);
					str = in.readLine();
				}
				for (int i = 0; i < 50; i++) {
					nsel.add(str);
					str = in.readLine();
				}
				for (int i = 0; i < 50; i++) {
					non_psel.add(str);
					str = in.readLine();
				}
				
			}
			in.close();
			
			Iterator id_itr = id.iterator();
			Iterator geneSymbol_itr = geneSymbol.iterator();
			Iterator id2_itr = id2.iterator();
			Iterator non_itr = non.iterator();
			Iterator psel_itr = psel.iterator();
			Iterator nsel_itr = nsel.iterator();
			Iterator non_psel_itr = non_psel.iterator();
			
			while (id_itr.hasNext()) {
				String id_txt = (String)id_itr.next();
				String geneSymbol_txt = (String)geneSymbol_itr.next();
				String id2_txt = (String)id2_itr.next();
				String non_txt = (String)non_itr.next();
				String psel_txt = (String)psel_itr.next();
				String nsel_txt = (String)nsel_itr.next();
				String non_psel_txt = (String)non_psel_itr.next();
				out.write(id_txt + "\t" + geneSymbol_txt + "\t" + id2_txt + "\t" + non_txt + "\t" + psel_txt + "\t" + nsel_txt + "\t" + non_psel_txt + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
