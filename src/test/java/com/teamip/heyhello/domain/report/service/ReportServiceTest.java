package com.teamip.heyhello.domain.report.service;

import com.teamip.heyhello.domain.report.dto.ReportRequestDto;
import com.teamip.heyhello.domain.report.entity.Report;
import com.teamip.heyhello.domain.report.entity.ReportCategory;
import com.teamip.heyhello.domain.report.entity.ReportCount;
import com.teamip.heyhello.domain.report.repository.ReportCountRepository;
import com.teamip.heyhello.domain.report.repository.ReportRepository;
import com.teamip.heyhello.domain.user.dto.SignupRequestDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application.yml")
class ReportServiceTest {

    User requestUser;
    User targetUser;
    UserDetailsImpl userDetails;
    ReportRequestDto reportRequestDto;
    ReportCount reportCount;
    @Mock
    UserRepository userRepository;

    @Mock
    ReportRepository reportRepository;

    @Mock
    ReportCountRepository reportCountRepository;

    @InjectMocks
    ReportService reportService;

    @BeforeEach
    void init() {
        SignupRequestDto signupRequestDto = new SignupRequestDto("test01@gmail.com", "DWQJIDJQOI390483018EADHJOIAJDOI2","닉네임1");
        String encoded = signupRequestDto.getPassword()+"123";
        requestUser = User.of(signupRequestDto, encoded, "http://sample.com/1");

        SignupRequestDto signupRequestDto2 = new SignupRequestDto("test02@gmail.com", "DWQJIDJQOI345t3rgf4IAJDOI2","닉네임2");
        String encoded2 = signupRequestDto.getPassword()+"123";
        targetUser = User.of(signupRequestDto2, encoded2, "http://sample.com/2");

        userDetails = new UserDetailsImpl(requestUser);
        reportRequestDto = new ReportRequestDto("닉네임2", "ABUSIVE_LANGUAGE") ;
    }

    @Test
    @DisplayName("신고 유효성 검사 통과 테스트")
    void test01() {
        // given
        given(userRepository.findByLoginId(userDetails.getUsername())).willReturn(Optional.of(requestUser));
        given(userRepository.findByNickname(reportRequestDto.getNickname())).willReturn(Optional.of(targetUser));

        // when
        StatusResponseDto statusResponseDto = reportService.reportUser(userDetails, reportRequestDto);

        // then
        assertThat(statusResponseDto).isNotNull();
        assertThat(statusResponseDto.getStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("신고 유저와 신고당한 유저정보가 동일한 경우")
    void test02() {
        // given
        given(userRepository.findByLoginId(userDetails.getUsername())).willReturn(Optional.of(requestUser));
        given(userRepository.findByNickname(reportRequestDto.getNickname())).willReturn(Optional.of(requestUser));

        // when
        Exception exception = assertThrows(RuntimeException.class, () -> reportService.reportUser(userDetails, reportRequestDto));

        // then
        assertThat(exception.getMessage()).isEqualTo("자기 자신을 신고할 수 없습니다.");
    }

    @Test
    @DisplayName("동일 유저를 신고하는 경우")
    void test03(){
        // given
        Report report = new Report();

        given(userRepository.findByLoginId(userDetails.getUsername())).willReturn(Optional.of(requestUser));
        given(userRepository.findByNickname(reportRequestDto.getNickname())).willReturn(Optional.of(targetUser));
        //동일 유저 신고 로직이 isPresent이므로 임의의 report 생성 후 반환
        given(reportRepository.findByRequestUserAndTargetUser(requestUser, targetUser)).willReturn(Optional.of(report));

        // when
        Exception exception = assertThrows(RuntimeException.class, () -> reportService.reportUser(userDetails, reportRequestDto));

        // then
        assertThat(exception.getMessage()).isEqualTo("같은 유저는 신고할 수 없습니다.");
    }

    @Test
    @DisplayName("처음 신고당한 신고 유형일 경우")
    void test04() {
        // given
        reportCount = new ReportCount(targetUser, ReportCategory.valueOf("ABUSIVE_LANGUAGE"));
        given(userRepository.findByLoginId(userDetails.getUsername())).willReturn(Optional.of(requestUser));
        given(userRepository.findByNickname(reportRequestDto.getNickname())).willReturn(Optional.of(targetUser));

        // when
        reportService.reportUser(userDetails,reportRequestDto);

        // then
        ArgumentCaptor<ReportCount> reportCountArgumentCaptor = ArgumentCaptor.forClass(ReportCount.class);
        verify(reportCountRepository).save(reportCountArgumentCaptor.capture());

        ReportCount savedReportCount = reportCountArgumentCaptor.getValue();
        assertThat(savedReportCount.getCount()).isEqualTo(reportCount.getCount());
        assertThat(savedReportCount.getCategory()).isEqualTo(reportCount.getCategory());
    }

    @Test
    @DisplayName("2회 이상 동일한 유형으로 신고당함")
    void test05(){
        // given
        ReportCount reportCount2 = new ReportCount(targetUser, ReportCategory.valueOf("ABUSIVE_LANGUAGE"));
        given(userRepository.findByLoginId(userDetails.getUsername())).willReturn(Optional.of(requestUser));
        given(userRepository.findByNickname(reportRequestDto.getNickname())).willReturn(Optional.of(targetUser));
        given(reportCountRepository.findByUserAndCategory(targetUser, ReportCategory.valueOf("ABUSIVE_LANGUAGE"))).willReturn(Optional.of(reportCount2));

        // when
        reportService.reportUser(userDetails,reportRequestDto);

        // then
        ArgumentCaptor<ReportCount> reportCountArgumentCaptor = ArgumentCaptor.forClass(ReportCount.class);
        verify(reportCountRepository).save(reportCountArgumentCaptor.capture());

        ReportCount savedReportCount = reportCountArgumentCaptor.getValue();
        assertThat(savedReportCount.getCount()).isEqualTo(reportCount2.getCount());
    }

    @Test
    @DisplayName("동일 사유로 4회 신고당한 유저가 5회째 신고 당할 시 정지")
    void test06(){
        // given
        List<ReportCount> reportCountList = new ArrayList<>();
        ReportCount reportCount3 = new ReportCount(targetUser, ReportCategory.valueOf("ABUSIVE_LANGUAGE"));
        IntStream.range(0, 3).forEach(i -> reportCount3.updateCount());
        reportCountList.add(reportCount3);

        given(userRepository.findByLoginId(userDetails.getUsername())).willReturn(Optional.of(requestUser));
        given(userRepository.findByNickname(reportRequestDto.getNickname())).willReturn(Optional.of(targetUser));
        given(reportCountRepository.findByUserAndCategory(targetUser, ReportCategory.valueOf("ABUSIVE_LANGUAGE"))).willReturn(Optional.of(reportCount3));
        given(reportCountRepository.findByUserId(targetUser.getId())).willReturn(reportCountList);

        // when
        reportService.reportUser(userDetails,reportRequestDto);

        // then
        assertThat(targetUser.getIsLocked()).isTrue();
    }
}