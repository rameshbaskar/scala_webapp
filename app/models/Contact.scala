package models

import play.api.db.DB

import scala.collection.immutable.SortedMap
import scala.collection.mutable

case class Contact(id: Int, firstName: String, lastName: String, emailAddress: String)

object Contact extends Database {

  def create(firstName: String, lastName: String, emailAddress: String) = {
    val sql = s"INSERT INTO contacts (first_name,last_name,email_address) VALUES ('$firstName','$lastName','$emailAddress')"
    DB.withConnection {connection =>
      connection.createStatement.execute(sql)
    }
  }

  def delete(id: Int) = {
    val sql = s"DELETE FROM contacts WHERE id = $id"
    DB.withConnection {connection =>
      connection.createStatement.execute(sql)
    }
  }

  def list(): List[Contact] = {
    var contacts = new mutable.HashMap[Int, Contact]

    val sql = "SELECT * FROM contacts"
    DB.withConnection { connection =>
      val resultSet = connection.createStatement.executeQuery(sql)
      while (resultSet.next()) {
        val id = resultSet.getInt("id")
        val firstName = resultSet.getString("first_name")
        val lastName = resultSet.getString("last_name")
        val emailAddress = resultSet.getString("email_address")
        contacts += id -> Contact(id, firstName, lastName, emailAddress)
      }
    }

    SortedMap(contacts.toSeq:_*).values.toList
  }
}
