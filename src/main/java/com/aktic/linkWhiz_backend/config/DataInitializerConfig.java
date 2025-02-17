package com.aktic.linkWhiz_backend.config;

import com.aktic.linkWhiz_backend.model.entity.Plan;
import com.aktic.linkWhiz_backend.model.entity.Role;
import com.aktic.linkWhiz_backend.repository.PlanRepository;
import com.aktic.linkWhiz_backend.repository.RoleRepository;
import com.aktic.linkWhiz_backend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.BigInteger;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializerConfig {
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final PlanRepository planRepository;
    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner insertRoles() {
        return args -> {
            try {
                if (!roleRepository.existsByRoleName("USER")) {
                    Role user = Role.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .roleName("USER")
                            .description("This is a user role")
                            .build();
                    roleRepository.save(user);
                    log.info("USER role inserted successfully.");
                }

                if (!roleRepository.existsByRoleName("ADMIN")) {
                    Role admin = Role.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .roleName("ADMIN")
                            .description("This is an admin role")
                            .build();
                    roleRepository.save(admin);
                    log.info("ADMIN role inserted successfully.");
                }
            } catch (Exception e) {
                log.error("Error inserting roles: " + e.getMessage());
            }
        };
    }

    @Bean
    CommandLineRunner insertPlans() {
        return args -> {
            try {
                if (!planRepository.existsByPlanName("Free")) {
                    Plan free = Plan.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .planName("Free")
                            .description("This is a free plan")
                            .maxLinks(new BigInteger("100"))
                            .price(new BigDecimal("9.99"))
                            .build();
                    planRepository.save(free);
                    log.info("Free plan inserted successfully.");
                }

                if (!planRepository.existsByPlanName("Basic")) {
                    Plan basic = Plan.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .planName("Basic")
                            .description("This is a basic plan")
                            .maxLinks(new BigInteger("10000"))
                            .price(new BigDecimal("19.99"))
                            .build();
                    planRepository.save(basic);
                    log.info("Basic plan inserted successfully.");
                }

                if (!planRepository.existsByPlanName("Pro")) {
                    Plan pro = Plan.builder()
                            .id(snowflakeIdGenerator.nextId())
                            .planName("Pro")
                            .description("This is a pro plan")
                            .maxLinks(null)
                            .price(new BigDecimal("29.99"))
                            .build();
                    planRepository.save(pro);
                    log.info("Pro plan inserted successfully.");
                }

            } catch (Exception e) {
                log.error("Error inserting plans: " + e.getMessage());
            }
        };
    }

}
