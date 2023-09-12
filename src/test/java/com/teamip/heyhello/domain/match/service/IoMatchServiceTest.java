//package com.teamip.heyhello.domain.match.service;
//
//import com.corundumstudio.socketio.SocketIOClient;
//import com.corundumstudio.socketio.SocketIOServer;
//import com.teamip.heyhello.domain.match.dto.RequestUserDto;
//import com.teamip.heyhello.domain.user.entity.User;
//import com.teamip.heyhello.domain.user.repository.UserRepository;
//import com.teamip.heyhello.global.test.service.WaitListService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.UUID;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@SpringBootTest
//class IoMatchServiceTest {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private IoMatchService ioMatchService;
//    @Autowired
//    private WaitListService waitListService;
//    @Mock
//    private SocketIOServer server;
//    @Mock
//    private SocketIOClient waitClient;
//    @Mock
//    private SocketIOClient client;
//    List<Integer> numberList;
//    Random random;
//
////    @Test
////    @Disabled
////    void createTestUser() {
////        for (int i = 1; i <= 1000; i++) {
////            if (i % 2 == 0) {
////                User user = User.builder()
////                        .loginId("testId" + i + "@gmail.com")
////                        .password("DKWAOPJDIAWOPDJ")
////                        .nickname("testNickname" + i)
////                        .language("한국어")
////                        .image("http://test")
////                        .country("한국")
////                        .gender("남성")
////                        .build();
////                userRepository.save(user);
////            } else {
////                User user = User.builder()
////                        .loginId("testId" + i + "@gmail.com")
////                        .password("DKWAOPJDIAWOPDJ")
////                        .nickname("testNickname" + i)
////                        .language("English")
////                        .image("http://test")
////                        .country("미국")
////                        .gender("남성")
////                        .build();
////                userRepository.save(user);
////            }
////        }
////    }
//
//    @BeforeEach
//    void setUp() {
//        numberList = new ArrayList<>();
//        for (int i = 1; i <= 1000; i++) {
//            numberList.add(i);
//        }
//    }
//
//    @Test
//    @DisplayName("대기열 예열")
//    void test00(){
//        String loginId;
//
//        for (int i = 1; i <= 500; i++) {
//            UUID sessionId = UUID.randomUUID();
//            given(client.getSessionId()).willReturn(sessionId);
//            given(server.getClient(any(UUID.class))).willReturn(waitClient);
//            loginId = "testId" + i + "@gmail.com";
//            waitListService.findMatchDb(server, client, new RequestUserDto(loginId));
//        }
//        for (int i = 501; i <= 1000; i++) {
//            UUID sessionId = UUID.randomUUID();
//            given(client.getSessionId()).willReturn(sessionId);
//            given(server.getClient(any(UUID.class))).willReturn(waitClient);
//            loginId = "testId" + i + "@gmail.com";
//            ioMatchService.findMatch(server, client, new RequestUserDto(loginId));
//        }
//    }
//    @Test
//    @DisplayName("DB 대기열 1000명 순차 매칭 시")
//    void test02() {
//        String loginId;
//
//        for (int i = 1; i <= 1000; i++) {
//            UUID sessionId = UUID.randomUUID();
//            given(client.getSessionId()).willReturn(sessionId);
//            given(server.getClient(any(UUID.class))).willReturn(waitClient);
//            loginId = "testId" + i + "@gmail.com";
//            waitListService.findMatchDb(server, client, new RequestUserDto(loginId));
//        }
//    }
//
//    @Test
//    @DisplayName("레디스 대기열 1000명 순차 매칭 시")
//    void test01() {
//        String loginId;
//        for (int i = 1; i <= 1000; i++) {
//            UUID sessionId = UUID.randomUUID();
//            given(client.getSessionId()).willReturn(sessionId);
//            given(server.getClient(any(UUID.class))).willReturn(waitClient);
//            loginId = "testId" + i + "@gmail.com";
//            ioMatchService.findMatch(server, client, new RequestUserDto(loginId));
//        }
//    }
//
//
//    @Test
//    @DisplayName("DB 대기열 1000명 랜덤 매칭 시")
//    void test04() {
//        Random random = new Random();
//        String loginId;
//        while (numberList.size() >= 1) {
//            UUID sessionId = UUID.randomUUID();
//            given(client.getSessionId()).willReturn(sessionId);
//            given(server.getClient(any(UUID.class))).willReturn(waitClient);
//            int index;
//            if (numberList.size() == 1) {
//                index = 0;
//            } else {
//                index = random.nextInt(numberList.size() - 1);
//            }
//            int userId = numberList.get(index);
//            numberList.remove(index);
//            loginId = "testId" + userId + "@gmail.com";
//            waitListService.findMatchDb(server, client, new RequestUserDto(loginId));
//        }
//    }
//
//    @Test
//    @DisplayName("레디스 대기열 1000명 랜덤 매칭 시")
//    void test03() {
//        Random random = new Random();
//        String loginId;
//        while (numberList.size() >= 1) {
//            UUID sessionId = UUID.randomUUID();
//            given(client.getSessionId()).willReturn(sessionId);
//            given(server.getClient(any(UUID.class))).willReturn(waitClient);
//            int index;
//            if (numberList.size() == 1) {
//                index = 0;
//            } else {
//                index = random.nextInt(numberList.size() - 1);
//            }
//            int userId = numberList.get(index);
//            numberList.remove(index);
//            loginId = "testId" + userId + "@gmail.com";
//            ioMatchService.findMatch(server, client, new RequestUserDto(loginId));
//        }
//    }
//
//    @Test
//    @DisplayName("DB 대기열 1000명 쓰레드 10 랜덤 매칭 시")
//    void test06() {
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        Random random = new Random();
//
//        Runnable matchingTask = () -> {
//            while (true) {
//                int userId;
//                synchronized (numberList) {
//                    if (numberList.isEmpty()) {
//                        break;
//                    }
//                    int index = random.nextInt(numberList.size());
//                    userId = numberList.remove(index);
//                }
//
//                UUID sessionId = UUID.randomUUID();
//                given(client.getSessionId()).willReturn(sessionId);
//                given(server.getClient(any(UUID.class))).willReturn(waitClient);
//                String loginId = "testId" + userId + "@gmail.com";
//                waitListService.findMatchDb(server, client, new RequestUserDto(loginId));
//            }
//        };
//
//        for (int i = 0; i < 10; i++) {
//            executorService.execute(matchingTask);
//        }
//
//        // 모든 작업이 완료될 때까지 대기
//        executorService.shutdown();
//        while (!executorService.isTerminated()) {
//            // 대기
//        }
//    }
//
//    @Test
//    @DisplayName("Redis 대기열 1000명 쓰레드 10 랜덤 매칭 시")
//    void test05() {
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        Random random = new Random();
//
//        Runnable matchingTask = () -> {
//            while (true) {
//                int userId;
//                synchronized (numberList) {
//                    if (numberList.isEmpty()) {
//                        break;
//                    }
//                    int index = random.nextInt(numberList.size());
//                    userId = numberList.remove(index);
//                }
//
//                UUID sessionId = UUID.randomUUID();
//                given(client.getSessionId()).willReturn(sessionId);
//                given(server.getClient(any(UUID.class))).willReturn(waitClient);
//                String loginId = "testId" + userId + "@gmail.com";
//                ioMatchService.findMatch(server, client, new RequestUserDto(loginId));
//            }
//        };
//
//        for (int i = 0; i < 10; i++) {
//            executorService.execute(matchingTask);
//        }
//
//        // 모든 작업이 완료될 때까지 대기
//        executorService.shutdown();
//        while (!executorService.isTerminated()) {
//            // 대기
//        }
//    }
//}
