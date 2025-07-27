package com.pangea.contacts.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.pangea.contacts.R
import com.pangea.contacts.models.Contact

class NewContactActivity : AppCompatActivity() {

    //Propiedades
    var indexPhoto: Int = 0
    val photos = arrayOf(R.drawable.foto_01, R.drawable.foto_02, R.drawable.foto_03, R.drawable.foto_04, R.drawable.foto_05, R.drawable.foto_06)
    var photo: ImageView? = null
    var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_contact)

        //Referenciar y inflar la toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Navegar hacia atras en la toolbar
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //Referenciar y seleccionar fotos
         photo = findViewById<ImageView>(R.id.imgPhoto)
         photo?.setOnClickListener {
            selectPhoto()
        }
        //Reconocer nuevo vs editar
        if (intent.hasExtra("ID")){
            index = intent.getStringExtra("ID")?.toInt() ?: 0
            fillInData(index)

        }
    }

    //Metodo para inflar el menu de la toolbar (items)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    //Metodo para agregarle funcionalidad a los items de la toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_new -> {
                val name = findViewById<EditText>(R.id.tvName)
                val lastname = findViewById<EditText>(R.id.tvLastname)
                val company = findViewById<EditText>(R.id.tvCompany)
                val age = findViewById<EditText>(R.id.tvAge)
                val weight = findViewById<EditText>(R.id.tvWeight)
                val email = findViewById<EditText>(R.id.tvEmail)
                val phone = findViewById<EditText>(R.id.tvPhone)
                val address = findViewById<EditText>(R.id.tvAddress)

                // Validaciones básicas
                val ageValue = age.text.toString().toIntOrNull()
                val weightValue = weight.text.toString().toDoubleOrNull()

                if (name.text.isNullOrBlank() || lastname.text.isNullOrBlank() || email.text.isNullOrBlank()
                    || ageValue == null || weightValue == null) {
                    Toast.makeText(this, "Completa correctamente todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                    return true
                }

                if (index > -1){
                    //Llamada a metodo para actualizar nuevo contacto
                    ContactsActivity.updateContact(index, Contact(
                        name.text.toString(),
                        lastname.text.toString(),
                        company.text.toString(),
                        ageValue,
                        email.text.toString(),
                        phone.text.toString(),
                        weightValue,
                        address.text.toString(),
                        getPhoto(indexPhoto)
                    ))
                }else{
                    //Llamada a metodo para agregar nuevo contacto
                    ContactsActivity.addContact(Contact(
                        name.text.toString(),
                        lastname.text.toString(),
                        company.text.toString(),
                        ageValue,
                        email.text.toString(),
                        phone.text.toString(),
                        weightValue,
                        address.text.toString(),
                        getPhoto(indexPhoto)
                    ))

                }

                finish()
                Log.d("NUMERO DE ELEMENTOS", ContactsActivity.contacts?.count().toString())
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    //Metodo para seleccionar photo
    fun selectPhoto(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleciona imagen de perfil")

        val adapterDialog = ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item)
        adapterDialog.add("Photo 01")
        adapterDialog.add("Photo 02")
        adapterDialog.add("Photo 03")
        adapterDialog.add("Photo 04")
        adapterDialog.add("Photo 05")
        adapterDialog.add("Photo 06")

        builder.setAdapter(adapterDialog){
            dialog, which ->
            indexPhoto = which
            photo?.setImageResource(getPhoto(indexPhoto))

        }

        builder.setNegativeButton("Cancelar"){
            dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    fun getPhoto(index: Int): Int{
        return photos.get(index)

    }

    fun fillInData(index: Int){
        val contact = ContactsActivity.getContact(index)

        //Referenciar a las vistas
        val image = findViewById<ImageView>(R.id.imgPhoto)
        val name = findViewById<EditText>(R.id.tvName)
        val lastname = findViewById<EditText>(R.id.tvLastname)
        val company = findViewById<EditText>(R.id.tvCompany)
        val age = findViewById<EditText>(R.id.tvAge)
        val weight = findViewById<EditText>(R.id.tvWeight)
        val email = findViewById<EditText>(R.id.tvEmail)
        val phone = findViewById<EditText>(R.id.tvPhone)
        val address = findViewById<TextView>(R.id.tvAddress)

        //Asignarle valor a las vistas
        image.setImageResource(contact.photo)
        name.setText(contact.name,TextView.BufferType.EDITABLE)
        lastname.setText(contact.lastname,TextView.BufferType.EDITABLE)
        company.setText(contact.company, TextView.BufferType.EDITABLE)
        age.setText(contact.age.toString(), TextView.BufferType.EDITABLE)
        weight.setText(contact.weight.toString(), TextView.BufferType.EDITABLE)
        email.setText(contact.email, TextView.BufferType.EDITABLE)
        phone.setText(contact.phone, TextView.BufferType.EDITABLE)
        address.setText(contact.address, TextView.BufferType.EDITABLE )

        var position = 0
        for(photo in photos){
            if(contact.photo == photo){
                indexPhoto = position
            }
            position++
        }

    }


}