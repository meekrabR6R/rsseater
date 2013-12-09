package com.meekrab.auction

/*
  A basic back end library for an online auction house. Contains various methods
  for auction operations.
*/
class Auction {
  
  /*
    Gets a list of auction items
    @return list of auction items
  */
  def items = AuctionStore.items.toList
  
  /*
    Adds a new item to the auction.
    @param name item name
    @param reservedPrice reservedPrice of item
  */
	def add(name: String, reservedPrice: BigDecimal) = AuctionStore.getItem(name) match {
	  case Some(_) => throw ItemAlreadyInAuctionException("item")
	  case None => AuctionStore.insert(Map("name" -> name, 
	  	                                   "reservedPrice" -> reservedPrice.toString,
	  	                                   "bid" -> "0",
	  	                                   "status" -> "pending",
	  	                                   "buyer" -> ""
	  	                              ))
  }

  /*
    Starts an auction on an auctionable item.
    @param item item to be auctioned
  */
	def start(item: String) = status(item).get("status") match {
    case Some("pending") => AuctionStore.setStatus(item, "started")
    case Some("started") => throw ItemNotAvailableException("Item already under auction.")
    case _ => throw ItemNotAvailableException("Cannot auction this item.")
  }

  /*
    Calls auction on item.
    @param item item auction is called on
  */
  def call(item: String) = 
    if (BigDecimal(AuctionStore.currBid(item)) > BigDecimal(AuctionStore.reservedPrice(item))) 
      AuctionStore.setStatus(item, "success")
    else 
      AuctionStore.setStatus(item, "failure")
	
	/*
	  Submits a valid bid on an auctionable item.
	  @param item item up for auction
	  @param bid amount bidded on item
	  @param buyer person making bid
	*/
	def submitBid(item: String, bid: BigDecimal, buyer: String) = status(item).get("status") match {
		case Some("started") => if (bid <= BigDecimal(AuctionStore.currBid(item))) 
                              throw InsufficientBidException("Bid must be greater than highest bid.")
	                          else AuctionStore.updateBid(item, bid, buyer)
	  case _ => throw ItemNotAvailableException("Cannot place bid on this item.")
	}
    
  /*
    Gets status of item
    @param item item status requested for
    @return map of status key/values of item
  */
	def status(item: String) = AuctionStore.status(item) match {
		case "success" => Map("status" -> "success", "buyer" -> AuctionStore.buyer(item),
		                      "finalBid" -> BigDecimal(AuctionStore.currBid(item)))
		case "failure" => Map("status" -> "failure", "finalBid" -> AuctionStore.currBid(item))
		case "started" => Map("status" -> "started", "currBid" -> BigDecimal(AuctionStore.currBid(item)),
			                    "buyer" -> AuctionStore.buyer(item)) 
		case "pending" => Map("status" -> "pending", "reservedPrice" -> BigDecimal(AuctionStore.reservedPrice(item)))
	} 

	def get(item: String) = AuctionStore.getItem(item) match {
	  case Some(x) => x
	  case None => throw ItemNotAvailableException("Item not in store.")
	}
}