package com.chariotsolutions

import akka.actor._

trait Player
case object X extends Player
case object O extends Player


trait GameState
case object PlayerXTurn extends GameState
case object PlayerOTurn extends GameState
case object GameOver extends GameState

object Game {
  case class PlayerXMove(squareId: Int)
  case class PlayerOMove(squareId: Int)
  case object InvalidMove
}

class Game extends Actor with FSM[GameState, Map[Int, Option[Player]]] {
  import Game._

  startWith(PlayerXTurn, Map.empty ++ (0 to 8 map(i => i -> None)))

  when(PlayerXTurn) {
    case Event(PlayerXMove(squareId), gameBoard) if gameBoard(squareId) == None =>
      val newBoard = gameBoard + (squareId -> Some(X))
      winner(newBoard) match {
        case None => goto(PlayerOTurn) using(newBoard) replying(newBoard)
        case _ => goto(GameOver) using(newBoard) replying(newBoard)
      }
    case _ =>
      stay() replying InvalidMove
    }
   when(PlayerOTurn) {
    case Event(PlayerOMove(squareId), gameBoard) if gameBoard(squareId) == None =>
      val newBoard = gameBoard + (squareId -> Some(O))
      winner(newBoard) match {
        case None => goto(PlayerXTurn) using(newBoard) replying(newBoard)
        case _ => goto(GameOver) using(newBoard) replying(newBoard)
      }
    case _ =>
      stay() replying InvalidMove
    }
   when(GameOver) {
     case Event(_, gameBoard) => stay replying(gameBoard)
   }

   def winner(gameBoard: Map[Int, Option[Player]]): Option[Player] = {
      Set(
        List(gameBoard(0), gameBoard(1), gameBoard(2)),
        List(gameBoard(3), gameBoard(4), gameBoard(5)),
        List(gameBoard(6), gameBoard(7), gameBoard(8)),
        List(gameBoard(0), gameBoard(3), gameBoard(6)),
        List(gameBoard(1), gameBoard(4), gameBoard(7)),
        List(gameBoard(2), gameBoard(5), gameBoard(8)),
        List(gameBoard(0), gameBoard(4), gameBoard(8)),
        List(gameBoard(2), gameBoard(4), gameBoard(6))).collect {
          case Some(X) :: Some(X) :: Some(X) :: Nil => X
          case Some(O) :: Some(O) :: Some(O) :: Nil => O
        }.headOption
    }
 }

// vim: set ts=2 sw=2 et sts=2:
