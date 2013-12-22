package com.meekrab.rsseater

import play.api.libs.json._
import play.api.libs.functional._
import scalaj.http._

/*
 * Singleton which queries Google's Feed API and returns a JSON formatted
 * list of results. 
 */
object GoogleFeedFinder {
  def apply(query: String) = { 
    try {
      val feeds = Http("https://ajax.googleapis.com/ajax/services/feed/find?v=1.0")
                  .param("q", query)
                  .option(HttpOptions.connTimeout(100000000))
                  .option(HttpOptions.readTimeout(500000000))
                  .option(HttpOptions.allowUnsafeSSL).asString

      FeedList(Json.parse(feeds))
    }
    catch {
      case ste: java.net.SocketTimeoutException => 
        FeedList(Json.toJson(Map("responseStatus" -> "Read timed out")))
      case he: scalaj.http.HttpException => 
        FeedList(Json.toJson(Map("responseStatus" -> 400)))
    }
  }

  /*
   * Hold feed list elements
   * @param json: JSON returned from Google Feed API
   */
  case class FeedList(json: JsValue) {
    val responseData = json \ "responseData"
    val query = (responseData \ "query").as[String]
    val entries = (responseData \ "entries").as[List[JsValue]].map({i => Entry((i \ "url").as[String], 
                                                                               (i \ "title").as[String],
                                                                               (i \ "contentSnippet").as[String],
                                                                               (i \ "link").as[String]
                                                                  )})
  }

  /*
   * Contains entry elements
   * @param url: URL of entry
   * @param title: Title of entry
   * @param contentSnippet: Snippet of entry
   * @param link: Link to source
   */
  case class Entry(url: String, title: String, contentSnippet: String, link: String)
}
