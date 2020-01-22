package lectures.part4implicits

import java.{util => ju}

// Lesson 8

object ScalaJavaConversions extends App {

  // JavaConverters has converters to convert collections in Java to Scala and vice versa
  import collection.JavaConverters._

  val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
  (1 to 5).foreach(javaSet.add)
  println(javaSet)

  val scalaSet = javaSet.asScala

  /*
    Iterator
    Iterable
    ju.List - collection.mutable.Buffer
    ju.Set - collection.mutable.Set
    ju.Map - collection.mutable.Map
   */

  import collection.mutable._
  // ArrayBuffer is a Buffer
  val scalaBuffer = ArrayBuffer[Int](1, 2, 3)
  val javaList = scalaBuffer.asJava

  println(javaList.asScala eq scalaBuffer) // eq method is used to compare references of two instances

  val scalaIterable = List(1,2,3) // immutable.List
  val javaIterable = scalaIterable.asJava
  val backToScala = javaIterable.asScala
  println(backToScala eq scalaIterable) // false
  println(backToScala == scalaIterable) // == operator is used to compare values of two instances
  // The series of conversions from scala to java back to scala may not remain the same object

  // scalaIterable is a immutable collection, when it's converted to javaIterable is still immutable
  // javaIterable.add(7) will throw exception

  /*
    Exercise
    create a Scala-Java Optional-Option
      .asScala
   */

  class ToScala[T](value: => T) {
    def asScala: T = value
  }

  implicit def asScalaOptional[T](o: ju.Optional[T]): ToScala[Option[T]] = new ToScala[Option[T]](
    if (o.isPresent) Some(o.get) else None
  )

  val javaOptional: ju.Optional[Int] = ju.Optional.empty()
  val scalaOption = javaOptional.asScala
  println(scalaOption)
}
