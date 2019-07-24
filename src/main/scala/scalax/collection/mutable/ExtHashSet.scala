package scalax.collection
package mutable

import scalax.collection.interfaces.ExtSetMethods

import scala.collection.mutable.{AbstractSet, GrowableBuilder, HashSet, SetOps}
import scala.collection.{IterableFactory, IterableFactoryDefaults, StrictOptimizedIterableOps}
import scala.util.Random

class ExtHashSet[A]
    extends AbstractSet[A]
    with SetOps[A,ExtHashSet,ExtHashSet[A]]
    with StrictOptimizedIterableOps[A, ExtHashSet, ExtHashSet[A]]
    with IterableFactoryDefaults[A, ExtHashSet]
    with ExtSetMethods[A] {

  val delegate = HashSet[A]()

  override def iterableFactory = ExtHashSet
  override def clear(): Unit = delegate.clear()
  override def addOne(elem: A) = { delegate.addOne(elem); this }
  override def subtractOne(elem: A) = { delegate.subtractOne(elem); this }
  override def contains(elem: A) = delegate.contains(elem)
  override def iterator = delegate.iterator

  def draw(random: Random): A = ???

  def findElem(elem: A): Option[A] = if (this.contains(elem)) Some(elem) else None // or Some(elem).filter(this.contains)

  def findElem[B](other: B, correspond: (A, B) => Boolean): A = ??? // no access to underlying table

  /** Returns an `Iterator` over all entries having the passed `hashCode`.
    */
  def hashCodeIterator(hcode: Int): Iterator[A] = ??? // no access to underlying table

  /** Updates or inserts `elem`.
    *  @return `true` if an insert took place.
    */
  protected[collection] def upsert(elem: A with AnyRef): Boolean = {
    val isUpdate = delegate.contains(elem)
    if (isUpdate) delegate.remove(elem)
    delegate.addOne(elem)
    !isUpdate
  }
}

object ExtHashSet extends IterableFactory[ExtHashSet] {
  override def from[A](source: IterableOnce[A]) = new ExtHashSet[A]
  override def empty[A] = new ExtHashSet[A]
  override def newBuilder[A] = new GrowableBuilder[A, ExtHashSet[A]](empty[A])
}
