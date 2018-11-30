import xmlprocessor.Tagrandomizer;
import xmlprocessor.XmlProcessorException;

/**
 * @author Christof Schulze
 * 
 */
public class Xmlrandomize {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 3) {
			System.err
					.println("Synopsis: Tagrandomizer <XML-File> <element1.element2...elementn[:attribute]> <outfile>\n");
			System.err.println("<XML-File> is the input xml file");
			System.err
					.println("<element1.element2...elementn[:attribute]> is the path of the tags in\n"
							+ "           the xml. This script is able to change either attributes of\n"
							+ "           a given tag or characters inside an xml tag.");
			System.err
					.println("<outfile>  is the name of the file where the result is saved.");
			System.err
					.println("\n This script will replace the content of xml tags or xml attributes by a random number that has the same length as the previous content. ");
			System.exit(1);
		}

		Tagrandomizer tr = new Tagrandomizer();
		try {
			tr.init(args[0], args[1], args[2]);
			tr.dowork();
		} catch (XmlProcessorException e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}
}
