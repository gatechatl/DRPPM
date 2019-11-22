package rnaseq.exon.quantification;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import misc.CommandLine;

/**
 * Original script was from DEXseq 
 * Reyes A, Anders S, Weatheritt R, Gibson T, Steinmetz L, Huber W (2013). “Drift and conservation of differential exon usage across tissues in primate species.” PNAS, 110, -5. doi: 10.1073/pnas.1307202110.
 * @author tshaw
 *
 */
public class GenerateGTFFileWithExonID {

	public static String type() {
		return "GTFFile";
	}
	public static String description() {
		return "Generate exon IDs based on  Coverage Plot";
	}
	public static String parameter_info() {
		return "[in.gtf] [out.gtf]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputGTF = args[1];
			CommandLine.writeFile("dexseq_prepare_annotation.py", generate_dexseq_script());
			CommandLine.executeCommand("py dexseq_prepare_annotation.py " + inputFile + " " + outputGTF);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generate_dexseq_script() {
		String script = "";
		script += "import sys, collections, itertools, os.path, optparse\n";
		script += "\n";
		script += "optParser = optparse.OptionParser( \n";
		script += "   \n";
		script += "   usage = \"python %prog [options] <in.gtf> <out.gff>\",\n";
		script += "   \n";
		script += "   description=\n";
		script += "      \"Script to prepare annotation for DEXSeq.\" +\n";
		script += "      \"This script takes an annotation file in Ensembl GTF format\" +\n";
		script += "      \"and outputs a 'flattened' annotation file suitable for use \" +\n";
		script += "      \"with the count_in_exons.py script \",\n";
		script += "      \n";
		script += "   epilog = \n";
		script += "      \"Written by Simon Anders (sanders@fs.tum.de), European Molecular Biology \" +\n";
		script += "      \"Laboratory (EMBL). (c) 2010. Released under the terms of the GNU General \" +\n";
		script += "      \"Public License v3. Part of the 'DEXSeq' package.\" )\n";
		script += "\n";
		script += "optParser.add_option( \"-r\", \"--aggregate\", type=\"choice\", dest=\"aggregate\",\n";
		script += "   choices = ( \"no\", \"yes\" ), default = \"yes\",\n";
		script += "   help = \"'yes' or 'no'. Indicates whether two or more genes sharing an exon should be merged into an 'aggregate gene'. If 'no', the exons that can not be assiged to a single gene are ignored.\" )\n";
		script += "\n";
		script += "(opts, args) = optParser.parse_args()\n";
		script += "\n";
		script += "if len( args ) != 2:\n";
		script += "   sys.stderr.write( \"Script to prepare annotation for DEXSeq.\n\n\" )\n";
		script += "   sys.stderr.write( \"Usage: python %s <in.gtf> <out.gff>\n\n\" % os.path.basename(sys.argv[0]) )\n";
		script += "   sys.stderr.write( \"This script takes an annotation file in Ensembl GTF format\n\" )\n";
		script += "   sys.stderr.write( \"and outputs a 'flattened' annotation file suitable for use\n\" )\n";
		script += "   sys.stderr.write( \"with the count_in_exons.py script.\n\" )\n";
		script += "   sys.exit(1)\n";
		script += "\n";
		script += "try:\n";
		script += "   import HTSeq\n";
		script += "except ImportError:\n";
		script += "   sys.stderr.write( \"Could not import HTSeq. Please install the HTSeq Python framework\n\" )   \n";
		script += "   sys.stderr.write( \"available from http://www-huber.embl.de/users/anders/HTSeq\n\" )   \n";
		script += "   sys.exit(1)\n";
		script += "\n";
		script += "\n";
		script += "\n";
		script += "\n";
		script += "gtf_file = args[0]\n";
		script += "out_file = args[1]\n";
		script += "\n";
		script += "aggregateGenes = opts.aggregate == \"yes\"\n";
		script += "\n";
		script += "# Step 1: Store all exons with their gene and transcript ID \n";
		script += "# in a GenomicArrayOfSets\n";
		script += "\n";
		script += "exons = HTSeq.GenomicArrayOfSets( \"auto\", stranded=True )\n";
		script += "for f in HTSeq.GFF_Reader( gtf_file ):\n";
		script += "   if f.type != \"exon\":\n";
		script += "      continue\n";
		script += "   f.attr['gene_id'] = f.attr['gene_id'].replace( \":\", \"_\" )\n";
		script += "   exons[f.iv] += ( f.attr['gene_id'], f.attr['transcript_id'] )\n";
		script += "\n";
		script += "\n";
		script += "# Step 2: Form sets of overlapping genes\n";
		script += "\n";
		script += "# We produce the dict 'gene_sets', whose values are sets of gene IDs. Each set\n";
		script += "# contains IDs of genes that overlap, i.e., share bases (on the same strand).\n";
		script += "# The keys of 'gene_sets' are the IDs of all genes, and each key refers to\n";
		script += "# the set that contains the gene.\n";
		script += "# Each gene set forms an 'aggregate gene'.\n";
		script += "\n";
		script += "if aggregateGenes == True:\n";
		script += "   gene_sets = collections.defaultdict( lambda: set() )\n";
		script += "   for iv, s in exons.steps():\n";
		script += "      # For each step, make a set, 'full_set' of all the gene IDs occuring\n";
		script += "      # in the present step, and also add all those gene IDs, whch have been\n";
		script += "      # seen earlier to co-occur with each of the currently present gene IDs.\n";
		script += "      full_set = set()\n";
		script += "      for gene_id, transcript_id in s:\n";
		script += "         full_set.add( gene_id )\n";
		script += "         full_set |= gene_sets[ gene_id ]\n";
		script += "      # Make sure that all genes that are now in full_set get associated\n";
		script += "      # with full_set, i.e., get to know about their new partners\n";
		script += "      for gene_id in full_set:\n";
		script += "         assert gene_sets[ gene_id ] <= full_set\n";
		script += "         gene_sets[ gene_id ] = full_set\n";
		script += "\n";
		script += "\n";
		script += "# Step 3: Go through the steps again to get the exonic sections. Each step\n";
		script += "# becomes an 'exonic part'. The exonic part is associated with an\n";
		script += "# aggregate gene, i.e., a gene set as determined in the previous step, \n";
		script += "# and a transcript set, containing all transcripts that occur in the step.\n";
		script += "# The results are stored in the dict 'aggregates', which contains, for each\n";
		script += "# aggregate ID, a list of all its exonic_part features.\n";
		script += "\n";
		script += "aggregates = collections.defaultdict( lambda: list() )\n";
		script += "for iv, s in exons.steps( ):\n";
		script += "   # Skip empty steps\n";
		script += "   if len(s) == 0:\n";
		script += "      continue\n";
		script += "   gene_id = list(s)[0][0]\n";
		script += "   ## if aggregateGenes=FALSE, ignore the exons associated to more than one gene ID\n";
		script += "   if aggregateGenes == False:\n";
		script += "      check_set = set()\n";
		script += "      for geneID, transcript_id in s:\n";
		script += "         check_set.add( geneID )\n";
		script += "      if( len( check_set ) > 1 ):\n";
		script += "         continue\n";
		script += "      else:\n";
		script += "         aggregate_id = gene_id\n";
		script += "   # Take one of the gene IDs, find the others via gene sets, and\n";
		script += "   # form the aggregate ID from all of them   \n";
		script += "   else:\n";
		script += "      assert set( gene_id for gene_id, transcript_id in s ) <= gene_sets[ gene_id ] \n";
		script += "      aggregate_id = '+'.join( gene_sets[ gene_id ] )\n";
		script += "   # Make the feature and store it in 'aggregates'\n";
		script += "   f = HTSeq.GenomicFeature( aggregate_id, \"exonic_part\", iv )   \n";
		script += "   f.source = os.path.basename( sys.argv[0] )\n";
		script += "#   f.source = \"camara\"\n";
		script += "   f.attr = {}\n";
		script += "   f.attr[ 'gene_id' ] = aggregate_id\n";
		script += "   transcript_set = set( ( transcript_id for gene_id, transcript_id in s ) )\n";
		script += "   f.attr[ 'transcripts' ] = '+'.join( transcript_set )\n";
		script += "   aggregates[ aggregate_id ].append( f )\n";
		script += "\n";
		script += "\n";
		script += "# Step 4: For each aggregate, number the exonic parts\n";
		script += "\n";
		script += "aggregate_features = []\n";
		script += "for l in list(aggregates.values()):\n";
		script += "   for i in range( len(l)-1 ):\n";
		script += "      assert l[i].name == l[i+1].name, str(l[i+1]) + \" has wrong name\"\n";
		script += "      assert l[i].iv.end <= l[i+1].iv.start, str(l[i+1]) + \" starts too early\"\n";
		script += "      if l[i].iv.chrom != l[i+1].iv.chrom:\n";
		script += "         raise ValueError(\"Same name found on two chromosomes: %s, %s\" % ( str(l[i]), str(l[i+1]) ))\n";
		script += "      if l[i].iv.strand != l[i+1].iv.strand:\n";
		script += "         raise ValueError(\"Same name found on two strands: %s, %s\" % ( str(l[i]), str(l[i+1]) ))\n";
		script += "   aggr_feat = HTSeq.GenomicFeature( l[0].name, \"aggregate_gene\", \n";
		script += "      HTSeq.GenomicInterval( l[0].iv.chrom, l[0].iv.start, \n";
		script += "         l[-1].iv.end, l[0].iv.strand ) )\n";
		script += "   aggr_feat.source = os.path.basename( sys.argv[0] )\n";
		script += "   aggr_feat.attr = { 'gene_id': aggr_feat.name }\n";
		script += "   for i in range( len(l) ):\n";
		script += "      l[i].attr['exonic_part_number'] = \"%03d\" % ( i+1 )\n";
		script += "   aggregate_features.append( aggr_feat )\n";
		script += "      \n";
		script += "      \n";
		script += "# Step 5: Sort the aggregates, then write everything out\n";
		script += "\n";
		script += "aggregate_features.sort( key = lambda f: ( f.iv.chrom, f.iv.start ) )\n";
		script += "\n";
		script += "fout = open( out_file, \"w\" ) \n";
		script += "for aggr_feat in aggregate_features:\n";
		script += "   fout.write( aggr_feat.get_gff_line() )\n";
		script += "   for f in aggregates[ aggr_feat.name ]:\n";
		script += "      fout.write( f.get_gff_line() )\n";
		script += "\n";
		script += "fout.close()      \n";
		script += "\n";
		return script;
	}
}
