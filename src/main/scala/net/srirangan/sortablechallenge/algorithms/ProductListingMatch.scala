package net.srirangan.sortablechallenge.algorithms

import net.srirangan.sortablechallenge.parsers.DataParser
import io.BufferedSource
import util.parsing.json.JSON

object ProductListingMatch extends ProductListingMatchTrait {

  def execute(rulesPath: String, productsPath: String, listingsPath: String): List[Map[String, Any]] = {
    var results: List[Map[String, Any]] = List()

    val rulesFile: BufferedSource = scala.io.Source.fromFile(rulesPath)
    val productsFile: BufferedSource = scala.io.Source.fromFile(productsPath)
    val listingsFile: BufferedSource = scala.io.Source.fromFile(listingsPath)
    val rules: Option[Any] = JSON.parseFull(rulesFile.mkString)
    val products: List[Any] = DataParser.parse(productsFile.getLines(), limitResults)
    val listings: List[Any] = DataParser.parse(listingsFile.getLines(), limitResults)

    products.foreach((productHolder) => {
      var filteredListings: List[Any] = List()
      val product = productHolder.asInstanceOf[Some[Map[String, Any]]].get
      val productManufacturer: String = product.get("manufacturer").get.asInstanceOf[String]
      val listingsMyManufacturer: List[Any] = getFilteredListingsByManufacturer(productManufacturer, listings)

      listingsMyManufacturer.foreach((listingHolder) => {
        filteredListings = getFilteredListingsByRules(listingHolder, rules, product)
      })

      if (filteredListings.size > 0) {
        results ::= getResultItem(filteredListings, product)
      }

    })

    results
  }
}
