package lectures.part5ts

// Lesson 3

object TypeMembers extends App {


  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    // abstract type member
    type AnimalType
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal

    // Alias type member
    type AnimalC = Cat
  }

  // Abstract type member is usually used for The compiler to do type inference
  val ac = new AnimalCollection
  // val dog: ac.AnimalType = ???
  // val dog: ac.AnimalType = new Dog -> Illegal

  // val cat: ac.BoundedAnimal = new Cat -> Illegal

  val pup: ac.SuperBoundedAnimal = new Dog // legal! Because SuperBoundedAnimal has bound >: Dog
  val cat: ac.AnimalC = new Cat


  // Type aliases is often used when we have name collisions when a lot of package imported
  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  // alternative to generics
  // Abstract type members is sometimes used in API, that's similar to generics
  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int // We assign Abstract type T explicitly by Int
    def add(element: Int): MyList = ???
  }

  // .type
  // We can use some value's type as type alias
  type CatsType = cat.type
  val newCat: CatsType = cat
  // new CatsType -> Illegal! Because, The compiler don't know the the constructor to be used in this case

  /*
    Exercise - enforce a type to be applicable to SOME TYPES only
   */
  // LOCKED
  trait MList {
    type A
    def head: A
    def tail: MList
  }

  trait ApplicableToNumbers {
    type A <: Number
  }

  // NOT OK
//  class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumbers {
//    type A = String
//    def head = hd
//    def tail = tl
//  }

  // OK
  class IntList(hd: Integer, tl: IntList) extends MList with ApplicableToNumbers {
    type A = Integer
    def head = hd
    def tail = tl
  }

  // Number
  // type members and type member constraints (bounds)

  /*
  Explanation:
    MList with ApplicableNumbers, with means "overridden by"
   */
}
