package com.infosupport.chess

import org.junit.Test
import kotlin.test.assertTrue

class StandTest {
    @Test
    fun testPartijenTweeSpelers() {
        val tweeSpelers = tweeSpelers()
        val stand = Stand(tweeSpelers)
        assertTrue(stand.regels.size == 2)

        val wit = tweeSpelers[0].wit
        assertTrue(stand.regels[wit]?.gespeeld == 4)
        assertTrue(stand.regels[wit]?.aantalPunten == 5)

        val zwart = tweeSpelers[0].zwart
        assertTrue(stand.regels[zwart]?.gespeeld == 4)
        assertTrue(stand.regels[zwart]?.aantalPunten == 3)
        println(stand)
    }

    @Test
    fun testPartijenDrieSpelers() {
        val drieSpelers = drieSpelers()
        val stand = Stand(drieSpelers)

        assertTrue(stand.regels.size == 3)
        stand.regels.keys.forEach { team -> assertTrue(stand[team]?.gespeeld == 4) } // every team has played 4 games
        assertTrue(stand.regels.values.zipWithNext { a, b -> a.aantalPunten >= b.aantalPunten }.all { it }) // regels is sorted descending

        assertTrue(stand[az]?.aantalPunten == 7)
        assertTrue(stand[ajax]?.aantalPunten == 3)
        assertTrue(stand[psv]?.aantalPunten == 2)

        println(stand)
    }
}