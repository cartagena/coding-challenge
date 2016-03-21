package models

import reactivemongo.bson.BSONObjectID

case class Challenge(
  _id: BSONObjectID,
  name: String,
  description: String)

case class DataPage[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

object JsonFormats {
  import play.api.libs.json.Json
  import play.modules.reactivemongo.json.BSONFormats._

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val challengeFormat = Json.format[Challenge]
}