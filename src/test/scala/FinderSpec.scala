package com.meekrab.rsseater

import org.scalatest.FunSpec

trait Tester {
  val feeds = GoogleFeedFinder("Skyrim")
}

class FinderSpec extends FunSpec {
  new Tester {
    describe("Query text") {
      it("should = 'Skyrim'"){
        assert(feeds.query === "Skyrim")
      }
    }
  }
}