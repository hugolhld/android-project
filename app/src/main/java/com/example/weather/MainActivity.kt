package com.androdocs.weatherapp

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.androdocs.weatherapp.R.*
import com.androdocs.weatherapp.R.id.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val API: String = "167d58d024f7bab696b3bb1dea0260ca"
    var CITY: String = "Paris"
    var lon: String = "2.35"
    var lat: String = "48.85"

    /*val content: String = search_city.getText().toString()*/



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

//        val test = "test"
//
//        second_hour.text = test

        weatherHourly().execute()

        weatherTask().execute()

        button_search.setOnClickListener {
            val searchCity: String = search_city.text.toString()

            CITY = searchCity

            weatherTask().execute()
            weatherHourly().execute()

        }

    }

    inner class weatherHourly(): AsyncTask<String, Void, String>(){

        override fun doInBackground(vararg params: String?): String? {
            var response:String?

            try {
                response = URL("https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&exclude=minutely&units=metric&appid=$API").readText(
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
                val jsonObj = JSONObject(result)
                val hourlyTab = jsonObj.getJSONArray("hourly")

                val firstHourJSON = hourlyTab.getJSONObject(1).getLong("dt")
                val firstHour = SimpleDateFormat("HH", Locale.FRANCE).format(Date(firstHourJSON * 1000)) + "h"
                first_hour.text = firstHour

                val firstWeather = hourlyTab.getJSONObject(1).getString("temp")
                first_weather.text = firstWeather.toDouble().toInt().toString() + "°"

                val secondtHourJSON = hourlyTab.getJSONObject(2).getLong("dt")
                val secondHour = SimpleDateFormat("HH", Locale.FRANCE).format(Date(secondtHourJSON * 1000)) + "h"
                second_hour.text = secondHour

                val secondWeather = hourlyTab.getJSONObject(2).getString("temp")
                second_weather.text = secondWeather.toDouble().toInt().toString() + "°"

                val thirdtHourJSON = hourlyTab.getJSONObject(3).getLong("dt")
                val thirdHour = SimpleDateFormat("HH", Locale.FRANCE).format(Date(thirdtHourJSON * 1000)) + "h"
                third_hour.text = thirdHour

                val thirdWeather = hourlyTab.getJSONObject(3).getString("temp")
                third_weather.text = thirdWeather.toDouble().toInt().toString() + "°"

                val fourHourJSON = hourlyTab.getJSONObject(4).getLong("dt")
                val fourHour = SimpleDateFormat("HH", Locale.FRANCE).format(Date(fourHourJSON * 1000)) + "h"
                four_hour.text = fourHour

                val fourWeather = hourlyTab.getJSONObject(4).getString("temp")
                four_weather.text = fourWeather.toDouble().toInt().toString() + "°"


            } catch (e: Exception) {
                Log.d("error", e.toString())
                findViewById<ProgressBar>(id.loader).visibility = View.GONE
            }
        }
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
                val coord = jsonObj.getJSONObject("coord")
                lon = coord.getString("lon")
                lat = coord.getString("lat")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val weatherDescription = weather.getString("description")
                val icon = weather.getString("main")


                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy HH:mm ", Locale.FRANCE).format(Date(updatedAt*1000))
                val temp = main.getString("temp").toDouble().toInt().toString() +"°C"


                val address = jsonObj.getString("name")+", "+sys.getString("country")

                findViewById<TextView>(id.address).text = address
                findViewById<TextView>(id.updated_at).text =  updatedAtText
                findViewById<TextView>(id.temp).text = temp
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
                findViewById<ProgressBar>(id.loader).visibility = View.GONE
            }

        }
    }
}