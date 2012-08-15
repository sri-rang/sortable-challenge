package net.srirangan.sortablechallenge.algorithms

import org.scalatest.{BeforeAndAfter, FunSuite}

class ConcurrentProductListingMatchSuite extends FunSuite with BeforeAndAfter {

  val rulesPath: String = "/home/srirangan/Projects/sortable-challenge/src/test/resources/rules.json"
  val productsPath: String = "/home/srirangan/Projects/sortable-challenge/src/test/resources/products.txt"
  val listingsPath: String = "/home/srirangan/Projects/sortable-challenge/src/test/resources/listings-ignore.txt"

  test("Multi Threaded") {
    val matches: List[Map[String, Any]] = ConcurrentProductListingsMatch.execute(rulesPath, productsPath, listingsPath)

    assert(matches.size > 0)

    // Do your real assertions here
  }
}
