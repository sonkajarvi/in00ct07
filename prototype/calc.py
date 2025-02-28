
# expr = number /
#        ( expr ( '!' / '%' ) ) /
#        ( expr op expr ) /
#        ( '(' expr ')' ) /
#        ( func '(' expr [ ',' expr ] ')' )
#
# op = '+' /
#      '-' /
#      '*' /
#      '/' /
#      '^'
#
# func = "sin" /
#        "cos" /
#        "tan" /
#        "sinh" /
#        "cosh" /
#        "tanh" /
#        "ln" /
#        "log10" /
#        "sqrt" /
#        "cbrt" /
#        "yroot" /      ; takes 2 arguments
#        "logy" /       ; takes 2 arguments
#        "asin" /
#        "acos" /
#        "atan" /
#        "asinh" /
#        "acosh" /
#        "atanh"

import re
from op import *

class Token():
    def __repr__(self):
        return self.repr

class Number(Token):
    def __init__(self, value):
        self.value = float(value)
        self.repr = f"{self.value:g}"

class Function(Token):
    def __init__(self, func, argc, repr):
        self.func = lambda *x: func(*x)
        self.argc = argc
        self.repr = repr

class Operator(Token):
    def __init__(self, func, argc, prec, assoc, repr):
        self.func = func
        self.argc = argc
        self.prec = prec
        self.assoc = assoc
        self.repr = repr

class Constant(Token):
    def __init__(self, value, repr):
        self.value = value
        self.repr = repr

class ParenLeft(Token):
    def __init__(self):
        self.repr = "("

class ParenRight(Token):
    def __init__(self):
        self.repr = ")"

class Comma(Token):
    def __init__(self):
        self.repr = ","

def tokenize(expr):
    r = re.compile(
        r"(?<=[\d\)])?\-(?=[\d\D\(-])+|[+*/^!%\(\),]"   # Operators
        r"|\-?[\d]+(?:\.\d+)?(?:[eE][-+]?\d+)?"         # Numbers
        r"|sinh|cosh|tanh|sin|cos|tan"                  # Functions
        r"|ln|log10|sqrt|cbrt|yroot|logy"
        r"|asinh|acosh|atanh|asin|acos|atan"
        r"|PI|E"                                        # Constants
    )

    tokens = []

    for token in r.findall(expr):
        match token:
            case "+": tokens.append(Operator(add_, 2, 2, "left", "+"))
            case "-": tokens.append(Operator(sub_, 2, 2, "left", "-"))
            case "*": tokens.append(Operator(mul_, 2, 3, "left", "*"))
            case "/": tokens.append(Operator(div_, 2, 3, "left", "/"))
            case "^": tokens.append(Operator(pow_, 2, 4, "right", "^"))
            case "!": tokens.append(Operator(fact_, 1, 5, "left", "!"))
            case "%": tokens.append(Operator(perc_, 1, 5, "left", "%"))
            case "(": tokens.append(ParenLeft())
            case ")": tokens.append(ParenRight())
            case ",": tokens.append(Comma())

            case "sin": tokens.append(Function(sin_, 1, "sin"))
            case "cos": tokens.append(Function(cos_, 1, "cos"))
            case "tan": tokens.append(Function(tan_, 1, "tan"))
            case "sinh": tokens.append(Function(sinh_, 1, "sinh"))
            case "cosh": tokens.append(Function(cosh_, 1, "cosh"))
            case "tanh": tokens.append(Function(tanh_, 1, "tanh"))
            case "ln": tokens.append(Function(ln_, 1, "ln"))
            case "log10": tokens.append(Function(log10_, 1, "log10"))
            case "sqrt": tokens.append(Function(sqrt_, 1, "sqrt"))
            case "cbrt": tokens.append(Function(cbrt_, 1, "cbrt"))
            case "yroot": tokens.append(Function(yroot_, 2, "yroot"))
            case "logy": tokens.append(Function(logy_, 2, "logy"))
            case "asin": tokens.append(Function(asin_, 1, "asin"))
            case "acos": tokens.append(Function(acos_, 1, "acos"))
            case "atan": tokens.append(Function(atan_, 1, "atan"))
            case "asinh": tokens.append(Function(asinh_, 1, "asinh"))
            case "acosh": tokens.append(Function(acosh_, 1, "acosh"))
            case "atanh": tokens.append(Function(atanh_, 1, "atanh"))

            case "PI": tokens.append(Constant(math.pi, "PI"))
            case "E": tokens.append(Constant(math.e, "E"))

            case _: tokens.append(Number(token))

    return tokens

