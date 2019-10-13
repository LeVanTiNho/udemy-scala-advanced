package exercises

import lectures.part4implicits.TypeClasses.User

/**
  * Created by Daniel.
  */
object EqualityPlayground extends App {

  /**
    * Equality
    */

  /*
  Exercise: implement the type class pattern for the Equality tc.
  */
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  // Equal type class instances
  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  val john = User("John", 32, "john@rockthejvm.com")
  val anotherJohn = User("John", 45, "anotherJohn@rtjvm.com")
  println(Equal(john, anotherJohn))

  // AD-HOC polymorphism

  /*
    Exercise - improve the Equal type class with an implicit conversion class
    ===(anotherValue: T)
    !==(anotherValue: T)
   */
  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, other)
    def !==(other: T)(implicit equalizer: Equal[T]): Boolean = ! equalizer.apply(value, other)
  }

  println(john === anotherJohn)
  /*
  The compiler do the steps for us:
    john.===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)(NameEquality)
   */

  /*
    TYPE SAFE
   */
  println(john == 43)
  //  println(john === 43) // The compiler complaints -> TYPE SAFE
}
