package com.infosupport.a

//fun lastChar(s: String) = s[s.length - 1]

// import this function somewhere to use it
fun String.lastChar() = get(length - 1)
//  ^^^^^^ receiver type
//                      this.get(this.length - 1) // this can be omitted

fun String.repeat(n: Int): String {
    val sb = StringBuilder(n * length)
    for (i in 1..n) {
        sb.append(this)
    }
    return sb.toString()
}