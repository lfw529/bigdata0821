package com.lfw.flink.adv_practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginEvent {
    private Long userId;
    private String ip;
    private String eventType;
    private Long eventTime;
}