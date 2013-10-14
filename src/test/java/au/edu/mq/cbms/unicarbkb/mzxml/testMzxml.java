package au.edu.mq.cbms.unicarbkb.mzxml;
import java.io.File;
import uk.ac.ebi.pride.tools.mzxml_parser.*;
import uk.ac.ebi.pride.tools.mzxml_parser.MzXMLFile.MzXMLScanIterator;
import uk.ac.ebi.pride.tools.mzxml_parser.mzxml.model.Scan;
public class testMzxml {
	public static void main(String[] args) throws Exception {
		// open a mzXML file 
		File mzxmlFile = new File("D:\\MassSpectrometry\\mzxml\\Fetuin.mzXML");
		MzXMLFile inputParser = new MzXMLFile(mzxmlFile);
		MzXMLScanIterator aMzXMLScanIterator = inputParser.geMS1ScanIterator();
		
		for (Scan aspectrum:aMzXMLScanIterator){
			System.out.println (aspectrum.getBasePeakMz() + "," + aspectrum.getBasePeakIntensity());
		}
	}
}
