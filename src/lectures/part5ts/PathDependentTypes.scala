package lectures.part5ts

// Lesson 4

/**
  * Inner classes, objects, type definitions
  */

object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)

    def printGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod: Int = {
    /*
    Inner classes, objects can be defined in expression and class body
    Inner abstract types be only defined in class body, inner alias types can be used in expression
     */
    class HelperClass
    object HelperObject
    type HelperType = String
    2
  }

  // Because Scala discards class-level members, so a particular inner class or object is specific to a particular instance
  // per-instance
  val outerInstance1 = new Outer
  val inner1 = new outerInstance1.Inner // o.Inner is a TYPE

  val outerInstance2 = new Outer
  val inner2 = new outerInstance2.Inner
  // val otherInner: outerInstance2.Inner = inner -> illegal!

  outerInstance1.print(inner1)
  // outerInstance2.print(inner1)

  // path-dependent types

  // outerInstance1.Inner, outerInstance2.Inner is subtype Outer#Inner, so we can
  outerInstance1.printGeneral(inner1)
  outerInstance2.printGeneral(inner1)

  /*
    Exercise
    DB keyed by Int or String, but maybe others
   */

  /*
    use path-dependent types
    abstract type members and/or type aliases
   */

  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }

  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  get[IntItem](42) // ok
  get[StringItem]("home") // ok

  // get[IntItem]("scala") // not ok

}
