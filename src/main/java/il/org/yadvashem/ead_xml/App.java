package il.org.yadvashem.ead_xml;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jdom2.Attribute;
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
    	String table_mappings, folder_input, result_file, file_name;

    	if(args.length > 2) {
    		folder_input = args[0];
        	table_mappings = folder_input + "/" + args[1];
        	file_name = folder_input + "/" + args[2];
        	String out_folder = folder_input + "_result";
        	File theDir = new File(out_folder);
        	
        	try 
        	{
        		if(!theDir.exists())
        			theDir.mkdir();
        	}
        	catch(Exception e) {
        		System.out.println(e.getMessage());
        		return;
        	}
        	result_file = out_folder + "/" + args[2];        	
    	}
    	else {
    		System.out.println("Syntax: javaw <mapping file> <input dir> <file name>");
    		return;
    	}
    	 

	    try 
	    {	
	    	Mappings map = new Mappings(table_mappings);
	    	HashMap<String, SubjectTerms> hash = map.build();
	    	SAXBuilder saxBuilder = new SAXBuilder();
	        Document document = saxBuilder.build(new File(file_name));
	        Element root = document.getRootElement();
	        System.out.println("root is: " + root.getName());
	        Element arch = getChild(root, "archdesc");
	        if(arch != null) {
	        	Element did = getChild(arch, "did");
	        	if(did != null) {
	        		Element origin = getChild(did, "origination");
	        		if(origin != null) {
	        			String name = origin.getText();
	        			SubjectTerms st = hash.get(name);
	        			if(st != null) {
		        			System.out.println("Found subject: " + st.Subject + " index: " + st.Index + " term: " + st.Term);
		        			// set the authfilenumber attribute of origination
		        			origin.setAttribute("authfilenumber", st.Index);
		        			// create controlaccess tags
/*		        	        Element ctrlAccess = new Element("controlaccess");
		        	        Element subject = new Element("subject").setText("somehing");
		        	        ctrlAccess.addContent(subject);
		        	        subject = new Element("subject").setText("somehing2");
		        	        ctrlAccess.addContent(subject);
		        	        arch.addContent(ctrlAccess);
		        	        ctrlAccess = new Element("controlaccess");
		        	        subject = new Element("geogname").setText("Germany");
		        	        ctrlAccess.addContent(subject);
		        	        arch.addContent(ctrlAccess);
		        	        ctrlAccess = new Element("controlaccess");
		        	        subject = new Element("corpname").setText(st.Subject);
		        	        ctrlAccess.addContent(subject);
		        	        arch.addContent(ctrlAccess); */
		        	// 		write the xml file   
		        			System.out.println("writing xml file");
		        			XMLOutputter xmlOutput = new XMLOutputter();
		        	        xmlOutput.setFormat(Format.getPrettyFormat());
		        	        xmlOutput.output(document, new FileOutputStream(result_file)); 
	        			}
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
