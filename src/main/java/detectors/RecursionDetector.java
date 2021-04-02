// Philip Zubel 2479229z

package detectors;

import java.util.List;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class RecursionDetector extends VoidVisitorAdapter<List<Breakpoints>> {

	// check if a method has a method call with the same name as the method
	@Override
	public void visit(MethodDeclaration md, List<Breakpoints> collector) {
		// get features of the method
		String className = md.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString();
		String methodName = md.getNameAsString();
		int begin = md.getRange().get().begin.line;
		int end = md.getRange().get().end.line;

		// get all method calls and determine if there is a method call with the same name as the method
		boolean b = md.findAll(MethodCallExpr.class).stream()
				.map(m -> m.getNameAsString())
				.filter(m -> m.equals(methodName))
				.findAny().isPresent();

		// if method call name is the same as method name then add method features to the collector
		if (b) collector.add(new Breakpoints(begin, end, className, methodName));
	}
}
