package com.sonkajarvi.calculator

import com.sonkajarvi.calculator.expression.Add
import com.sonkajarvi.calculator.expression.Divide
import com.sonkajarvi.calculator.expression.Exponentiation
import com.sonkajarvi.calculator.expression.Factorial
import com.sonkajarvi.calculator.expression.Multiply
import com.sonkajarvi.calculator.expression.Number
import com.sonkajarvi.calculator.expression.Subtract
import com.sonkajarvi.calculator.expression.Token
import com.sonkajarvi.calculator.expression.eval
import com.sonkajarvi.calculator.expression.Negate
import com.sonkajarvi.calculator.expression.Percentage
import com.sonkajarvi.calculator.expression.LeftParen
import com.sonkajarvi.calculator.expression.RightParen
import com.sonkajarvi.calculator.expression.shuntingYard
import org.junit.Test

class EvaluationTest {
    private fun assert(expected: Double, list: List<Token>) {
        val evald = eval(list)

        //println(list)
        //println(shuntingYard(list))
        //println(">>> $expected = $evald")
        assert(expected == evald)
    }

    @Test
    fun binaryOps() {
        assert(7.0, listOf(Number("3"), Add, Number("4")))
        assert(-1.0, listOf(Number("3"), Subtract, Number("4")))
        assert(12.0, listOf(Number("3"), Multiply, Number("4")))
        assert(0.75, listOf(Number("3"), Divide, Number("4")))
        assert(27.0, listOf(Number("81"), Divide, Number("3")))

        assert(81.0, listOf(Number("3"), Exponentiation, Number("4")))
        assert(1024.0, listOf(Number("2"), Exponentiation, LeftParen, Number("5"), Add, Number("5"), RightParen))
    }

    @Test
    fun unaryOps() {
        assert(6.0, listOf(Number("3"), Factorial))
        assert(9.0, listOf(Number("3"), Add, Number("3"), Factorial))
        assert(720.0, listOf(LeftParen, Number("3"), Add, Number("3"), RightParen, Factorial))

        assert(0.05, listOf(Number("5"), Percentage))

        assert(-5.0, listOf(Negate, Number("5")))
        assert(-2.0, listOf(Negate, Number("5"), Subtract, Negate, Number("3")))
        assert(-25.0, listOf(Number("5"), Multiply, LeftParen, Negate, Number("5"), RightParen))
    }
}
