import java.util.Stack;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

public class infixToPostAndPre {
    private Stack<String> expression = new Stack<>();


    infixToPostAndPre(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the expressions in infix form: ");

        String input = scanner.nextLine();
        while (!input.matches(("-?\\d+")) && !input.equals("x") && !input.equals(")") &&!input.equals("(") ) {
            System.out.println("infix expression should start with a number or variable please enter you input again");
            input = scanner.nextLine();
        }

        int openParenthesesCount = 0;

        boolean prevIsDigit = false;
        while(!input.equals("F")){
            if((input.matches("-?\\d+") && !prevIsDigit )|| (input.matches("[+\\-^*/]") && prevIsDigit) || (input.equals("x") && !prevIsDigit )){
                expression.push(input);

                System.out.println("enter the next element or F to exit");
                prevIsDigit = !prevIsDigit;

            }

            else if(input.equals("(")){
                expression.push(input);
                openParenthesesCount++;
                System.out.println("enter the next element or END to exit");
            }

            else if(input.equals(")")){
                if(openParenthesesCount > 0){

                    expression.push(input);
                    openParenthesesCount--;
                }
                else{
                    System.out.println("no open parenthese to close");
                }

                System.out.println("enter the next element or END to exit");
            }

            else if(input.matches("-?\\d+") && prevIsDigit){
                System.out.println("after a number or closing Parentheses or variable there must be an operator please enter the element again or END to exit");
            }
            else if(input.matches("[+\\-^*/]") && !prevIsDigit){
                System.out.println("after an operator or opening Parentheses there must be a number or variable please enter the element again or END to exit");
            }
            else{
                System.out.println("invalid input enter END if the exppression is finished otherwise ");
            }

            input = scanner.nextLine();
        }

        if(!prevIsDigit){
            System.out.println("there was no operand after the last operator so it was removed");

            expression.pop();
        }

        if(openParenthesesCount > 0){
            for(int i = 0; i < openParenthesesCount; i++){

                expression.push(")");
            }
        }
        System.out.println("the expression is: " + expression);


    }

    public Stack<String> toPostFix() {
        Stack<String> postfixStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();


        Stack<String> expressionCopy = new Stack<>();
        expressionCopy.addAll(expression);

        while (!expressionCopy.isEmpty()) {
            String token = expressionCopy.remove(0);
            if (token.matches("-?\\d+|^x$")) {
                postfixStack.push(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    postfixStack.push(operatorStack.pop());
                }
                operatorStack.pop();
            } else {
                while (!operatorStack.isEmpty() && prec(token) <= prec(operatorStack.peek())) {
                    postfixStack.push(operatorStack.pop());
                }
                operatorStack.push(token);
            }
        }

        while (!operatorStack.isEmpty()) {
            postfixStack.push(operatorStack.pop());
        }

        return postfixStack;
    }


    public Stack<String> toPreFix() {
        Stack<String> operatorStack = new Stack<>();
        Stack<String> operandStack = new Stack<>();


        Stack<String> expressionCopy = new Stack<>();
        expressionCopy.addAll(expression);

        while (!expressionCopy.isEmpty()) {
            String token = expressionCopy.pop();

            if (token.matches("-?\\d+|^x$")) {
                operandStack.push(token);
            } else if (token.equals(")")) {
                operatorStack.push(token);
            } else if (token.equals("(")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals(")")) {
                    String operator = operatorStack.pop();
                    String operand1 = operandStack.pop();
                    String operand2 = operandStack.pop();
                    String prefix = operator + " " + operand1 + " " + operand2;
                    operandStack.push(prefix);
                }
                operatorStack.pop();
            } else {
                while (!operatorStack.isEmpty() && prec(token) < prec(operatorStack.peek())) {
                    String operator = operatorStack.pop();
                    String operand1 = operandStack.pop();
                    String operand2 = operandStack.pop();
                    String prefix = operator + " " + operand1 + " " + operand2;
                    operandStack.push(prefix);
                }
                operatorStack.push(token);
            }
        }

