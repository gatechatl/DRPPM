package proteomics.phospho.tools.coordinate.converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;




import java.net.URL;


import java.util.HashMap;
import java.util.Iterator;


/*
import org.biojava.bio.alignment.NeedlemanWunsch;
import org.biojava.bio.alignment.SequenceAlignment;
import org.biojava.bio.alignment.SmithWaterman;
import org.biojava.bio.alignment.SubstitutionMatrix;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.symbol.AlphabetManager;
import org.biojava.bio.symbol.FiniteAlphabet;
*/
import jaligner.Alignment;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.formats.Pair;
import jaligner.matrix.Matrix;
import jaligner.matrix.MatrixLoader;
import jaligner.util.Commons;
import jaligner.util.SequenceParser;

//import org.biojava3.alignment;

public class HumanCentricProteinAlignment {

	
	public static void execute(String[] args) {

		try {
			
			String inputFile = args[0];
			String outputHomolog = args[1];
			String alignment_human = args[2];
			HashMap human_map = new HashMap();
			HashMap mouse_map = new HashMap();
			HashMap rat_map = new HashMap();
			
			FileWriter fwriter = new FileWriter(outputHomolog);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_human = new FileWriter(alignment_human);
			BufferedWriter out_human = new BufferedWriter(fwriter_human);
			
			String name = "";
			String organism = "";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				
				if (str.contains(">")) {
					name = str;
					if (name.contains("Homo sapiens")) {
						organism = "human";
					} else if (name.contains("Mus musculus")) {
						organism = "mouse";
					} else if (name.contains("Rattus norvegicus")) {
						organism = "rat";
					} else {
						organism = "";
					}
				} else {
					if (organism.equals("human")) {
						if (human_map.containsKey(name)) {
							String seq = (String)human_map.get(name);
							seq += str.trim();
							human_map.put(name, seq);
						} else {
							human_map.put(name, str.trim());
						}
					} else if (organism.equals("mouse")) {
						if (mouse_map.containsKey(name)) {
							String seq = (String)mouse_map.get(name);
							seq += str.trim();
							mouse_map.put(name, seq);
						} else {
							mouse_map.put(name, str.trim());
						}
					} else if (organism.equals("rat")) {
						if (rat_map.containsKey(name)) {
							String seq = (String)rat_map.get(name);
							seq += str.trim();
							rat_map.put(name, seq);
						} else {
							rat_map.put(name, str.trim());
						}
					}
				}
				
			}
			in.close();
			System.out.println(human_map.size() + "\t" + mouse_map.size() + "\t" + rat_map.size());
			
			Iterator itr_human = human_map.keySet().iterator();
			while (itr_human.hasNext()) {
				String human_key = (String)itr_human.next();
				String human_seq = (String)human_map.get(human_key);
				Iterator itr_mouse = mouse_map.keySet().iterator();
				while (itr_mouse.hasNext()) {
					String mouse_key = (String)itr_mouse.next();
					String mouse_seq = (String)mouse_map.get(mouse_key);
					if (mouse_key.split(" ")[0].split("\\|")[2].equals(human_key.split(" ")[0].replaceAll("_HUMAN", "_MOUSE").split("\\|")[2])) {
						
						out.write(human_key + "\t" + mouse_key + "\n");
						
						out_human.write(human_key + "\t" + mouse_key + "\n");
						out_human.write(printAlignmnet(human_seq, mouse_seq));
						out.flush();
						out_human.flush();
					}										
					
				}
				
				Iterator itr_rat = rat_map.keySet().iterator();
				while (itr_rat.hasNext()) {
					String rat_key = (String)itr_rat.next();
					String rat_seq = (String)rat_map.get(rat_key);
					if (rat_key.split(" ")[0].split("\\|")[2].equals(human_key.split(" ")[0].replaceAll("_HUMAN", "_RAT").split("\\|")[2])) {						
						out.write(human_key + "\t" + rat_key + "\n");
						
						out_human.write(human_key + "\t" + rat_key + "\n");
						out_human.write(printAlignmnet(human_seq, rat_seq));
						out.flush();
						out_human.flush();
					}															
				}
				System.out.println(human_key);
			}
			//System.exit(0);
			//String seq1 = "MLRGGRRGQLGWHSWAAGPGSLLAWLILASAGAAPCPDACCPHGSSGLRCTRDGALDSLH";
			//String seq2 = "GRRGQLGWHSWAAGPGSLLWWWLILASAGAAPCPDACCPHGSSGLRCTRDGALDSLH";
			

			//System.out.println(alignment.getSequence1IndexFromSequence2Index(1));
			//System.out.println(alignment.getSequence2IndexFromSequence1Index(4));
			out.close();
			out_human.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String printAlignmnet(String seq1, String seq2) {
		try {
			Sequence s1 = SequenceParser.parse(seq1);
			Sequence s2 = SequenceParser.parse(seq2);
			Alignment alignment = SmithWatermanGotoh.align(s1, s2, MatrixLoader.load("BLOSUM62"), 10f, 0.5f);
			//System.out.println ( alignment.getSummary() );
			String aln1 = new String(alignment.getSequence1());
			String line = new String(alignment.getMarkupLine());
			String aln2 = new String(alignment.getSequence2());
			
			String result = seq1 + "\t" + seq2 + "\n";
			result += aln1 + "\n";
			result += line + "\n";
			result += aln2 + "\n";
			System.out.println(aln1);
			System.out.println(line);
			System.out.println(aln2);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/*private static void alignPairGlobal(String id1, String id2) throws Exception {
        ProteinSequence s1 = getSequenceForId(id1), s2 = getSequenceForId(id2);
        SubstitutionMatrix<AminoAcidCompound> matrix = new SimpleSubstitutionMatrix<AminoAcidCompound>(null, null);
        SequencePair<ProteinSequence, AminoAcidCompound> pair = Alignments.getPairwiseAlignment(s1, s2,
                PairwiseSequenceAlignerType.GLOBAL, new SimpleGapPenalty(), matrix);
        System.out.printf("%n%s vs %s%n%s", pair.getQuery().getAccession(), pair.getTarget().getAccession(), pair);
    }
	private static ProteinSequence getSequenceForId(String uniProtId) throws Exception {
        URL uniprotFasta = new URL(String.format("http://www.uniprot.org/uniprot/%s.fasta", uniProtId));
        ProteinSequence seq = FastaReaderHelper.readFastaProteinSequence(uniprotFasta.openStream()).get(uniProtId);
        System.out.printf("id : %s %s%n%s%n", uniProtId, seq, seq.getOriginalHeader());
        return seq;
    }*/
    
}
