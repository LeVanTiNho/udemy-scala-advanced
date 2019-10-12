package lectures.part4implicits

// Lesson: 3, 5

/**
  * What is type classes?
  * A type class is a trait, that ticks a type and describes what operations can be applied to this type
  */
object TypeClasses extends App {

  /**
    * Part 1: Type class
    */


  /**
    * Scenario: We want to define classes, its instances can be serialized to HTML
    */

  // Option 1: trait (interface)
  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  User("John", 32, "john@rockthejvm.com").toHtml

  /*
    1 - for the types WE write
    2 - ONE implementation out of quite a number
   */

  // option 2 - pattern matching
  object HTMLSerializerPM {
    def serializeToHtml(value: Any) = value match {
      case User(n, a, e) =>
      case _ =>
    }
  }

  /*
    1 - lost type safety
    2 - need to  modify the code every time
    3 - still ONE implementation
   */

  /**
    * Type class
    */
  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  // type class instance
  implicit object UserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  val john = User("John", 32, "john@rockthejvm.com")
  println(UserSerializer.serialize(john))

  // 1 - we can define serializers for other types
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString()}</div>"
  }

  // 2 - we can define MULTIPLE serializers fon one type
  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} </div>"
  }

  /**
    * Type class template
    */
  trait MyTypeClassTemplate[T] {
    def action(value: T): Any
  }

  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
  }

  /*
  Exercise
    Equality
   */
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

  /**
    * Part 2: implicits and type class
    */
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    // Better design
    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }

  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john))

  // access to the entire type class interface
  println(HTMLSerializer[User].serialize(john))

  /*
  Exercise: implement the type class pattern for the Equality
   */

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  /*
  Equal type-class instances and HTMLSerializer type-class instances
  will be as implicits for the compiler looks for them when needing.
  */

  /*
  Ad-hoc polymorphism = type class + implicit
   */

  /**
    * Part 3
    */
  /*
  The HTMLEnrichment class is a wrapper of an generic type
   */
  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  println(john.toHtml)  // println(new HTMLEnrichment[User](john).toHTML(UserSerializer))
  // COOL!
  /*
    - extend to new types
    - choose implementation
    - super expressive!
   */

  println(2.toHTML)
  println(john.toHTML(PartialUserSerializer))

  /*
    - type class itself --- HTMLSerializer[T] { .. }
    - type class instances (some of which are implicit) --- UserSerializer, IntSerializer
    - conversion with implicit classes --- HTMLEnrichment
   */

  // context bounds
  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body> ${content.toHTML(serializer)}</body></html>"

  def htmlSugar[T : HTMLSerializer](content: T): String = {
    val serializer = implicitly[HTMLSerializer[T]]
    // use serializer
    s"<html><body> ${content.toHTML(serializer)}</body></html>"
  }

  // implicitly
  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other part of the  code
  val standardPerms = implicitly[Permissions]


}
