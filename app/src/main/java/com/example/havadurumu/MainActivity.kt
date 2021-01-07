package com.example.havadurumu


import android.content.pm.PackageManager
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import im.delight.android.location.SimpleLocation

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*



@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    var location:SimpleLocation?=null
    var latitude:String?=null
    var longitude:String?=null
    var tvSehir:TextView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var spinnerAdapter=ArrayAdapter.createFromResource(this,R.array.sehirler,R.layout.spinner_tek_satir)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        snpSehir.background.setColorFilter(resources.getColor(R.color.colorAccent),PorterDuff.Mode.SRC_ATOP)
        snpSehir.setTitle("Şehir Seçiniz")
        snpSehir.setPositiveButton("Seç")
        snpSehir.adapter=spinnerAdapter

        snpSehir.setOnItemSelectedListener(this)

        snpSehir.setSelection(1)
        verileriGetir("İstanbul")

    }

    private fun oankiSehirGetir(lat: String?,longt:String?){
        val url="http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+longt+"&appid=f57944d9dd4122f54c55072ef691b147&lang=tr&lang=tr&units=metric"
        var sehirAdi:String?=null

        val havaDurumuObjeRequest2 = JsonObjectRequest(Request.Method.GET, url,null,
            object : Response.Listener<JSONObject>{
                override fun onResponse(response: JSONObject?) {
                    val main=response?.getJSONObject("main")
                    val sicaklik=main?.getInt("temp")
                    tvSicaklik.text=sicaklik.toString()

                    sehirAdi=response?.getString("name")

                    val weather=response?.getJSONArray("weather")

                    val aciklama=weather?.getJSONObject(0)?.getString("description")
                    twAciklama.text=aciklama

                    val icon=weather?.getJSONObject(0)?.getString("icon")
                    if(icon?.last()=='d'){
                        rootLayout.background=getDrawable(R.drawable.bg)
                        tvCelcius.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                        tvSicaklik.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                        tvTarih.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                        twAciklama.setTextColor(resources.getColor(R.color.colorPrimaryDark))


                    }else{
                        rootLayout.background=getDrawable(R.drawable.gece)

                        tvTarih.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                        tvSicaklik.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                        tvCelcius.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                        twAciklama.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                    }
                    val resimDosyaAdi=resources.getIdentifier("icon_"+icon?.karakterSil(),"drawable",packageName)
                    imgHavaDurumu.setImageResource(resimDosyaAdi)
                    tvTarih.text=tarihYazdir()
                }

            }, object : Response.ErrorListener  {
                override fun onErrorResponse(error: VolleyError?) {

                }

            })


        MySingleton.getInstance(this).addToRequestQueue(havaDurumuObjeRequest2)

    }


    fun verileriGetir(sehir:String) {
        val url="https://api.openweathermap.org/data/2.5/weather?q="+sehir+"&appid=f57944d9dd4122f54c55072ef691b147&lang=tr&units=metric"
        val havaDurumuObjeRequest = JsonObjectRequest(Request.Method.GET, url,null,
            object : Response.Listener<JSONObject>{
                override fun onResponse(response: JSONObject?) {
                    val main=response?.getJSONObject("main")
                    val sicaklik=main?.getInt("temp")
                    tvSicaklik.text=sicaklik.toString()

                    val sehirAdi=response?.getString("name")
                    tvSehir?.setText(sehirAdi)


                    val weather=response?.getJSONArray("weather")

                    val aciklama=weather?.getJSONObject(0)?.getString("description")
                    twAciklama.text=aciklama

                    val icon=weather?.getJSONObject(0)?.getString("icon")
                    if(icon?.last()=='d'){
                        rootLayout.background=getDrawable(R.drawable.bg)
                        tvCelcius.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                        tvSicaklik.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                        tvTarih.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                        twAciklama.setTextColor(resources.getColor(R.color.colorPrimaryDark))


                    }else{
                        rootLayout.background=getDrawable(R.drawable.gece)

                        tvTarih.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                        tvSicaklik.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                        tvCelcius.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                        twAciklama.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                    }
                    val resimDosyaAdi=resources.getIdentifier("icon_"+icon?.karakterSil(),"drawable",packageName)
                    imgHavaDurumu.setImageResource(resimDosyaAdi)
                    tvTarih.text=tarihYazdir()
                }

            }, object : Response.ErrorListener  {
                override fun onErrorResponse(error: VolleyError?) {

                }

            })


        MySingleton.getInstance(this).addToRequestQueue(havaDurumuObjeRequest)
    }
    fun tarihYazdir():String{
        val takvim =Calendar.getInstance().time
        val formatlayici=SimpleDateFormat("EEEE,MMMM,yyyy",Locale("tr"))
        val tarih=formatlayici.format(takvim)
        return tarih
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        tvSehir=view as TextView
        if (position==0){
            location= SimpleLocation(this)
            if(!location!!.hasLocationEnabled()){
                Toast.makeText(this,"GPS'i AÇ",Toast.LENGTH_LONG).show()
                SimpleLocation.openSettings(this)
            }else{
                if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),60)

                }else{
                    location= SimpleLocation(this)
                    latitude= String.format("%.6f",location?.latitude)
                    longitude=String.format("%.6f",location?.longitude)
                    Log.e("lat",""+latitude)
                    Log.e("long", " "+longitude)

                    var oankiSehiradi=oankiSehirGetir(latitude,longitude)
                }
            }
        }else {
            val secilenSehir=parent?.getItemAtPosition(position).toString()
            tvSehir=view as TextView

            verileriGetir(secilenSehir)
        }






    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {if(requestCode== 60){
        if (grantResults.size > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            location= SimpleLocation(this)
            latitude= String.format("%.6f",location?.latitude)
            longitude=String.format("%.6f",location?.longitude)
            Log.e("lat",""+latitude)
            Log.e("long", " "+longitude)

            var oankiSehiradi=oankiSehirGetir(latitude,longitude)

        }else{
            snpSehir.setSelection(1)
            Toast.makeText(this,"GPS'i AÇ",Toast.LENGTH_LONG).show()
        }
    }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}




private fun String?.karakterSil(): String? {
    return this?.substring(0,this.length-1)
}
