package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity()
{

    var givereview: EditText? = null
    var predict: Button? = null
    var result: TextView? = null
    var url = "http://vinnu-sentiment-analysis-app.herokuapp.com/sentiment"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("MainActivity","onCreate")

        givereview = findViewById(R.id.editText)
        predict = findViewById(R.id.button)
        result = findViewById(R.id.textView2)

        predict!!.setOnClickListener{ // hit the API -> Volley
            Log.i("MainActivity","onClick")
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                object : Response.Listener<String?>
                {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(response: String?)
                    {
                        try
                        {
                            Log.i("MainActivity","onResponse")
                            val jsonObject = JSONObject(response)
                            val data = jsonObject.getString("Result")
                            if (data == "1")
                            {
                                Log.i("MainActivity", data)
                                result!!.text = "This is a Positive Review"
                            }
                            else
                            {
                                Log.i("MainActivity", data)
                                result!!.text = "This is a Negative Review"
                            }
                        }
                        catch (e: JSONException)
                        {
                            e.printStackTrace()
                        }
                    }
                },
                object : Response.ErrorListener
                {
                    override fun onErrorResponse(error: VolleyError)
                    {
                        Log.i("MainActivity","onErrorResponse")
//                        Toast.makeText(this@MainActivity, "error", Toast.LENGTH_SHORT).show()
                    }
                })
            {
                override fun getParams(): MutableMap<String, String>
                {
                    val params: MutableMap<String, String> = hashMapOf()
                    params.put("review",givereview!!.text.toString())
                    return params
                }
            }
            val queue: RequestQueue = Volley.newRequestQueue(this@MainActivity)
            queue.add(stringRequest)
        }
    }
}