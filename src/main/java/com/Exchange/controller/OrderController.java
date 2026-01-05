package com.Exchange.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Exchange.request.CancelOrderRequest;
import com.Exchange.request.PlaceOrderRequest;
import com.Exchange.response.ApiResponse;
import com.Exchange.response.OrderResponse;
import com.Exchange.service.OrderService;


@RestController
@RequestMapping("/api/orders")
public class OrderController 
{
    @Autowired
    private OrderService orderService;

    @PostMapping()
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(@RequestBody PlaceOrderRequest request)
    {
        try 
        {
            OrderResponse response=orderService.placeOrder(request);
            ApiResponse<OrderResponse> apiResponse=ApiResponse.success(response, "Order placed Successfully");
            return new ResponseEntity<>(apiResponse,HttpStatus.OK);
        } 
        catch (Exception e) 
        {
            ApiResponse<OrderResponse> response=ApiResponse.error("Failed to place order: " + e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Boolean>> cancelOrder(@RequestBody CancelOrderRequest request)
    {
        try 
        {
            boolean cancelled=orderService.cancelOrder(request);
            if(cancelled)
            {
                ApiResponse<Boolean> response=ApiResponse.success(true, "Order cancelled Successfully");
                return new ResponseEntity<>(response,HttpStatus.OK);
            }
            else
            {
                ApiResponse<Boolean> response=ApiResponse.error("Order not found");
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }
            
        } 
        catch (Exception e) 
        {
            ApiResponse<Boolean> response=ApiResponse.error("Failed to cnacel order");
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(
        @PathVariable String orderId,
        @RequestParam String baseAsset,
        @RequestParam String quoteAsset)
    {
        Optional<OrderResponse> order=orderService.getOrder(baseAsset, quoteAsset, orderId);
        if(order.isPresent())
        {
            ApiResponse<OrderResponse> response=ApiResponse.success(order.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else
        {
            ApiResponse<OrderResponse> response=ApiResponse.error("Order not found");
            return new ResponseEntity<ApiResponse<OrderResponse>>(response,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getUserOrders(
            @PathVariable String userId,
            @RequestParam String baseAsset,
            @RequestParam String quoteAsset) 
    {
        List<OrderResponse> orders=orderService.getuserOrders(baseAsset, quoteAsset, userId);
        ApiResponse<List<OrderResponse>> response=ApiResponse.success(orders);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

}
