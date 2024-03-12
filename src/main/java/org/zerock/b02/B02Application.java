package org.zerock.b02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//  AuditingEntityListener Spring JPA가 데이터베이스에 넣거나 변경할 때 자동으로 시간 값을 지정할 수 있도록 함
@EnableJpaAuditing
public class B02Application {

    public static void main(String[] args) {
        SpringApplication.run(B02Application.class, args);
    }

}
