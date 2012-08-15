package net.srirangan.sortablechallenge.algorithms

import io.BufferedSource
import util.parsing.json.JSON
import net.srirangan.sortablechallenge.parsers.DataParser
import akka.actor.{Props, ActorSystem, Actor}
import akka.routing.RoundRobinRouter

object Result {
  var results: List[Map[String, Any]] = List()
}

sealed trait ActorMessage

case class AlgorithmStart(traits: ProductListingMatchTrait,
                          rulesPath: String, productsPath: String,
                          listingsPath: String) extends ActorMessage

case class FilterListingsByRulesStart(traits: ProductListingMatchTrait,
                                      listingHolder: Any,
                                      rules: Option[Any],
                                      product: Map[String, Any]) extends ActorMessage

case class FilterListingsByRulesComplete(traits: ProductListingMatchTrait,
                                         filteredListings: List[Any],
                                         product: Map[String, Any]) extends ActorMessage

class Algorithm extends Actor {

  var listingsRouter = context.actorOf(Props[ListingsWorker].withRouter(RoundRobinRouter(8)))

  var numberOfResultsExpected: Int = 0

  def receive = {
    case AlgorithmStart(traits, rulesPath, productsPath, listingsPath) => {
      val rulesFile: BufferedSource = scala.io.Source.fromFile(rulesPath)
      val productsFile: BufferedSource = scala.io.Source.fromFile(productsPath)
      val listingsFile: BufferedSource = scala.io.Source.fromFile(listingsPath)
      val rules: Option[Any] = JSON.parseFull(rulesFile.mkString)
      val products: List[Any] = DataParser.parse(productsFile.getLines(), traits.limitResults)
      numberOfResultsExpected = products.size
      val listings: List[Any] = DataParser.parse(listingsFile.getLines(), traits.limitResults)
      products.foreach((productHolder) => {
        val product = productHolder.asInstanceOf[Some[Map[String, Any]]].get
        val productManufacturer: String = product.get("manufacturer").get.asInstanceOf[String]
        val listingsMyManufacturer: List[Any] = traits.getFilteredListingsByManufacturer(productManufacturer, listings)
        listingsMyManufacturer.foreach((listingHolder) => {
          listingsRouter ! FilterListingsByRulesStart(traits, listingHolder, rules, product)
        })
      })
    }
    case FilterListingsByRulesComplete(traits, filteredListings, product) => {
      Result.results ::= traits.getResultItem(filteredListings, product)
      if (Result.results.size == numberOfResultsExpected) {
        sender ! Result.results
        context.system.shutdown()
      }
    }
  }
}

class ListingsWorker extends Actor {
  protected def receive = {
    case FilterListingsByRulesStart(traits, listingHolder, rules, product) => {
      val filteredListings: List[Any] = traits.getFilteredListingsByRules(listingHolder, rules, product)
      sender ! FilterListingsByRulesComplete(traits, filteredListings, product)
    }
  }
}

object ConcurrentProductListingsMatch extends ProductListingMatchTrait {

  def execute(rulesPath: String, productsPath: String, listingsPath: String): List[Map[String, Any]] = {

    val actorSystem = ActorSystem()
    val algorithm = actorSystem.actorOf(Props[Algorithm])
    algorithm ! AlgorithmStart(this, rulesPath, productsPath, listingsPath)

    while (!actorSystem.isTerminated) {
      ;
    }

    Result.results
  }

}