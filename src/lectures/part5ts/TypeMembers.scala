package lectures.part5ts

// Lesson 3
object TypeMembers extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    // We can use type member in define variables, values, method signatures

    // abstract type member
    // Abstract type member is usually used for The compiler to do type inference
    type AnimalType
    type UpperBoundedAnimal <: Animal // Upper bounded
    type SuperBoundedAnimal >: Dog // supper bounded
    type MultiBoundedAnimal >: Dog <: Animal

    // Concrete type member
    type AnimalC = Cat  // Alias type member
  }

  val ac = new AnimalCollection

  // The following declarations is illegal
  // val dog: ac.AnimalType = ???
  // val dog: ac.AnimalType = new Dog
  // val cat: ac.UpperBoundedAnimal = new Cat
  // val cat: ac.UpperBoundedAnimal = new Animal

  // The following declarations is legal
  val pup: ac.SuperBoundedAnimal = new Dog
  val cat: ac.AnimalC = new Cat


  // Type aliases is often used when we have name collisions when a lot of package imported
  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat


  /**
    * Abstract type members is sometimes used in API, that's similar to generics
    */
  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList extends MyList {
    // We override type T
    override type T = Int
    def add(element: T): MyList = ???
  }

  // .type to get the type of a value, and then we can assign it to a type member
  type CatsType = cat.type
  // val newCat: CatsType = cat
  // new CatsType -> Illegal! Because, The compiler don't know the the constructor to be used in this case like any other type members


  /**
    * Exercise - enforce a type to be applicable to SOME TYPES only
    */

  // MyList trait in some library, we want to enforce A is applicable to Number only
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

  // type member A in ApplicableToNumbers trait is more specific than A in MList -> override
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
