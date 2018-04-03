package spray.http

/** Removes sensitive data from a request prior to outputting it e.g. in logs */
trait HttpRequestRedactor {
  def redact(req: HttpRequest): HttpRequest
}

object HttpRequestRedactor {
  private var redactors = Set.empty[HttpRequestRedactor]

  def add(r: HttpRequestRedactor): Unit = this.synchronized {
    redactors += r
  }

  def redact(request: HttpRequest): HttpRequest = {
    redactors.foldLeft(request)((req, redactor) ⇒ redactor.redact(req))
  }
}
