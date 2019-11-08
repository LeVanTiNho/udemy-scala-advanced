package lectures.part5ts

// Lesson 9

object Reflection extends App {

  /**
    * How do I instantiate a class or invoke a method by calling just its name dynamically at runtime?
    */

  // reflection + macros + quasiquotes => METAPROGRAMMING

  case class Person(name: String) {
    def sayMyName(): Unit = println(s"Hi, my name is $name")
  }

  // 0 - import universe package (contains reflection apis)

  import scala.reflect.runtime.{universe => ru}

  // 1 - MIRROR
  val runtimeMirror = ru.runtimeMirror(getClass.getClassLoader)
  // 2 - create a class object = "description"
  val symbolOfPerson = runtimeMirror.staticClass("lectures.part5ts.Reflection.Person") // creating a class object by NAME
  // 3 - create a reflected mirror = "can DO things"
  val mirrorOfPerson = runtimeMirror.reflectClass(symbolOfPerson)
  // 4 - get the constructor
  val constructor = symbolOfPerson.primaryConstructor.asMethod
  // 5 - reflect the constructor
  val constructorMirror = mirrorOfPerson.reflectConstructor(constructor)
  // 6 - invoke the constructor
  val instance = constructorMirror.apply("John")

  println(instance)

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

  // type erasure

  // pp #1: differentiate types at runtime
  val numbers = List(1,2,3)
  numbers match  {
    case listOfStrings: List[String] => println("list of strings")
    case listOfNumbers: List[Int] => println("list of numbers")
  }

  // pp #2: limitations on overloads
  //  def processList(list: List[Int]): Int = 43
  //  def processList(list: List[String]): Int = 45

  // TypeTags

  // 0 - import
  import ru._

  // 1 - creating a type tag "manually"
  val ttag = typeTag[Person]
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

  /*
  def isSubtype[A, B](implicit ttagA: TypeTag[A], ttagB: TypeTag[B]): Boolean = {
    ttagA.tpe <:< ttagB.tpe
  }

  class Animal
  class Dog extends Animal
  println(isSubtype[Dog, Animal])

  // I have an instance


  // 3 - method symbol
  val anotherMethodSymbol = typeTag[Person].tpe.decl(ru.TermName(methodName)).asMethod
  // 4 - reflect the method = can DO things
  val sameMethod = instanceMirrorOfThePerson.reflectMethod(anotherMethodSymbol)
  // 5 - invoke the method
  sameMethod.apply()*/
}
