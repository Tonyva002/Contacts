package com.pangea.contacts.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.GridView
import android.widget.ListView
import androidx.appcompat.widget.SwitchCompat

import android.widget.ViewSwitcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pangea.contacts.R
import com.pangea.contacts.adapters.CustomAdapter
import com.pangea.contacts.adapters.CustomAdapterGrid
import com.pangea.contacts.models.Contact

class ContactsActivity : AppCompatActivity() {

    //Propiedades
    var listView: ListView? = null
    var gridView: GridView? = null
    var viewSwitcher: ViewSwitcher? = null

    //Propiedades y metodos estacticos para acceder desde cualquier parte de la aplicacion
    companion object {
        var contacts: ArrayList<Contact>? = null
        var listAdapter: CustomAdapter? = null
        var gridAdapter: CustomAdapterGrid? = null

        fun addContact(contact: Contact){
            listAdapter?.addItem(contact)
        }

        fun getContact(index: Int): Contact {
            return listAdapter?.getItem(index) as Contact
        }

        fun deleteContact(index: Int){
            listAdapter?.deleteItem(index)
        }

        fun updateContact(index: Int, newcontact: Contact) {
            listAdapter?.updateItem(index, newcontact)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contacts)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Referencia a las vistas
        listView = findViewById(R.id.listView)
        gridView = findViewById(R.id.gridView)
        viewSwitcher = findViewById(R.id.viewSwitcher)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //LLenado del Array (modelo)
        contacts = ArrayList()
        contacts?.add(Contact("Tony", "Vasquez", "Indra", 36, "tonyva002@hotmail.com", "809-564-7724", 154.5,"C/ b #35, Pantoja", R.drawable.foto_01,))
        contacts?.add(Contact("Ana", "Solano", "Hiberus", 29, "ana@hotmail.com", "809-564-5566", 124.2, "C/25 #12, Bella Vista", R.drawable.foto_05))
        contacts?.add(Contact("Pedro", "Gonzales", "Oesia", 39, "pedro@hotmail.com", "809-564-3333", 166.8, "C/ Progreso #10, Pantoja", R.drawable.foto_02))
        contacts?.add(Contact("Maria", "Fernandez", "Spirit", 24, "maria@hotmail.com", "809-777-5555", 134.7, "C/ P #5, Nuevo Amanecer", R.drawable.foto_06))
        contacts?.add(Contact("Juan", "Garcia", "surum", 33, "juan002@hotmail.com", "809-258-1234", 174.5, "C/ 20 #32, Pueblo Nuevo", R.drawable.foto_03))
        contacts?.add(Contact("Paty", "Montola", "Channel", 35, "paty@hotmail.com", "809-888-2222", 129.3, "C/ Fernandez #65, Villa Aura", R.drawable.foto_04))

        //Inflar los adapter y asignarlos al listView y al gridView
        listAdapter = CustomAdapter(this, contacts!!)
        gridAdapter = CustomAdapterGrid(this, contacts!!)
        listView?.adapter = listAdapter
        gridView?.adapter = gridAdapter

        //Navegar con el item seleccionado a la pantalla de detalles
        listView?.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("ID", i.toString())
            startActivity(intent)
        }

        gridView?.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("ID", i.toString())
            startActivity(intent)
        }
    }

    // Inflar el menu a la toolbar (items)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        //Search
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val itemSearch = menu?.findItem(R.id.action_search)
        val searchView = itemSearch?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Search contact..."
        
        searchView.setOnQueryTextFocusChangeListener { view, b ->
            //Preparar datos

        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                //Filtrar
                if (newText != null) {
                    listAdapter?.filtrar(newText)
                }
                if (newText != null && !gridAdapter?.isEmpty!!) {
                    gridAdapter?.filtrar(newText)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                //Filtrar
                return true
            }
        })
        //Switch
        val itemSwitch = menu.findItem(R.id.action_switch)
        itemSwitch?.setActionView(R.layout.switch_item)
        val switchView = itemSwitch?.actionView?.findViewById<SwitchCompat>(R.id.schangeView)

        if (switchView == null) {
            Log.e("ContactsActivity", "⚠️ Switch no encontrado en switch_item.xml")
        }

        switchView?.setOnCheckedChangeListener { _, isChecked ->
            viewSwitcher?.showNext()
        }


        return super.onCreateOptionsMenu(menu)
    }

    //Agregar funcionalidad a las opciones de la toolbar (items)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add -> {
                val intent = Intent(this, NewContactActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    //Actualiza la activity con los nuevos cambios (adapter)
    override fun onResume() {
        super.onResume()
        if (!listAdapter?.isEmpty!!){
            listAdapter?.notifyDataSetChanged()
        }
        if (!gridAdapter?.isEmpty!!){
            gridAdapter?.notifyDataSetChanged()
        }
    }
}