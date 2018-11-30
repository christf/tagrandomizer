import xmlprocessor.XmlNop;
import xmlprocessor.XmlProcessorException;

public class Xmlnop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length < 2) {
			System.err
					.println("Synopsis: Tagrandomizer <XML-File> <outfile>\n");
			System.err.println("<XML-File> is the input xml file");

			System.err
					.println("<outfile>  is the name of the file where the result is saved.");
			System.err
					.println("\n This script will replace the content of xml tags or xml attributes by a random number that has the same length as the previous content. ");
			System.exit(1);
		}

		XmlNop nop = new XmlNop();
		try {
			nop.init(args[0], args[1]);
			nop.dowork();
		} catch (XmlProcessorException e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}

}
