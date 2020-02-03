package com.infosupport.chess

class Stand(val partijen: List<Partij>) {

    private var mutableRegels: MutableMap<String, StandRegel> = mutableMapOf()

    var regels: Map<String, StandRegel> = mapOf()
        get() = field
        private set(value) {
            field = value
        }

    init {
        maak()
    }

    operator fun get(index: String): StandRegel? = mutableRegels[index] // get indexer!

    private fun maak() {
        for (partij in partijen) {
            updateRegel(partij, true)
            updateRegel(partij, false)
        }
        regels = mutableRegels.toList().sortedByDescending { pair -> pair.second.aantalPunten }.toMap()
    }

    private fun updateRegel(partij: Partij, wit: Boolean) {
        val speler = if (wit) partij.wit else partij.zwart

        val standRegel = mutableRegels[speler] ?: StandRegel(speler, 0, 0, 0, 0)
        val gespeeld = standRegel.gespeeld

        val win = standRegel.win
        val verlies = standRegel.verlies
        val remise = standRegel.remise

        val newWin = if (partij.uitslag == if (wit) Uitslag.WIT else Uitslag.ZWART) win + 1 else win
        val newVerlies = if (partij.uitslag == if (wit) Uitslag.ZWART else Uitslag.WIT) verlies + 1 else verlies
        val newRemise = if (partij.uitslag == Uitslag.REMISE) remise + 1 else remise

        val standRegelNew = StandRegel(speler, gespeeld + 1, newWin, newRemise, newVerlies)

        mutableRegels[speler] = standRegelNew
    }

    override fun toString(): String {
        return regels.toString()
    }


}

data class StandRegel(val naam: String, var gespeeld: Int, val win: Int, val remise: Int, val verlies: Int) {
    val aantalPunten: Int = win * 2 + remise
    override fun toString(): String {
        return "StandRegel(naam='$naam', gespeeld=$gespeeld, win=$win, remise=$remise, verlies=$verlies, aantalPunten=$aantalPunten)"
    }

}
