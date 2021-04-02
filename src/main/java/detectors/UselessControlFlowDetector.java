// Philip Zubel 2479229z

package detectors;

import java.util.List;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class UselessControlFlowDetector extends VoidVisitorAdapter<List<Breakpoints>> {
	
	// extracts all the features from the control statement
	public void visitHelper(Statement stmt, List<Breakpoints> collector) {
		String className = stmt.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString();
		String methodName = stmt.findAncestor(MethodDeclaration.class).get().getNameAsString();
		int begin = stmt.getRange().get().begin.line;
		int end = stmt.getRange().get().end.line;
		// add features to collector
		collector.add(new Breakpoints(begin, end, className, methodName));
	}

	// detects useless if statements
	@Override
	public void visit(IfStmt ifstmt, List<Breakpoints> collector) {
		super.visit(ifstmt, collector);

		// check if statement is not empty (omit comments)
		boolean doChildrenExist = ifstmt.getThenStmt().getChildNodes().stream()	
			.findAny()
			.filter(c -> !Comment.class.isAssignableFrom(c.getClass()))
			.isPresent();
		
		// if statement is empty, add its features to the collector
		if(!doChildrenExist) visitHelper(ifstmt, collector);
		
	}
	
	// detects useless for loop statements
	@Override
	public void visit(ForStmt forstmt, List<Breakpoints> collector) {
		super.visit(forstmt, collector);
		
		// check if statement is not empty (omit comments)
		boolean doChildrenExist = forstmt.getBody().getChildNodes().stream()
				.findAny()
				.filter(c -> !Comment.class.isAssignableFrom(c.getClass()))
				.isPresent();
		
		// if statement is empty, add its features to the collector
		if(!doChildrenExist) visitHelper(forstmt, collector);		
	}
	
	// detects useless while loop statements
	@Override
	public void visit(WhileStmt whilestmt, List<Breakpoints> collector) {
		super.visit(whilestmt, collector);
		
		// check if statement is not empty (omit comments)
		boolean doChildrenExist = whilestmt.getBody().getChildNodes().stream()
			.findAny()
			.filter(c -> !Comment.class.isAssignableFrom(c.getClass()))
			.isPresent();
		
		// if statement is empty, add its features to the collector
		if(!doChildrenExist) visitHelper(whilestmt, collector);
	}
	
	// detects useless do while loop statements
	@Override
	public void visit(DoStmt dostmt, List<Breakpoints> collector) {
		super.visit(dostmt, collector);

		// check if statement is not empty (omit comments)
		boolean doChildrenExist = dostmt.getBody().getChildNodes().stream()
			.findAny()
			.filter(c -> !Comment.class.isAssignableFrom(c.getClass()))
			.isPresent();
		
		// if statement is empty, add its features to the collector
		if(!doChildrenExist) visitHelper(dostmt, collector);	
	}
	
	//	detects useless switch statements
	@Override
	public void visit(SwitchStmt switchstmt, List<Breakpoints> collector) {
		super.visit(switchstmt, collector);
		
		// check if any of the switch cases is not empty (omit comments and case arguments) 
		boolean doChildrenExist = switchstmt.getEntries().stream()
			.map(e -> e.getChildNodes().stream()
					.filter(c -> !Comment.class.isAssignableFrom(c.getClass()))
					.filter(c -> c.getClass() != CharLiteralExpr.class)
					.filter(c -> c.getClass() != BooleanLiteralExpr.class)
					.filter(c -> c.getClass() != IntegerLiteralExpr.class)
					.findAny()
					.isPresent()
					)
			.anyMatch(b -> b == true);

		// if all cases are empty, add the switch statement's features to the collector
		if(!doChildrenExist) visitHelper(switchstmt, collector); 
	}

	
}
