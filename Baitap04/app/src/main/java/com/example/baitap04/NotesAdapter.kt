package com.example.baitap04

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class NotesAdapter(private val context: MainActivity, private val layout: Int, private val noteList: List<NotesModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return noteList.size
    }

    override fun getItem(position: Int): Any {
        return noteList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private class ViewHolder {
        lateinit var textViewNote: TextView
        lateinit var imageViewEdit: ImageView
        lateinit var imageViewDelete: ImageView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(layout, null)
            holder.textViewNote = view.findViewById(R.id.textViewNameNote)
            holder.imageViewDelete = view.findViewById(R.id.imageViewDelete)
            holder.imageViewEdit = view.findViewById(R.id.imageViewEdit)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val notes = noteList[position]
        holder.textViewNote.text = notes.nameNote

        //bắt sự kiện sửa
        holder.imageViewEdit.setOnClickListener {
            context.DialogCapNhatNotes(notes.nameNote, notes.idNote)
        }

        //bắt sự kiện xóa
        holder.imageViewDelete.setOnClickListener {
            context.DialogDelete(notes.nameNote, notes.idNote)
        }

        return view!!
    }
}
