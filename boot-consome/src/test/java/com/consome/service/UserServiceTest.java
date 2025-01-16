package com.consome.service;

import com.consome.domain.User;
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

}