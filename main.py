
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

class Number:
    def __init__(self, value):
        self.value = float(value)

    def __repr__(self):
        return f"{self.value:g}"

class Op:
    def __init__(self, func, argc, precedence, repr):
        self.func = func
        self.argc = argc
        self.precedence = precedence
        self.repr = repr

    def __repr__(self):
        return self.repr

class ParenLeft(Op):
    def __init__(self):
        self.repr = "("

class ParenRight(Op):
    def __init__(self):
        self.repr = ")"

def tokenize(expr):
    r = re.compile(
        r"[\d]+(?:\.\d+)?(?:[eE][-+]?\d+)?"     # Numbers
        r"|[+\-*/^!%\(\)]"                      # Operators
        r"|sin|cos|tan|sinh|cosh|tanh"          # Functions
        r"|ln|log10|sqrt|cbrt|yroot|logy"
        r"|asin|acos|atan|asinh|acosh|atanh"
    )

    tokens = []
    for token in r.findall(expr):
        match token:
            case "+": tokens.append(Op(add_, 2, 2, "+"))
            case "-": tokens.append(Op(sub_, 2, 2, "-"))
            case "*": tokens.append(Op(mul_, 2, 3, "*"))
            case "/": tokens.append(Op(div_, 2, 3, "/"))
            case "^": tokens.append(Op(pow_, 2, 4, "^"))
            case "!": tokens.append(Op(fact_, 1, 5, "!"))
            case "%": tokens.append(Op(perc_, 1, 5, "%"))
            case "(": tokens.append(ParenLeft())
            case ")": tokens.append(ParenRight())

            case "sin": tokens.append(Op(sin_, 1, 6, "sin"))
            case "cos": tokens.append(Op(cos_, 1, 6, "cos"))
            case "tan": tokens.append(Op(tan_, 1, 6, "tan"))
            case "sinh": tokens.append(Op(sinh_, 1, 6, "sinh"))
            case "cosh": tokens.append(Op(cosh_, 1, 6, "cosh"))
            case "tanh": tokens.append(Op(tanh_, 1, 6, "tanh"))
            case "ln": tokens.append(Op(ln_, 1, 6, "ln"))
            case "log10": tokens.append(Op(log10_, 1, 6, "log10"))
            case "sqrt": tokens.append(Op(sqrt_, 1, 6, "sqrt"))
            case "cbrt": tokens.append(Op(cbrt_, 1, 6, "cbrt"))
            case "yroot": tokens.append(Op(yroot_, 2, 6, "yroot"))
            case "logy": tokens.append(Op(logy_, 2, 6, "logy"))
            case "asin": tokens.append(Op(asin_, 1, 6, "asin"))
            case "acos": tokens.append(Op(acos_, 1, 6, "acos"))
            case "atan": tokens.append(Op(atan_, 1, 6, "atan"))
            case "asinh": tokens.append(Op(asinh_, 1, 6, "asinh"))
            case "acosh": tokens.append(Op(acosh_, 1, 6, "acosh"))
            case "atanh": tokens.append(Op(atanh_, 1, 6, "atanh"))
            case "max": tokens.append(Op(max_, 2, 6, "max"))

            case _: tokens.append(Number(token))

    return tokens

def test(expr):
    print(expr)

    tokens = tokenize(expr)
    print(tokens)

test("3+4*2/(1-5)^2^3")
# test("1e3")
# test("(1+2)*3")
# test("0.4+1.6")
# test("sin(0.5)")
# test("yroot(25,2)")
