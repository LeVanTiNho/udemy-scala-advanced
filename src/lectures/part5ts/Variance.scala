package lectures.part5ts

// Lesson 2

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  /**
    * what is variance?
    * Why we need variance?
    * "inheritance" - type substitution of generic types
    */

  class Cage[T]

  // Cat extends Animal, the question is if Cage[Cat] can extend Cage[Animal] or vice versa?

  /**
    * Covariance, contra-variance, invariance
    */

  /**
    * covariance
    */

  // Only 2 below cases is covariance:
  class CCage[+T]
  val ccage1: CCage[Animal] = new CCage[Cat]
  val ccage2: CCage[Animal] = new CCage[Animal]

  /**
    * Invariance
    */
  // Only 1 below case:
  class ICage[T]
  val icage: ICage[Cat] = new ICage[Cat]

  /**
    * Contra-variance
    */
  class XCage[-T]
  // Only 2 below cases is contra-variance
  val xcage1: XCage[Cat] = new XCage[Animal]
  val xcage2: XCage[Cat] = new XCage[Cat]

  /**
    * Variant position, Contra-variant position
    */
  class InvariantCage[T](val animal: T) // invariant

  /**
    * value class constructor parameters is in covariant position
    */

  class CovariantCage[+T](val animal: T)
  // COVARIANT POSITION, the animal para is at covariant position
  // Covariant means CovariantCage[Animal](val animal: Animal), animal can be Dog, Cat, Crocodile

  class SmallCat extends Cat
  val covariantCage1: CovariantCage[Animal] = new CovariantCage[Cat](new Cat)
  val covariantCage2: CovariantCage[Animal] = new CovariantCage[Cat](new SmallCat) // that's covariance

  /*
  class ContravariantCage[-T](val animal: T)
  // the animal para is at covariant position
  val contravariantCage: ContravariantCage[Cat] = new ContravariantCage[Animal](new Dog) // that's the problem
  */

  /**
    * Variable class constructor parameters
    */

  /*
  class CovariantVariableCage[+T](var animal: T) // types of vars are in CONTRAVARIANT POSITION
  val covariantVariableCage: CovariantVariableCage[Animal] = new CovariantVariableCage[Cat](new Cat)
  covariantVariableCage.animal = new Crocodile // -> that's the problem!
  */

  /*
  class ContravariantVariableCage[-T](var animal: T) // also in COVARIANT POSITION
  val contravariantVariableCage: ContravariantVariableCage[Cat] = new ContravariantVariableCage[Animal](new Dog) // that's the problem!
  */

  /**
    * Because var can be covariant or contravariant, so var is used in invariant case
    */
  class InvariantVariableCage[T](var animal: T) // ok

  /**
    * METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION.
    */

  /*
  class AnotherCovariantCage[+T] {
    def addAnimal(animal: T): Boolean = true
  }

  val anotherCovariantCage: AnotherCovariantCage[Animal] = new AnotherCovariantCage[Cat]
  // Because now, T is Animal, so we can:
  anotherCovariantCage.addAnimal(new Dog) // -> that's the problem!
  */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }
  val anotherContravariantCage: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  // Because now, T is Animal, so we can not:
  // anotherContravariantCage.addAnimal(new Dog) // -> the compiler shouts out at us, here.
  anotherContravariantCage.addAnimal(new Cat) // -> ok
  class Kitty extends Cat
  anotherContravariantCage.addAnimal(new Kitty)

  /**
    * Fix the above problem
    * Widening the type
    */
  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B]
  }

  val myList1 = new MyList[Kitty]
  val myList2 = myList1.add(new Kitty) // A is Kitty
  val myList3 = myList2.add(new Cat) // A is Cat -> return a MyList[Cat]
  val myList4 = myList3.add(new Dog) // A is Animal -> return a MyList[Animal]

  val myList5: MyList[Animal] = new MyList[Kitty] // A is covariant -> ok, A is Animal
  val myList6 = myList5.add(new Cat) // A and B is Animal
  val myList7 = myList6.add(new Dog) // A and B is Animal


  class AnotherMyList[A] {
    def add[A](element: A): MyList[A] = new MyList[A]
  }

  val anotherMyList1 = new AnotherMyList[Kitty]
  val anotherMyList2 = anotherMyList1.add(new Cat)
  val anotherMyList3 = anotherMyList2.add(new Dog)

  /*
  /**
    * METHOD RETURN TYPES ARE IN COVARIANT POSITION
    */
  class PetShop[-T] {

    /*
    def get(isItaPuppy: Boolean): T

    val animalShop: PetShop[Animal] = new PetShop[Animal]
      -> def get(isItaPuppy: Boolean): Animal = new Cat


    val dogShop: PetShop[Dog] = animalShop
    dogShop.get(true) // EVIL CAT!
    */

    /**
      * Fix the above problem
      */
    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
  // val evilCat = shop.get(true, new Cat) -> error
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  /*
    Big rule
    - method arguments are in CONTRAVARIANT position
    - return types are in COVARIANT position
   */


  /**
    * 1. Invariant, covariant, contravariant
    *   Parking[T](things: List[T]) {
    *     park(vehicle: T)
    *     impound(vehicles: List[T])
    *     checkVehicles(conditions: String): List[T]
    *   }
    *
    * 2. used someone else's API: IList[T]
    * 3. Parking = monad!
    *     - flatMap
    */
  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle
  class IList[T]

  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](f: Function1[R, XParking[S]]): XParking[S] = ???
  }

  /*
    Rule of thumb
    - use covariance = COLLECTION OF THINGS
    - use contravariance = GROUP OF ACTIONS
   */

  class CParking2[+T](vehicles: IList[T]) {
    def park[S >: T](vehicle: S): CParking2[S] = ???
    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  class XParking2[-T](vehicles: IList[T]) {
    def park(vehicle: T): XParking2[T] = ???
    def impound[S <: T](vehicles: IList[S]): XParking2[S] = ???
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }

  // flatMap

 */
}
