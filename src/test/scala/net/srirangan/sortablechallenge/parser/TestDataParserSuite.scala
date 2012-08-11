package net.srirangan.sortablechallenge.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import net.srirangan.sortablechallenge.parsers.TestDataParser
import io.BufferedSource

class TestDataParserSuite extends FunSuite with BeforeAndAfter {

  var forceSkipAtFive: Boolean = true

  var productsFile: BufferedSource = scala.io.Source.fromFile("/home/srirangan/Projects/sortable-challenge/src/test/resources/products.txt")
  var listingsFile: BufferedSource = scala.io.Source.fromFile("/home/srirangan/Projects/sortable-challenge/src/test/resources/listings.txt")

  test("Parse and verifiy test data") {
    val products: List[Any] = TestDataParser.parse(productsFile.getLines(), forceSkipAtFive)
    val listings: List[Any] = TestDataParser.parse(listingsFile.getLines(), forceSkipAtFive)

    assert(products.length === 5)
    assert(listings.length === 5)
  }

  after {
    productsFile.close()
    listingsFile.close()
  }

}
