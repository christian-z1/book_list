package com.example.bookList.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.bookList.API.SingletonVolley
import com.example.bookList.Database.adBD
import com.example.bookList.Adapters.StudentListAdapter
import com.example.bookList.Models.Student
import com.example.bookList.R

import com.example.bookList.Utils.Utils


class ListaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)
        val rview=findViewById<RecyclerView>(R.id.lista_rv)
        val adapter= StudentListAdapter(this)
        rview.adapter=adapter
        //rview.layoutManager=GridLayoutManager(this,2) //Muestra los elementos en grillas,
        // si quieren que les muestre grillas de 2,3 o 4 o N numeros
        //Solo cambién el 2 que tiene ahí
        rview.layoutManager=LinearLayoutManager(this) //Muestra los elementos en lista de 1, es alternativo al de arriba,
        //Solo pueden usar uno, por eso comenté el otro v:
        getStudents(adapter)
//        adapter.setStudentList(DbStudentList())
    }

    fun DbStudentList():List<Student>{
        var students= ArrayList<Student>()
        val database= adBD(this)
        val cursor=database.consulta("SELECT idL,nombre,editorial,autor FROM libros")
        if(cursor!!.moveToFirst())do{
            val stu= Student(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
            )
            students.add(stu)
        }
        while(cursor!!.moveToNext())
        cursor.close()
        return students
    }

    fun getStudents(adapter:StudentListAdapter){
        var studentsList= ArrayList<Student>()

        val url = Utils.SERVER_IP+Utils.SITE+Utils.get
        val jsonArrayRequest=JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {response->
                for (i in 0 until response.length()){
                    val studentJson=response.getJSONObject(i)
                    val student=Student(
                        studentJson["idL"].toString().toInt(),
                        studentJson["nombre"].toString(),
                        studentJson["editorial"].toString(),
                        studentJson["autor"].toString()
                    )
                    studentsList.add(student)
                    adapter.setStudentList(studentsList)
                }
            },
            Response.ErrorListener { error->

            })
        SingletonVolley.getInstance(this).addToRequestQueue(jsonArrayRequest)
    }
}