package ca.derekellis.reroute.map.compose

// TODO: Replace this with Array<dynamic> if possible
typealias Expression = Array<out Any>

fun expression(vararg parts: Any): Expression = parts

fun at(index: Int, array: dynamic): Expression = arrayOf("at", index, array)

fun get(prop: String): Expression = arrayOf("get", prop)

fun get(prop: String, obj: dynamic): Expression = arrayOf("get", prop, obj)

fun has(prop: String): Expression = arrayOf("has", prop)

fun has(prop: String, obj: dynamic): Expression = arrayOf("has", prop, obj)

fun `in`(keyword: dynamic, input: dynamic): Expression = arrayOf("in", keyword, input)

fun not(value: dynamic): Expression = arrayOf("!", value)

fun neq(a: dynamic, b: dynamic): Expression = arrayOf("!=", a, b)

fun neq(a: dynamic, b: dynamic, collator: dynamic): Expression = arrayOf("!=", a, b, collator)

fun lt(a: dynamic, b: dynamic): Expression = arrayOf("<", a, b)

fun lt(a: dynamic, b: dynamic, collator: dynamic): Expression = arrayOf("<", a, b, collator)

fun lte(a: dynamic, b: dynamic): Expression = arrayOf("<=", a, b)

fun lte(a: dynamic, b: dynamic, collator: dynamic): Expression = arrayOf("<=", a, b, collator)

fun eq(a: dynamic, b: dynamic): Expression = arrayOf("==", a, b)

fun eq(a: dynamic, b: dynamic, collator: dynamic): Expression = arrayOf("==", a, b, collator)

fun gt(a: dynamic, b: dynamic): Expression = arrayOf(">", a, b)

fun gt(a: dynamic, b: dynamic, collator: dynamic): Expression = arrayOf(">", a, b, collator)

fun gte(a: dynamic, b: dynamic): Expression = arrayOf(">=", a, b)

fun gte(a: dynamic, b: dynamic, collator: dynamic): Expression = arrayOf(">=", a, b, collator)

fun all(a: dynamic, b: dynamic): Expression = arrayOf("all", a, b)

fun all(a: dynamic, b: dynamic, vararg rest: dynamic): Expression = arrayOf("all", a, b, *rest)

fun any(a: dynamic, b: dynamic): Expression = arrayOf("any", a, b)

fun any(a: dynamic, b: dynamic, vararg rest: dynamic): Expression = arrayOf("any", a, b, *rest)

fun interpolate(type: Expression, input: Expression, vararg stops: Pair<Number, dynamic>): Expression =
    arrayOf("interpolate", type, input, *stops.flatMap { (input, output) -> listOf(input, output) }.toTypedArray())
