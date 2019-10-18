package lectures.part5ts

// Lesson 1

/**
  * In this lesson, we learn about some advanced points in Scala Inheritance
  */
object RockingInheritance extends App {

  /**
    * convenience
    */
  trait Writer[T] {
    def write(value: T): Unit
  }
  trait Closeable {
    def close(status: Int): Unit
  }
  trait GenericStream[T] {
    // some methods
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  /**
    * diamond problem
    */
  trait Animal { def name: String }
  trait Lion extends Animal { override def name: String = "lion" }
  trait Tiger extends Animal { override def name: String = "tiger" }
  class Mutant extends Lion with Tiger

  val m = new Mutant
  println(m.name)

  /*
    Mutant
    extends Animal with { override def name: String = "lion" }
    with { override def name: String = "tiger" }

    LAST OVERRIDE GETS PICKED
   */

  /**
    * type linearization and super keyword problem
    */

  trait Cold {
    def print = println("cold")
  }

  trait Green extends Cold {
    override def print: Unit = {
      println("green")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print: Unit = {
      println("blue")
      super.print
    }
  }

  class Red {
    def print = println("red")
  }

  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("white")
      super.print
    }
  }

  val color = new White
  color.print

  /*
  Explain:
  - The meaning of "with" -> "overridden by"

  - Cold = AnyRef with Cold
  - Green = AnyRef with Cold with Green
  - Blue = AnyRef with Cold with Blue
  - Red = AnyRef with Red

  - White = AnyRef with Red with AnyRef with Cold with Green with AnyRef with Cold with Blue with White

  - Rule:
    + AnyRef with Red -> remaining, because Red is subtype of AnyRef
    + Red with AnyRef -> throw away AnyRef (the compiler does that)
    + Red with Cold -> remaining, because Red and Cold isn't related

  - White = AnyRef with Red with Cold with Green with Blue with White

  - super is Blue -> Green -> Cold
  - The print method of Red and the print method of Red isn't related
   */
}
