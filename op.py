import math

def add_(x, y):
    return x + y

def sub_(x, y):
    return y - x

def mul_(x, y):
    return x * y

def div_(x, y):
    return y / x

def pow_(x, y):
    return y ** x

def fact_(x):
    return math.gamma(x + 1)

def perc_(x):
    return x / 100

def sin_(x):
    return math.sin(x)

def cos_(x):
    return math.cos(x)

def tan_(x):
    return math.tan(x)

def sinh_(x):
    return math.sinh(x)

def cosh_(x):
    return math.cosh(x)

def tanh_(x):
    return math.tanh(x)

def ln_(x):
    return math.log(x)

def log10_(x):
    return math.log10(x)

def sqrt_(x):
    return math.sqrt(x)

def cbrt_(x):
    return x ** (1 / 3)

def yroot_(x, y):
    return y ** (1 / x)

def logy_(x, y):
    return math.log(y, x)

def asin_(x):
    return math.asin(x)

def acos_(x):
    return math.acos(x)

def atan_(x):
    return math.atan(x)

def asinh_(x):
    return math.asinh(x)

def acosh_(x):
    return math.acosh(x)

def atanh_(x):
    return math.atanh(x)
