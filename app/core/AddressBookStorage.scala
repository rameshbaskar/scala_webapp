package core

import java.sql.ResultSet

import play.api.db.DB
import play.api.Play.current

import scala.collection.mutable

class AddressBookStorage {

  def write(sql: String) = {
    DB.withConnection {connection =>
      connection.createStatement.execute(sql)
    }
  }

  def read(sql: String): List[mutable.Map[String, Object]] = {
    var queryResult: Set[mutable.Map[String, Object]] = Set()

    DB.withConnection {connection =>
      val statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      val resultSet = statement.executeQuery(sql)
      val metaData = resultSet.getMetaData
      val columnCount = metaData.getColumnCount
      while(resultSet.next()) {
        var row: mutable.Map[String, Object] = mutable.Map()
        for (index <- 1 to columnCount) {
          row += (metaData.getColumnName(index) -> resultSet.getObject(index))
        }
        queryResult += row
      }
    }

    queryResult.toList
  }
}
