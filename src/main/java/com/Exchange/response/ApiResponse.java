package com.Exchange.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> 
{
    private Boolean success;
    private String message;
    private long timestamp;
    private T data;

    public static <T> ApiResponse<T> success(T data)
    {
        return ApiResponse.<T>builder()
            .success(true)
            .message("Operation Successful")
            .timestamp(System.currentTimeMillis())
            .data(data)       
            .build();

    } 

    public static <T> ApiResponse<T> success(T data,String message)
    {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .data(data)
        .build();
    }

    public static <T> ApiResponse<T> error(String message)
    {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .timestamp(System.currentTimeMillis())
        .build();
        
    }

    
}
