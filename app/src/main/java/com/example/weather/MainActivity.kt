package com.androdocs.weatherapp

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val API: String = "167d58d024f7bab696b3bb1dea0260ca"
    val CITY: String = "Resolute"
    /*val content: String = search_city.getText().toString()*/



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherTask().execute()

    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                        Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val weatherDescription = weather.getString("description")
                val icon = weather.getString("main")


                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy HH:mm ", Locale.FRANCE).format(Date(updatedAt*1000))
                val temp = main.getString("temp")+"°C"



                val address = jsonObj.getString("name")+", "+sys.getString("country")

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text =  updatedAtText
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()

                if (icon.equals("Rain")){
                    linearLayout3.setBackgroundResource(R.drawable.rain);
                    background_layout.setBackgroundResource(R.drawable.background_rain)
                }
                else if (icon.equals("Thunderstorm")){
                    linearLayout3.setBackgroundResource(R.drawable.thunderstorm);
                    background_layout.setBackgroundResource(R.drawable.background_light)

                }
                else if (icon.equals("Drizzle")){
                    linearLayout3.setBackgroundResource(R.drawable.shower_rain);
                    background_layout.setBackgroundResource(R.drawable.background_rain)

                }
                else if (icon.equals("Snow")){
                    linearLayout3.setBackgroundResource(R.drawable.snow)
                    background_layout.setBackgroundResource(R.drawable.background_snow)
                }
                else if (icon.equals("Clear")){
                    linearLayout3.setBackgroundResource(R.drawable.clearsky);
                    background_layout.setBackgroundResource(R.drawable.background_sun)

                }

                else if (icon.equals("Clouds")){
                    linearLayout3.setBackgroundResource(R.drawable.brokencloud);
                    background_layout.setBackgroundResource(R.drawable.background_cloud)

                }






                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE



            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
            }

        }
    }
}