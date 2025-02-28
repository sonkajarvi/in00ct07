package com.sonkajarvi.calculator.expression

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.max

fun <T> MutableList<T>.add(vararg args: T) {
    for (arg in args)
        this.add(arg)
}

fun <T> MutableList<T>.isJust(e: T): Boolean {
    return this.size == 1 && this[0] == e
}

fun <T> MutableList<T>.pop(): T? {
    if (this.size > 0)
        return this.removeAt(this.size - 1)

    return null
}

enum class Associativity {
    LEFT,
    RIGHT
}

interface Token

class Number(value: String = "") : Token {
    val digits = mutableStateListOf(*value.toList().toTypedArray())

    override fun toString(): String {
        return this.digits.joinToString("")
    }

    fun get(): Double {
        return this.digits.joinToString("").toDouble()
    }
}

object LeftParen : Token {
    override fun toString(): String {
        return "("
    }
}

object RightParen : Token {
    override fun toString(): String {
        return ")"
    }
}

object Comma : Token {
    override fun toString(): String {
        return ","
    }
}

object ExpressionHandler {
    private val tokens = mutableStateListOf<Token>()
    var latest by mutableStateOf("")

    private fun last(offset: Int = 0): Token? {
        if (this.tokens.size - offset <= 0)
            return null

        return this.tokens[tokens.size - offset - 1]
    }

    override fun toString(): String {
        return this.tokens.joinToString("")
    }

    private fun requireNumber() {
        when (this.last()) {
            null -> tokens.add(Number())
            is Number -> return
            is UnaryOp -> tokens.add(Number())
            is BinaryOp -> tokens.add(Number())
            is LeftParen -> tokens.add(Number())
            is RightParen -> tokens.add(Multiply, Number())
            is Function -> tokens.add(Multiply, Number())
        }

        (this.last() as Number).digits.add('0')
    }

    private fun insertHelper(c: Char) {
        this.requireNumber()
        val digits = (this.last() as Number).digits

        if (digits.isJust('0')) {
            digits[0] = c
            return
        }

        digits.add(c)

        Log.d("expr", "added $c")
    }

    fun insert1(e: (() -> Unit)?) { insertHelper('1') }
    fun insert2(e: (() -> Unit)?) { insertHelper('2') }
    fun insert3(e: (() -> Unit)?) { insertHelper('3') }
    fun insert4(e: (() -> Unit)?) { insertHelper('4') }
    fun insert5(e: (() -> Unit)?) { insertHelper('5') }
    fun insert6(e: (() -> Unit)?) { insertHelper('6') }
    fun insert7(e: (() -> Unit)?) { insertHelper('7') }
    fun insert8(e: (() -> Unit)?) { insertHelper('8') }
    fun insert9(e: (() -> Unit)?) { insertHelper('9') }

    fun insert0(e: (() -> Unit)?) {
        this.requireNumber()
        val digits = (this.last() as Number).digits

        if (digits.isJust('0'))
            return

        digits.add('0')
    }

    fun comma(e: (() -> Unit)?) {
        this.requireNumber()
        val digits = (this.last() as Number).digits

        digits.add('.')
    }

    private fun opHelper(op: BinaryOp) {
        if (this.last() is Negate)
            this.tokens.pop()
        if (this.last() is BinaryOp)
            this.tokens.pop()

        if (this.last() !is RightParen && this.last() !is UnaryOp)
            this.requireNumber()

        this.tokens.add(op)
    }

    fun add(e: (() -> Unit)?) { opHelper(Add) }
    fun mul(e: (() -> Unit)?) { opHelper(Multiply) }
    fun div(e: (() -> Unit)?) { opHelper(Divide) }

    fun sub(e: (() -> Unit)?) {
        if (this.tokens.isEmpty()
            || (this.last() is Number && (this.last() as Number).digits.isJust('0'))) {
            this.tokens.pop()
            this.tokens.add(Negate)
            return
        }

        if (this.last() is Multiply || this.last() is Divide) {
            this.tokens.add(Negate)
            return
        }

        if (this.last() is Add)
            this.tokens.pop()

        this.tokens.add(Subtract)
    }

    // expr = number
    //        [ unary-op ] expr [ unary-op ]
    //        [ '(' ] expr [ ')' ]
    //        '(' expr binary-op expr ')'
    private fun lastExpression(): Int {
        var i = this.tokens.size - 1

        if (this.tokens.isEmpty())
            return 0

        while (this.tokens[i] is UnaryOp)
            i--

        if (this.tokens[i] is RightParen) {
            var leftParens = 0
            var rightParens = 1

            while (leftParens != rightParens) {
                i--

                when (this.tokens[i]) {
                    is LeftParen -> leftParens++
                    is RightParen -> rightParens++
                }
            }

            return i
        }
        else if (i > 0 && this.tokens[i] is Number)
            i--

        if (this.tokens[i] is UnaryOp)
            i--
        else if (this.tokens[i] is BinaryOp)
            i++

        return max(0, i)
    }

    fun negate(e: (() -> Unit)?) {
        if (this.tokens.isEmpty() || this.last() is BinaryOp)
            return

        val i = this.lastExpression()

        if (this.tokens[i] is Negate) {
            this.tokens.removeAt(i)
            return
        }
        else if (i > 0 && this.tokens[i - 1] is Subtract) {
            this.tokens[i - 1] = Add
            return
        }

        if (i + 1 < this.tokens.size - 1 && this.tokens[i + 1] is Negate) {
            this.tokens.removeAt(i)
            this.tokens.removeAt(i)
            this.tokens.pop()
            return
        }

        this.tokens.add(i, Negate)
        this.tokens.add(i, LeftParen)
        this.tokens.add(RightParen)
    }

    fun percentage(e: (() -> Unit)?) {
        if (this.last() is Percentage) {
            this.tokens.add(this.lastExpression(), LeftParen)
            this.tokens.add(RightParen)
            this.tokens.add(Percentage)
            return
        }

        if (this.last() is RightParen) {
            this.tokens.add(Percentage)
            return
        }

        if (this.last() is BinaryOp)
            this.tokens.pop()

        this.requireNumber()
        this.tokens.add(Percentage)
    }

    fun clear(e: (() -> Unit)?) {
        this.tokens.clear()
        this.latest = ""
    }

    fun remove(e: (() -> Unit)?) {
        if (this.last() is Number) {
            val digits = (this.last() as Number).digits
            digits.pop()

            if (!digits.isEmpty())
                return
        }

        this.tokens.pop()
    }

    fun eval(e: (() -> Unit)?) {
        val value = eval(this.tokens)
        latest = this.toString()
        this.tokens.clear()
        this.tokens.add(Number(value.toString().removeSuffix(".0")))
        e?.invoke()
    }
}
