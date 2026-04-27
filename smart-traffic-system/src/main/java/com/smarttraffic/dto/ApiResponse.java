package com.smarttraffic.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(int status, String message, T data) {
        this.status    = status;
        this.message   = message;
        this.data      = data;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus()             { return status; }
    public String getMessage()         { return message; }
    public T getData()                 { return data; }
    public LocalDateTime getTimestamp(){ return timestamp; }
}