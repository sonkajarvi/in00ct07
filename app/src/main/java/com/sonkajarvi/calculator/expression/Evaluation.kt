package com.sonkajarvi.calculator.expression

fun shuntingYard(input: List<Token>): List<Token> {
    val outputQueue = mutableListOf<Token>()
    val opStack = mutableListOf<Token>()

    for (token in input) {
        when (token) {
            is Number -> outputQueue.add(token)

            is Negate -> {
                opStack.add(token)
            }

            is Factorial -> {
                while (opStack.isNotEmpty() && opStack.last() is LeftParen)
                    opStack.pop()

                opStack.add(token)
            }

            is BinaryOp -> {
                while (opStack.isNotEmpty() && opStack.last() is BinaryOp
                    && ((opStack.last() as BinaryOp).precedence > token.precedence
                            || ((opStack.last() as BinaryOp).precedence == token.precedence && token.associativity == Associativity.LEFT)))
                    outputQueue.add(opStack.pop()!!)

                opStack.add(token)
            }

            is Comma -> {
                while (opStack.isNotEmpty() && opStack.last() is LeftParen)
                    outputQueue.add(opStack.pop()!!)
            }

            is LeftParen -> opStack.add(token)

            is RightParen -> {
                while (opStack.isNotEmpty() && opStack.last() is LeftParen)
                    outputQueue.add(opStack.pop()!!)

                //opStack.pop()

                //if (opStack.isNotEmpty() && opStack.last() is Function)
                //    outputQueue.add(opStack.pop()!!)
            }
        }

        println("Output queue: $outputQueue")
        println("Op stack: $opStack")
    }

    while (opStack.isNotEmpty())
        outputQueue.add(opStack.pop()!!)

    return outputQueue
}

fun eval(input: List<Token>): Double {
    val rpn = shuntingYard(input)
    val stack = mutableListOf<Double>()

    for (token in rpn) {
        when (token) {
            is Number -> stack.add(token.get())

            is UnaryOp -> stack.add(token.callback(stack.pop()!!))

            is BinaryOp -> {
                val right = stack.pop()!!
                val left = stack.pop()!!

                stack.add(token.callback(left, right))
            }
        }
    }

    return stack[0]
}
