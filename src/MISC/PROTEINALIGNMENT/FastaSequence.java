package misc.proteinalignment;
import java.lang.*;
import java.io.*;
import java.util.*;
/**
 * http://www.cs.utexas.edu/~mobios/cs329e/rosetta/src/FastaSequence.java
 * This class will read first sequence from a Fasta format file 
 */

public final class FastaSequence {

    private String [] description;
    private String [] sequence;

    public FastaSequence(String filename)
    {
       	readSequenceFromFile(filename);
    }

    void readSequenceFromFile(String file)
    {
	List desc= new ArrayList();
	List seq = new ArrayList();
	try{
        BufferedReader in     = new BufferedReader( new FileReader( file ) );
        StringBuffer   buffer = new StringBuffer();
        String         line   = in.readLine();
     
        if( line == null )
            throw new IOException( file + " is an empty file" );
     
        if( line.charAt( 0 ) != '>' )
            throw new IOException( "First line of " + file + " should start with '>'" );
        else
            desc.add(line);
        for( line = in.readLine().trim(); line != null; line = in.readLine() )
	{
            if( line.length()>0 && line.charAt( 0 ) == '>' )
	    {
		seq.add(buffer.toString());
		buffer = new StringBuffer();
		desc.add(line);
	    } else  
            	buffer.append( line.trim() );
        }   
        if( buffer.length() != 0 )
	     seq.add(buffer.toString());
      }catch(IOException e)
      {
        System.out.println("Error when reading "+file);
        e.printStackTrace();
      }

	description = new String[desc.size()];
	sequence = new String[seq.size()];
	for (int i=0; i< seq.size(); i++)
	{
	  description[i]=(String) desc.get(i);
	  sequence[i]=(String) seq.get(i);
	}
 
    }
    
    //return first sequence as a String
    public String getSequence(){ return sequence[0];}

    //return first xdescription as String
    public String getDescription(){return description[0];}

    //return sequence as a String
    public String getSequence(int i){ return sequence[i];}

    //return description as String
    public String getDescription(int i){return description[i];}
    
    public int size(){return sequence.length;}
    public static void main(String [] args) throws Exception
    {
	String fn ="";
	if (args.length>0) fn=args[0];
	else 
	{
	   System.out.print("Enter the name of the FastaFile:");
	   fn = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	}
	FastaSequence fsf= new FastaSequence(fn);
	for (int i=0; i< fsf.size(); i++)
	{
	System.out.println("One sequence read from file "+ fn +" with length "+fsf.getSequence().length() );
	System.out.println("description: \n"+ fsf.getDescription(i));
	System.out.println("Sequence: \n"+ fsf.getSequence(i));
	}
    }

}
