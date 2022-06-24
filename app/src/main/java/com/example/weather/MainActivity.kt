package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var text: TextView
    private lateinit var button: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val daysOfWeek = listOf(
            resources.getString(R.string.app_name),
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
        )

        val note = Note("title", "name")
        val note1 = note.copy()
        val textToView = note1.title + " " + note1.note

        text = findViewById(R.id.text_view)
        button = findViewById(R.id.button)

        // жесткий вызов 3 методов, обычно так не делаю, но тут сплошной эксперемент так думаю простите
        val cobbledString = nextCycle(cycle(testingWhen(daysOfWeek)), textToView)

        button.setOnClickListener { text.text = cobbledString }
        findViewById<MaterialButton>(R.id.clear_button).setOnClickListener { text.text = "" }
    }

    private fun testingWhen(list : List<String>) : List<String> {
        val translateDaysOfWeek = mutableListOf<String>()
        for (s in list) {
            when(s){
                "Weather" -> {translateDaysOfWeek.add("Погода")}
                "Tuesday" -> {translateDaysOfWeek.add("Вторник")}
                "Wednesday" -> {translateDaysOfWeek.add("Среда")}
                "Thursday" -> {translateDaysOfWeek.add("Четверг")}
                "Friday" -> {translateDaysOfWeek.add("Пятница")}
                "Saturday" -> {translateDaysOfWeek.add("Субота")}
            }
        }
        return translateDaysOfWeek
    }

    private fun cycle(list : List<String>): List<String> {
        val daysWithIndex = mutableListOf<String>()
        list.forEachIndexed { index, s ->
            val temp = "$s ${index + 1}"
            daysWithIndex.add(temp)
        }
        return daysWithIndex
    }

    private fun nextCycle(list: List<String>, textToView: String): String {
        val s = StringBuilder()
        for (i in list.indices) {
            s.append(list[i])
                .append(" ")
                .append(textToView)
                .append(" ")
                .append(i + 1)
                .append("\n")
        }
        return s.toString()
    }
}