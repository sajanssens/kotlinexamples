package com.infosupport.chess


fun tweeSpelers(): List<Partij> = listOf(
        Partij("Bram", "Marco", Uitslag.WIT),
        Partij("Bram", "Marco", Uitslag.ZWART),
        Partij("Bram", "Marco", Uitslag.REMISE),
        Partij("Bram", "Marco", Uitslag.WIT)
        // Stand:
        // Naam     #   w   r   v   p
        // Bram     4   2   1   1   5
        // Marco    4   1   1   2   3
)

val ajax = "AJAX"
val psv = "PSV"
val az = "AZ"

fun drieSpelers(): List<Partij> = listOf(
        Partij(ajax, psv, Uitslag.WIT),
        Partij(psv, az, Uitslag.ZWART),
        Partij(psv, ajax, Uitslag.REMISE),
        Partij(az, psv, Uitslag.REMISE),
        Partij(ajax, az, Uitslag.ZWART),
        Partij(az, ajax, Uitslag.WIT)
        // Stand:
        // Naam     #   w   r   v   p
        // AJAX     4   1   1   2   3
        // PSV      4   0   2   2   2
        // AZ       4   3   1   0   7
)