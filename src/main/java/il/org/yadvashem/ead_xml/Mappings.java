package il.org.yadvashem.ead_xml;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Mappings 
{
	private String file_path;
	public Mappings(String filePath) {
		file_path = filePath;
	}
	
	public HashMap<String, SubjectTerms> build() 
	{
        try 
        {
        	HashMap<String, SubjectTerms> hash = new HashMap<String, SubjectTerms>();
            File f = new File(file_path);
            BufferedReader b = new BufferedReader(new FileReader(f));
            String readLine = "";
//            System.out.println("Reading file using Buffered Reader");

            while ((readLine = b.readLine()) != null) 
            {
//                System.out.println(readLine);
                String ary[] = readLine.split("\t");
                if(ary != null && ary.length > 2)
                {
                	String subj = ary[2];
                	String term = ary[0];
                	String id = ary[1];
                	SubjectTerms st = new SubjectTerms(subj, id, term);
                	hash.put(subj, st);
                }
            }
            return hash;
        } 
        catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
    }
}
