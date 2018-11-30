import xmlprocessor.Structprint;
import xmlprocessor.XmlProcessorException;

/**
 * @author Christof Schulze
 */
public class Xmlstruc {
	/**
	 * This program will print the structure of an xml file while parsing it.
	 * 
	 * @param inputfile
	 *            is the name of the input xml-fil
	 */
	public static void main(String[] inputfile) {
		if (inputfile[0].isEmpty()) {
			System.err
					.println("This program will print the structure of an xml file");
			System.err.println("Synopsis: Xmlstruc <XML-File>\n");
			System.err.println("<XML-File> is the input xml file");
			System.exit(1);
		}
		Structprint sp = new Structprint();
		try {
			sp.init(inputfile[0]);
			sp.dowork();
		} catch (XmlProcessorException e) {
			e.printStackTrace();
		} finally {
			sp.cleanup();
		}
	}
}
