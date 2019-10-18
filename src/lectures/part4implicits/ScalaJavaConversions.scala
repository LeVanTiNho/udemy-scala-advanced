package lectures.part4implicits

import java.{util => ju}

// Lesson 8

object ScalaJavaConversions extends App {

  // JavaConverters has converters to convert collections in Java to Scala and vice versa
  import collection.JavaConverters._

  val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
  (1 to 5).foreach(javaSet.add)
  println(javaSet)

  /*
  The steps Scala compiler does:
  1. The compiler notices that the Java Set doesn't has asScala method
  2. The compiler will search for an implicit wrapper class of a Java Set has the asScala method, but it won't find out
  3. The Compiler will search for an implicit conversion method of a Java Set, it will find out the asScalaConverter method,
  it is aware that method converts a Java Set to an AsScala, and the AsScala wrapper class has asScala method
   */
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

  println(javaList.asScala eq scalaBuffer)


  val scalaIterable = List(1,2,3) // immutable.List
  val javaIterable = scalaIterable.asJava
  val backToScala = javaIterable.asScala
  println(backToScala eq scalaIterable) // false
  println(backToScala == scalaIterable) // true
  // The series of conversions from scala to java back to scala may not remain the same object

  // scalaIterable is a immutable collection, when it's converted to javaIterable is still immutable
  // javaIterable.add(7)

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
