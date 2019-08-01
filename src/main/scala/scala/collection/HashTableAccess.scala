package scala.collection

import mutable.HashSet

@deprecated("this class is internal to Graph for Scala")
class HashTableAccess[A](hashSet: HashSet[A], table: Array[HashSet.Node[A]]) {

  def findElem[B](other: B, correspond: (A, B) => Boolean): A = {
    val NotFound: A = null.asInstanceOf[A]
    hashCodeIterator(other.##).find(a => correspond(a, other)).getOrElse(NotFound)
  }

  def hashCodeIterator(originalHash: Int): Iterator[A] = {
    val hash = improveHash(originalHash)
    new AbstractIterator[A] {
      var node = table(index(hash))
      override def hasNext = (node ne null) && (node.hash == hash)
      override def next() = {
        val prev = node
        node = node.next;
        prev.key
      }
    }
  }

  def upsert(elem: A with AnyRef): Boolean = {
    val hash = computeHash(elem)
    table(index(hash)) match {
      case null =>
        hashSet.add(elem)
        true
      case node =>
        //node.findNode(elem, hash).
        // this is pretty complicated; skipping for now
        ???
    }
  }

  @inline private def index(improvedHash: Int): Int = improvedHash & (table.length - 1)
  @inline private def computeHash(elem: Any): Int = improveHash(elem.##)
  @inline private def improveHash(originalHash: Int): Int = originalHash ^ (originalHash >>> 16)
}

object HashTableAccess {
  def apply[A](hashSet: HashSet[A]): HashTableAccess[A] = {
    val hashSetClass = classOf[HashSet[A]]
    val field = hashSetClass.getDeclaredField("scala$collection$mutable$HashSet$$table")
    field.setAccessible(true)
    new HashTableAccess(hashSet, field.get(hashSet).asInstanceOf[Array[HashSet.Node[A]]])
  }
}
