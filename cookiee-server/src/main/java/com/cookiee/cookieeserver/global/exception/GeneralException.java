package com.cookiee.cookieeserver.global.exception;

import com.cookiee.cookieeserver.global.ErrorCode;
import com.cookiee.cookieeserver.global.dto.ErrorResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class GeneralException extends RuntimeException {

    private final ErrorCode errorCode;
}
