package models

import java.sql.{Connection, Statement, ResultSet}
import play.api.db.DB
import play.api.Play.current

class Database {

  def getConnection = {
    DB.getConnection()
  }

  def write(sql: String) = {
    val connection = getConnection
    val statement = connection.createStatement()
    statement.execute(sql)
    close(statement, connection)
  }

  def read(sql: String): (ResultSet, Statement, Connection) = {
    val connection = getConnection
    val statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
    val resultSet = statement.executeQuery(sql)
    (resultSet, statement, connection)
  }

  def close(statement: Statement, connection: Connection) = {
    statement.closeOnCompletion()
    connection.close()
  }
}
