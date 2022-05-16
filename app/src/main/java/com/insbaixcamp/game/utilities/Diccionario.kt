package com.insbaixcamp.game.utilities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import com.insbaixcamp.game.MainActivity
import com.insbaixcamp.game.R
import java.util.*

object Diccionario {

    private const val SHARED_LANG_KEY = "lang"
    var language = "en"
    var dictionary:MutableList<String>? = null
    var shared:SharedPreferences? = null
    var rawResources: Resources? = null
    var packageName: String? = null
    //var dailyWord = null

    //Metode per carregar les varaibles necessaries per tractar
    //amb els documents dels diccionaris i els llenguatges
    fun inicialitzar(resources: Resources, preferences: SharedPreferences, packageName: String){
        this.rawResources = resources
        this.shared = preferences
        this.packageName = packageName
        this.language = setUpLanguage()
        Log.i("idioma", language);
        this.dictionary = setUpDictionary(language)
        mostraDiccionari(50)
    }

    private fun mostraDiccionari(i: Int) {
        for (j in 0..i){
            dictionary?.get(j)?.let {  }
        }
    }

    private fun setUpDictionary(language: String): MutableList<String>? {
        val input = rawResources!!.openRawResource(rawResources!!.getIdentifier(language, "raw", packageName))
        val reader = input.bufferedReader()
        var line : String? = ""
        var parts : List<String>
        var paraules = mutableListOf<String>()

        line = reader.readLine()
        while (line != null) {
            if (line.contains("/")){
                parts = line.split("/")
                paraules!!.add(parts.elementAt(0))
            } else {
                paraules!!.add(line)
            }
            line = reader.readLine()
        }

        return paraules
    }

    //Para iniciar el diccionario se cargaran las shared preferences
    //i se recojera el valor del idioma, si este no existe se
    //se escribira el idioma del dispositivo, ingles si no esta traduccion
    fun setUpLanguage(): String{
        var lang = shared!!.getString(SHARED_LANG_KEY, Locale.getDefault().language)

        //Despues si no hay valor, se comprueba el idioma del sistema
        when(lang){
            "pt" -> return "pt"
            "es" -> return "es"
            "fr" -> return "fr"
            "ru" -> return "ru"
            "de" -> return "de"
            else -> {
                //cuando no tenemos ningun registro del idioma automaticamente ingles
                Log.i("lang", "default language loaded, 'en'")
                return "en"
            }
        }
        //Tener en cuenta la escritura sharedPref
        return lang.toString()
    }

    fun getRandomWord(size:Int): MutableList<Char> {
        var word:String
        do{
            word = dictionary!![(0 until dictionary!!.size).random()]
        }while(word.length>=size)

       return word.toMutableList()
    }

    fun getRandomWordString(size:Int): String {
        var word:String
        do{
            word = dictionary!![(0 until dictionary!!.size).random()]
        }while(word.length>=size || word.length<4)

        return word
    }





//    fun setSharedPreferences(s:SharedPreferences){
//        shared = s
//        Log.i("lang", findLanguage())
//    }
//
//    fun loadRawResources(r:Resources) {
//        rawResources = r
//    }
//
//    fun inicialitzar(){
//          loadDictionary(findLanguage()!!)
//    }
//
//    init {

//
//    }
//
//    fun findLanguage(): String {
//        //Comprobamos las sharedPreferences en busca de algun idioma
//        var lang = shared!!.getString(SHARED_LANG_KEY, Locale.getDefault().language)
//
////            Despues si no hay valor, se comprueba el idioma del sistema
//            when(lang){
//                "pt" -> return "pt"
//                "es" -> return "es"
//                "fr" -> return "fr"
//                "ru" -> return "ru"
//                "de" -> return "de"
//                else -> {
//                    //cuando no tenemos ningun registro del idioma automaticamente ingles
//                    Log.i("lang", "default language loaded, 'en'")
//                    return "en"
//                }
//            }
//
//        //Tener en cuenta la escritura sharedPref
//        return lang.toString()
//    }
//
//    private fun loadDictionary(lang:String) {
//        switchLanguage(lang)
//        dictionary = mutableListOf()
//        val input = activity!!.resources.openRawResource(activity!!.resources.getIdentifier("$lang.txt", "raw", packageName))
//        val reader = input.bufferedReader()
//        var line : String? = ""
//        var parts : List<String>
//
//        line = reader.readLine()
//        while (line != null) {
//            if (line.contains("/")){
//                parts = line.split("/")
//                dictionary!!.add(parts.elementAt(0))
//            } else {
//                dictionary!!.add(line)
//            }
//            Log.i("paraula", dictionary!![dictionary!!.size-1]);
//            line = reader.readLine()
//        }
//
//    }
//
//    fun switchLanguage(lang:String){
//        //Una vez cambiado el lenguaje en la clase, se actualiza en
//        //las shared preferences para que los cambios se mantengan
//        if (lang != language) {
//            language = lang
//            with(shared!!.edit()){
//                putString(SHARED_LANG_KEY, lang)
//                commit()
//            }
//        }
//    }
//
//    fun getRandomWord(): MutableList<Char> {
//        var word = dictionary!![(0 until dictionary!!.size).random()]
//
//        return word.toMutableList()
//    }
//
//    fun findWord(paraula:String): Boolean {
//
//        for (s in dictionary!!){
//            if (s.uppercase() == paraula.uppercase())
//                return true
//        }
//
//        return false
//    }
//
//    fun setPackagedName(packageName: String) {
//        this.packageName = packageName
//    }
//
//    fun getLang(): String{
//        return language
//    }
//
//    fun getLocaleDiccionari(resources: Resources?, s: String): Boolean {
//        if (resources!!.getIdentifier(s, "raw", packageName) == null){
//            return false
//        }
//        return true
//    }
//
//    fun agafaParaulaDiccionari(resources: Resources?, s: String, int: Int, packageName: String) : String{
//        val input = resources!!.openRawResource(resources!!.getIdentifier(s, "raw", packageName))
//        val reader = input.bufferedReader()
//        var line : String? = ""
//        var parts : List<String>
//        dictionary = mutableListOf()
//
//        line = reader.readLine()
//        while (line != null) {
//            if (line.contains("/")){
//                parts = line.split("/")
//                dictionary!!.add(parts.elementAt(0))
//            } else {
//                dictionary!!.add(line)
//            }
////            Log.i("paraula", dictionary!![dictionary!!.size-1]);
//            line = reader.readLine()
//        }
//        return dictionary!![int]
//    }

}