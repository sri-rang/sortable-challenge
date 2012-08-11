package net.srirangan.sortablechallenge.algorithms

import net.srirangan.sortablechallenge.parsers.DataParser
import io.BufferedSource
import util.parsing.json.JSON

object ProductListingMatch {

  def execute(rulesPath: String, productsPath: String, listingsPath: String): List[Map[String, Any]] = {

    val rulesFile: BufferedSource = scala.io.Source.fromFile(rulesPath)
    val productsFile: BufferedSource = scala.io.Source.fromFile(productsPath)
    val listingsFile: BufferedSource = scala.io.Source.fromFile(listingsPath)

    val rules: Option[Any] = JSON.parseFull(rulesFile.mkString)
    val products: List[Any] = DataParser.parse(productsFile.getLines())
    val listings: List[Any] = DataParser.parse(listingsFile.getLines())

    products.foreach((product: Any) => {
      var productMap: Map[String, Any] = product.asInstanceOf[Some[Map[String, Any]]].get
      var productsListings: List[Map[String, Any]] = List()
      listings.foreach((listing: Any) => {
        var listingMap: Map[String, Any] = listing.asInstanceOf[Some[Map[String, Any]]].get
        var score: Double = 0.0
        var passesRules = true
        rules.get.asInstanceOf[List[Map[String, Any]]].foreach((rule) => {
          val rule_type: String = rule.get("type").get.asInstanceOf[String]
          val product_field: String = rule.get("product_field").get.asInstanceOf[String]
          val listing_field: String = rule.get("listing_field").get.asInstanceOf[String]
          val weight: Double = rule.get("weight").get.asInstanceOf[Double]
          val product_field_exists = productMap.get(product_field).isDefined
          val listing_field_exists = listingMap.get(listing_field).isDefined
          var product_field_value: String = ""
          var listing_field_value: String = ""
          if (product_field_exists) product_field_value = productMap.get(product_field).get.asInstanceOf[String].toLowerCase
          if (listing_field_exists) listing_field_value = listingMap.get(listing_field).get.asInstanceOf[String].toLowerCase
          if (product_field_exists && listing_field_exists) {
            if (rule_type == "similar") {
              score += (LetterPairSimilarity.compareStrings(product_field_value, listing_field_value) * weight)
            } else if (rule_type == "contains") {
              if (
                (product_field_value.size == 0 || listing_field_value.size == 0)
                  ||
                  (!product_field_value.contains(listing_field_value) && !listing_field_value.contains(product_field_value))
              ) {
                passesRules = false
              }
            }
          } else {
            passesRules = false
          }
        })
        if (passesRules && score > 0) {
          listingMap = listingMap + (("score", score))
          productsListings ::= listingMap
        }
      })

      productsListings = productsListings.sortWith((l1: Map[String, Any], l2: Map[String, Any]) => {
        l1.get("score").get.asInstanceOf[Double] > l2.get("score").get.asInstanceOf[Double]
      })

      if (productsListings.size > 0) {
        productMap = productMap + (("listings", productsListings.head))
        println(productsListings.head.get("score"))
        println(productsListings.head.get("title"))
        println(productMap.get("manufacturer"), productMap.get("family"), productMap.get("model"), productMap.get("product_name"))
        println()
      }
    })

    rulesFile.close()
    productsFile.close()
    listingsFile.close()

    null
  }

}
