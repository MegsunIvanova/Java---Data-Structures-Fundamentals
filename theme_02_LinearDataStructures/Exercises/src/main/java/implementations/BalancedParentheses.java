package implementations;

import interfaces.Solvable;

import java.util.ArrayDeque;

public class BalancedParentheses implements Solvable {
    private String parentheses;

    public BalancedParentheses(String parentheses) {
        this.parentheses = parentheses;
    }

    @Override
    public Boolean solve() {

        ArrayDeque<Character> stack = new ArrayDeque<>();

        for (char symbol : parentheses.toCharArray()) {
            if (symbol == '(' || symbol == '{' || symbol == '[') {
                stack.push(symbol);
            } else {
                if (stack.size() == 0) {
                    return false;
                }

                char oppositeSymbol = getOppositeSymbol(stack.pop());
                if (symbol != oppositeSymbol) {
                    return false;
                }
            }
        }

        if (stack.size() > 0) {
            return false;
        }

        return true;
    }

    public char getOppositeSymbol(char symbol) {
        switch (symbol) {
            case '(':
                return ')';
            case '{':
                return '}';
            case '[':
                return ']';
            default:
                return 0;
        }
    }
}
