package com.denidove.trading.config;

import com.denidove.trading.aspects.SavingProductAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = {"com.denidove.trading.services", "com.denidove.trading.aspects"})
@EnableAspectJAutoProxy
public class ProjectConfig {

}
