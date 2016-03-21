package controllers


import java.util.concurrent.TimeoutException

import javax.inject.Inject

import reactivemongo.api.QueryOpts

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

import play.api.Logger
import play.api.i18n.MessagesApi
import play.api.mvc.{ Action, Controller }
import play.api.data.Form
import play.api.data.Forms.{ date, ignored, mapping, nonEmptyText }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json, Json.toJsFieldJsValueWrapper

import play.modules.reactivemongo.{
MongoController, ReactiveMongoApi, ReactiveMongoComponents
}
import play.modules.reactivemongo.json._, collection.JSONCollection

import reactivemongo.bson.BSONObjectID

import models.{ Challenge, JsonFormats, DataPage }, JsonFormats.challengeFormat
import views.html

class ChallengeController @Inject()(
  val messagesApi: MessagesApi,
  val reactiveMongoApi: ReactiveMongoApi) extends Controller with MongoController with ReactiveMongoComponents {

  val challengeForm = Form(
    mapping(
      "id" -> ignored(BSONObjectID.generate: BSONObjectID),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText)
    (Challenge.apply)(Challenge.unapply))

  def collection: JSONCollection = db.collection[JSONCollection]("challenges")

  val dataPageSize = 10;
  val List = Redirect(routes.ChallengeController.list())

  def list(page: Int, orderBy: Int, filter: String) = Action.async { implicit request =>
    val futurePage: Future[List[Challenge]] = getData(filter)

    futurePage.map({ challenges =>
      implicit val msg = messagesApi.preferred(request)

      val offset = dataPageSize * page;
      val total = challenges.length
      val challengesPage = challenges.slice(offset, offset + dataPageSize)

      Ok(html.challenge.list(DataPage(challengesPage, page, offset, total), orderBy, filter))
    }).recover {
      case t: TimeoutException =>
        Logger.error("Problem found in employee list process")
        InternalServerError(t.getMessage)
    }
  }

  def create = Action { request =>
    implicit val msg = messagesApi.preferred(request)
    Ok(html.challenge.create(challengeForm))
  }

  def save = Action.async { implicit request =>
    challengeForm.bindFromRequest.fold(
      { formWithErrors =>
        implicit val msg = messagesApi.preferred(request)
        Future.successful(BadRequest(html.challenge.create(formWithErrors)))
      },
      employee => {
        val futureUpdateEmp = collection.insert(employee.copy(_id = BSONObjectID.generate))
        futureUpdateEmp.map { result =>
          List.flashing("success" -> s"Employee ${employee.name} has been created")
        }.recover {
          case t: TimeoutException =>
            Logger.error("Problem found in employee update process")
            InternalServerError(t.getMessage)
        }
      })
  }

  def getData(filter: String): Future[List[Challenge]] = {
    if (filter.length > 0)
      collection
        .find(Json.obj("name" -> filter))
        .cursor[Challenge]()
        .collect[List]()
    else
      collection
        .genericQueryBuilder
        .cursor[Challenge]()
        .collect[List]()
  }
}