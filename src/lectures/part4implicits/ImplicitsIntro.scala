package lectures.part4implicits

// Lesson 1
object ImplicitsIntro extends App {

  /**
    * implicit class
    */

  // "Daniel" string implicitly is converted to ArrowAssoc object
  // ArrowAssoc is a wrapper of the generic type A, is this case is String
  // First, "Daniel" is converted to ArrowAssoc instance, then the -> method will be called
  val pair = "Daniel" -> "555"
  val intPair = 1 -> 2

  case class Person(name: String) {
    def greet = s"Hi, my name is $name!"
  }

  /**
    * conversion method - implicit method
    */
  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("Peter".greet) // println(fromStringToPerson("Peter").greet)

//  class A {
//    def greet: Int = 2
//  }
//  implicit def fromStringToA(str: String): A = new A

  /**
    * implicit parameters
    */
  def increment(x: Int)(implicit amount: Int) = x + amount
  implicit val defaultAmount = 10

  increment(2)
  // differs to default args
}
