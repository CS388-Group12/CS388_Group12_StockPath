package com.example.cs388_group12_stockpath

class Candlestick {
    var timestamp: Long = 0
    var open: Double = 0.0
    var high: Double = 0.0
    var low: Double = 0.0
    var close: Double = 0.0
    var volume: Double = 0.0

    constructor()

    constructor(
        timestamp: Long,
        open: Double,
        high: Double,
        low: Double,
        close: Double,
        volume: Double
    ) {
        this.timestamp = timestamp
        this.open = open
        this.high = high
        this.low = low
        this.close = close
        this.volume = volume
    }
    constructor(candlestick: Candlestick) { //may be needed
        this.timestamp = candlestick.timestamp
        this.open = candlestick.open
        this.high = candlestick.high
        this.low = candlestick.low
        this.close = candlestick.close
        this.volume = candlestick.volume
    }

}