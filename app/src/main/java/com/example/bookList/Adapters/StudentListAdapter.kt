package com.example.bookList.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookList.R
import com.example.bookList.Models.Student

class StudentListAdapter internal  constructor(
    context:Context
): RecyclerView.Adapter<StudentListAdapter.StudentViewHolder>(){
    private val inflater:LayoutInflater= LayoutInflater.from(context)
    private var students= emptyList<Student>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView=inflater.inflate(R.layout.student_item,parent,false)
        return StudentViewHolder(itemView)
    }

    override fun getItemCount()=students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val current=students[position]
        holder.idL.text="idL: "+current.idL
        holder.nombre.text="Nombre: "+current.nombre
        holder.editorial.text="editorial: "+current.editorial
        holder.autor.text="autor: "+current.autor
    }

    inner class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val idL:TextView =itemView.findViewById(R.id.item_idL)
        val nombre:TextView=itemView.findViewById(R.id.item_nombre)
        val autor:TextView=itemView.findViewById(R.id.item_autor)
        val editorial:TextView=itemView.findViewById(R.id.item_editorial)
    }

    fun setStudentList(studentList: List<Student>){
        this.students=studentList
        notifyDataSetChanged()
    }
}