package com.haedal.backend.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder // AllArgsConstructor이 존재해야 가능
public class MessageDTO {
    String to;
    String content;
}
