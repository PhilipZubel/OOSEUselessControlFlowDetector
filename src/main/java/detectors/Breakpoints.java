// Philip Zubel 2479229z

package detectors;

// This class stores the features for a single detection.
// className - name of the class where the control flow or recursive method is located
// methodName - name of the method where the control flow is located/name of the recursive method
// lineStart - beginning of the control flow/recursive method
// lineStart - end of the control flow/recursive method

public class Breakpoints {
	private String className;
	private String methodName;
	private int lineStart;
	private int lineEnd;
	
	Breakpoints(int lineStart, int lineEnd, String className, String methodName) {
		this.className = className;
		this.methodName = methodName;
		this.lineStart = lineStart;
		this.lineEnd = lineEnd;
	}

	@Override
	public String toString() {
		return "className = "+className+", methodName = "+methodName+", startline = "+lineStart + ", endline = " + lineEnd;
	}

}
