package com.insbaixcamp.game.ui.word_search

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.insbaixcamp.game.R
import kotlinx.android.synthetic.main.fragment_word_search.*
import java.util.*
import kotlin.concurrent.schedule
import com.insbaixcamp.game.utilities.Diccionario
import java.lang.Math.abs
import java.text.Normalizer


class WordSearchFragment : Fragment() , View.OnTouchListener , OnClickListener{
    // Inicializar el temporizador
    var totalTimeSpent: Long = 0
    private var startTime: Long = 0

    // Configuración de las variables necesarias
    private var xInitial = -1f
    private var yInitial = -1f
    private var xDiff = -1f
    private var yDiff = -1f
    private var prevXDiff = -1f
    private var prevYDiff = -1f
    lateinit var root: View
    var restart_btn: Button? = null
    var words = getWords()

    // Si la usuario está deslizando vertical u horizontalmente
    enum class SwipeState { Undefined, Vertical, Horizontal }

    private var swipeState = SwipeState.Undefined

    private var cellWidth = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_word_search, container, false)
        restart_btn = root.findViewById(R.id.restart_btn) as Button
        restart_btn!!.setOnClickListener(this);
        val tvWord0: TextView = root.findViewById(R.id.tvWord0)
        val tvWord1: TextView = root.findViewById(R.id.tvWord1)
        val tvWord2: TextView = root.findViewById(R.id.tvWord2)
        val tvWord3: TextView = root.findViewById(R.id.tvWord3)
        val tvWord4: TextView = root.findViewById(R.id.tvWord4)
        val tvWord5: TextView = root.findViewById(R.id.tvWord5)

        tvWord0.setText(words[0])
        tvWord1.setText(words[1])
        tvWord2.setText(words[2])
        tvWord3.setText(words[3])
        tvWord4.setText(words[4])
        tvWord5.setText(words[5])

        for(i in 0..5){
            words[i] = remove1(words[i].toUpperCase())
        }

       return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //El temporizador comienza
        startTime = System.currentTimeMillis()
        // Ocultar el mensaje de felicitación
        congrats_layout.visibility = View.GONE


        // Establecer el ancho de cada celda de la cuadrícula (solo para una cuadrícula de 10*10)
        cellWidth = resources.displayMetrics.widthPixels / 10

        for (i in 0 until numWords) {
            wordArray[i] = Word(words[i])
        }

        val childCount = words_grid.childCount
        for (i in 0 until childCount) {
            val linearLayout: LinearLayout = words_grid.getChildAt(i) as LinearLayout
            for (t in 0 until linearLayout.childCount) {
                linearLayout.getChildAt(t).setOnTouchListener(this)
            }
        }

        // Ajustar la altura de la grilla
        val params = words_grid.layoutParams as ConstraintLayout.LayoutParams
        params.height = resources.displayMetrics.widthPixels
        words_grid.layoutParams = params

        // Rellena el resto de la cuadrícula con letras aleatorias
        generateRandomLetters()

    }

    // Cada vez que el usuario desliza la palabra, se usará un color aleatorio para el trazo
    private var selectedColour = R.drawable.selected_cell_background

    override fun onTouch(v: View, event: MotionEvent): Boolean{

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                // El usuario comenzó a seleccionar celdas
                // Ahora seleccionamos un color aleatorio para colorear las celdas que tocaron
                val random = Random()

                v.background = ContextCompat.getDrawable(requireContext(), R.drawable.selected_cell_background)
                selectedColour = R.drawable.selected_cell_background

                xInitial = event.x
                yInitial = event.y

            }

            MotionEvent.ACTION_MOVE -> {
                // El usuariosigue seleccionando celdas
                if (xInitial != -1f && yInitial != -1f) {

                    val tag = v.tag.toString()
                    val tagInt = tag.toInt()

                    xDiff = xInitial - event.x
                    yDiff = yInitial - event.y


                    if (swipeState == SwipeState.Undefined || swipeState == SwipeState.Horizontal) {
                        when {
                            xDiff > cellWidth -> {
                                // moviéndonos a la izquierda
                                if (prevXDiff == -1f || prevXDiff != -1f && prevXDiff < xDiff) {
                                    selectSingleCell((tagInt - (xDiff / cellWidth).toInt()).toString())
                                    Log.i("xInitial",xInitial.toString())
                                    Log.i("tagInt",(tagInt - (xDiff / cellWidth).toInt()).toString())
                                    swipeState = SwipeState.Horizontal
                                } else if (prevXDiff != -1f && prevXDiff > xDiff) {
                                    unselectSingleCell((tagInt - (prevXDiff / cellWidth).toInt()).toString())
                                }
                            }
                            (-1) * xDiff > cellWidth -> {
                                // moviéndonos a la derecha
                                if (prevXDiff == -1f || prevXDiff != -1f && prevXDiff > xDiff) {
                                    selectSingleCell((tagInt + -1 * (xDiff / cellWidth).toInt()).toString())
                                    swipeState = SwipeState.Horizontal
                                } else if (prevXDiff != -1f && prevXDiff < xDiff) {
                                    unselectSingleCell((tagInt - (prevXDiff / cellWidth).toInt()).toString())
                                }
                            }
                        }
                    }

                    if (swipeState == SwipeState.Undefined || swipeState == SwipeState.Vertical) {
                        when {
                            yDiff > cellWidth -> {
                                // subiendo
                                if (prevYDiff == -1f || prevYDiff != -1f && prevYDiff < yDiff) {
                                    selectSingleCell((tagInt - 10 * (yDiff / cellWidth).toInt()).toString())
                                    swipeState = SwipeState.Vertical
                                } else if (prevYDiff != -1f && prevYDiff > yDiff) {
                                    unselectSingleCell((tagInt - 10 * (yDiff / cellWidth).toInt()).toString())
                                }
                            }
                            (-1) * yDiff > cellWidth -> {
                                // bajando
                                if (prevYDiff == -1f || prevYDiff != -1f && prevYDiff > yDiff) {
                                    selectSingleCell((tagInt + -10 * (yDiff / cellWidth).toInt()).toString())
                                    swipeState = SwipeState.Vertical
                                } else if (prevYDiff != -1f && prevYDiff < yDiff) {
                                    unselectSingleCell((tagInt - 10 * (yDiff / cellWidth).toInt()).toString())
                                }
                            }
                        }
                    }

                    prevXDiff = xDiff
                    prevYDiff = yDiff
                }
            }

            MotionEvent.ACTION_UP -> {
                // El usuario ha terminado de seleccionar celdas
                // Verificamos si la palabra que tocaron es válida
                val tag = v.tag.toString()
                val tagInt = tag.toInt()
                var finalTag = tag

                if (swipeState == SwipeState.Horizontal) {
                    finalTag = when {
                        xDiff > cellWidth -> {
                            (tagInt - (xDiff / cellWidth).toInt()).toString()
                        }
                        -1 * xDiff > cellWidth -> {
                            (tagInt + -1 * (xDiff / cellWidth).toInt()).toString()
                        }
                        else -> tag
                    }
                } else if (swipeState == SwipeState.Vertical) {
                    finalTag = when {
                        yDiff > cellWidth -> {
                            (tagInt - 10 * (yDiff / cellWidth).toInt()).toString()
                        }
                        -1 * yDiff > cellWidth -> {
                            (tagInt + -10 * (yDiff / cellWidth).toInt()).toString()
                        }
                        else -> tag
                    }
                }
                checkIfRangeIsValid(v.tag.toString(), finalTag)
            }
        }
        return true
    }

    // Si se desliza una palabra válida, la sugerencia de palabra se tachará y se resaltará en verde
    private fun crossOutWords(wordObj : Word){
        if (wordObj.content.trim() == words[0]){
            tvWord0.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            tvWord0.setTextColor(Color.GREEN)
        } else if (wordObj.content.trim() == words[1]){
            tvWord1.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            tvWord1.setTextColor(Color.GREEN)
        } else if (wordObj.content.trim() == words[2]){
            tvWord2.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            tvWord2.setTextColor(Color.GREEN)
        } else if (wordObj.content.trim() == words[3]){
            tvWord3.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            tvWord3.setTextColor(Color.GREEN)
        } else if (wordObj.content.trim() == words[4]){
            tvWord4.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            tvWord4.setTextColor(Color.GREEN)
        } else if (wordObj.content.trim() == words[5]){
            tvWord5.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            tvWord5.setTextColor(Color.GREEN)
        }
    }

    // Rehacer todo el tachado y resaltado
    private fun redoAllCrossedOutWords(){
        tvWord4.paintFlags = 0
        tvWord0.paintFlags = 0
        tvWord1.paintFlags = 0
        tvWord5.paintFlags = 0
        tvWord3.paintFlags = 0
        tvWord2.paintFlags = 0
        tvWord4.setTextColor(Color.BLACK)
        tvWord0.setTextColor(Color.BLACK)
        tvWord1.setTextColor(Color.BLACK)
        tvWord5.setTextColor(Color.BLACK)
        tvWord3.setTextColor(Color.BLACK)
        tvWord2.setTextColor(Color.BLACK)
    }

    // Comprueba si la palabra que encontró el usuario es una de las palabras objetivo
    private fun checkIfRangeIsValid(initTag: String, endTag: String){
        var found = false
        for(wordObj in wordArray){
            if(wordObj.checkLoc(
                    initTag.toInt(),
                    endTag.toInt(),
                    swipeState == SwipeState.Horizontal
                )){
                // Es una coincidencia con una de las palabras objetivo
                if(wordObj.found){
                    // Pero la palabra ya fue encontrada
                    // Entonces no se puede encontrar de nuevo
                    xInitial = -1f
                    yInitial = -1f
                    xDiff = -1f
                    yDiff = -1f
                    prevXDiff = -1f
                    prevYDiff = -1f
                    swipeState = SwipeState.Undefined
                    return
                }

                // De lo contrario, el usuario ha encontrado una palabra válida
                markCellsAsFound(
                    initTag.toInt(),
                    endTag.toInt(),
                    swipeState == SwipeState.Horizontal
                )
                wordObj.found = true
                found = true
                crossOutWords(wordObj)  // Tachamos la pista de esta palabra
                break
            }
        }

        // Si el usuario encuentra una palabra válida, muestra un tick
        if (found){
            greenCheck.visibility = View.VISIBLE
            Timer("delay", false).schedule(1200) {
                getActivity()?.runOnUiThread {
                    greenCheck.visibility = View.GONE
                }
            }
            var showCongrats = true
            // Tachar las palabras que ya encontramos
            for(wordObj in wordArray){
                if(wordObj.found) {
                    crossOutWords(wordObj)
                }
            }
            for(wordObj in wordArray){
                if(!wordObj.found){
                    showCongrats = false
                    break
                }
            }
            // Si el usuario ha encontrado todas las palabras válidas
            // Muestra el mensaje de felicitación y el tiempo total empleado para terminar el juego
            if (showCongrats){
                totalTimeSpent = System.currentTimeMillis() - startTime
                var points = 50000 / (totalTimeSpent/1000)
                if (totalTimeSpent/1000 >= 60){
                    var minutes = totalTimeSpent/1000/60
                    var seconds = totalTimeSpent/1000%60
                    if (minutes < 10 && seconds < 10){
                        timeTxt.text = "Time Spent: 0" + minutes.toString() + ":0" + seconds.toString()
                    } else if (minutes >= 10 && seconds < 10){
                        timeTxt.text = "Time Spent: " + minutes.toString() + ":0" + seconds.toString()
                    } else if (minutes < 10 && seconds >= 10){
                        timeTxt.text = "Time Spent: 0" + minutes.toString() + ":" + seconds.toString()
                    } else{
                        timeTxt.text = "Time Spent: " + minutes.toString() + ":" + seconds.toString()
                    }

                }else {
                    timeTxt.text = "Time Spent: " + (totalTimeSpent / 1000).toString() + "s"

                }
                tvPuntos.text = "Felicidades has consegudio: "+points+" puntos"
                congrats_layout.visibility = View.VISIBLE
            }

        } else {
            // De lo contrario, el usuario encontró una palabra que no es válida
            redX.visibility = View.VISIBLE
            Timer("delay", false).schedule(1200) {
                getActivity()?.runOnUiThread {
                    redX.visibility = View.GONE
                }
            }
            unselectCellRange(initTag.toInt(), endTag.toInt(), swipeState == SwipeState.Horizontal)
        }

        // Restableciendo valores
        xInitial = -1f
        yInitial = -1f
        xDiff = -1f
        yDiff = -1f
        swipeState = SwipeState.Undefined
    }

    // Deseleccionar un rango de celdas
    private fun unselectCellRange(initTag: Int, endTag: Int, isHorizontal: Boolean){
        var start = initTag
        var end = endTag
        if (endTag < initTag){
            start = endTag
            end = initTag
        }
        if(isHorizontal){
            for (i in start..end){
                unselectSingleCell(i.toString())
            }
        } else {
            for (i in start..end step 10){
                unselectSingleCell(i.toString())
            }
        }
    }

    // Selecciona una celda por etiqueta
    private fun selectSingleCell(tag: String){
        val childCount = words_grid.childCount
        for (i in 0 until childCount){
            val linearLayout: LinearLayout = words_grid.getChildAt(i) as LinearLayout
            for (t in 0 until linearLayout.childCount){
                if(linearLayout.getChildAt(t).tag == tag){
                    linearLayout.getChildAt(t).background = ContextCompat.getDrawable(requireContext(), selectedColour)
                    return
                }
            }
        }
    }


    private fun unselectSingleCell(tag: String){
        var tagInt = tag.toInt()
        val childCount = words_grid.childCount
        for (i in 0 until childCount){
            val linearLayout: LinearLayout = words_grid.getChildAt(i) as LinearLayout
            for (t in 0 until linearLayout.childCount){
                if(linearLayout.getChildAt(t).tag == tag){
                    if(!foundWordsFlags[tagInt / 10][tagInt % 10]){
                        linearLayout.getChildAt(t).background = ContextCompat.getDrawable(requireContext(), R.drawable.unselected_cell_background)
                    }
                    return
                }
            }
        }
    }

    // Marcar las celdas encontradas para que no se deseleccionen hasta que se inicie un nuevo juego
    private fun markCellsAsFound(initTag: Int, endTag: Int, isHorizontal: Boolean){
        var start = initTag
        var end = endTag
        if (endTag < initTag){
            start = endTag
            end = initTag
        }
        if(isHorizontal){
            for (i in start..end){
                foundWordsFlags[i / 10][i % 10] = true
            }
        } else {
            for (i in start..end step 10){
                foundWordsFlags[i / 10][i % 10] = true
            }
        }
    }

    // Borrar todas las celdas previamente seleccionadas
    private fun unselectAllCells(){
        val childCount = words_grid.childCount
        for (i in 0 until childCount){
            val linearLayout: LinearLayout = words_grid.getChildAt(i) as LinearLayout
            for (t in 0 until linearLayout.childCount){
                linearLayout.getChildAt(t).background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.unselected_cell_background
                )
            }
        }
    }

    // Reorganiza aleatoriamente las letras en la cuadrícula
    private fun generateRandomLetters(){
        gridFlags = Array(gridSize) { BooleanArray(gridSize) { false } }
        foundWordsFlags = Array(gridSize) { BooleanArray(gridSize) { false } }
        val rnd = Random()
        var toggle: Boolean = rnd.nextInt(2) != 0

        for(r in 0 until gridSize){
            for (c in 0 until gridSize){
                gridLetters[r][c] = vocabulary[rnd.nextInt(vocabulary.length)].toString()
            }
        }

        // Posicionamiento de palabras a encontrar
        for (w in 0 until words.size){
            var found = false

            while (!found){
                var forward: Boolean = rnd.nextInt(2) != 0


                var r = 0
                if(words[w].length < gridSize){
                    r = rnd.nextInt(gridSize - (words[w].length))
                } else if (words[w].length > gridSize){
                    // No sucede ya que solo estamos probando con las 6 palabras dadas
                    // que tienen una longitud máxima de 10
                    break
                }

                // Verifica todas las filas o columnas dependiendo del valor de alternar
                var start = rnd.nextInt(gridSize - 1)

                for (n in 0 until gridSize){
                    var _n = (n + start) % gridSize
                    // Comprobar si la fila o columna está lo suficientemente vacía para colocar la palabra
                    for (i in r until r + words[w].length ) {
                        if(toggle){
                            // Mirar a lo largo de la fila
                            if(gridFlags[_n][i]) {
                                if(gridLetters[_n][i].equals(words[w][i-r].toString()) ){
                                    found = true
                                } else{
                                    found = false
                                    break
                                }
                            } else if (i == r + words[w].length - 1) {
                                // Hemos llegado al final
                                found = true
                            }
                        } else {
                            // Mirar a lo largo de la columna
                            if(gridFlags[i][_n]) {
                                if(gridLetters[i][_n].equals(words[w][i-r].toString())){
                                    found = true
                                }else{
                                    found = false
                                    break
                                }
                            } else if (i == r + words[w].length - 1) {
                                // Hemos llegado al final
                                found = true
                            }
                        }
                    }
                    if(found) {
                        // Registrar la ubicación de la palabra válida en el objeto de Word
                        if(toggle){
                            wordArray[w].setLoc(_n * 10 + r, toggle)
                        } else {
                            wordArray[w].setLoc(r * 10 + _n, toggle)
                        }

                        for (i in r until r + words[w].length ) {
                            if(toggle){
                                // Llenando a lo largo de la fila
                                gridLetters[_n][i] = words[w][i - r].toString()
                                gridFlags[_n][i] = true
                            } else {
                                // Llenando a lo largo de la columna
                                gridLetters[i][_n] = words[w][i - r].toString()
                                gridFlags[i][_n] = true
                            }
                        }
                        break
                    }
                }
                toggle = !toggle
            }
        }

        // Mostrar las letras
        val childCount = words_grid.childCount
        for (i in 0 until childCount){
            val linearLayout: LinearLayout = words_grid.getChildAt(i) as LinearLayout
            for (t in 0 until linearLayout.childCount){
                (linearLayout.getChildAt(t) as TextView).text = gridLetters[i][t]
            }
        }
    }

    // Reiniciar el juego cuando el usuario haga clic en el botón de nuevo juego
    fun reStartGame(view: View){
        words=getWords()

        tvWord0.setText(words[0])
        tvWord1.setText(words[1])
        tvWord2.setText(words[2])
        tvWord3.setText(words[3])
        tvWord4.setText(words[4])
        tvWord5.setText(words[5])

        for(i in 0..5){
            words[i] = remove1(words[i].toUpperCase())
        }

        congrats_layout.visibility = View.GONE
        for (i in 0 until numWords){
            wordArray[i] = Word(words[i])
        }
        unselectAllCells()
        redoAllCrossedOutWords()
        generateRandomLetters()



        for(i in 0..5){
            words[i] = remove1(words[i].toUpperCase())
        }

        startTime = System.currentTimeMillis()
    }


    companion object {
        const val vocabulary = "ABCDEFGHIJKLMOPQRSTUVWSYZ"
        const val numWords = 6
        const val gridSize = 10 // 10*10 grid

        var gridLetters = Array(gridSize) { Array<String>(gridSize) { "A" } }
        var gridFlags = Array(gridSize) { BooleanArray(gridSize) { false } }
        var foundWordsFlags = Array(gridSize) { BooleanArray(gridSize) { false } }

        val wordArray = Array<Word>(numWords) { Word("")}

        // Las palabras válidas que el usuario necesitará localizar
        @JvmName("getWords1")
        fun getWords(): Array<String> {
            var words : Array<String> = arrayOf("", "", "", "", "", "")
            for(i in 0..5){
                words[i] = (Diccionario.getRandomWordString(10).toLowerCase())
            }
            return words
        }
    }

    override fun onClick(p0: View?) {
        reStartGame(root)
    }

    fun remove1(input: String): String {
        // Cadena de caracteres original a sustituir.
        val original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ"
        // Cadena de caracteres ASCII que reemplazarán los originales.
        val ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC"
        var output = input
        for (i in 0 until original.length) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original[i], ascii[i])
        } //for i
        return output
    }
}


