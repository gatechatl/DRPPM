package MotifTools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class TestRun {

	
	/*public static String[] loadMotif(String fileName) {
		String[] seqs = null;
		try {
			LinkedList list = new LinkedList();
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			while (in.ready()) {
				String str = in.readLine();
				list.add(str);
			}
			in.close();
			seqs = new String[list.size()];
			int index = 0;
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				String str = (String)itr.next();
				seqs[index] = str;
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seqs;
	}
	public static void main(String[] args) {
		
		String[] tests = loadMotif("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Paper_Powerpoint\\CEASAR\\AKT.txt");
		Alignment alignment = new Alignment();
		int index = 1;
		for (String seq: tests) {
			alignment.addSequence("Name" + index + "\t" + seq);
			index++;
			System.out.println(seq);
		}
		alignment.setAlignmentMethod(new ClustalAlign());
		alignment.noAlign();
		PSSM pssm = new PSSM(alignment);
		System.out.println(pssm.getProfileFragAlignment());
		RyansPSSMSearch search = new RyansPSSMSearch();
		LinkedList<PSSM> pssms = new LinkedList<PSSM>();
		pssms.add(pssm);
		String seq = "TMEARHRVPTTELCRPPAGITTIEAVKRKIQVLQQQADDEARHRVPTTELCRPPAEERAERLQREVEGERRAREQAEAEVASLNRRIQLVEEELDRAQERLATALQKLEEAEKAADESERGMKVIENRALKDEEKMELQEIQLKEAKHIAEEADRKYEEVARKLVIIEGDLERTEERAELAESKCSELEEELKNVTNNLKSLEAQAEKYSQKEDKYEEEIKILTDKLKEAETRAEFAERSVAKLEKTIDDLEDTNSTSGDPVEKKDETPFGVSVAVGLAVFACLFLSTLLLVLNKCGRRNKFGINRPAVLAPEDGLAMSLHFMTLGGSSLSPTEGKGSGLQGHIIENPQYFSDACVHHIKRRDIVLKWELGEGAFGKVFLAECHNLLPEQDKMLVAVKALKEASESARQDFQREAELLTMLQHQHIVRFFGVCTEGRPLLMVFEYMRHGDLNRFLRSHGPDAKLLAGGEDVAPGPLGLGQLLAVASQVAAGMVYLAGLHFVHRDLATRNCLVGQGLVVKIGDFGMSRDIYSTDYYRVGGRTMLPIRWMPPESILYRKFTTESDVWSFGVVLWEIFTYGKQPWYQLSNTEAIDCITQGRELERPRACPPEVYAIMRGCWQREPQQRHSIKDVHARLQALAQAPPVYLDVLGDYKDDDDK";
		int xCount = 0;
		SearchResult[] searchResults = search.search(pssms, seq, xCount);
		for (SearchResult result: searchResults) {
			System.out.println("Position: " + result.getIndex());
			System.out.println("P-value: " + result.getPvalue());
			System.out.println("Log10(Pval): " + -Math.log10(result.getPvalue()));			
		}
		
	}*/
}
