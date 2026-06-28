package com.thirdeye30.resumehelper.pdfgenerater.services.impl;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thirdeye30.resumehelper.pdfgenerater.dtos.CourseDto;
import com.thirdeye30.resumehelper.pdfgenerater.dtos.ResumeContentDto;
import com.thirdeye30.resumehelper.pdfgenerater.externalcontrollers.ResumeManagerClient;
import com.thirdeye30.resumehelper.pdfgenerater.externalcontrollers.TokenManagerClient;
import com.thirdeye30.resumehelper.pdfgenerater.services.CourseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
	
    private final StringRedisTemplate redisTemplate;
    private final TokenManagerClient tokenManager;
    private final ObjectMapper objectMapper;
    
    @Value("${thirdeye.redis.course-prefix}")
    private String redisCoursePrefix;

	@Override
	public CourseDto getCourse(UUID courseId) {
		String cacheKey = redisCoursePrefix + "content:" + courseId.toString();
        String cachedData = redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedData == null) {
            log.info("Cache miss for course {}. Fetching from TokenManager service.", courseId);
            
            ResponseEntity<CourseDto> response = tokenManager.getCourse(courseId);
            CourseDto courseDto = response.getBody();
            
            if (courseDto != null) {
                try {
                    String jsonToCache = objectMapper.writeValueAsString(courseDto);
                    redisTemplate.opsForValue().set(cacheKey, jsonToCache, Duration.ofMinutes(30));
                } catch (JsonProcessingException e) {
                    log.error("Failed to serialize CourseDto for caching: {}", courseDto, e);
                }
            }
            
            return courseDto != null ? courseDto : new CourseDto();
            
        } else {
            log.info("Cache hit for resume {}.", courseId);
            try {
                return objectMapper.readValue(cachedData, CourseDto.class);
            } catch (JsonProcessingException e) {
                log.error("Failed to deserialize cached CourseDto for: {}", courseId, e);
                return new CourseDto(); 
            }
        }
	}

}
