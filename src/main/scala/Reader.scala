package com.meekrab.rsseater

import scalaj.http._
import scala.xml._
import sys.process._
import java.io._

/*
 * Based trait for RSS, Atom, and Podcast feeds
 */
trait Feed

/*
 * Singleton factory that creates new instances of RSS and Atom feeds
 */
object Feeder {

  /*
   * Contains RSS Feed elements
   * @param feed: xml feed
   */
  class RSSFeed(feed: scala.xml.NodeSeq) extends Feed {
    private val channel = feed \ "channel"
    val title = (channel \ "title").text
    val link = (channel \ "link").text
    val category = (channel \ "category").text
    val desc = (channel \ "description").text
    val image = Image(channel \ "image")

    val items: List[Item] = 
      (for(item <- (channel \ "item")) yield { Item(item) }).toList

    /*
     * Writes XML feed to file for local storage
     * @param path: Path to file location
     */
    def store(path: String) = {
      val writer = new PrintWriter(new File(path))
      writer.write(feed.toString)
      writer.close
    }

    /*
     * Contains image elements
     * @param image: Image node
     */
    case class Image(image: scala.xml.NodeSeq) {
      val title = (image \ "title").text
      val url = (image \ "url").text
      val link = (image \ "link").text
      val desc = (image \ "description").text
      val width = (image \ "width").text
      val height = (image \ "height").text
    }

    /*
     * Contains item elements
     * @param item: Item node
     */
    case class Item(item: scala.xml.NodeSeq) {
      val title = (item \ "title").text
      val link = (item \ "link").text
      val desc = (item \ "description").text

      val thumbs = 
        (for(thumb <- (item \ "thumbnail")) 
          yield { Thumb((thumb \ "@url").text, 
                        (thumb \ "@width").text, 
                        (thumb \ "@height").text)
        }).toList

      val pubDate = (item \ "pubDate").text
      val category = (item \ "category").text
    }

    /*
     * Contains thumbnail elements
     * @param url: URL of thumbnail
     * @param width: Width of thumbnail
     * @param height: Height of thumbnail
     */
    case class Thumb(url: String, width: String, height: String)
  }

  /*
   * Contains Atom Feed elements
   * @param feed: xml feed
   */
  class AtomFeed(feed: scala.xml.NodeSeq) extends Feed {
    val title = (feed \ "title").text
    val link = (feed \ "link" \ "@href").text
    val id = (feed \ "id").text
    val desc = (feed \ "description").text

    val entries: List[Entry] = 
      (for(entry <- (feed \ "entry")) yield { Entry(entry) }).toList

    /*
     * Writes XML feed to file for local storage
     * @param path: Path to file location
     */
    def store(path: String) = {
      val writer = new PrintWriter(new File(path))
      writer.write(feed.toString)
      writer.close
    }

    /*
     * Contains entry elements
     * @param item: Item Node
     */
    case class Entry(item: scala.xml.NodeSeq) {
      val title = (item \ "title").text
      val id = (item \ "id").text
      val link = (item \ "link" \ "@href").text
      val updated = (item \ "updated").text
      val summary = (item \ "summary").text
    }
  }
  
  /*
   * Creates a new instance of RSSFeed (either from a local file or a feed)
   * @param path: Directory path or URL
   * @param kind: Flag for feed/local
   * @return new RSSFeed instance
   */
  def createRSS(path: String, kind: String = "feed"): RSSFeed = kind match  {
    case "feed" => new RSSFeed(Http(path).option(HttpOptions.connTimeout(100000000))
                                         .option(HttpOptions.readTimeout(500000000)).asXml)
    case "local" => new RSSFeed(XML.load(path))
    case _ => throw new RuntimeException("bad kind")
  }

  /*
   * Creates a new instance of AtomFeed (either from a local file or a feed)
   * @param path: Directory path or URL
   * @param kind: Flag for feed/local
   * @return new AtomFeed instance
   */
  def createAtom(path: String, kind: String = "feed"): AtomFeed = kind match  {
    case "feed" => new AtomFeed(Http(path).option(HttpOptions.connTimeout(100000000))
                                          .option(HttpOptions.readTimeout(500000000)).asXml)
    case "local" => new AtomFeed(XML.load(path))
    case _ => throw new RuntimeException("bad kind")
  }

  private def matcher(feed: scala.xml.NodeSeq) = feed match {
    case Seq(<rss>{x @ _*}</rss>) => new RSSFeed(feed)
    case Seq(<feed>{x @ _*}</feed>) => new AtomFeed(feed)
    case _ => throw new RuntimeException("bad format")
  }
}         