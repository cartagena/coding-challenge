import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      val boum = route(FakeRequest(GET, "/boum")).get

      status(boum) must equalTo(NOT_FOUND)
    }

    "render the a challenge" in new WithApplication{
      val home = route(FakeRequest(GET, "/challenge")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Coding challenge:")
    }

  }
}
