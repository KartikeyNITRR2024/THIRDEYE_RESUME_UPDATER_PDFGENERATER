package com.thirdeye30.resumehelper.pdfgenerater.externalcontrollers;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.thirdeye30.resumehelper.pdfgenerater.dtos.CourseDto;

@FeignClient(name = "TOKENMANAGER")
public interface TokenManagerClient {
	
    @GetMapping("/tokenmanager/v1/courses/{id}")
    ResponseEntity<CourseDto> getCourse(
        @PathVariable("id") UUID id
    );
}