        while (!operatorStack.isEmpty()) {
            String operator = operatorStack.pop();
            String operand1 = operandStack.pop();
            String operand2 = operandStack.pop();
            String prefix = operator + " " + operand1 + " " + operand2;
            operandStack.push(prefix);
        }

        Stack<String> resultStack = new Stack<>();
        for (String item : operandStack.pop().split(" ")) {
            resultStack.push(item);
        }

        return resultStack;

    }


    public double calculate(){
        Stack<String> postfix = toPreFix();
        Stack<Double> operands = new Stack<>();
        while (!postfix.empty()){
            String token = postfix.pop();
            if (token.matches(("-?\\d+(\\.\\d+)?"))){
                operands.push(Double.parseDouble(token));
            }
            else if (token.matches("[+\\-*/^]")) {
                double operand1 = operands.pop();
                double operand2 = operands.pop();
                switch (token) {
                    case "+":
                        operands.push(operand1 + operand2);
                        break;
                    case "-":
                        operands.push(operand1 - operand2);
                        break;
                    case "*":
                        operands.push(operand1 * operand2);
                        break;
                    case "/":
                        operands.push((double)operand1 / operand2);
                        break;
                    case "^":
                        operands.push(Math.pow(operand1, operand2));
                        break;
                    default:
                        break;
                }
            }
        }
        return operands.pop();
    }


    public double calculate(double value){
        Stack<String> postfix = toPreFix();
        Stack<Double> operands = new Stack<>();
        while (!postfix.empty()){
            String token = postfix.pop();
            if (token.matches(("-?\\d+(\\.\\d+)?"))){

                operands.push(Double.parseDouble(token));
            }
            else if(token.equals("x")){
                operands.push(value);
            }
            else if (token.matches("[+\\-*/^]")) {
                double operand1 = operands.pop();
                double operand2 = operands.pop();
                switch (token) {
                    case "+":
                        operands.push(operand1 + operand2);
                        break;
                    case "-":
                        operands.push(operand1 - operand2);
                        break;
                    case "*":
                        operands.push(operand1 * operand2);
                        break;
                    case "/":
                        operands.push((double)operand1 / operand2);
                        break;
                    case "^":
                        operands.push(Math.pow(operand1, operand2));
                        break;
                    default:
                        break;
                }
            }
        }
        return operands.pop();
    }


    public double calculatePostfix(){
        Stack<String> postfix = toPostFix();
        Stack<Double> operands = new Stack<>();
        while (!postfix.empty()){
            String token = postfix.remove(0);
            if (token.matches(("-?\\d+(\\.\\d+)?"))){
                operands.push(Double.parseDouble(token));
            }
            else if (token.matches("[+\\-*/^]")) {
                double operand1 = operands.pop();
                double operand2 = operands.pop();
                switch (token) {
                    case "+":
                        operands.push(operand1 + operand2);
                        break;
                    case "-":
                        operands.push(operand2 - operand1);
                        break;
                    case "*":
                        operands.push(operand1 * operand2);
                        break;
                    case "/":
                        operands.push((double)operand2 / operand1);
                        break;
                    case "^":
                        operands.push(Math.pow(operand2, operand1));
                        break;
                    default:
                        break;
                }
            }
        }
        return operands.pop();
    }

    void graph(){
        ArrayList<ArrayList<Double>> points = new ArrayList<>();
        for(double i = -50; i <= 50; i = i + 0.01){
            ArrayList<Double> temp = new ArrayList<>();
            temp.add(i);
            temp.add(calculate(i));
            points.add(temp);
        }
        rasm d = new rasm(points);
    }


    int prec(String c) {
        if (c.equals("^"))
            return 3;
        else if (c.equals("/") || c.equals( "*"))
            return 2;
        else if (c.equals("+") || c.equals("-"))
            return 1;
        else
            return -1;
    }


}
