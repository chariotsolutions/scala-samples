package com.chariotsolutions

import akka.actor.ActorSystem
import akka.http._
import akka.http.marshalling._
import akka.stream._
import akka.http.model._
import akka.http.server._
import akka.http.server.Directives._
import akka.stream.scaladsl.Flow
import com.typesafe.config.{ ConfigFactory, Config }

trait SampleApp {
  implicit val system = ActorSystem("akka-http-sample")
  sys.addShutdownHook({ system.shutdown() })

  implicit val materializer = ActorFlowMaterializer()

  import system.dispatcher

  val route =
    path("") {
      getFromResource("web/index.html");
    }

    val serverBinding = Http(system).bind(interface = "localhost", port = 8080)

    serverBinding.startHandlingWith(route)
}

object Main extends App with SampleApp {

}
