package com.insbaixcamp.word.read.games.learn.online.wordgames.firebase.data

//Las misiones se añadirian en otra parte, i en este registro se haria
//referencia a ellas con el identificador de la mision convertido en un boolean
data class WordleProfile(val rachaVictorias: Int? = 0, val dailyWord: Boolean? = false, val currentProgress: Int? = 0, val maxScore: Int? = 0)
