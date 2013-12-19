package com.meekrab.rsseater

import scalaj.http._
import scala.xml._
import sys.process._
import java.io._

trait Feed {
  val title: String
  def store(path: String)
}

trait Elem

case class Thumb(url: String, width: String, height: String)

case class Entry(item: scala.xml.Node) extends Elem

case class Item(item: scala.xml.Node) extends Elem {
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

case class Image(image: scala.xml.NodeSeq) {
  val title = (image \ "title").text
  val url = (image \ "url").text
  val link = (image \ "link").text
  val desc = (image \ "description").text
  val width = (image \ "width").text
  val height = (image \ "height").text
}

class RSSFeed(feed: scala.xml.NodeSeq) extends Feed {
  private val channel = feed \ "channel"
  val title = (channel \ "title").text
  val link = (channel \ "link").text
  val category = (channel \ "category").text
  val desc = (channel \ "description").text
  val image = Image(channel \ "image")

  val items: List[Item] = 
    (for(item <- (channel \ "item")) yield { Item(item) }).toList

  def store(path: String) = {
    val writer = new PrintWriter(new File(path))
    writer.write(feed.toString)
    writer.close
  }
}

class AtomFeed(atomFeed: scala.xml.NodeSeq) extends Feed {
  private val feed = atomFeed \ "feed"
  val title = (feed \ "title").text
  val link = (feed \ "link").text
  val id = (feed \ "id").text
  val desc = (feed \ "description").text
  def store(path: String) = Unit
}

case class FeedReader(url: String) extends RSSFeed(Http(url).option(HttpOptions.connTimeout(100000000))
                                                        .option(HttpOptions.readTimeout(500000000)).asXml)
case class LocalReader(path: String) extends RSSFeed(XML.load(path))