package playground

import scala.collection.mutable

/**
  * Created by Daniel.
  */
object ScalaPlayground extends App {
  println("Hello, Scala")
}

object Test extends App {
  val aMutableHashMap = mutable.HashMap[Int, Int]()
  println(aMutableHashMap)
  def addToMutalbeHashMap(inputMutableHashMap: mutable.HashMap[Int, Int]): Unit = {
    inputMutableHashMap(1) = 1
    //println(inputMutableHashMap.hashCode())
  }
  addToMutalbeHashMap(aMutableHashMap)
  println(aMutableHashMap)
}