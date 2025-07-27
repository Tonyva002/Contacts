package com.pangea.contacts.models

class Contact(name: String, lastname: String, company: String, age: Int, email: String, phone: String, weight: Double, address: String, photo: Int) {
    var name: String = ""
    var lastname: String = ""
    var company: String = ""
    var age: Int = 0
    var email: String = ""
    var phone: String = ""
    var weight: Double = 0.0
    var address: String = ""
    var photo: Int = 0


    init {
        this.name = name
        this.lastname = lastname
        this.company = company
        this.age = age
        this.email = email
        this.phone = phone
        this.weight = weight
        this.address = address
        this.photo = photo
    }
}

