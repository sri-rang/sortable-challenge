package net.srirangan.sortablechallenge.parsers

import util.parsing.json.JSON

object DataParser {

  def parse(lines: Iterator[String], limitResults: Boolean = false): List[Any] = {
    var result: List[Any] = List()
    lines.foreach((line: String) => {
      result ::= JSON.parseFull(line)
      if (result.length == 5 && limitResults) return result
    })
    result
  }

}
