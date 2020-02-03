package com.infosupport.fp

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

// See: https://medium.com/swlh/a-taste-of-functional-programming-in-kotlin-3b163b5c8101

fun filterOddsImperative(ns: Collection<Int>): Collection<Int> {
    val output = mutableListOf<Int>()
    for (n in ns) {
        if (n % 2 == 0) output.add(n)
    }
    return output
}


fun filterOdds(ns: Collection<Int>): Collection<Int> =
        if (ns.isEmpty()) listOf()
        else {
            val head = ns.take(1)[0]
            if (head % 2 == 0)
                listOf(head) + filterOdds(ns.drop(1))
            else
                filterOdds(ns.drop(1))
        }

fun <T> filterRecursive(ts: Collection<T>, p: (t: T) -> Boolean): Collection<T> =
        if (ts.isEmpty()) listOf()
        else {
            val head = ts.take(1)[0]
            if (p(head)) {
                val filterRecursive = filterRecursive(ts.drop(1), p)
                listOf(head) + filterRecursive // less-than-efficient recursive call: keeps putting stuff on the stack before calling itself
                // with every call to itself, filterRecursive leaves some value on the stack to be used later
            } else
                filterRecursive(ts.drop(1), p)
        } // For the compiler to be able to optimize recursive calls, recursive functions must be written in a way such that after a function calls itself, nothing is kept on the call stack. In other words, the function must call itself as that last thing it does

fun <T> filterTCO(ts: Collection<T>, p: (t: T) -> Boolean): Collection<T> {
    tailrec fun inner(ts: Collection<T>, p: (t: T) -> Boolean, listOf: List<T>): Collection<T> = // 1) additional parameter which is the accumulator, listOf
            if (ts.isEmpty()) listOf // 2) return accumulator
            else {
                val head = ts.take(1)[0]
                // requires quite a bit of low-level work.
                if (p(head))
                    inner(ts.drop(1), p, listOf + head) // 3) only calls itself as the last thing it does = tail call recursive style
                else
                    inner(ts.drop(1), p, listOf)
            }
    return inner(ts, p, listOf()) // pass empty list as starting accumulator
}

tailrec fun <A, B> fold(init: B, col: Collection<A>, f: (A, B) -> B): B =
        if (col.isEmpty()) init
        else {
            val head = col.take(1)
            val tail = col.drop(1)
            val initUpdatedWithHead = f(head[0], init) // f updates init by combining init with the first element in col to a "new init"
            fold(initUpdatedWithHead, tail, f) // next folding
        }


fun <T> filter(ts: Collection<T>, p: (T) -> Boolean): Collection<T> =
        fold(listOf(), ts, { t, lst -> if (p(t)) lst + t else lst })

fun <I, O> map(input: Collection<I>, f: (I) -> O): Collection<O> =
        fold(listOf(), input, { i, acc -> acc + f(i) })

fun <T> length(ts: Collection<T>): Int =
        fold(0, ts, { _, tempLength -> tempLength + 1 }) // 1 is added to the tempLength, no matter the value of the element. This is why we use the underscore

fun <T> reverse(ts: Collection<T>): Collection<T> =
        fold(listOf(), ts, { t, acc -> listOf(t) + acc })


fun nGrams(n: Int, str: String): Collection<String> {
    tailrec fun inner(n: Int, str: String, acc: List<String>): Collection<String> =
            if (str.length < n) acc else
                inner(n, str.drop(1), acc + str.take(n))
    return inner(n, str, listOf())
}

fun allGrams(str: String): Collection<String> =
        fold(listOf(), (1..str.length).toList()) { n, acc -> acc + nGrams(n, str) }

fun <A, B> pairWithValue(col: Collection<A>, value: B): Collection<Pair<A, B>> =
        col.map { Pair(it, value) } // == { el -> Pair(el, value) }


