package net.srirangan.sortablechallenge.algorithms

trait ProductListingMatchTrait {

  var limitResults: Boolean = false

  var listingsByManufacturer: Map[String, List[Any]] = Map()

  def getFilteredListingsByManufacturer(manufacturer: String, listings: List[Any]): List[Any] = {
    var filteredListings: List[Any] = List()
    if (listingsByManufacturer.get(manufacturer) == None) {
      filteredListings = listings.filter((listing: Any) => {
        listing.asInstanceOf[Some[Map[String, Any]]].get("manufacturer") == manufacturer
      })
      listingsByManufacturer = listingsByManufacturer + ((manufacturer, filteredListings))
    } else {
      filteredListings = listingsByManufacturer.get(manufacturer).get
    }
    filteredListings
  }

  def smartContains(s1: String, s2: String): Boolean = {
    var needle: String = ""
    var haystack: String = ""
    if (s1.size > s2.size) {
      needle = s2
      haystack = s1
    } else {
      needle = s1
      haystack = s2
    }
    haystack.contains(needle)
  }

  def getResultItem(_filteredListings: scala.List[Any], product: Map[String, Any]): Map[String, Any] = {
    var filteredListings: scala.List[Any] = _filteredListings
    filteredListings = filteredListings.sortWith((l1: Any, l2: Any) => {
      l1.asInstanceOf[Some[Map[String, Any]]].get.get("score").get.asInstanceOf[Double] > l2.asInstanceOf[Some[Map[String, Any]]].get.get("score").get.asInstanceOf[Double]
    })

    var resultItem: Map[String, Any] = Map()
    resultItem = resultItem + (("product_name", product.get("product_name")))
    resultItem = resultItem + (("listings", filteredListings))
    resultItem
  }

  def getFilteredListingsByRules(listingHolder: Any, rules: Option[Any], product: Map[String, Any]): List[Any] = {
    var filteredListings: List[Any] = List()
    var listing: Map[String, Any] = listingHolder.asInstanceOf[Some[Map[String, Any]]].get
    var doesListingMatchCriticalRules = true
    var score: Double = 0.0
    rules.get.asInstanceOf[List[Map[String, Any]]].foreach((rule) => {
      var doesListingMatchRule = true
      val operation = rule.get("operation").get

      val product_field: String = rule.get("product_field").get.asInstanceOf[String]
      val listing_field: String = rule.get("listing_field").get.asInstanceOf[String]
      val critical: Boolean = rule.get("critical").get.asInstanceOf[Boolean]
      val weight: Double = rule.get("weight").get.asInstanceOf[Double]
      val product_field_exists = product.get(product_field).isDefined
      val listing_field_exists = listing.get(listing_field).isDefined
      var product_field_value: String = ""
      var listing_field_value: String = ""
      if (product_field_exists) product_field_value = product.get(product_field).get.asInstanceOf[String]
      if (listing_field_exists) listing_field_value = listing.get(listing_field).get.asInstanceOf[String]

      if (!product_field_exists || !listing_field_exists) {
        doesListingMatchRule = false
      } else {
        doesListingMatchRule = operation match {
          case "contains" => {
            smartContains(product_field_value.toLowerCase, listing_field_value.toLowerCase)
          }
        }
      }

      if (!doesListingMatchRule) {
        if (critical) {
          doesListingMatchCriticalRules = false
        }
      } else {
        score += weight
      }

    })
    if (doesListingMatchCriticalRules) {
      listing = listing + (("score", score))
      filteredListings ::= Some(listing)
    }
    filteredListings
  }

  def execute(rulesPath: String, productsPath: String, listingsPath: String): List[Map[String, Any]]
}
