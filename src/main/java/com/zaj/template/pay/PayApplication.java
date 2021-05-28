package com.zaj.template.pay;

import com.zaj.template.pay.config.AlipayConfig;
import com.zaj.template.pay.util.AliPayUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }

    @Bean
    @ConfigurationProperties(prefix = "alipay")
    public AlipayConfig configAlipayConfig() {
        return new AlipayConfig();
    }

    @Bean
    public AliPayUtil configAlipayUtil() {
        return new AliPayUtil();
    }
}
