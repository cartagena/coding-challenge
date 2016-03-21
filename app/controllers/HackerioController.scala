package controllers

import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

class HackerioController @Inject()(
  val messagesApi : MessagesApi)
    extends Controller with I18nSupport {

  /**
   * Handle default path requests, redirect to employee list
   */
  def index = Action { Redirect(routes.ChallengeController.list()) }

}
