package lectures.part5ts

// Lesson 2

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  /**
    * what is variance?
    * "inheritance" - type substitution of generics
    */

  class Cage[T]

  // Cat extends Animal, the question is if Cage[Cat] extend Cage[Animal] or vice versa?

  /*
  Yes-covariance
  Only 2 below cases is covariance:
   */
  class CCage[+T]
  val ccage1: CCage[Animal] = new CCage[Cat]
  val ccage2: CCage[Animal] = new CCage[Animal]
  // val ccage3: CCage[Cat] = new CCage[Animal] -> not covariance

  /*
  No - invariance
  Only 1 below case:
   */
  class ICage[T]
  val icage: ICage[Cat] = new ICage[Cat]
  // val icage: ICage[Animal] = new ICage[Cat] -> covariance, not invariance
  // val icage: ICage[Cat] = new ICage[Animal] -> contravariance, not invariance

  /*
  No - opposite = contra-variance
   */
  class XCage[-T]
  // Only 2 below cases is contra-variance
  val xcage1: XCage[Cat] = new XCage[Animal]
  val xcage2: XCage[Cat] = new XCage[Cat]

  /**
    * Variant position, invariant position
    */
  class InvariantCage[T](val animal: T) // invariant

  /**
    * covariant positions
    */

  class CovariantCage[+T](val animal: T)
  // COVARIANT POSITION, the animal para is at covariant position
  // Covariant means CovariantCage[Animal](val animal: Animal), animal can be Dog, Cat, Crocodile

  // class ContravariantCage[-T](val animal: T)
  // the animal para is at covariant position
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

  /**
    * Contravariance
    */

  // class CovariantVariableCage[+T](var animal: T)
  // types of vars are in CONTRAVARIANT POSITION
  /*
    val ccage: CCage[Animal] = new CCage[Cat](new Cat) -> Ok, no problem!
    ccage.animal = new Crocodile -> That is problem
   */

  /**
    * Covariance
    */
  // class ContravariantVariableCage[-T](var animal: T) // also in COVARIANT POSITION
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Animal) -> ok, no problem!
    catCage.animal = new Crocodile -> that is problem
   */

  /**
    * Because var can be covariant or contravariant, so var is used in invariant case
    */
  class InvariantVariableCage[T](var animal: T) // ok

  /**
    * Generic type paras is CONTRAVARIANT POSITION
    */
  trait AnotherCovariantCage[+T] {
    // def addAnimal(animal: T)
  }
  /*
    val ccage: CCage[Animal] = new CCage[Dog]

    Because T is Animal, we can:
      ccage.add(new Cat) -> that is problem!
   */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }

  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  /*
  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog)

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION.

  // return types
  class PetShop[-T] {
    //    def get(isItaPuppy: Boolean): T // METHOD RETURN TYPES ARE IN COVARIANT POSITION
    /*
      val catShop = new PetShop[Animal] {
        def get(isItaPuppy: Boolean): Animal = new Cat
      }

      val dogShop: PetShop[Dog] = catShop
      dogShop.get(true)   // EVIL CAT!
     */

    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
  //  val evilCat = shop.get(true, new Cat)
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
