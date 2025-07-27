package com.pangea.contacts.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.pangea.contacts.R
import com.pangea.contacts.models.Contact
import java.util.Locale

class CustomAdapter(var context: Context, items: ArrayList<Contact>):BaseAdapter() {

    //Almacena los elementos que se mostraran en el ListView
    var items: ArrayList<Contact>? = null
    var copyItems: ArrayList<Contact>? = null

    init {
        this.items = ArrayList(items)
        this.copyItems = items
    }

    override fun getCount(): Int {
        return items?.count()?: 0
    }

    override fun getItem(p0: Int): Any {
        return items?.get(p0)?: 0
    }

    override fun getItemId(p0: Int): Long {
        return  p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var viewHolder: ViewHolder? = null
        var view: View? = p1

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_contact, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder

        }else {
            viewHolder = view.tag as? ViewHolder
        }
        val item = getItem(p0) as Contact
        viewHolder?.name?.text = buildString {
            append(item.name)
            append(" ")
            append(item.lastname)
        }
        viewHolder?.company?.text = item.company
        viewHolder?.photo?.setImageResource(item.photo)

        return view!!
    }
    //Metodo para agregar contacto
    fun addItem(item: Contact){
        copyItems?.add(item)
        items = copyItems?.let { ArrayList(it) }
        notifyDataSetChanged()
    }

    //Metodo para eliminar contacto
    fun deleteItem(index: Int){
        copyItems?.removeAt(index)
        items = copyItems?.let { ArrayList(it) }
        notifyDataSetChanged()
    }

    //Metodo para actualizar contacto
    fun updateItem(index: Int, newItem: Contact){
        copyItems?.set(index, newItem)
        items = copyItems?.let { ArrayList(it) }
        notifyDataSetChanged()
    }


    //Metodo para filtrar contacto
    fun filtrar(str: String){
        items?.clear()
        if (str.isEmpty()){
            items = ArrayList(copyItems)
            notifyDataSetChanged()
            return
        }
        var search = str
        search = search.lowercase(Locale.getDefault())

        for (item in copyItems!!){
            val name = item.name.lowercase(Locale.getDefault())

            if (name.contains(search)){
                items?.add(item)
            }
        }
        notifyDataSetChanged()

    }

    private class ViewHolder(view: View) {
        val name: TextView = view.findViewById(R.id.tvName)
        val photo: ImageView = view.findViewById(R.id.imgPhoto)
        val company: TextView = view.findViewById(R.id.tvCompany)
    }

}