package net.srirangan.sortablechallenge.algorithms

import org.scalatest.{BeforeAndAfter, FunSuite}

class ProductListingMatchSuite extends FunSuite with BeforeAndAfter {

  var rulesPath: String = "/home/srirangan/Projects/sortable-challenge/src/test/resources/rules.json"
  var productsPath: String = "/home/srirangan/Projects/sortable-challenge/src/test/resources/products.txt"
  var listingsPath: String = "/home/srirangan/Projects/sortable-challenge/src/test/resources/listings.txt"

  test("Match Products and Listings") {
    val matches: List[Map[String, Any]] = ProductListingMatch.execute(rulesPath, productsPath, listingsPath)
  }
}
