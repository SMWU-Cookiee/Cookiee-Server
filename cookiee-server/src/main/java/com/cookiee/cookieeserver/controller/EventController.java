/*
package com.cookiee.cookieeserver.controller;



import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@RestController
@RequiredArgsConstructor
public class EventController {

    @ResponseBody   // Long 타입을 리턴하고 싶은 경우 붙여야 함 (Long - 객체)
    @PostMapping(value="/diary/new",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long saveDiary(HttpServletRequest request, @RequestParam(value="image") MultipartFile image, Diary diary) throws IOException {
        System.out.println("DiaryController.saveDiary");
        System.out.println(image);
        System.out.println(diary);
        System.out.println("------------------------------------------------------");
        Long diaryId = diaryService.keepDiary(image, diary);
        return diaryId;
    }


}*/
