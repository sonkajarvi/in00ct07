package com.sonkajarvi.calculator.expression

import kotlin.math.pow

interface Operator : Token {
    val precedence: Int
    val associativity: Associativity
}

//
// Unary operators
//

interface UnaryOp : Operator {
    fun callback(operand: Double): Double
}

object Negate : UnaryOp {
    override val precedence = 4
    override val associativity = Associativity.RIGHT

    override fun toString(): String {
        return "-"
    }

    override fun callback(operand: Double): Double {
        return -operand
    }
}

object Factorial : UnaryOp {
    override val precedence = 4
    override val associativity = Associativity.LEFT

    override fun toString(): String {
        return "!"
    }

    override fun callback(operand: Double): Double {
        var retval = 1.0

        if (operand < 0.0)
            throw Exception("Non-positive in factorial")

        for (i in 1..operand.toInt())
            retval *= i.toDouble()

        return retval
    }
}

object Percentage : UnaryOp {
    override val precedence = 4
    override val associativity = Associativity.LEFT

    override fun toString(): String {
        return "%"
    }

    override fun callback(operand: Double): Double {
        return operand * 0.01
    }
}

//
// Binary operators
//

interface BinaryOp : Operator {
    fun callback(left: Double, right: Double): Double
}

object Add : BinaryOp {
    override val precedence = 2
    override val associativity = Associativity.LEFT

    override fun toString(): String {
        return "+"
    }

    override fun callback(left: Double, right: Double): Double {
        return left + right
    }
}

object Subtract : BinaryOp {
    override val precedence = 2
    override val associativity = Associativity.LEFT

    override fun toString(): String {
        return "-"
    }

    override fun callback(left: Double, right: Double): Double {
        return left - right
    }
}

object Multiply : BinaryOp {
    override val precedence = 3
    override val associativity = Associativity.LEFT

    override fun toString(): String {
        return "×"
    }

    override fun callback(left: Double, right: Double): Double {
        return left * right
    }
}

object Divide : BinaryOp {
    override val precedence = 3
    override val associativity = Associativity.LEFT

    override fun toString(): String {
        return "÷"
    }

    override fun callback(left: Double, right: Double): Double {
        return left / right
    }
}

object Exponentiation : BinaryOp {
    override val precedence = 4
    override val associativity = Associativity.RIGHT

    override fun toString(): String {
        return "^"
    }

    override fun callback(left: Double, right: Double): Double {
        return left.pow(right)
    }
}

object Remainder : BinaryOp {
    override val precedence = 3
    override val associativity = Associativity.LEFT

    override fun callback(left: Double, right: Double): Double {
        return left % right
    }
}
