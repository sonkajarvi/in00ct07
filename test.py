import numpy as np
from calc import *

def test(expr, expected):
    result = float("nan")

    try:
        tokens = tokenize(expr)
        rpn = shunting_yard(tokens)
        result = eval_rpn(rpn)

        if not np.isclose(result, expected):
            raise Exception("Wrong result")

    except Exception as e:
        print(f"\033[1;91m{expr} != {result}, expected {expected},")
        print(f"due to: {e}\033[0m")

        print("> Expr:  ", expr)
        print("> Tokens:", *tokens)
        print("> RPN:   ", *rpn)
        print("> Result:", result)

        exit()

    print(f"{expr} = {result:g}\033[90m, expected {expected:g}\033[0m")

# Operators
test("3+4", 7)
test("3-4", -1)
test("3*4", 12)
test("3/4", 0.75)
test("3^4", 81)
test("3+4*2/(1-5)^2^3", 3.0001220703125)

test("5!", 120)
test("5%", 0.05)

test("-5", -5)
test("5-5", 0)
test("5-(-5)", 10)
test("(-5)-5", -10)
test("(-5)-(-5)", 0)
test("-5--5", 0)
test("-5%", -0.05)
test("-(5)", -5)
test("-PI", -np.pi)
test("5-(-PI)", 5-(-np.pi))
test("-sin(0.5)", -np.sin(0.5))
test("5-(-sin(0.5))", 5-(-np.sin(0.5)))
test("5^-5", 5**-5)
test("-sin(0.5)^-PI", -np.sin(0.5)**-np.pi)
test("5^-(5)", 5**-(5))

# Functions
test("sin(0.5)", np.sin(0.5))
test("cos(0.5)", np.cos(0.5))
test("tan(0.5)", np.tan(0.5))
test("sinh(0.5)", np.sinh(0.5))
test("cosh(0.5)", np.cosh(0.5))
test("tanh(0.5)", np.tanh(0.5))

test("ln(0.5)", np.log(0.5))
test("log10(0.5)", np.log10(0.5))
test("sqrt(0.5)", np.sqrt(0.5))
test("cbrt(0.5)", np.cbrt(0.5))
test("yroot(0.5, 5)", np.power(0.5, 1 / 5))
test("logy(0.5, 5)", np.log(0.5) / np.log(5))

test("asin(0.5)", np.arcsin(0.5))
test("acos(0.5)", np.arccos(0.5))
test("atan(0.5)", np.arctan(0.5))
test("asinh(1.5)", np.arcsinh(1.5))
test("acosh(1.5)", np.arccosh(1.5))
test("atanh(0.5)", np.arctanh(0.5))

# Constants
test("PI", np.pi)
test("E", np.e)
test("PI+E", np.pi + np.e)
