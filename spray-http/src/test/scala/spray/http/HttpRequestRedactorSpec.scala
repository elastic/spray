package spray.http

import org.specs2.mutable.Specification

class HttpRequestRedactorSpec extends Specification {

  "HttpRequestRedactor" should {
    "redact sensitive information" in {

      HttpRequestRedactor.add(SampleHttpRedactor)

      val r = HttpRequest(headers = List(
        HttpHeaders.RawHeader("foo", "bar"),
        HttpHeaders.RawHeader("x-something", "x-value")))

      val toStr = r.toString

      toStr must contain("foo")
      toStr must contain("bar")
      toStr.contains("x-something") mustEqual false
    }
  }
}

object SampleHttpRedactor extends HttpRequestRedactor {
  override def redact(req: HttpRequest): HttpRequest = {
    req.copy(headers = req.headers.filterNot(h ⇒ h.name.startsWith("x-")))
  }
}
