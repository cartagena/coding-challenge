package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def challenge = Action {
    Ok(views.html.challenge("Fizz Buzz", "Write a program that prints the numbers from 1 to 100. " +
      "For multiples of three print “Fizz” instead of the number and for the multiples of five print “Buzz”. " +
      "For numbers which are multiples of both three and five print “FizzBuzz”."))
  }

}