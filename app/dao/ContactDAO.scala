package dao

import core.AddressBookStorage
import models.Contact

import scala.collection.mutable

object ContactDAO extends AddressBookStorage {

  def create(contact: Contact) = {
    val sql = s"INSERT INTO contacts (first_name,last_name,email_address) " +
      s"VALUES ('${contact.firstName}','${contact.lastName}','${contact.emailAddress}')"
    write(sql)
  }

  def all(): List[Contact] = {
    val sql = "SELECT * FROM contacts"
    val records = read(sql)
    mapContacts(records)
  }

  def findById(id: Int): Contact = {
    val sql = s"SELECT * FROM contacts WHERE id = $id"
    val records = read(sql)
    mapContacts(records).head
  }

  def update(oldContact: Contact, newContact: Contact) = {
    val sql = s"UPDATE contacts SET first_name='${newContact.firstName}'," +
      s"last_name='${newContact.lastName}',email_address='${newContact.emailAddress}' " +
      s"where id=${oldContact.id}"
    write(sql)
  }

  def delete(id: Int) = {
    val sql = s"DELETE FROM contacts WHERE id = $id"
    write(sql)
  }

  private def mapContacts(records: List[mutable.Map[String, Object]]): List[Contact] = {
    var contacts: Set[Contact] = Set()
    for(record <- records) {
      contacts += new Contact {
        id = record("id").asInstanceOf[Int]
        firstName = record("first_name").asInstanceOf[String]
        lastName = record("last_name").asInstanceOf[String]
        emailAddress = record("email_address").asInstanceOf[String]
      }
    }
    contacts.toList
  }
}
