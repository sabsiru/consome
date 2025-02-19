package com.consome.service;

import com.consome.domain.user.PointHistory;
import com.consome.domain.user.User;
import com.consome.dto.user.request.UserValidationRequest;
import com.consome.repository.user.CurrentPointRepository;
import com.consome.repository.user.PointHistoryRepository;
import com.consome.repository.user.UserRepository;
import com.consome.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired PointHistoryRepository pointHistoryRepository;
    @Autowired CurrentPointRepository currentPointRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    public void 회원가입() throws Exception{
        //given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        UserValidationRequest user = new UserValidationRequest("test123","testnick","test@gmail.com","1234");

        //when
        User saveId = userService.register(user,request);

        //then
        User foundUser = userRepository.findById(saveId.getId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // ✅ 로그인 아이디를 기준으로 검증
        assertThat(foundUser.getLoginId()).isEqualTo(user.getLoginId());
    }

    @Test
    public void 회원가입시포인트조회() throws Exception{
        //given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String clientIp = userService.getClientIp(request);
        UserValidationRequest user = new UserValidationRequest("test123","testnick","test@gmail.com","1234");
        //when
        User saveId = userService.register(user,request);

        //then
        PointHistory pointHistory = pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(saveId.getId())
                .orElseThrow(() -> new IllegalArgumentException("포인트 히스토리를 찾을 수 없습니다."));
        assertEquals(100,pointHistory.getCurrentPoint());
        assertEquals("회원가입",pointHistory.getReason());

    }

    @Test()
    public void 중복_아이디예외() throws Exception {
        //given

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String clientIp = userService.getClientIp(request);
        UserValidationRequest user = new UserValidationRequest("test123","testnick","test@gmail.com","1234");
        userService.register(user,request);
        
        UserValidationRequest user2 = new UserValidationRequest("test123","test","tes1t@gmail.com","1234");
        
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user2,request);//then

        });
        //then
        assertEquals("사용 중인 로그인 ID입니다.", exception.getMessage());
    }

    @Test
    public void 중복_이메일예외() throws Exception{
        //given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String clientIp = userService.getClientIp(request);
        UserValidationRequest user = new UserValidationRequest("test123","testnick","test@gmail.com","1234");
        UserValidationRequest user2 = new UserValidationRequest("test","test","test@gmail.com","1234");
        User saveId = userService.register(user,request);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user2,request);
        });
        //then
        assertEquals("사용 중인 이메일입니다.", exception.getMessage());
    }

    @Test
    public void 중복_닉네임예외() throws Exception{
        //given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String clientIp = userService.getClientIp(request);
        UserValidationRequest user = new UserValidationRequest("test123","testnick","test@gmail.com","1234");
        UserValidationRequest user2 = new UserValidationRequest("test1231","testnick","test@gmai1l.com","1234");
        User saveId = userService.register(user,request);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user2,request);
        });
        //then
        assertEquals("사용 중인 닉네임입니다.", exception.getMessage());
    }

}