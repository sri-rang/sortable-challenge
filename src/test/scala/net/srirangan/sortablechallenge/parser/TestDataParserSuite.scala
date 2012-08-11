package net.srirangan.sortablechallenge.parser

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import net.srirangan.sortablechallenge.parsers.TestDataParser
import io.BufferedSource

class TestDataParserSuite extends FunSuite with BeforeAndAfter {

  var productsFile: BufferedSource = scala.io.Source.fromFile("/home/srirangan/Projects/sortable-challenge/src/test/resources/products.txt")
  var listingsFile: BufferedSource = scala.io.Source.fromFile("/home/srirangan/Projects/sortable-challenge/src/test/resources/listings.txt")

  test("Parse and verifiy test data") {
    val lines: Iterator[String] = productsFile.getLines()
    TestDataParser.parse(lines)
  }

  after {
    productsFile.close()
    listingsFile.close()
  }

}
