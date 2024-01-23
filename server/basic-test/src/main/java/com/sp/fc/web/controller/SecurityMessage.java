package com.sp.fc.web.controller;

//권한과 페이지가 어떤 페이지를 말해 줋 수 있는 메시지를 담고 있는 정보

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityMessage {
    private Authentication auth; //권한 정보
    private String message; //어떤 페이지인지에 대한 메시지 정보
}
