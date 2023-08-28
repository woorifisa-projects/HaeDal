package com.haedal.backend.profile.model;

import org.springframework.expression.spel.ast.OpNE;

import java.util.concurrent.ThreadPoolExecutor;

public enum UserAgeGroup {
    ONE, // 30대 이하
    TWO, // 30-40대
    THREE, // 40-50대
    FOUR, // 50-60대
    FIVE, // 60대 이상

    ONETWOTHREEFOURFIVE; // 전 연령대
}
