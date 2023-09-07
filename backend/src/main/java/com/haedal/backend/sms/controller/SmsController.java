package com.haedal.backend.sms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haedal.backend.log.model.Log;
import com.haedal.backend.log.model.LogType;
import com.haedal.backend.log.repository.LogRepository;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.sms.dto.MessageDTO;
import com.haedal.backend.sms.dto.response.SmsResponseDTO;
import com.haedal.backend.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@EnableScheduling
@Transactional
public class SmsController {

    private final SmsService smsService;
    private final ProductService productService;
    private final LogRepository logRepository;

    @GetMapping("/send")
    public String getSmsPage() {
        return "sendSms";
    }

    @PostMapping("/sms/send")
    public String sendSmsTest(@RequestBody MessageDTO messageDto, Model model) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println("요청은 들어옴");
        log.info(String.valueOf(messageDto));
        System.out.println(messageDto);
        SmsResponseDTO response = smsService.sendSms(messageDto);
        model.addAttribute("response", response);
        return "result";
    }

    //로그 설명에서 product를 추출하는 메서드
    Product findProductFromDesc(String logDesc) {
        String[] logDescParts = logDesc.split(" ");
        if (logDescParts.length >= 3) {
            String productIdString = logDescParts[1];
            Long productId = Long.parseLong(productIdString);

            Product product = productService.findByProductId(productId);
            return product;
        }
        return null;
    }


    @Scheduled(cron= "0 0 1 3 * ?")// 매주 3일 1시에 실행
    @PostMapping("/sms/dibs/send")
    public void sendDibsSmS() throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        // 1. 6시간 이전 로그 조회
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(9);
        LocalDateTime sixHoursAgo = currentTime.minusHours(12);

        // 현재 시간과 6시간 전 사이의 로그 기록들 조회
        List<Log> logs = logRepository.findByLogTimeBetween(sixHoursAgo, currentTime);

        // 2. DIBS 목록 및 사용자 조회
        List<Log> dibsLogs = logs.stream()
                .filter(log -> log.getLogType() == LogType.DIB)
                .collect(Collectors.toList());

        // userId와 productId를 맵핑하는 Map 생성
        Map<Long, Set<Product>> userIdToProducts = new HashMap<>();

        // 이미 SMS를 보낸 고객을 추적하는 Set 생성
        Set<Long> sentSmsCustomers = new HashSet<>();

        System.out.println(sentSmsCustomers);

        for (Log log : dibsLogs) {
            Long userId = log.getUser().getUserId();

            // 이미 SMS를 보낸 고객인지 확인
            if (sentSmsCustomers.contains(userId)) {
                continue; // 이미 SMS를 보냈다면 다음 로그로 이동
            }

            Product product = findProductFromDesc(log.getLogDesc());

            // userIdToProducts에서 해당 userId의 Set을 가져옴
            Set<Product> userProducts = userIdToProducts.getOrDefault(userId, new HashSet<>());

            // 현재 로그에서 얻은 product을 Set에 추가
            userProducts.add(product);

            // userIdToProducts에 업데이트
            userIdToProducts.put(userId, userProducts);
        }

        // 3. 현재 로그와 이전 로그 비교
        for (Log log : dibsLogs) {
            Long userId = log.getUser().getUserId();
            Set<Product> userProducts = userIdToProducts.get(userId); // 해당 사용자의 찜한 Product 목록 가져오기

            // DIBCANCEL 또는 SUBSCRIBE 로그가 없는 Product만 남기기
            userProducts.removeIf(product -> logs.stream()
                    .anyMatch(l -> (l.getLogType() == LogType.CANCELDIB || l.getLogType() == LogType.SUBSCRIBE)
                            && l.getUser().getUserId().equals(userId)
                            && product.equals(findProductFromDesc(l.getLogDesc()))));

            if (!userProducts.isEmpty()) {
                // userProducts Set에서 랜덤으로 하나의 Product을 선택
                List<Product> productList = new ArrayList<>(userProducts);
                Random random = new Random();
                Product randomProduct = productList.get(random.nextInt(productList.size()));

                // SMS 전송 및 정보 저장
                String userPhoneNumber = log.getUser().getPhoneNumber();
                String content
                        = log.getUser().getName() + "고객님 안녕하세요, 해달 입니다!" + randomProduct.getProductName()+"관심등록 상품을 기억해주세요:)";
                MessageDTO messageDTO = new MessageDTO(userPhoneNumber, content);
                SmsResponseDTO response = smsService.sendSms(messageDTO);
                System.out.println(userPhoneNumber + content);

                // SMS를 보냈음을 표시
                sentSmsCustomers.add(userId);

                // 해당 고객에게 SMS가 전송되었으므로 이후 고객은 처리하지 않도록 하려면 다음 줄을 추가합니다.
                userProducts.clear();
            }
        }
        System.out.println("전송 완료");
    }
}