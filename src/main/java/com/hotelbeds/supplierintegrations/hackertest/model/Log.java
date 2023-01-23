package com.hotelbeds.supplierintegrations.hackertest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    
    private String ipAddress;
    private LocalDateTime localDateTime;
    private LoginActionEnum loginAction;
    private String userName;
    
}
