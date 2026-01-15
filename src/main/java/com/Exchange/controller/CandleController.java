package com.Exchange.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Exchange.model.Candle;
import com.Exchange.response.ApiResponse;
import com.Exchange.response.CandleResponse;
import com.Exchange.service.CandleService;



@RestController
@RequestMapping
public class CandleController 
{
    @Autowired
    private CandleService candleService;

     @GetMapping("/{baseAsset}/{quoteAsset}")
    public ResponseEntity<ApiResponse<List<CandleResponse>>> getCandles(
        @PathVariable String baseAsset,
            @PathVariable String quoteAsset,
            @RequestParam(defaultValue = "1m") String interval,
            @RequestParam(defaultValue = "100") int limit)
    {
        try 
        {
            List<Candle> candles=candleService.getCandles(interval, baseAsset, quoteAsset, limit);
            List<CandleResponse> candleResponses=candles.stream().map(c->convertToResponse(c)).collect(Collectors.toList());
            ApiResponse<List<CandleResponse>> response=ApiResponse.success(candleResponses);
            return new ResponseEntity<>(response,HttpStatus.OK);
            
        }
        catch (IllegalArgumentException e) 
        {
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Invalid Intervak: " + e.getMessage()));
        } 
         catch (Exception e) {
             return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to fetch candles: " + e.getMessage()));
        }
    }

    @GetMapping("/{baseAsset}/{quoteAsset}/range")
    public ResponseEntity<ApiResponse<List<CandleResponse>>> getCandlesInRange(
            @PathVariable String baseAsset,
            @PathVariable String quoteAsset,
            @RequestParam(defaultValue = "1m") String interval,
            @RequestParam long startTime,
            @RequestParam long endTime)
    {

        try 
        {
            List<Candle> candles=candleService.getCandlesInRange(baseAsset, quoteAsset, interval, startTime, endTime);
            List<CandleResponse> candleResponses=candles.stream().map(c->convertToResponse(c)).collect(Collectors.toList());
            ApiResponse<List<CandleResponse>> response=ApiResponse.success(candleResponses);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (IllegalArgumentException e) 
        {
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Invalid parameters: " + e.getMessage()));
        }
        catch (Exception e) 
        {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to fetch candles: " + e.getMessage()));
        }


    }

    private CandleResponse convertToResponse(Candle candle) 
    {
        return CandleResponse.builder()
            .baseAsset(candle.getBaseAsset())
            .quoteAsset(candle.getQuoteAsset())
            .interval(candle.getInterval())
            .openTime(candle.getOpenTime())
            .closeTime(candle.getCloseTime())
            .open(candle.getOpen())
            .high(candle.getHigh())
            .low(candle.getLow())
            .close(candle.getClose())
            .volume(candle.getVolume())
            .numberOfTrades(candle.getNumberOfTrades())
            .build();
    }

}
