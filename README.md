# EAD-XML
EAD XML project for Yad Vashem

This project is about reading and modifying EAD XML files. The project main application is to read an xml file containing certain tags. The tag text is searched in a mappings file which is a CSV file with additional data. If a match has been found, the XML file is modified by adding attributes and some tags and written to the output directory. 

The code is written in JAVA. Syntax: javaw target/classes/il/org/yadvashem/ead_xml/App.class <mapping file> <input dir> <xml file name>

Author: Mark Sasson
Supervisor at Yad Vashem: Kepa Rodriguez
