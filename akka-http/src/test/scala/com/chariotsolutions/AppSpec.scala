package com.chariotsolutions

import akka.stream.scaladsl.{ Sink, Source }
import org.scalatest._
import org.scalatest.concurrent._
import akka.http.Http
import akka.http.model._
import akka.stream.ActorFlowMaterializer
import akka.http.unmarshalling._

class AppSpec extends FlatSpec with Matchers with ScalaFutures with BeforeAndAfterAll {

  implicit val testSystem = akka.actor.ActorSystem("test-system")
  import testSystem.dispatcher
  implicit val fm = ActorFlowMaterializer()
  val server = new SampleApp {}

  override def afterAll = testSystem.shutdown()

  def sendRequest(req: HttpRequest) = Source.single(req).via(
    Http().outgoingConnection(host = "localhost", port = 8080).flow
    ).runWith(Sink.head)

  "The app" should "return index.html on a GET to /" in {
    whenReady(sendRequest(HttpRequest())) { response =>
      whenReady(Unmarshal(response.entity).to[String]) { str =>
        str should include("Hello World!")
      }
    }
  }
  "The app" should "return 404 on a GET to /foo" in {
    whenReady(sendRequest(HttpRequest(uri = "/foo"))) { response =>
      response.status shouldBe StatusCodes.NotFound
    }
  }
}
