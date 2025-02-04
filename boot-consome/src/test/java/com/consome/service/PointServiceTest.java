package com.consome.service;

import com.consome.domain.PointHistory;
import com.consome.domain.User;
import com.consome.repository.CurrentPointRepository;
import com.consome.repository.PointHistoryRepository;
import com.consome.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class PointServiceTest {
    @Autowired UserService userService;
    @Autowired PointService pointService;
    @Autowired UserRepository userRepository;
    @Autowired PointHistoryRepository pointHistoryRepository;
    @Autowired CurrentPointRepository currentPointRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void 포인트_차감() throws Exception{
        //given
        User user = User.createUser("zero0515","test","jin","zero0515@gmail.com","1234","01091940785",passwordEncoder);
        Long saveId = userService.register(user);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()-> {
                pointService.updatePoint(saveId,-101,"테스트 차감");
        });

        //then
        assertEquals("포인트가 부족하여 차감할 수 없습니다.",exception.getMessage());
    }

    @Test
    public void 포인트_조회() throws Exception{
        //given
        User user = User.createUser("zero0515","test","jin","zero0515@gmail.com","1234","01091940785",passwordEncoder);
        Long saveId = userService.register(user);
        pointService.updatePoint(saveId,-50,"테스트 차감");
        pointService.updatePoint(saveId,-30,"테스트 차감2");

        //when
        int currentPoint = currentPointRepository.findById(saveId).get().getCurrentPoint();

        //then
        assertEquals(20,currentPoint);
    }
}