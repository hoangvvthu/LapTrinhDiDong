package com.example.baitap04

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var listView: ListView
    private lateinit var arrayList: ArrayList<NotesModel>
    lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView1)
        arrayList = ArrayList()
        adapter = NotesAdapter(this, R.layout.row_notes, arrayList)
        listView.adapter = adapter

        //khởi tạo database
        databaseHandler = DatabaseHandler(this, "notes.sqlite", null, 1)

        //tạo bảng Notes
        databaseHandler.queryData("CREATE TABLE IF NOT EXISTS Notes(Id INTEGER PRIMARY KEY AUTOINCREMENT, NameNote VARCHAR(200))")

        loadData()
    }

    fun loadData() {
        val cursor = databaseHandler.getData("SELECT * FROM Notes")
        arrayList.clear()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            arrayList.add(NotesModel(id, name))
        }
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuAddNotes) {
            DialogThem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun DialogThem() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_notes)

        val editText = dialog.findViewById<EditText>(R.id.editTextName)
        val buttonAdd = dialog.findViewById<Button>(R.id.buttonAdd)
        val buttonHuy = dialog.findViewById<Button>(R.id.buttonHuy)

        buttonAdd.setOnClickListener {
            val name = editText.text.toString().trim()
            if (name.equals("")) {
                Toast.makeText(this, "Vui lòng nhập tên Notes", Toast.LENGTH_SHORT).show()
            } else {
                databaseHandler.queryData("INSERT INTO Notes VALUES(null, '$name')")
                Toast.makeText(this, "Đã thêm Notes", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                loadData()
            }
        }

        buttonHuy.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun DialogCapNhatNotes(name: String, id: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_edit_notes)

        val editText = dialog.findViewById<EditText>(R.id.editTextName)
        val buttonEdit = dialog.findViewById<Button>(R.id.buttonEdit)
        val buttonHuy = dialog.findViewById<Button>(R.id.buttonHuy)

        editText.setText(name)

        buttonEdit.setOnClickListener {
            val newName = editText.text.toString().trim()
            databaseHandler.queryData("UPDATE Notes SET NameNote = '$newName' WHERE Id = '$id'")
            Toast.makeText(this, "Đã cập nhật Notes thành công", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            loadData()
        }

        buttonHuy.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun DialogDelete(name: String, id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Bạn có muốn xóa Notes $name không ?")
        builder.setPositiveButton("Có") { _, _ ->
            databaseHandler.queryData("DELETE FROM Notes WHERE Id = '$id'")
            Toast.makeText(this, "Đã xóa Notes $name thành công", Toast.LENGTH_SHORT).show()
            loadData()
        }
        builder.setNegativeButton("Không") { _, _ -> }
        builder.show()
    }
}
