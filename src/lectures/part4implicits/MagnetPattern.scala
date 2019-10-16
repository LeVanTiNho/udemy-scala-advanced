package lectures.part4implicits

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

// Lesson 7

object MagnetPattern extends App {

  /**
    * What is Magnet Pattern?
    * Magnet pattern is a use case of type class pattern, which solves problems created by method overloading
    */

  // method overloading

  class P2PRequest
  class P2PResponse
  class Serializer[T]


  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    def receive[T : Serializer](message: T): Int
    def receive[T : Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
    // def receive(future: Future[P2PResponse]): Int
    // lots of overloads
  }

  /*
    1 - type erasure - The generic types are erased at compile time

    2 - lifting doesn't work for all overloads

      val receiveFV = receive _ // ?!

    3 - code duplication
    4 - type inference and default args

      actor.receive(?!)
   */

  trait MessageMagnet[ResultType] {
    def apply(): ResultType
  }

  // The center of gravity
  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling a P2PRequest
      println("Handling P2P request")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling a P2PResponse
      println("Handling P2P response")
      24
    }
  }

  /*
  What the compiler does here?
  - The the compiler notices that the P2PRequest isn't a MessageMagnet
  - But it doesn't give up, it searches for the suitable implicit conversion, which must extend MessageMagnet
   */
  receive(new P2PRequest)
  receive(new P2PResponse)

  // 1 - no more type erasure problems!
  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply(): Int = 3
  }

  receive(Future(new P2PRequest)) // receive(new FromResponseFuture(new Future[P2PRequest]))
  receive(Future(new P2PResponse)) // receive(new FromRequestFuture(new Future[P2PRequest]))

  // 2 - lifting works
  trait MathLib {
    def add1(x: Int): Int = x + 1
    def add1(s: String): Int = s.toInt + 1
    // add1 overloads
  }

  // "magnetize"
  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(s: String) extends AddMagnet {
    override def apply(): Int = s.toInt + 1
  }

  // Notice: The add1 method must have the concrete return type
  val addFV = add1 _ // add1 is lifted into Function1[para: AddMagnet, return: Int]
  println(addFV(1))
  println(addFV("3"))

  //  val receiveFV = receive _
  //  receiveFV(new P2PResponse)

  /*
    Drawbacks
    1 - verbose
    2 - harder to read
    3 - you can't name or place default arguments
    4 - call by name doesn't work correctly
    (exercise: prove it!) (hint: side effects)
   */

  class Handler {

    // Recall: call-by-name parameter can receive a executable thing, like expression, function
    def handle(s: => String) = {
      println(s)
      println(s)
    }

    def handle(intNumber: Int) = {
      println(intNumber)
    }
    // other overloads
  }

  trait HandleMagnet  {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet) = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello, Scala")
    "hahaha"
  }

  // handle(sideEffectMethod()) // execute normally

  handle {
    println("Hello, Scala")
    "magnet"
  }
  // careful!
}
