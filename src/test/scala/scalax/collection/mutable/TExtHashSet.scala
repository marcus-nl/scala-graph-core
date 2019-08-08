package scalax.collection
package mutable

import scala.collection.mutable
import org.scalatest.Matchers
import org.scalatest.refspec.RefSpec
import scalax.collection.mutable.HashSet.Enrichments

class TExtHashSetTest extends RefSpec with Matchers {
  import Data._
  val set                    = mutable.HashSet(outerElemsOfDi_1: _*)
  val outerEdge: DiEdge[Int] = outerElemsOfDi_1.head
  val innerEdge              = new Wrapper(outerEdge)

  object `Hash set extensions work properly` {
    def `find entry` {
      /* `inner.edge == outer` returns the expected result because Graph#InnerEdge.equal
       * is aware of the inner edge structure. The opposite will be false since, as a rule,
       * outer object types will not be Graph-aware with regard to their equal.
       */
      def eq(outer: DiEdge[Int], wrapper: Wrapper[DiEdge[Int]]) = wrapper.edge == outer
      set.findElem(innerEdge, eq) should be(outerEdge)
    }
    def `draw element` {
      val randomElems = mutable.Set.empty[DiEdge[Int]]
      val r           = new util.Random
      for (i <- 1 to (set.size * 32))
        randomElems += set draw r
      randomElems should have size (set.size)
    }
    def `iterate over hashCodes` {
      set.hashCodeIterator(-228876066).toList should have size (0)
      outerEdge.hashCode should be(innerEdge.hashCode)
      val elems = set.hashCodeIterator(outerEdge.hashCode).toList
      elems should have size (1)
      elems.head should be(outerEdge)
      elems should contain only (outerEdge)
    }
    def `iterate over duplicate hashCodes` {
      case class C(i: Int, j: Int) {
        override def hashCode = i.##
      }
      val multi = mutable.HashSet(C(1, 0), C(1, 1), C(2, 2), C(1, 3), C(2, 0))
      for (i <- 0 to 2) {
        val elems = multi.hashCodeIterator(i.##).toList
        elems should have size (multi count (_.i == i))
      }
    }
  }
}


object Data {
  // WDi-1.jpg without weights
  val outerElemsOfDi_1 = List(1 ~> 2, 2 ~> 3, 4 ~> 3, 3 ~> 5, 1 ~> 5, 1 ~> 3)

  implicit final class EdgeCons[N](private val n1: N) extends AnyVal {
    @inline def ~>(n2: N) = DiEdge[N](n1, n2)
  }
}

case class UnDiEdge[N](source: N, target: N)
case class DiEdge[N](source: N, target: N)

class Wrapper[T](val edge: T) {
  override def hashCode = edge.##
}
