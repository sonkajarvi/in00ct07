
# expr = number /
#        ( expr '!' ) /
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
#        "yroot" /      ; takes 2nd argument
#        "logy" /       ; takes 2nd argument
#        "arcsin" /
#        "arccos" /
#        "arctan" /
#        "arcsinh" /
#        "arccosh" /
#        "arctanh"

from tests import run

run()
