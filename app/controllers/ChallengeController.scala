package controllers

import play.api.mvc._
import play.api.i18n._
import play.api.data.Form
import play.api.data.Forms._

import javax.inject._

class ChallengeController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val solutionForm: Form[SubmitSolutionForm] = Form {
    mapping(
      "code" -> nonEmptyText
    )(SubmitSolutionForm.apply)(SubmitSolutionForm.unapply)
  }


  def challenge = Action {
    Ok(views.html.challenge("Fizz Buzz",
      solutionForm,
      "Write a program that prints the numbers from 1 to 100. " +
      "For multiples of three print “Fizz” instead of the number and for the multiples of five print “Buzz”. " +
      "For numbers which are multiples of both three and five print “FizzBuzz”."))
  }

  def submitSolution = Action { implicit request =>
      solutionForm.bindFromRequest().fold(
      hasErrors => {
        Ok(views.html.challenge("Fizz Buzz",
          solutionForm,
          "Write a program that prints the numbers from 1 to 100. " +
            "For multiples of three print “Fizz” instead of the number and for the multiples of five print “Buzz”. " +
            "For numbers which are multiples of both three and five print “FizzBuzz”."))
      },
      solution => {
        println(solution.code)

        Ok(views.html.challenge("Fizz Buzz",
          solutionForm,
          "Write a program that prints the numbers from 1 to 100. " +
            "For multiples of three print “Fizz” instead of the number and for the multiples of five print “Buzz”. " +
            "For numbers which are multiples of both three and five print “FizzBuzz”."))
      }
    )
  }

}

case class SubmitSolutionForm(code: String)