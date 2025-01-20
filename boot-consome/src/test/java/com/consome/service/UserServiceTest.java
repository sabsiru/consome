package com.consome.service;

import com.consome.domain.PointHistory;
import com.consome.domain.User;
import com.consome.repository.PointHistoryRepository;
import com.consome.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    @Autowired
    PointHistoryRepository pointHitoryRepository;

    @Test
    public void 회원가입() throws Exception{
        //given
        User user = User.createUser("zero0515","test","jin","zero0515@gmail.com","1234","01091940785");
        //when
        Long saveId = userService.register(user);

        //then
        User foundUser = userRepository.findById(saveId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void 회원가입시포인트조회() throws Exception{
        //given
        User user = User.createUser("zero0515","test","jin","zero0515@gmail.com","1234","01091940785");
        //when
        Long saveId = userService.register(user);
        //then
        PointHistory pointHistory = pointHitoryRepository.findById(saveId).get();
        assertEquals(100,pointHistory.getCurrentPoint());
        assertEquals("회원가입",pointHistory.getReason());
    }

    @Test()
    public void 중복_아이디예외() throws Exception {
        //given
        User user =User.createUser("zero0515", "sab", "jin", "zero0515@gmail.com", "1234", "01091940785");
        User user2 = User.createUser("zero0515", "123", "123", "123@gmail.com", "1234", "01091940785");
        Long saveId = userService.register(user);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user2);//then

        });
        //then
        assertEquals("이미 사용 중인 로그인 ID입니다.", exception.getMessage());
    }

    @Test
    public void 중복_이메일예외() throws Exception{
        //given
        User user = User.createUser("zero0515", "test", "jin", "zero0515@gmail.com", "1234", "01091940785");
        User user2 = User.createUser("123", "123", "123", "zero0515@gmail.com", "1234", "01091950000");
        Long saveId = userService.register(user);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user2);
        });
        //then
        assertEquals("이미 사용 중인 이메일입니다.", exception.getMessage());
    }

    @Test
    public void 중복_닉네임예외() throws Exception{
        //given
        User user = User.createUser("zero0515", "test", "jin", "zero0515@gmail.com", "1234", "01091940785");
        User user2 = User.createUser("123", "test", "123", "123@gmail.com", "1234", "01091950000");
        Long saveId = userService.register(user);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user2);
        });
        //then
        assertEquals("이미 사용 중인 닉네임입니다.", exception.getMessage());
    }

    @Test
    public void 중복_전화번호예외() throws Exception{
        //given
        User user = User.createUser("zero0515", "test", "jin", "zero0515@gmail.com", "1234", "01091940785");
        User user2 = User.createUser("123", "123", "123", "123@gmail.com", "1234", "01091940785");
        Long saveId = userService.register(user);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user2);
        });
        //then
        assertEquals("이미 사용 중인 전화번호입니다.", exception.getMessage());
    }

   @Test
   public void 전화번호로_로그인아이디찾기() throws Exception{
       //given
       User user = User.createUser("zero0515", "test", "jin", "zero0515@gmail.com", "1234", "01091940785");
        userService.register(user);
       //when
       String findId = userService.findLoginIdByPhoneNumber("010-9194-0785");
       //then
       assertEquals("zero0515", findId);
   }

   @Test
   public void 이메일로_로그인아이디찾기() throws Exception{
       //given
       User user = User.createUser("zero0515", "test", "jin", "zero0515@gmail.com", "1234", "01091940785");
       userService.register(user);
       //when
       User userByEmail = userService.findUserByEmail("zero0515@gmail.com");
       //then
       assertEquals("zero0515@gmail.com", userByEmail.getEmail());
   }
}