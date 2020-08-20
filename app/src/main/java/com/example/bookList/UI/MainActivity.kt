package com.example.bookList.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.bookList.API.SingletonVolley
import com.example.bookList.Database.adBD
import com.example.bookList.R
import com.example.bookList.Utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var idL:Int=0
    var nombre:String=""
    var autor:String=""
    var editorial:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }



    //Cambia a la siguiente actividad
    fun navigateToList(v:View){
        startActivity(Intent(this, ListaActivity::class.java))
    }


    fun searchStudent(v: View){
        if(!etid.text.isEmpty()){
            idL=etid.text.toString().toInt()
            val database = adBD(this)
            val tupla=database.consulta("SELECT idL,nombre,editorial,autor FROM libros where idL='$idL'")
            if(tupla!!.moveToFirst()){
                //etnombre.setText(tupla.getString(0))
                etnombre.setText(tupla.getString(1))
                eteditorial.setText(tupla.getString(2))
                etautor.setText(tupla.getString(3))
            }else{
                Toast.makeText(this, "No hay registros almacenados", Toast.LENGTH_SHORT).show();
                etid.requestFocus()
            }
        }else{
            Toast.makeText(this, "No puedes dejar los campos vacios", Toast.LENGTH_SHORT).show();
        }
    }
    fun addStudent(v:View) {
        if (!etid.text.isEmpty() && !eteditorial.text.isEmpty() && !etautor.text.isEmpty() && !etnombre.text.isEmpty()) {
            getValues()
            val database= adBD(this)
            var studentJson=JSONObject()
            studentJson.put("idL",idL)
            studentJson.put("nombre",nombre)
            studentJson.put("editorial",editorial)
            studentJson.put("autor",autor)
            sendRequest(Utils.SERVER_IP+Utils.SITE+Utils.Insert, studentJson)
            Toast.makeText(this, "por aqui ya paso", Toast.LENGTH_SHORT).show();
            val tupla=database.Ejecuta("INSERT INTO libros(idL,nombre,editorial,autor) VALUES(" +
                    "'$idL'," +
                    "'$nombre'," +
                    "'$editorial'," +
                    "'$autor')")
            if(tupla==1 ){
                Toast.makeText(this, "DB: Registro insertado", Toast.LENGTH_SHORT).show()
                clearFields()
            }else{
                Toast.makeText(this, "Error al insertar", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "esto es que esta saliendo", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "No puedes dejar ningún campo de texto vacio", Toast.LENGTH_SHORT).show()
        }
    }



    fun updateStudent(v:View){
        if (!etid.text.isEmpty() && !eteditorial.text.isEmpty() && !etautor.text.isEmpty() && !etnombre.text.isEmpty()) {
            getValues()
            // actualiza la base de datos en el servidor
            var jsonEntrada = JSONObject()
            jsonEntrada.put("idL", etid.text.toString().toInt())
            jsonEntrada.put("nombre", etnombre.text.toString())
            jsonEntrada.put("editorial", eteditorial.text.toString())
            jsonEntrada.put("autor", etautor.text.toString())
            sendRequest(Utils.SERVER_IP+Utils.SITE+Utils.update, jsonEntrada)
            // actualiza la base de datos en el dispositivo
            val idL = etid.text.toString().toInt()
            val nombre = etnombre.text.toString()
            val editorial = eteditorial.text.toString()
            val autor = etautor.text.toString()
            val database= adBD(this)
            val tupla=database.Ejecuta("update libros set nombre='$nombre',editorial='$editorial',autor='$autor'     WHERE idL='$idL'")
            if(tupla==1){
                Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show()
                clearFields()
            }else{
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "No puedes dejar ningún campo de texto vacio", Toast.LENGTH_SHORT).show()
        }
    }
    fun deleteStudent(v:View){
        if(!etid.text.isEmpty()){

            // elimina el registro de la base de datos del servidor
            var jsonEntrada = JSONObject()
            jsonEntrada.put("idL", etid.text.toString())
            sendRequest(Utils.SERVER_IP+Utils.SITE+Utils.delete, jsonEntrada)
            // elimina el registro de la base de datos
            idL=etid.text.toString().toInt()
            val database = adBD(this)
            val tupla=database.Ejecuta("DELETE from libros where idL='$idL'")
            if(tupla==1){
                Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show()
                clearFields()
            }else{
                Toast.makeText(this, "Error al eliminado", Toast.LENGTH_SHORT).show()
            }
        }else{
            etid.requestFocus()
            Toast.makeText(this, "No puedes dejar los campos vacios", Toast.LENGTH_SHORT).show();
        }
    }
    fun getValues(){
        idL=etid.text.toString().toInt()
        nombre=etnombre.text.toString()
        editorial=eteditorial.text.toString()
        autor=etautor.text.toString()
    }
    fun clearFields(){
        etnombre.setText("")
        etnombre.setText("")
        eteditorial.setText("")
        etautor.setText("")
        etid.setText("")
        etid.requestFocus()
    }
    fun sendRequest(wsUrl:String, jsonEnt: JSONObject){

        // val wsUrl = Utils.SERVER_IP+Utils.SITE+Utils.Insert
        val jsonObjectRequest=JsonObjectRequest(
            Request.Method.POST, wsUrl, jsonEnt,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                if (succ == 200) {
                    clearFields()
                    etid.requestFocus()
                    Toast.makeText(
                        this,
                        "Success:${succ}  Message:${msg}",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", "${error.message}");
                Toast.makeText(this, "Error de capa 8 checa URL", Toast.LENGTH_SHORT).show();
            }
        )
        SingletonVolley.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


}