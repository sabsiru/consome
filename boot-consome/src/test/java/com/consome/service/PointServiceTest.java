package com.consome.service;

import com.consome.domain.user.User;
import com.consome.dto.user.request.UserValidationRequest;
import com.consome.repository.user.CurrentPointRepository;
import com.consome.repository.user.PointHistoryRepository;
import com.consome.repository.user.UserRepository;
import com.consome.service.user.PointService;
import com.consome.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
    @Autowired
    UserService userService;
    @Autowired
    PointService pointService;
    @Autowired UserRepository userRepository;
    @Autowired PointHistoryRepository pointHistoryRepository;
    @Autowired CurrentPointRepository currentPointRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void 포인트_차감() throws Exception{
        //given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String clientIp = userService.getClientIp(request);
        UserValidationRequest user = new UserValidationRequest("test123","testnick","test@gmail.com","1234");
        User saveId = userService.register(user,request);

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
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String clientIp = userService.getClientIp(request);
        UserValidationRequest user = new UserValidationRequest("test123","testnick","test@gmail.com","1234");
        User saveId = userService.register(user,request);
        pointService.updatePoint(saveId.getId(),-50,"테스트 차감");
        pointService.updatePoint(saveId.getId(),-30,"테스트 차감2");

        //when
        int currentPoint = currentPointRepository.findById(saveId.getId()).get().getCurrentPoint();

        //then
        assertEquals(20,currentPoint);
    }
}