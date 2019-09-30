package lectures.part4implicits

object ImplicitsIntro extends App {

  // "Daniel" string implicitly is converted to ArrowAssoc object
  // ArrowAssoc is a wrapper of the generic type A, is this case is String
  val pair = "Daniel" -> "555"
  val intPair = 1 -> 2

  case class Person(name: String) {
    def greet = s"Hi, my name is $name!"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("Peter".greet)
  // Firstly, the compiler looks for the implicit method for convert a String to some type, in this case is Person
  // after that, the compiler will know the methods of Person can be applied to the String
  // println(fromStringToPerson("Peter").greet)

//  class A {
//    def greet: Int = 2
//  }
//  implicit def fromStringToA(str: String): A = new A

  // implicit parameters
  def increment(x: Int)(implicit amount: Int) = x + amount
  implicit val defaultAmount = 10

  increment(2)
  // NOT default args

}
