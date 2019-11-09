package lectures.part5ts

// Lesson 9

object Reflection extends App {

  /**
    * How do I instantiate a class or invoke a method by calling just its name dynamically at runtime?
    */

  // reflection + macros + quasiquotes => METAPROGRAMMING

  /**
    * Reflection
    */
  case class Person(name: String) {
    def sayMyName(): Unit = println(s"Hi, my name is $name")
  }

  // 0 - import universe package (contains reflection apis)
  import scala.reflect.runtime.{universe => ru}

  // 1 - MIRROR
  val runtimeMirror = ru.runtimeMirror(getClass.getClassLoader)
  // ClassLoader is an instance of JVM classes, that can load other classes at runtime
  // universe.runtimeMirror returns the Mirror instance of the ClassLoader instance

  // 2 - create a class object (Class Symbol)
  val symbolOfPerson = runtimeMirror.staticClass("lectures.part5ts.Reflection.Person")
  // staticClass method takes the full qualified name of the Person class

  // 3 - create a reflected mirror
  val mirrorOfPerson = runtimeMirror.reflectClass(symbolOfPerson)

  // 4 - get the constructor
  val constructor = symbolOfPerson.primaryConstructor.asMethod

  // 5 - reflect the constructor
  val constructorMirror = mirrorOfPerson.reflectConstructor(constructor)

  // 6 - invoke the constructor
  val instance = constructorMirror.apply("John")

  println(instance)

  /**
    *   Symbol: Symbols are used to establish bindings between a name and the entity it refers to,
    * such as a class or a method. Anything you define and can give a name to in Scala has an associated symbol.
    *     + ClassSymbol
    *     + MethodSymbol
    *     + TypeSymbol
    *     + ...
    *
    * Mirror: Can do things.
    *   Mirrors are a central part of Scala Reflection. All information provided by reflection is made accessible through Mirrors.
    *   Depending on the type of information to be obtained, or the reflective action to be taken, different flavors of mirrors must be used
    *   + ClassMirror
    *   + MethodMirror
    *   + FieldMirro
    *   + ...
    */

  /**
    * Use case:
    *   We have a object, we don't know the type of that object, but we know the name of the method of the type of that object.
    *   We can invoke that method by name!
    */
  // I have an instance
  val person = Person("Mary") // from the wire as a serialized object
  // method name computed from somewhere else
  val methodName = "sayMyName"
  // 1 - mirror
  // 2 - reflect the instance
  val instanceMirrorOfThePerson = runtimeMirror.reflect(person)
  // 3 - method symbol
  val methodSymbol = ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod
  // 4 - reflect the method = can DO things
  val method = instanceMirrorOfThePerson.reflectMethod(methodSymbol)
  // 5 - invoke the method

  method.apply()

  /**
    * Type erasure
    *   Because of backwards compatibility, the JVM does type erasure at compile time
    */

  // pp #1: cannot differentiate types at runtime
  val numbers = List(1,2,3)
  numbers match  {
    case listOfStrings: List[String] => println("list of strings")
    case listOfNumbers: List[Int] => println("list of numbers")
  }

  // pp #2: limitations on overloads
  //  def processList(list: List[Int]): Int = 43
  //  def processList(list: List[String]): Int = 45

  /**
    * TypeTags
    *   TypeTags is created at compile-time and then is used at runtime
    *   Information within TypeTags is carried to the runtime, by calling the tpe method, we have access to the Type instance
    */

  // 0 - import
  import ru._

  // 1 - creating a type tag "manually"
  val ttag = typeTag[Person]  // retrieve the TypeTag of Person Class at runtime
  println(ttag.tpe)

  class MyMap[K, V]

  // 2 - pass type tags as implicit parameters
  def getTypeArguments[T](value: T)(implicit typeTag: TypeTag[T]) = typeTag.tpe match {
    case TypeRef(_, _, typeArguments) => typeArguments
    case _ => List()
  }

  val myMap = new MyMap[Int, String]
  val typeArgs = getTypeArguments(myMap)//(typeTag: TypeTag[MyMap[Int,String]])
  println(typeArgs)

  def isSubtype[A, B](implicit ttagA: TypeTag[A], ttagB: TypeTag[B]): Boolean = {
    ttagA.tpe <:< ttagB.tpe
  }

  class Animal
  class Dog extends Animal
  println(isSubtype[Dog, Animal])

  /**
    * Type is a trait defines types and operations on them
    *     This includes its members (methods, fields, type parameters, nested classes, traits, etc.)
    *   either declared directly or inherited, its base types, its erasure and so on
    */

  // I have an instance


  // 3 - method symbol
  val anotherMethodSymbol = typeTag[Person].tpe.decl(ru.TermName(methodName)).asMethod
  // 4 - reflect the method = can DO things
  val sameMethod = instanceMirrorOfThePerson.reflectMethod(anotherMethodSymbol)
  // 5 - invoke the method
  sameMethod.apply()
}
