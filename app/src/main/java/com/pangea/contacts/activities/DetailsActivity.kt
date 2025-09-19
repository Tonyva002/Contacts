package com.pangea.contacts.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pangea.contacts.R

class DetailsActivity : AppCompatActivity() {

    var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Referenciar y inflar la la toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Agregar la navegacion hacia atras en la toolbar
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.contact_details)

        //Obtener el index enviado desde la pantalla anterior (ContactActivity)
         index = intent.getStringExtra("ID")?.toInt()?: 0

        mapearDatos()



    }

    fun mapearDatos(){
        //Obtener los contactos por index
        val contact = ContactsActivity.getContact(index ?: 0)

        //Referenciar a las vistas
        val image = findViewById<ImageView>(R.id.imgPhoto)
        val name = findViewById<TextView>(R.id.tvName)
        val company = findViewById<TextView>(R.id.tvCompany)
        val age = findViewById<TextView>(R.id.tvAge)
        val weight = findViewById<TextView>(R.id.tvWeight)
        val email = findViewById<TextView>(R.id.tvEmail)
        val phone = findViewById<TextView>(R.id.tvPhone)
        val address = findViewById<TextView>(R.id.tvAddress)

        //Asignarle valor a las vistas
        image.setImageResource(contact.photo)
        name.text = buildString {
            append(contact.name)
            append(" ")
            append(contact.lastname)
        }
        company.text = contact.company
        age.text = buildString {
            append(contact.age.toString())
            append(" ")
            append("años")
        }
        weight.text = buildString {
            append(contact.weight.toString())
            append(" ")
            append("kg")
        }
        email.text = contact.email
        phone.text = contact.phone
        address.text = contact.address
    }

    // Inflar el menu a la toolbar (items)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Agregar funcionalidad a las opciones de la toolbar (items)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_delete -> {
                ContactsActivity.deleteContact(index)
                finish()
                return true

            }

            R.id.action_edit_contact -> {
                val intent = Intent(this, NewContactActivity::class.java)
                intent.putExtra("ID", index.toString())
                startActivity(intent)
                return true

            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapearDatos()
    }
}