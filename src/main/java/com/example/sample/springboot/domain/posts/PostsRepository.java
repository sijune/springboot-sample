package com.example.sample.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//JPA Repo: Posts 클래스로 Database 접근을 가능하게 해준다.
//DAO처럼 DB Layer와 비슷
//인터페이스로 생성한다.
//JpaRepository를 상속하면 기본적인 CRUD메소드가 자동으로 생성된다.
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

}
