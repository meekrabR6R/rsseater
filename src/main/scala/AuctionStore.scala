package com.meekrab.auction

import com.mongodb.casbah.Imports._

/*
  Custom exceptions for auction library
*/
case class ItemAlreadyInAuctionException(message: String) extends RuntimeException 
case class InsufficientBidException(message: String) extends RuntimeException 
case class ItemNotAvailableException(message: String) extends RuntimeException 

/*
  Singleton for utility methods for interacting with mongo datastore.
*/
object AuctionStore {
	
	private val mongoClient = MongoClient("localhost", 27017)
  private val db = mongoClient("auction")
	val items = db("items")
	
	private def updateItem(item: String, field: String, update: String) =
    items.update(MongoDBObject("name" -> item), $set(field -> update))
  
  def getItem(item: String) = items.findOne(MongoDBObject("name" -> item))

	def insert(item: Map[String, Any]) = items.insert(item)


  def setStatus(item: String, status: String) = updateItem(item, "status", status)

  def updateBid(item: String, bid: BigDecimal, buyer: String) = {
    updateItem(item, "bid", bid.toString)
    updateItem(item, "buyer", buyer)
  }
  
  def buyer(item: String) = getItem(item).get("buyer").toString

  def currBid(item: String) = getItem(item).get("bid").toString
  
  def reservedPrice(item: String) = getItem(item).get("reservedPrice").toString

  def status(item: String) = getItem(item).get("status").toString
}