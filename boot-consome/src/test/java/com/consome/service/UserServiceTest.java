package com.consome.service;

import com.consome.domain.PointHistory;
import com.consome.domain.User;
import com.consome.repository.CurrentPointRepository;
import com.consome.repository.PointHistoryRepository;
import com.consome.repository.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired PointHistoryRepository pointHistoryRepository;
    @Autowired CurrentPointRepository currentPointRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    public void 회원가입() throws Exception{
        //given
        String ip = "127.0.0.1";
        User user = User.createUser("test","test","zero0515@gmail.com","1234",passwordEncoder,ip);
        //when
        User saveId = userService.register(user,ip);

        //then
        User foundUser = userRepository.findById(saveId.getId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // ✅ 로그인 아이디를 기준으로 검증
        assertThat(foundUser.getLoginId()).isEqualTo(user.getLoginId());
    }

    @Test
    public void 회원가입시포인트조회() throws Exception{
        //given
        String ip = "127.0.0.1";
        User user = User.createUser("test","test","zero0515@gmail.com","1234",passwordEncoder,ip);

        //when
        User saveId = userService.register(user,ip);

        //then
        PointHistory pointHistory = pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(saveId.getId())
                .orElseThrow(() -> new IllegalArgumentException("포인트 히스토리를 찾을 수 없습니다."));
        assertEquals(100,pointHistory.getCurrentPoint());
        assertEquals("회원가입",pointHistory.getReason());

    }

    @Test()
    public void 중복_아이디예외() throws Exception {
        //given
        String ip = "127.0.0.1";
        User user = User.createUser("test","test","zero0515@gmail.com","1234",passwordEncoder,ip);
        User user2 = User.createUser("test","test1","zero05151@gmail.com","1234",passwordEncoder,ip);
        User saveId = userService.register(user,ip);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user2,ip);//then

        });
        //then
        assertEquals("이미 사용 중인 로그인 ID입니다.", exception.getMessage());
    }

    @Test
    public void 중복_이메일예외() throws Exception{
        //given
        String ip = "127.0.0.1";
        User user = User.createUser("test","test","zero0515@gmail.com","1234",passwordEncoder,ip);
        User user2 = User.createUser("test1","test1","zero0515@gmail.com","1234",passwordEncoder,ip);
        User saveId = userService.register(user,ip);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user2,ip);
        });
        //then
        assertEquals("이미 사용 중인 이메일입니다.", exception.getMessage());
    }

    @Test
    public void 중복_닉네임예외() throws Exception{
        //given
        String ip = "127.0.0.1";
        User user = User.createUser("test","test","zero0515@gmail.com","1234",passwordEncoder,ip);
        User user2 = User.createUser("test1","test","zero05151@gmail.com","1234",passwordEncoder,ip);
        User saveId = userService.register(user,ip);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user2,ip);
        });
        //then
        assertEquals("이미 사용 중인 닉네임입니다.", exception.getMessage());
    }


   @Test
   public void 이메일로_로그인아이디찾기() throws Exception{
       //given
       String ip = "127.0.0.1";
       User user = User.createUser("test","test","zero0515@gmail.com","1234",passwordEncoder,ip);
       userService.register(user,ip);
       //when
       User userByEmail = userService.findUserByEmail("zero0515@gmail.com");
       //then
       assertEquals("zero0515@gmail.com", userByEmail.getEmail());
   }
}