# https://en.wikipedia.org/wiki/Shunting_yard_algorithm
def shunting_yard(tokens):
    output_queue = []
    op_stack = []

    # while there are tokens to be read:
    #     read a token
    for token in tokens:
        # if the token is:
        match token:
            # - a number:
            #     put it into the output queue
            case Number():
                output_queue.append(token)

            case Constant():
                output_queue.append(token)

            # - a function:
            #     push it onto the operator stack 
            case Function():
                op_stack.append(token)

            # - an operator o1:
            #     while (
            #         there is an operator o2 at the top of the operator stack which is not a left parenthesis,
            #         and (o2 has greater precedence than o1 or (o1 and o2 have the same precedence
            #         and o1 is left-associative))
            #     ):
            #         pop o2 from the operator stack into the output queue
            #     push o1 onto the operator stack
            case Operator():
                # Hack: Special case for negative exponents
                if not (op_stack and op_stack[-1].repr == "^" and token.repr == "-"):
                    while op_stack and isinstance(op_stack[-1], Operator) and not (isinstance(op_stack[-1], ParenLeft)) and \
                        (op_stack[-1].prec > token.prec or (op_stack[-1].prec == token.prec and token.assoc == "left")):
                            output_queue.append(op_stack.pop())

                op_stack.append(token)

            # - a ",":
            #     while the operator at the top of the operator stack is not a left parenthesis:
            #         pop the operator from the operator stack into the output queue
            case Comma():
                while op_stack and not isinstance(op_stack[-1], ParenLeft):
                    output_queue.append(op_stack.pop())

            #  - a left parenthesis (i.e. "("):
            #     push it onto the operator stack
            case ParenLeft():
                op_stack.append(token)

            # - a right parenthesis (i.e. ")"):
            #     while the operator at the top of the operator stack is not a left parenthesis:
            #         {assert the operator stack is not empty}
            #         /* If the stack runs out without finding a left parenthesis, then there are mismatched parentheses. */
            #
            #         pop the operator from the operator stack into the output queue
            #
            #     {assert there is a left parenthesis at the top of the operator stack}
            #
            #     pop the left parenthesis from the operator stack and discard it
            #     if there is a function token at the top of the operator stack, then:
            #         pop the function from the operator stack into the output queue
            case ParenRight():
                while op_stack and not isinstance(op_stack[-1], ParenLeft):
                    output_queue.append(op_stack.pop())

                assert op_stack and isinstance(op_stack[-1], ParenLeft)
                op_stack.pop()

                if op_stack and isinstance(op_stack[-1], Function):
                    output_queue.append(op_stack.pop())

    # /* After the while loop, pop the remaining items from the operator stack into the output queue. */
    #
    # while there are tokens on the operator stack:
    #     /* If the operator token on the top of the stack is a parenthesis, then there are mismatched parentheses. */
    #     {assert the operator on top of the stack is not a (left) parenthesis}
    #
    #     pop the operator from the operator stack onto the output queue
    while op_stack:
        assert not isinstance(op_stack[-1], ParenLeft)
        output_queue.append(op_stack.pop())

    return output_queue

def eval_rpn(rpn):
    stack = []

    for i, token in enumerate(rpn):
        match token:
            case Number():
                stack.append(token.value)

            case Constant():
                stack.append(token.value)

            case Function():
                args = [stack.pop() for _ in range(token.argc)]
                stack.append(token.func(*args))

            case Operator():
                if token.repr == "-":
                    if (len(stack) == 1 or (i < len(rpn) - 1 and rpn[i + 1].repr == "-")) \
                        or (i < len(rpn) - 1 and rpn[i + 1].repr == "^"):
                            stack.append(-stack.pop())
                            continue

                if len(stack) < token.argc:
                    raise Exception(f"Not enough arguments in stack: {stack}")

                args = [stack.pop() for _ in range(token.argc)]
                stack.append(token.func(*args))

    return stack[0]
