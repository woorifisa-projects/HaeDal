package com.haedal.backend.sms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haedal.backend.sms.dto.MessageDTO;
import com.haedal.backend.sms.dto.response.SmsResponseDTO;
import com.haedal.backend.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @GetMapping("/send")
    public String getSmsPage() {
        return "sendSms";
    }

    @PostMapping("/sms/send")
    public String sendSms(@RequestBody MessageDTO messageDto, Model model) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println("요청은 들어옴");
        log.info(String.valueOf(messageDto));
        System.out.println(messageDto);
        SmsResponseDTO response = smsService.sendSms(messageDto);
        model.addAttribute("response", response);
        return "result";
    }
}
