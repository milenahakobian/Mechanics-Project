import java.util.Stack;

public class SpringArray {
    
    public static Spring equivalentSpring(String springExpr) {
        Stack<Spring> stack = new Stack<>();
        
        for (int i = 0; i < springExpr.length(); i++) {
            char c = springExpr.charAt(i);
            if (c == '[') {
                stack.push(new Spring());
            } else if (c == '{') {
                stack.push(new Spring(1));
            } else if (c == ']') {
                Spring spring = stack.pop();
                Spring prevSpring = stack.peek();
                prevSpring.inParallel(spring);
            } else if (c == '}') {
                Spring spring = stack.pop();
                Spring prevSpring = stack.peek();
                prevSpring.inSeries(spring);
            }
        }
        
        return stack.pop();
    }
    
    public static Spring equivalentSpring(String springExpr, Spring[] springs) {
        Stack<Spring> stack = new Stack<>();
        
        for (int i = 0; i < springExpr.length(); i++) {
            char c = springExpr.charAt(i);
            if (c == '[') {
                stack.push(springs[0]);
            } else if (c == '{') {
                stack.push(springs[1]);
            } else if (c == ']') {
                Spring spring = stack.pop();
                Spring prevSpring = stack.peek();
                prevSpring.inParallel(spring);
            } else if (c == '}') {
                Spring spring = stack.pop();
                Spring prevSpring = stack.peek();
                prevSpring.inSeries(spring);
            }
        }
        
        return stack.pop();
    }
}
