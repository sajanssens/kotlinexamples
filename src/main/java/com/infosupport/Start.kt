package com.infosupport

import com.infosupport.a.lastChar
import java.io.IOException


fun main(args: Array<String>) {
    val p = Person("Bram", 27)
    val age = p.age
    println(age)


    args.forEachIndexed { index, element ->
        println("Argument $index is: $element")
    }

    yellAt(PersonNullableName(null))
    yellAt(PersonNullableName("Piet"))

    val d1 = MyDate(1)

    val d2 = MyDate(2)

    val d3 = MyDate(3)
    d3.value2 = 30

    println(if (d3 in d1..d2) "Ja" else "Nee")


//    foo()
    val lastChar = "abc".lastChar()

    val pair = 1 to "one" // i.e. "bind 1 to "one" "
    val first = pair.first
    val second = pair.second

    val trimIndent1 = """        
                        test    
                """.trimIndent()

    val trimIndent = "test".trimIndent()

    val bram = "Bram"
    val removeRange = bram.removeRange(3, 4)
//    println(bram)
//    println(removeRange)

    for ((i, g) in bram.withIndex()) {
        println(bram.removeRange(i, i + 1))
    }

    val secret = "ABCD"
    val count = secret.count { c -> c == 'A' }

}


fun yellAt(person: PersonNullableName) {
    println((person.name ?: "unknown").toUpperCase() + "!!!")
}

class MyDate(private val value: Int) : Comparable<MyDate> {
    var value2: Int = 0
        get() = field
        set(value) {
            field = value * 10
        }

    override fun compareTo(other: MyDate): Int = value - other.value

}

fun foo() {
    throw IOException()
}

@Throws(IOException::class)
fun bar() {
    throw IOException()
}

