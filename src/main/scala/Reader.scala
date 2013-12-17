package com.meekrab.rsseater

import scalaj.http.Http
import scala.xml._
import sys.process._
import java.io._

case class Thumb(url: String, width: String, height: String)

case class Item(item: scala.xml.Node) {
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

class FeedReader(feed: scala.xml.NodeSeq) {
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

case class Feed(url: String) extends FeedReader(Http(url).asXml)
case class Local(path: String) extends FeedReader(XML.load(path))