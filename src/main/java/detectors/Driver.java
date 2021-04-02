// Philip Zubel 2479229z

package detectors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

public class Driver {
//	private static final String FILE_PATH = "C:\\Users\\Philip\\eclipse-workspace\\coursework2\\src\\test\\testcode\\Calculator.java";

	public static void main(String args[]) {
		try {
			// parse the file
			CompilationUnit cu = JavaParser.parse(new FileInputStream(args[0]));

			// intialize the UselessControlFlowDetector and a list breakpoints to store the statement features
			VoidVisitor<List<Breakpoints>> controlFlowVisitor = new UselessControlFlowDetector();
			List<Breakpoints> collectorUseless = new ArrayList<Breakpoints>();

			System.out.println("Useless Control Flows:");
			
			// call the abstract VoidVisitor class with and 
			// pass the parsed file + an empty list breakpoints
			controlFlowVisitor.visit(cu, collectorUseless);
			
			// print the useless control flow statement features 
			collectorUseless.forEach(m -> {
				System.out.println(m);
			});

			// intialize the RecursionDetector and a list breakpoints to store the method features
			VoidVisitor<List<Breakpoints>> methodVisitor = new RecursionDetector();
			List<Breakpoints> collectorRecursion = new ArrayList<Breakpoints>();

			System.out.println("Recursions:");
			
			// call the abstract VoidVisitor class with and 
			// pass the parsed file + an empty list breakpoints
			methodVisitor.visit(cu, collectorRecursion);
			
			// print the recursion method features 
			collectorRecursion.forEach(m -> {
				System.out.println(m);
			});

		}
		// handle command line exceptions 
		catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: File not found.");
		} 
		catch (IndexOutOfBoundsException e) {
			System.out.println("IndexOutOfBoundsException: Path not entered.");
		}
		
	}
}
