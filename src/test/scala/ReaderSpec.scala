package com.meekrab.rsseater

import org.scalatest.FunSpec
import java.io._
import scala.xml._

trait TestRunner {
  val rss = Feeder.createRSS("src/test/assets/rss-test.xml", "local")
  val atom = Feeder.createAtom("src/test/assets/atom-test.xml", "local")
}

class ReaderSpec extends FunSpec {
  new TestRunner {  
    describe("RSS elements") {
      it("title should = 'BBC News - World'"){
        assert(rss.title === "BBC News - World")
      }
      it("link should = 'http://www.bbc.co.uk/news/world/#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa'") {
        assert(rss.link === "http://www.bbc.co.uk/news/world/#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa")
      }
      it("desc should = 'The latest stories from the World section of the BBC News web site.'") {
        assert(rss.desc === "The latest stories from the World section of the BBC News web site.")
      }
      it("image url should = 'http://news.bbcimg.co.uk/nol/shared/img/bbc_news_120x60.gif'") {
        assert(rss.image.url === "http://news.bbcimg.co.uk/nol/shared/img/bbc_news_120x60.gif")
      }
      it("image title should = 'BBC News - World'") {
        assert(rss.image.title === "BBC News - World")
      }
      it("image link should = 'http://www.bbc.co.uk/news/world/#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa'") {
        assert(rss.image.link === "http://www.bbc.co.uk/news/world/#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa")
      }
      it("image width should be 120") {
        assert(rss.image.width === "120")
      }
      it("image height should be 60") {
        assert(rss.image.height === "60")
      }
    }

    describe("rss items") {
      it("should have 3 items") {
        assert(rss.items.count(item => true) === 3)
      }
      it("title of head item should = 'Thousands file past Mandela's body'") {
        assert(rss.items.head.title === "Thousands file past Mandela's body")
      }
      it("description of head item should = 'South Africans are queuing in their thousands to view the body of former President Nelson Mandela, which is lying in state for three days.'") {
        assert(rss.items.head.desc === "South Africans are queuing in their thousands to view the body of former President Nelson Mandela, which is lying in state for three days.")
      }
      it("link for head item should = 'http://www.bbc.co.uk/news/world-africa-25328266#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa'") {
        assert(rss.items.head.link === "http://www.bbc.co.uk/news/world-africa-25328266#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa")
      }
      it("should have 2 thumbs") {
        assert(rss.items.head.thumbs.count(item => true) === 2)
      }
      it("thumbnail url for head item should = 'http://news.bbcimg.co.uk/media/images/71670000/jpg/_71670164_71670157.jpg'") {
        assert(rss.items.head.thumbs.head.url === "http://news.bbcimg.co.uk/media/images/71670000/jpg/_71670164_71670157.jpg")
      }
      it("thumbnail width for head thumbnail of head item should = 66") {
        assert(rss.items.head.thumbs.head.width === "66")
      }
      it("thumbnail width for head thumbnail of head item should = 49") {
        assert(rss.items.head.thumbs.head.height === "49")
      }
    }

    describe("Storing XML") {
      it("should store in /assets") {
        rss.store("/home/meekrabr6r/projects/rsseater/src/test/assets/store.xml")
        val loc = Feeder.createRSS("/home/meekrabr6r/projects/rsseater/src/test/assets/store.xml", "local")
        assert(loc.title === "BBC News - World")
        new File("/home/meekrabr6r/projects/rsseater/src/test/assets/store.xml").delete
      }
    }

    describe("Atom elements") {
      it("title should = 'Elder Scrolls Official News'"){
        assert(atom.title === "Elder Scrolls Official News")
      }
      it("link should = 'http://elderscrolls.com/'") {
        assert(atom.link === "http://elderscrolls.com/")
      }
      it("id should = 'urn:uuid:86328bf6-5523-eeb1-d6e4-b2b09399ee80'") {
        assert(atom.id === "urn:uuid:86328bf6-5523-eeb1-d6e4-b2b09399ee80")
      }
      it("desc should = 'Elder Scrolls : Official News and Updates'") {
        assert(atom.desc === "Elder Scrolls : Official News and Updates")
      }
    }

    describe("atom entries") {
      it("should have 2 items") {
        assert(atom.entries.count(entries => true) === 2)
      }
      it("title of head entry should = 'The Elder Scrolls Anthology Available today!'") {
        assert(atom.entries.head.title === "The Elder Scrolls Anthology Available today!")
      }
      it("link for head item should = 'http://cms.elderscrolls.com/news/index.php'") {
        assert(atom.entries.head.link === "http://cms.elderscrolls.com/news/index.php")
      }
    }
  } 
}

