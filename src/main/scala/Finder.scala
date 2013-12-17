package com.meekrab.rsseater

import play.api.libs.json._
import play.api.libs.functional._
import scalaj.http._

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

  case class FeedList(json: JsValue) {
    val responseData = json \ "responseData"
    val query = (responseData \ "query").as[String]
    val entries = (responseData \ "entries" \\ "url")
  }
}
