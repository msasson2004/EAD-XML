package il.org.yadvashem.ead_xml;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.util.HashMap;
import java.util.List;

public class App 
{
    public static void main( String[] args )
    {
    	String table_mappings, folder_input, result_file, out_folder;
    	File[] listOfFiles;

    	if(args.length > 1) {
    		folder_input = args[0];
        	table_mappings = args[1];
        	out_folder = folder_input + "_result";
        	File theDir = new File(out_folder);
        	
        	try 
        	{
        		if(!theDir.exists())
        			theDir.mkdir();
        		File folder = new File(folder_input);
        		listOfFiles = folder.listFiles();
        	}
        	catch(Exception e) {
        		System.out.println(e.getMessage());
        		return;
        	}
    	}
    	else {
    		System.out.println("Syntax: java App.class <mapping file> <input dir>");
    		return;
    	}
    	 

	    try 
	    {	
	    	String name;
			SubjectTerms st;
	    	Mappings map = new Mappings(table_mappings);
	    	HashMap<String, SubjectTerms> hash = map.build();
	    	SAXBuilder saxBuilder = new SAXBuilder();
	    	for(int i=0;i < listOfFiles.length;i++) 
	    	{
	    		if (listOfFiles[i].isFile()) 
	    		{	   
	                String file_name = listOfFiles[i].getName();
	            	result_file = out_folder + "/" + file_name;        	
			        System.out.println("working on: " + file_name);
			        Document document = saxBuilder.build(listOfFiles[i]);
			        Element root = document.getRootElement();
			        Element arch = getChild(root, "archdesc");
			        if(arch != null) {
			        	Element did = getChild(arch, "did");
			        	if(did != null) {
			        		Element origin = getChild(did, "origination");
			        		if(origin != null) {
			        			name = origin.getText();
			        			st = hash.get(name);
			        			if(st != null) {
				        			System.out.println("Found subject: " + st.Subject + " index: " + st.Index + " term: " + st.Term);
				        			// set the authfilenumber attribute of origination
				        			origin.setAttribute("authfilenumber", st.Index);
				        			origin.setAttribute("source", st.Term);
			        			}
			        		}
			        	}
	        			List<Element> list = arch.getChildren();
	        			// find controlaccess tag
	        			for(int j=0;j<list.size();j++) 
	        			{
	        				Element ctrlAccess = list.get(j);
		        	        if(ctrlAccess != null && ctrlAccess.getName() == "controlaccess") 
		        	        {
		        	        	List<Element> children = ctrlAccess.getChildren();
		        	        	for(int k=0;k<children.size();k++) 
		        	        	{			
		        	        		Element elem = children.get(k);
		        	        		if(elem.getName() == "subject") {
		        	        			name = elem.getText();
		        	        			st = hash.get(name);
		        	        			if(st != null) {
		        	        				elem.setAttribute("authfilenumber", st.Index);
		        	        				elem.setAttribute("source", st.Term);					        	        				
		        	        			}
		        	        		}
		        	        		else if(elem.getName() == "geogname") {
		        	        			name = elem.getText();
		        	        			st = hash.get(name);
		        	        			if(st != null) {
		        	        				elem.setAttribute("authfilenumber", st.Index);
		        	        				elem.setAttribute("source", st.Term);					        	        				
		        	        			}
		        	        		}
		        	        		if(elem.getName() == "corpname") {
		        	        			name = elem.getText();
		        	        			st = hash.get(name);
		        	        			if(st != null) {
		        	        				elem.setAttribute("authfilenumber", st.Index);
		        	        				elem.setAttribute("source", st.Term);					        	        				
		        	        			}
		        	        		}
		        	        	}
		        	        }
	        			}
			        	// 		write the xml file   
	        			System.out.println("writing xml file");
	        			XMLOutputter xmlOutput = new XMLOutputter();
	        	        xmlOutput.setFormat(Format.getPrettyFormat());
	        	        xmlOutput.output(document, new FileOutputStream(result_file)); 
			        }
	    		}
	    	}
	        System.out.println("Done");
	    } 
	    catch (FileNotFoundException e) {
	    	System.out.println(e.getMessage());
    	} 
	    catch (Exception e) {
    		System.out.println(e.getMessage());
    	}	    	
    }
    //
    // unfortunately, the getChild of element by name does not work and I have to resort to this
    // way of finding a child element.
    // Takes an element and searches its children for the given tag name.
    // returns: the child element with the given name or null.
    public static Element getChild(Element elem, String name) {
    	if(elem == null)
    		return null;
        List<Element> list = elem.getChildren();
        for(int temp =0;temp< list.size();temp++)
        {
        	Element child = list.get(temp);
        	if(child.getName() == name)
        		return child;
        }
        return null;
    }   
}