package com.nova.mq.kafka.controller;

import com.nova.common.core.model.result.ResResult;
import com.nova.mq.kafka.config.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description: kafka-Controller
 * @date: 2025/03/21 16:57
 */
@Slf4j(topic = "KafkaController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class KafkaController {

    private final KafkaService kafkaService;

    @GetMapping("/kafka")
    public ResResult<Void> kafka() {
        kafkaService.sendMessage("topicA","你好");
        return ResResult.success();
    }

}
