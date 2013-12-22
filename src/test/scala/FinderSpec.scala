package com.meekrab.rsseater

import org.scalatest.FunSpec
import play.api.libs.json._
import play.api.libs.functional._

trait Tester {
  val feeds = GoogleFeedFinder("Skyrim")  
  val testJson = scala.io.Source.fromFile("src/test/assets/test.json").mkString
  val testFeeds = GoogleFeedFinder.FeedList(Json.parse(testJson))
}

class FinderSpec extends FunSpec {
  new Tester {
    describe("Query text") {
      it("should = 'Skyrim'"){
        assert(feeds.query === "Skyrim")
      }
    }
    describe("First feed") {
    	it("url should = 'http://cms.elderscrolls.com/feeds/rss/index.php?locale\u003den-us'") {
    		assert(testFeeds.entries.head.url === "http://cms.elderscrolls.com/feeds/rss/index.php?locale\u003den-us")
    	}
    	it("title should = 'The Elder Scrolls Official Site | \u003cb\u003eSkyrim\u003c/b\u003e - The Elder Scrolls V: \u003cb\u003eSkyrim\u003c/b\u003e'") {
    		assert(testFeeds.entries.head.title === "The Elder Scrolls Official Site | \u003cb\u003eSkyrim\u003c/b\u003e - The Elder Scrolls V: \u003cb\u003eSkyrim\u003c/b\u003e")
    	}
    }
  }
}