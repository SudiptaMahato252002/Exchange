package com.Exchange.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Exchange.response.ApiResponse;
import com.Exchange.response.OrderBookResponse;
import com.Exchange.service.OrderBookService;

@RestController
@RequestMapping("/api/orderbook")
public class OrderBookController 
{
    @Autowired
    private OrderBookService orderBookService;
    
    @GetMapping("/{baseAsset}/{quoteAsset}")
    public ResponseEntity<ApiResponse<OrderBookResponse>> getOrderBook(
            @PathVariable String baseAsset,
            @PathVariable String quoteAsset,
            @RequestParam(defaultValue = "10") int depth)
    {
        OrderBookResponse orderBookResponse=orderBookService.getOrderBookSnapshot(baseAsset, quoteAsset, depth);
        ApiResponse<OrderBookResponse> response=ApiResponse.success(orderBookResponse);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/symbols")
    public ResponseEntity<ApiResponse<List<String>>> getAvailableSymbols() 
    {
        List<String> symbols = orderBookService.getAllSymbols();
        return ResponseEntity.ok(ApiResponse.success(symbols));
    }



}
