package com.aktic.linkWhiz_backend;

import com.aktic.linkWhiz_backend.repository.PlanRepository;
import com.aktic.linkWhiz_backend.repository.RoleRepository;
import com.aktic.linkWhiz_backend.service.fileStorage.FileStorageService;
import com.aktic.linkWhiz_backend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class LinkWhizBackendApplication implements CommandLineRunner {

    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final PlanRepository planRepository;
    private final RoleRepository roleRepository;
    private final FileStorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(LinkWhizBackendApplication.class, args);

    }


    @Override
    public void run(String... arg) throws Exception {
        storageService.init();
    }
//
//    @Bean
//    CommandLineRunner insertRoles() {
//        return args -> {
//            Role user = Role.builder()
//                    .id(snowflakeIdGenerator.nextId())
//                    .roleName("USER")
//                    .description("This is a user role")
//                    .build();
//
//            Role admin = Role.builder()
//                    .id(snowflakeIdGenerator.nextId())
//                    .roleName("ADMIN")
//                    .description("This is an admin role")
//                    .build();
//
//            if (roleRepository.count() == 0) { // Avoid duplicate inserts
//                List<Role> roles = List.of(user, admin);
//                roleRepository.saveAll(roles);
//                System.out.println("Predefined roles inserted successfully.");
//            }
//        };
//    }
//
//
//    @Bean
//    CommandLineRunner insertPlans() {
//        return args -> {
//            Plan free = Plan.builder()
//                    .id(snowflakeIdGenerator.nextId())
//                    .planName("Free")
//                    .description("This is a free plan")
//                    .maxLinks(new BigInteger("100"))
//                    .price(new BigDecimal("9.99"))
//                    .build();
//
//            Plan basic = Plan.builder()
//                    .id(snowflakeIdGenerator.nextId())
//                    .planName("Basic")
//                    .description("This is a basic plan")
//                    .maxLinks(new BigInteger("10000"))
//                    .price(new BigDecimal("19.99"))
//                    .build();
//
//            Plan pro = Plan.builder()
//                    .id(snowflakeIdGenerator.nextId())
//                    .planName("Pro")
//                    .description("This is a pro plan")
//                    .maxLinks(null)
//                    .price(new BigDecimal("29.99"))
//                    .build();
//
//
//            if (planRepository.count() == 0) { // Avoid duplicate inserts
//                List<Plan> plans = List.of(free, basic, pro);
//                planRepository.saveAll(plans);
//                System.out.println("Predefined plans inserted successfully.");
//            }
//        };
//    }

}
