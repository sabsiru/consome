package com.consome.service;

import com.consome.domain.User;
import com.consome.repository.CurrentPointRepository;
import com.consome.repository.PointHistoryRepository;
import com.consome.repository.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
        String ip="127.0.0.1";
        User user = User.createUser("test","test","zero0515@gmail.com","1234",passwordEncoder,ip);
        User saveId = userService.register(user,ip);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()-> {
                pointService.updatePoint(saveId.getId(),-101,"테스트 차감");
        });

        //then
        assertEquals("포인트가 부족하여 차감할 수 없습니다.",exception.getMessage());
    }

    @Test
    public void 포인트_조회() throws Exception{
        //given
        String ip="127.0.0.1";
        User user = User.createUser("test","test","zero0515@gmail.com","1234",passwordEncoder,ip);
        User saveId = userService.register(user,ip);
        pointService.updatePoint(saveId.getId(),-50,"테스트 차감");
        pointService.updatePoint(saveId.getId(),-30,"테스트 차감2");

        //when
        int currentPoint = currentPointRepository.findById(saveId.getId()).get().getCurrentPoint();

        //then
        assertEquals(20,currentPoint);
    }
}