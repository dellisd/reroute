package ca.derekellis.reroute.utils

fun <T> jsObject(block: T.() -> Unit): T {
  val o = js("{}").unsafeCast<T>()
  block(o)
  return o
}
