package com.insbaixcamp.game.utilities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import com.insbaixcamp.game.MainActivity
import com.insbaixcamp.game.R
import java.util.*

object Diccionaro {

    private const val SHARED_LANG_KEY = "lang"
    var language = "en"
    var dictionary:MutableList<String>? = null
    var shared:SharedPreferences? = null
    var rawResources: Resources? = null
    var packageName: String? = null
    var activity: MainActivity? = null
    //var dailyWord = null


    fun setSharedPreferences(s:SharedPreferences){
        shared = s
    }

    fun loadRawResources(r:Resources) {
        rawResources = r
    }

    fun inicialitzar(){
          loadDictionary(findLanguage()!!)
    }

    init {
        //Para iniciar el diccionario se cargaran las shared preferences
        //i se recojera el valor del idioma, si este no existe se
        //se escribira el idioma del dispositivo, ingles si no esta traduccion

    }

    fun findLanguage(): String {
        //Comprobamos las sharedPreferences en busca de algun idioma
        var lang = shared!!.getString(SHARED_LANG_KEY, Locale.getDefault().language)

//            Despues si no hay valor, se comprueba el idioma del sistema
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

    private fun loadDictionary(lang:String) {
        switchLanguage(lang)
        dictionary = mutableListOf()
        val input = activity!!.resources.openRawResource(activity!!.resources.getIdentifier(lang, "raw", packageName))
        val reader = input.bufferedReader()
        var line : String? = ""
        var parts : List<String>

        line = reader.readLine()
        while (line != null) {
            if (line.contains("/")){
                parts = line.split("/")
                dictionary!!.add(parts.elementAt(0))
            } else {
                dictionary!!.add(line)
            }
            line = reader.readLine()
        }

    }

    fun switchLanguage(lang:String){
        //Una vez cambiado el lenguaje en la clase, se actualiza en
        //las shared preferences para que los cambios se mantengan
        if (lang != language) {
            language = lang
            with(shared!!.edit()){
                putString(SHARED_LANG_KEY, lang)
                commit()
            }
        }
    }

    fun getRandomWord(): MutableList<Char> {
        var word = dictionary!![(0 until dictionary!!.size).random()]

        return word.toMutableList()
    }

    fun findWord(paraula:String): Boolean {

        for (s in dictionary!!){
            if (s.uppercase() == paraula.uppercase())
                return true
        }

        return false
    }

    fun setPackagedName(packageName: String) {
        this.packageName = packageName
    }

    fun getLang(): String{
        return language
    }

    @JvmName("setActivity1")
    fun setActivity(mainActivity: MainActivity) {
        activity = mainActivity
    }

}