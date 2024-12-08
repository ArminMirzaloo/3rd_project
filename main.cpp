#include <iostream>
#include <stack>
#include <string>
#include <sstream>
#include <cmath>
#include <cctype>
#include <algorithm>
using namespace std;

// infix to postfix conversion class
class InfixToPostfix {
private:
    string infix;
    string postfix;

    int precedence(char op) {
        switch (op) {
            case '^': return 3;
            case '*':
            case '/': return 2;
            case '+':
            case '-': return 1;
            default: return 0;
        }
    }

    bool isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    bool isNumeric(const string& str) {
        if (str.empty()) return false;
        size_t i = (str[0] == '_') ? 1 : 0;
        bool dotFound = false;
        for (; i < str.size(); ++i) {
            if (str[i] == '.') {
                if (dotFound) return false;
                dotFound = true;
            } else if (!isdigit(str[i])) {
                return false;
            }
        }
        return true;
    }

public:
    InfixToPostfix(string expr) : infix(expr) {}

    void convertToPostfix() {
        stack<char> operators;
        stringstream result;

        for (size_t i = 0; i < infix.size(); ++i) {
            char current = infix[i];

            if (isalnum(current) || current == '.' || current == '_') {
                string operand;

                if (current == '_') {
                    operand += '-';
                    ++i;
                }

                while (i < infix.size() && (isalnum(infix[i]) || infix[i] == '.')) {
                    operand += infix[i];
                    ++i;
                }
                --i;

                result << operand << " ";
            } else if (current == '(') {
                operators.push(current);
            } else if (current == ')') {
                while (!operators.empty() && operators.top() != '(') {
                    result << operators.top() << " ";
                    operators.pop();
                }
                if (!operators.empty()) operators.pop();
            } else if (isOperator(current)) {
                while (!operators.empty() && precedence(operators.top()) >= precedence(current)) {
                    result << operators.top() << " ";
                    operators.pop();
                }
                operators.push(current);
            }
        }

        while (!operators.empty()) {
            result << operators.top() << " ";
            operators.pop();
        }

        postfix = result.str();
    }

    double evaluatePostfix() {
        stack<double> values;
        stringstream ss(postfix);
        string token;

        while (ss >> token) {
            if (isNumeric(token)) {
                double value = stod(token);
                values.push(value);
            } else if (isOperator(token[0])) {
                if (values.size() < 2) {
                    cerr << "Error: Invalid postfix expression!" << endl;
                    return 0.0;
                }
                double b = values.top(); values.pop();
                double a = values.top(); values.pop();

                switch (token[0]) {
                    case '+': values.push(a + b); break;
                    case '-': values.push(a - b); break;
                    case '*': values.push(a * b); break;
                    case '/':
                        if (b == 0) {
                            cerr << "Error: Division by zero!" << endl;
                            return 0.0;
                        }
                        values.push(a / b);
                        break;
                    case '^': values.push(pow(a, b)); break;
                }
            }
        }

        if (values.size() == 1) {
            return values.top();
        } else {
            cerr << "Error: Invalid evaluation result!" << endl;
            return 0.0;
        }
    }

    void processExpression() {
        convertToPostfix();
        cout << "Postfix Expression: " << postfix << endl;

        if (postfix.find_first_of("0123456789") != string::npos) {
            double result = evaluatePostfix();
            cout << "Result: " << result << endl;
        }
    }

    string getPostfix() {
        return postfix;
    }
};

//*****************************************************************************

// infix to prefix conversion class
class InfixToPrefix {
private:
    string infix;
    string prefix;

    void reverseExpression(string& expr) {
        reverse(expr.begin(), expr.end());
        for (char& c : expr) {
            if (c == '(') c = ')';
            else if (c == ')') c = '(';
        }
    }

    void convertToPrefix() {
        reverseExpression(infix);
        InfixToPostfix postfixConverter(infix);
        postfixConverter.convertToPostfix();
        stringstream ss(postfixConverter.getPostfix());
        stack<string> st;
        string token;

        while (ss >> token) {
            st.push(token);
        }

        stringstream result;
        while (!st.empty()) {
            result << st.top() << " ";
            st.pop();
        }

        prefix = result.str();
    }

public:
    InfixToPrefix(string expr) : infix(expr) {}

    void processExpression() {
        convertToPrefix();
        cout << "Prefix Expression: " << prefix << endl;
    }
};

//******************************************************************

int main() {
    while (true) {
        cout << "Choose an option:\n";
        cout << "0 - Exit\n";
        cout << "1 - Convert to Postfix\n";
        cout << "2 - Convert to Prefix\n";
        cout << "Enter your choice: ";

        int choice;
        cin >> choice;
        cin.ignore();

        if (choice == 0) {
            cout << "Exiting program. Goodbye!" << endl;
            exit(0);
        } else if (choice == 1) {
            cout << "Enter an infix expression: ";
            string expression;
            getline(cin, expression);
            InfixToPostfix postfixConverter(expression);
            postfixConverter.processExpression();
        } else if (choice == 2) {
            cout << "Enter an infix expression: ";
            string expression;
            getline(cin, expression);
            InfixToPrefix prefixConverter(expression);
            prefixConverter.processExpression();
        } else {
            cout << "Invalid choice. Please try again." << endl;
        }
    }
    return 0;
}