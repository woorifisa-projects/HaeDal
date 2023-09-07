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
    //메세지를 받을 전화번호
    String to;
    //메세지 내용
    String content;

    public static MessageDTO from(String to, String content){
        MessageDTO messageDTO=MessageDTO.builder()
                .to(to)
                .content(content)
                .build();
        return messageDTO;
    }

}
