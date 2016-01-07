package models

import scala.collection.immutable.SortedMap
import scala.collection.mutable

case class Contact(id: Int, firstName: String, lastName: String, emailAddress: String)

object Contact extends Database {

  def create(firstName: String, lastName: String, emailAddress: String) = {
    var sql = "INSERT INTO contacts (first_name, last_name, email_address) VALUES ('"
    sql += firstName + "', '"
    sql += lastName + "', '"
    sql += emailAddress + "')"
    write(sql)
  }

  def delete(id: Int) = {
    val sql = "DELETE FROM contacts WHERE id = " + id
    write(sql)
  }

  def list(): List[Contact] = {
    var contacts = new mutable.HashMap[Int, Contact]

    val (resultSet, statement, connection) = read("SELECT * FROM contacts")
    while (resultSet.next()) {
      val id = resultSet.getInt("id")
      val firstName = resultSet.getString("first_name")
      val lastName = resultSet.getString("last_name")
      val emailAddress = resultSet.getString("email_address")
      contacts += id -> Contact(id, firstName, lastName, emailAddress)
    }
    close(statement, connection)

    SortedMap(contacts.toSeq:_*).values.toList
  }
}
