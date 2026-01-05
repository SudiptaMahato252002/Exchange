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
import com.Exchange.response.TradeResponse;
import com.Exchange.service.TradeService;

@RestController
@RequestMapping("/api/trades")
public class TradeController 
{
    @Autowired
    private TradeService tradeService;

    @GetMapping("/{baseAsset}/{quoteAsset}")
    public ResponseEntity<ApiResponse<List<TradeResponse>>> getRecentTrades(
            @PathVariable String baseAsset,
            @PathVariable String quoteAsset,
            @RequestParam(defaultValue = "50") int limit)
    {
        List<TradeResponse> trades=tradeService.getRecentTrades(baseAsset, quoteAsset, limit);
        ApiResponse<List<TradeResponse>> response=ApiResponse.success(trades);
        return new ResponseEntity<>(response,HttpStatus.OK);        
    }
    
}
