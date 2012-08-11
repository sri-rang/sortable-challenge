package net.srirangan.sortablechallenge.parsers

import util.parsing.json.JSON

object TestDataParser {

  def parse(lines: Iterator[String]): List[Any] = {
    var result: List[Any] = List()
    lines.foreach((line: String) => {
      result ::= JSON.parseFull(line)
    })
    result
  }

}
