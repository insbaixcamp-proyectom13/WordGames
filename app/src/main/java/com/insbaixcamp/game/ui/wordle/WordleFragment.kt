package com.insbaixcamp.game.ui.wordle

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.core.view.size
import com.insbaixcamp.game.R
import com.insbaixcamp.game.databinding.FragmentWordleBinding
import com.insbaixcamp.game.ui.notifications.NotificationsViewModel

class WordleFragment : Fragment(), OnClickListener {
    private var _binding: FragmentWordleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var input:MutableList<Char>
    private lateinit var word:MutableList<Char>
    private lateinit var root: View
    private lateinit var wordTextView: ViewGroup
    private lateinit var intents:MutableList<ViewGroup>
    private var intentos = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentWordleBinding.inflate(inflater, container, false)
        root = binding.root
        word = mutableListOf('c', 'a', 'n', 't', 'a')
        input = mutableListOf()
        initializeKeyBoard()

        //añade el primer edit text para luego ir agregando
        //y cambiando cuando se completen la palabras
        intents = mutableListOf(binding.incWord1.root, binding.incWord2.root, binding.incWord3.root,
            binding.incWord4.root, binding.incWord5.root, binding.incWord6.root,)
        wordTextView = intents[intentos]

//        setWord()

        return root
    }

    private fun setWord() {

        for (c in wordTextView) {
            var tv = c as TextView
            tv.text = ""
        }


        for (i in 0 until input.size){
            var tv = wordTextView[i] as TextView
            tv.text = input[i].toString().uppercase()
        }
    }

    private fun initializeKeyBoard() {
        //Leer shared preferences para cargar el teclado segun idioma(ingles por defecto)

        var container1 = binding.incKeyboard.linearLayout as ViewGroup
        var container2 = binding.incKeyboard.linearLayout2 as ViewGroup
        var container3 = binding.incKeyboard.linearLayout3 as ViewGroup

        Log.i("butons", container1.childCount.toString())
        Log.i("butons", container2.childCount.toString())
        Log.i("butons", container3.childCount.toString())

        for (i in 0 until container1.childCount){
            var v = container1.getChildAt(i)
            v.setOnClickListener(this)
        }
        for (i in 0 until container2.childCount){
            var v = container2.getChildAt(i)
            v.setOnClickListener(this)
        }
        for (i in 0 until container3.childCount){
            var v = container3.getChildAt(i)
            v.setOnClickListener(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {

        if (p0 == binding.incKeyboard.ivSend){
            if (input.size == 5 && isValidWord(input.toString())) {

                showValidChars()

                //comprobar si es la palabra correcta i si alguna de las letras es correcta
                if ((input.toString().uppercase().equals(word.toString().uppercase()))) {
                    Log.i("enviado", "palabra correcta")

                } else {
                    Log.i("enviado", "palabra incorrecta")
                    intentos++

                    //en cuenta si es el ultimo intento
                    if (intentos == intents.size)
                        Toast.makeText(context, "Has perdido", Toast.LENGTH_LONG).show()

                    else
                        //Deberia borrar la palabra recibida i saltar al siguiente include de la vista
                        input = mutableListOf()
                        wordTextView = intents[intentos]
                }

            } else { Toast.makeText(context, "Palabra no reconocida por el diccionario o tamaño no valido", Toast.LENGTH_LONG).show() }

        } else if(p0 == binding.incKeyboard.ivDelete) {
            if (input.isNotEmpty())
                input.removeLast()
            setWord()

        } else {
            var butn = p0 as TextView
            var char = butn.text[0]

            if (input.size < 5)
                input.add(char)
                setWord()
        }
    }

    private fun showValidChars() {
        var palabra = word.toString().uppercase()
        for (i in 0 until wordTextView.size) {
            var char = input[i]
            var tv = wordTextView.getChildAt(i) as TextView

            if (char.uppercase() == word[i].uppercase()) {
                tv.background = resources.getDrawable(R.drawable.wordle_char_green_background)

            } else {
                if (wordContainsChar(char, i)) {
                    tv.background = resources.getDrawable(R.drawable.wordle_char_orange_background)
                }
            }
        }
    }

    private fun wordContainsChar(char: Char, i: Int): Boolean {
        for (c in 0 until word.size) {
            if (word[c].uppercase() == char.uppercase() && !revealedChar(char.uppercase(), i)){
                return true
            }
        }
        return false
    }

    private fun revealedChar(uppercase: String, int:Int): Boolean {
        var charCount = 0
        for ( c in 0 until word.size) {
            if (word[c].uppercase() == uppercase && input[c].uppercase() != uppercase){
                charCount++
            }
        }

        if (charCount == 0)
            return true

        return false
    }

    //Comprueba que la palabra se encuentre dentro del diccionario
    private fun isValidWord(toString: String): Boolean {

        return true
    }

}