package scalax.collection.mutable

import scala.collection.{FilteredSet, HashTableAccess}
import scala.collection.mutable.{HashSet => MHashSet}
import scala.util.Random

protected[collection] object HashSet {
  implicit class Enrichments[A](val hashSet: MHashSet[A]) extends AnyVal {
    def draw(random: Random): A = {
      val drawn = random.nextInt(hashSet.size) // IllegalArgumentException if len == 0
      hashSet.iterator.drop(drawn).next()
    }

    def findElem(elem: A): Option[A] = if (hashSet.contains(elem)) Some(elem) else None

    def findElem[B](other: B, correspond: (A, B) => Boolean): A = HashTableAccess(hashSet).findElem(other, correspond)

    /** Returns an `Iterator` over all entries having the passed `hashCode`.
      */
    def hashCodeIterator(originalHash: Int): Iterator[A] = HashTableAccess(hashSet).hashCodeIterator(originalHash)

    /** Updates or inserts `elem`.
      * @return `true` if an insert took place.
      */
    def upsert(elem: A with AnyRef): Boolean = {
      // TODO could be optimized in HashTableAccess
      val isUpdate = hashSet.contains(elem)
      if (isUpdate) hashSet.remove(elem)
      hashSet.addOne(elem)
      !isUpdate
    }

    def withSetFilter(p: A => Boolean) = new FilteredSet(hashSet, p)
  }
}
