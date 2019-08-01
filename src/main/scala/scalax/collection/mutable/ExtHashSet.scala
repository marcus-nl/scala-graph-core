package scalax.collection
package mutable

import scalax.collection.interfaces.ExtSetMethods

import scala.collection.mutable.{AbstractSet, GrowableBuilder, HashSet, SetOps}
import scala.collection.{HashTableAccess, IterableFactory, IterableFactoryDefaults, StrictOptimizedIterableOps}
import scala.util.Random

class ExtHashSet[A] private (val hashSet: HashSet[A] = HashSet[A]())
    extends AbstractSet[A]
    with SetOps[A,ExtHashSet,ExtHashSet[A]]
    with StrictOptimizedIterableOps[A, ExtHashSet, ExtHashSet[A]]
    with IterableFactoryDefaults[A, ExtHashSet]
    with ExtSetMethods[A] {

  private val tableAccess = HashTableAccess(hashSet)

  override def iterableFactory = ExtHashSet
  override def clear(): Unit = hashSet.clear()
  override def addOne(elem: A) = { hashSet.addOne(elem); this }
  override def subtractOne(elem: A) = { hashSet.subtractOne(elem); this }
  override def contains(elem: A) = hashSet.contains(elem)
  override def iterator = hashSet.iterator

  def draw(random: Random): A = {
    val drawn = random.nextInt(hashSet.size) // IllegalArgumentException if len == 0
    hashSet.iterator.drop(drawn).next()
  }

  def findElem(elem: A): Option[A] = if (this.contains(elem)) Some(elem) else None

  def findElem[B](other: B, correspond: (A, B) => Boolean): A = tableAccess.findElem(other, correspond)

  /** Returns an `Iterator` over all entries having the passed `hashCode`.
    */
  def hashCodeIterator(originalHash: Int): Iterator[A] = tableAccess.hashCodeIterator(originalHash)

  /** Updates or inserts `elem`.
    * @return `true` if an insert took place.
    */
  protected[collection] def upsert(elem: A with AnyRef): Boolean = {
    val isUpdate = hashSet.contains(elem)
    if (isUpdate) hashSet.remove(elem)
    hashSet.addOne(elem)
    !isUpdate
  }
}

object ExtHashSet extends IterableFactory[ExtHashSet] {
  override def from[A](source: IterableOnce[A]) = new ExtHashSet[A](HashSet.from(source))
  override def empty[A] = new ExtHashSet[A]()
  override def newBuilder[A] = new GrowableBuilder[A, ExtHashSet[A]](empty[A])
}
