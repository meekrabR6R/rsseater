package com.meekrab.rsseater

import org.scalatest.FunSpec
import java.io._
import scala.xml._

trait TestRunner {
  val feed = Local("src/test/assets/test.xml")
}

class AuctionSpec extends FunSpec {
  new TestRunner {  
    describe("Feed elements") {
      it("title should = 'BBC News - World'"){
        assert(feed.title === "BBC News - World")
      }
      it("link should = 'http://www.bbc.co.uk/news/world/#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa'") {
        assert(feed.link === "http://www.bbc.co.uk/news/world/#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa")
      }
      it("desc should = 'The latest stories from the World section of the BBC News web site.'") {
        assert(feed.desc === "The latest stories from the World section of the BBC News web site.")
      }
      it("image url should = 'http://news.bbcimg.co.uk/nol/shared/img/bbc_news_120x60.gif'") {
        assert(feed.image.url === "http://news.bbcimg.co.uk/nol/shared/img/bbc_news_120x60.gif")
      }
      it("image title should = 'BBC News - World'") {
        assert(feed.image.title === "BBC News - World")
      }
      it("image link should = 'http://www.bbc.co.uk/news/world/#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa'") {
        assert(feed.image.link === "http://www.bbc.co.uk/news/world/#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa")
      }
      it("image width should be 120") {
        assert(feed.image.width === "120")
      }
      it("image height should be 60") {
        assert(feed.image.height === "60")
      }
    }

    describe("Feed items") {
      it("should have 3 items") {
        assert(feed.items.count(item => true) === 3)
      }
      it("title of head item should = 'Thousands file past Mandela's body'") {
        assert(feed.items.head.title === "Thousands file past Mandela's body")
      }
      it("description of head item should = 'South Africans are queuing in their thousands to view the body of former President Nelson Mandela, which is lying in state for three days.'") {
        assert(feed.items.head.desc === "South Africans are queuing in their thousands to view the body of former President Nelson Mandela, which is lying in state for three days.")
      }
      it("link for head item should = 'http://www.bbc.co.uk/news/world-africa-25328266#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa'") {
        assert(feed.items.head.link === "http://www.bbc.co.uk/news/world-africa-25328266#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa")
      }
      it("should have 2 thumbs") {
        assert(feed.items.head.thumbs.count(item => true) === 2)
      }
      it("thumbnail url for head item should = 'http://news.bbcimg.co.uk/media/images/71670000/jpg/_71670164_71670157.jpg'") {
        assert(feed.items.head.thumbs.head.url === "http://news.bbcimg.co.uk/media/images/71670000/jpg/_71670164_71670157.jpg")
      }
      it("thumbnail width for head thumbnail of head item should = 66") {
        assert(feed.items.head.thumbs.head.width === "66")
      }
      it("thumbnail width for head thumbnail of head item should = 49") {
        assert(feed.items.head.thumbs.head.height === "49")
      }
    }

    describe("Storing XML") {
      it("should store in /assets") {
        feed.store("/home/meekrabr6r/projects/rsseater/src/test/assets/store.xml")
        assert(1 + 1 === 1)
      }
    }
  } 
}

