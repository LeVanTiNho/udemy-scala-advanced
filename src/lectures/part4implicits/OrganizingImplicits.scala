package lectures.part4implicits

// Lesson 2
object OrganizingImplicits extends App {


  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  // implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  // implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)

  println(List(1,4,5,3,2).sorted)

  // scala.Predef is automatically imported to code

  /*
    Implicits (used as implicit parameters):
      - val/var
      - object
      - accessor methods = defs with no parentheses
   */

  // Exercise
  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

  object Person {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
    // Ordering.fromLessThan(a, b) means if a < b return true
  }

  // Bad design: Putting the implicit argument in local scope
  // implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  println(persons.sorted)

  /**
    Implicit scope with priority
    - normal scope = LOCAL SCOPE
    - imported scope
    - companions of all types involved in the method signature

    vd: def sorted[B >: A](implicit ord: Ordering[B]): List[B]
      - List
      - Ordering
      - all the types involved = A or any supertype
    */

  /**
    * Best practise:
    * Implicits for implicit paras should be put in different objects, when needing, we im
    */

  object AlphabeticNameOrdering {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  }

  import AgeOrdering._
  println(persons.sorted)

  /*
    Exercise.
    - totalPrice = most used (50%)
    - by unit count = 25%
    - by unit price = 25%
   */

  case class Purchase(nUnits: Int, unitPrice: Double)
  object Purchase {
    // The most used implicit will be putted in the companion, the others will putted in another object, when need we import them
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a,b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }
}