fun <K, V> updateIndex(currentIndex: Map<K, Collection<V>>, someNewEntries: Collection<Pair<K, V>>): Map<K, Collection<V>> =
        fold(currentIndex, someNewEntries) { aNewEntry, newIndex ->
            val searchKey = aNewEntry.first
            val currentValuesAtIndex = currentIndex[searchKey]
            when (currentValuesAtIndex) {
                null -> newIndex + Pair(searchKey, listOf(aNewEntry.second)) // no entry for this key in the index yet; create a new one
                else -> newIndex + Pair(searchKey, currentValuesAtIndex + aNewEntry.second)// entry for this key in the index already exists; add the new one
            }
        }

fun <T> traverse(root: Path, init: T, f: (Path, T) -> T): T =
        fold(init, Files.list(root).toList(),
                { path, acc -> if (!Files.isDirectory(path)) f(path, acc) else if (!Files.isSymbolicLink(path)) traverse(path, acc, f) else acc })

fun normalize(s: String, stopList: Collection<Char>): String =
        fold("", s.toList(), { ch, acc -> if (!stopList.contains(ch)) acc + ch.toLowerCase() else acc })

fun computeIndex(path: Path): Map<String, Collection<Path>> =
        traverse(path, mapOf(), { p, acc ->
            updateIndex(acc, pairWithValue(allGrams(normalize(p.fileName.toString(), listOf('_', '-', '.', '$', ' '))), p.toAbsolutePath()))
        })

fun main() {
    // Enable runtime assertions on the JVM using the -ea JVM option!!
    assert(filterOddsImperative(listOf(1, 2, 3, 4, 5, 6)) == listOf(2, 4, 6))
    assert(filterOddsImperative(listOf(2, 6, 8, 0)) == listOf(2, 6, 8, 0))

    assert(filterOdds(listOf(1, 2, 3, 4, 5, 6)) == listOf(2, 4, 6))
    assert(filterOdds(listOf(2, 6, 8, 0)) == listOf(2, 6, 8, 0))

    val even: (Int) -> Boolean = { i -> i % 2 == 0 }
    assert(filterRecursive(listOf(1, 2, 3, 4, 5, 6), even) == listOf(2, 4, 6))
    assert(filterRecursive(listOf(2, 6, 8, 0)) { it % 2 == 0 } == listOf(2, 6, 8, 0))
    assert(filterRecursive(listOf("this", "is", "a", "Good", "Idea")) { it.length > 1 } == listOf("this", "is", "Good", "Idea"))

    assert(filterTCO(listOf(1, 2, 3, 4, 5, 6), even) == listOf(2, 4, 6))
    assert(filterTCO(listOf(2, 6, 8, 0)) { it % 2 == 0 } == listOf(2, 6, 8, 0))
    assert(filterTCO(listOf("this", "is", "a", "Good", "Idea")) { it.length > 1 } == listOf("this", "is", "Good", "Idea"))

    assert(filter(listOf(1, 2, 3, 4, 5, 6), even) == listOf(2, 4, 6))
    assert(filter(listOf(2, 6, 8, 0)) { it % 2 == 0 } == listOf(2, 6, 8, 0))
    assert(filter(listOf("this", "is", "a", "Good", "Idea")) { it.length > 1 } == listOf("this", "is", "Good", "Idea"))

    assert(length(listOf(1, 2, 3, 4, 5, 6)) == 6)
    assert(reverse(listOf(1, 2, 3, 4, 5, 6)) == listOf(6, 5, 4, 3, 2, 1))

    assert(map(listOf(1, 2, 3, 4, 5, 6)) { it * 2 } == listOf(2, 4, 6, 8, 10, 12))

    assert(nGrams(1, "from") == listOf("f", "r", "o", "m"))
    assert(nGrams(2, "from") == listOf("fr", "ro", "om"))
    assert(nGrams(3, "from") == listOf("fro", "rom"))
    assert(nGrams(4, "from") == listOf("from"))
    assert(nGrams(5, "from") == listOf<String>())

    assert(allGrams("from") == listOf("f", "r", "o", "m", "fr", "ro", "om", "fro", "rom", "from"))

    println(pairWithValue(allGrams("from"), "from"))

    val index = computeIndex(Paths.get("C:\\temp\\demo\\src"))
    val collection = index["kt"]
    println(collection)
    println(index)

}

