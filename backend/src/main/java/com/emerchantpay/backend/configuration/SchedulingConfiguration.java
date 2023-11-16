package com.emerchantpay.backend.configuration;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync(mode = AdviceMode.ASPECTJ)
public class SchedulingConfiguration {

}
