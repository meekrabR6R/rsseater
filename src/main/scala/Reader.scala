package com.meekrab.rsseater

import scalaj.http.Http
import scala.xml._

case class Thumb(url: String, width: String, height: String)

case class Item(item: scala.xml.Node) {
  val title = (item \ "title").text
  val link = (item \ "link").text
  val desc = (item \ "description").text
  val thumb = Thumb((item \\ "@url").toString, 
                    (item \\ "@width").toString, 
                    (item \\ "@height").toString)
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
}

case class Feed(url: String) extends FeedReader(Http(url).asXml)
case class Local(path: String) extends FeedReader(XML.load(path))

