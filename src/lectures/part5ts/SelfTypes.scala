package lectures.part5ts

// Lesson 6
object SelfTypes extends App {

  // A way of requiring a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer {
    // SELF TYPE force whoever implements Singer to implement Instrumentalist
    // this is completely independent on the rest of the implementation
    // this: Instrumentalist =>
    // scala: Instrumentalist =>
    self: Instrumentalist =>


    def sing(): Unit
  }

  // Legal
  class LeadSinger extends Singer with Instrumentalist  {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  // Illegal!
  /*
    class Vocalist extends Singer {
      override def sing(): Unit = ???
    }
   */

  // With Anonymous class

//  // Illegal
//  val jamesHetfield = new Singer {
//    override def sing(): Unit = ???
//  }

  // legal
  val jamesHetfield = new Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  // Legal
  val instrumentalist = new Instrumentalist with Singer {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("(guitar solo)")
  }

  // Legal
  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  /**
    * Self-type vs inheritance
    */
  class A
  class B extends A // B IS AN A

  trait T
  trait S { self: T => } // S REQUIRES a T, T is part of S

  /**
    * CAKE PATTERN => "dependency injection"
    */

  // Dependency injection
  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  /**
    * Cake pattern
    */

  trait ScalaComponent {
    // API
    def action(x: Int): String
  }

  trait ScalaDependentComponent { self: ScalaComponent =>
    // We can use apis of ScalaComponent in ScalaDependentComponent
    def dependentAction(x: Int): String = action(x) + " this rocks!"
  }

  trait ScalaApplication { self: ScalaDependentComponent => }

  // Why calling Cake Pattern? Because we have multiple layers of abstraction

  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  // layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics

  // cyclic extension is not allowed in Scala
  // class X extends Y
  // class Y extends X

  // cyclical dependencies is allowed!
  trait X { self: Y => }
  trait Y { self: X => }
}
