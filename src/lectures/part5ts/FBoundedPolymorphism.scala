package lectures.part5ts

// Lesson 7

object FBoundedPolymorphism extends App {

//  trait Animal {
//    def breed: List[Animal]
//  }
//
//  class Cat extends Animal {
//    override def breed: List[Animal] = ??? // List[Cat] !!
//  }
//
//  class Dog extends Animal {
//    override def breed: List[Animal] = ??? // List[Dog] !!
//  }

  /**
    * Solution 1 - naive
    */

  trait Animal {
    def breed: List[Animal]
  }

  class Cat extends Animal {
    override def breed: List[Cat] = ??? // Ok, because List[+A]
  }

  class Dog extends Animal {
    override def breed: List[Dog] = ??? // Ok, because List[+A]
    // override def breed: List[Cat] -> The compiler say Ok, because List[+A]
    // the compiler will not force the programmer define return type is List[Dog] -> That's the problem
  }

  /**
    * The problem is we need some mechanism that the compiler force us to define return type is List[Dog]
    */

  /**
    * Solution 2 - recursive type: F-Bounded Polymorphism
    */

//  trait Animal[A <: Animal[A]] { // recursive type: F-Bounded Polymorphism
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Animal[Cat]] = ??? // List[Cat] !!
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ??? // List[Dog] !!
//  }
//
//  trait Entity[E <: Entity[E]] // ORM
//
//  class Person extends Comparable[Person] { // FBP
//    override def compareTo(o: Person): Int = ???
//  }
//
//  class Crocodile extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ??? // List[Dog] !!
//  } // -> That's the problem

  /**
    * Solution 3 - F-bounded Polymorphism + self-types
    */

//  trait Animal[A <: Animal[A]] {
//    self: A =>
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Animal[Cat]] = ??? // List[Cat] !!
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ??? // List[Dog] !!
//  }
//
////  class Crocodile extends Animal[Dog] {
////    override def breed: List[Animal[Dog]] = ??? // List[Dog] !!
////  }
//
//  trait Fish extends Animal[Fish]
//  class Shark extends Fish {
//    override def breed: List[Animal[Fish]] = List(new Cod) // wrong
//  }
//
//  class Cod extends Fish {
//    override def breed: List[Animal[Fish]] = ??? // wrong
//  }

  // Exercise

  /**
    * Solution 4 type classes!
    */

//  trait Animal
//
//  // b1: Type class
//  trait CanBreed[A] {
//    def breed(a: A): List[A]
//  }
//
//  class Dog extends Animal
//  object Dog {
//    // b2: Type class object
//    implicit object DogsCanBreed extends CanBreed[Dog] {
//      def breed(a: Dog): List[Dog] = List[Dog]()
//    }
//  }
//
//  implicit class CanBreedOps[A <: Animal](animal: A) {
//    def breed(implicit canBreed: CanBreed[A]): List[A] =
//      canBreed.breed(animal)
//  }
//
//  val dog = new Dog
//  dog.breed // List[Dog]!!
//  /*
//    new CanBreedOps[Dog](dog).breed(Dog.DogsCanBreed)
//  implicit value to pass to breed: Dog.DogsCanBreed
//  */
//
//  class Cat extends Animal
//  object Cat {
//    implicit object CatsCanBreed extends CanBreed[Dog] {
//      def breed(a: Dog): List[Dog] = List()
//    }
//  }
//
//  // val cat = new Cat
//  // cat.breed -> The compiler shouts at us!!!

  /**
    * Solution #5 -> What the fuck? The solution below just trolls us!!!
    */

//  trait Animal[A] { // pure type classes
//    def breed(a: A): List[A]
//  }
//
//  class Dog
//  object Dog {
//    implicit object DogAnimal extends Animal[Dog] {
//      override def breed(a: Dog): List[Dog] = List()
//    }
//  }
//
//  class Cat
//  object Cat {
//    implicit object CatAnimal extends Animal[Dog] {
//      override def breed(a: Dog): List[Dog] = List()
//    }
//  }
//
//  implicit class AnimalOps[A](animal: A) {
//    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
//      animalTypeClassInstance.breed(animal)
//  }
//
//  val dog = new Dog
//  dog.breed
//
//  val cat = new Cat
//  cat.breed
}
