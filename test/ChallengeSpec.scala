import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json.JsValue

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class ChallengeSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      val boum = route(FakeRequest(GET, "/boum")).get

      status(boum) must equalTo(NOT_FOUND)
    }

    "render the a challenge" in new WithApplication {
      val challengePage = route(FakeRequest(GET, "/challenge")).get

      status(challengePage) must equalTo(OK)
      contentType(challengePage) must beSome.which(_ == "text/html")
      contentAsString(challengePage) must contain ("Coding challenge:")
    }

    "render a space for upload solution" in new WithApplication() {
      val challengePage = route(FakeRequest(GET, "/challenge")).get

      status(challengePage) must equalTo(OK)
      contentType(challengePage) must beSome.which(_ == "text/html")
      contentAsString(challengePage) must contain("Submit your solution")

    }

    "render a successful result when solution is right" in new WithApplication() {
      val challengePage = route(FakeRequest(POST, "/challenge").withFormUrlEncodedBody("Your code" -> "test")).get

      status(challengePage) must equalTo(OK)
      contentType(challengePage) must beSome.which(_ == "text/html")
      contentAsString(challengePage) must contain("Your solution is correct!")
    }

  }
}
