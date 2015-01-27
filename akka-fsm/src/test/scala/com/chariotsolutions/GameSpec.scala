package com.chariotsolutions

import akka.actor._
import akka.pattern.ask
import akka.testkit._
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import scala.concurrent.duration._
import scala.util._
import Game._

class GameSpec extends TestKit(ActorSystem("game-spec-system")) with FlatSpecLike with Matchers with ScalaFutures {
  implicit val timeout = akka.util.Timeout(5000)
  val EmptyBoard = Map.empty[Int, Option[Player]] ++ (0 to 8).map(i => i -> None)

  "GameActor" should "allow X player to move first in new game" in {
    val fsm = TestFSMRef(new Game)
    fsm.stateName shouldBe PlayerXTurn
    fsm.stateData shouldBe EmptyBoard
    fsm ! PlayerXMove(4)
    fsm.stateData shouldBe EmptyBoard + (4 -> Some(X))
  }
  "GameActor" should "not allow O player to move first in new game" in {
    val fsm = TestFSMRef(new Game)
    fsm ! PlayerOMove(4)
    fsm.stateData shouldBe EmptyBoard
  }
  "GameActor" should "allow O player to move after X player" in {
    val fsm = TestFSMRef(new Game)
    fsm ! PlayerXMove(4)
    fsm ! PlayerOMove(0)
    fsm.stateName shouldBe PlayerXTurn
    fsm.stateData shouldBe EmptyBoard ++ Map(0 -> Some(O), 4 -> Some(X))
  }
  "GameActor" should "reject O player moving to same square as X player" in {
    val fsm = TestFSMRef(new Game)
    fsm ! PlayerXMove(4)
    val moveFuture = fsm ? PlayerOMove(4)
    moveFuture.value shouldBe Some(Success(InvalidMove))
    fsm.stateName shouldBe PlayerOTurn
    fsm.stateData shouldBe EmptyBoard ++ Map(4 -> Some(X))
  }
  "GameActor" should "recognize that a player won" in {
    val fsm = TestFSMRef(new Game)
    fsm ! PlayerXMove(4)
    fsm ! PlayerOMove(0)
    fsm ! PlayerXMove(1)
    fsm ! PlayerOMove(3)
    fsm ! PlayerXMove(7)
    fsm.stateName shouldBe GameOver
    fsm.stateData shouldBe EmptyBoard ++ Map(4 -> Some(X), 0 -> Some(O), 1 -> Some(X),
      3 -> Some(O), 7 -> Some(X))
  }
}

// vim: set ts=2 sw=2 et sts=2:
