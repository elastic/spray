package spray.http

/** Removes sensitive data from a request prior to outputting it e.g. in logs */
trait HttpRequestRedactor {
  def redact(req: HttpRequest): HttpRequest
}

/**
  * A global set of request redactors. This mutable set allows every component of an application to register
  * redactors that filter out what's relevant to them.
  */
object HttpRequestRedactor {
  private var redactors = Set.empty[HttpRequestRedactor]

  def add(r: HttpRequestRedactor): Unit = this.synchronized {
    redactors += r
  }

  def redact(request: HttpRequest): HttpRequest = {
    redactors.foldLeft(request)((req, redactor) ⇒ redactor.redact(req))
  }

  /** Clears the redactors that have been registered. Use with care, meant to be used in tests. */
  def clear(): Unit = this.synchronized {
    redactors = Set.empty
  }
}
