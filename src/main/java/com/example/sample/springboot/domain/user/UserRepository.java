package com.example.sample.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    //Optional 클래스: 래퍼 클래스로 User가 NULL이라도 채워서 객체로 생성한다. 즉 예외를 따로 발생시키지 않는다.
}
