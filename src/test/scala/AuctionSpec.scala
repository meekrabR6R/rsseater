package com.meekrab.auction

import org.scalatest.FunSpec

trait TestSets {
  val auction = new Auction
  auction.add("Skyrim", BigDecimal(19.99))
}

class AuctionSpec extends FunSpec {
  new TestSets {

    describe("Adding Skyrim to auctionable items") {
      it("should return AuctionableItem('Skyrim', 19.99) as head of list"){
        assert(auction.items.head.get("name") === "Skyrim")
      }
    }
    
    describe("After item is added") {
      it("status should be 'pending'") {
        assert(auction.status("Skyrim").get("status") === Some("pending"))
      }

      it("should set have a reserved price of 19.99") {
        assert(auction.status("Skyrim").get("reservedPrice") === Some(BigDecimal(19.99)))
      }
    }

    describe("Starting auction on Skyrim") {
      it("should set status to 'started'") {
        auction.start("Skyrim")
        assert(auction.status("Skyrim").get("status") === Some("started"))
      }
    }
    
    describe("Placing a sufficiently large bid on an item") {
      it("should set bid to new bid amount") {
        auction.submitBid("Skyrim", BigDecimal(5.00), "Frank")
        assert(auction.status("Skyrim").get("currBid") === Some(BigDecimal(5.00)))
      }

      it("buyer name should be 'Frank.'") {
        assert(auction.status("Skyrim").get("buyer") === Some("Frank"))
      }
    }
    
    describe("Placing an insufficient bid") {
      it("should throw InsufficientBidException") {
        intercept[InsufficientBidException](auction.submitBid("Skyrim", BigDecimal(3.00), "Frank"))
      }
    }
    
    describe("Attempting to add another item to auction list") {
      it("should throw ItemAlreadyInAuctionException if item is already available for auction") {
        intercept[ItemAlreadyInAuctionException](auction.add("Skyrim", BigDecimal(21.45)))
      }

      it("should add 'World of Smurfcraft.'") {
        auction.add("World of Smurfcraft", BigDecimal(25.99)) 
        assert(auction.get("World of Smurfcraft").get("name").toString === "World of Smurfcraft")
      }
    }
    
    describe("A successful auction") {
      it("should set status to 'success.'") {
        auction.start("World of Smurfcraft")
        auction.submitBid("World of Smurfcraft", BigDecimal(30.00), "Freddy")
        auction.call("World of Smurfcraft")

        assert(auction.status("World of Smurfcraft").get("status") === Some("success"))
      }

      it("should have a final bid amount of '30.00.'") {
        assert(auction.status("World of Smurfcraft").get("finalBid") === Some(BigDecimal(30.00)))
      }

      it("should have a buyer named 'Freddy'") {
        assert(auction.status("World of Smurfcraft").get("buyer") === Some("Freddy"))
      }
    }

    describe("Calling auction on Skyrim with a low bid") {
      it("should set 'status' to failure.") {
        auction.call("Skyrim")
        assert(auction.status("Skyrim").get("status") === Some("failure"))
        AuctionStore.items.drop()
      }
    }
  }
}

