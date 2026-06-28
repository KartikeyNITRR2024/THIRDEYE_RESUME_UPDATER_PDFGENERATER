package com.thirdeye30.resumehelper.pdfgenerater.services.impl;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thirdeye30.resumehelper.pdfgenerater.dtos.ResumeContentDto;
import com.thirdeye30.resumehelper.pdfgenerater.externalcontrollers.ResumeManagerClient;
import com.thirdeye30.resumehelper.pdfgenerater.services.ResumeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeServiceImpl implements ResumeService {
	
    private final StringRedisTemplate redisTemplate;
    private final ResumeManagerClient resumeManager;
    private final ObjectMapper objectMapper;
    
    @Value("${thirdeye.redis.resume-prefix}")
    private String redisResumePrefix;

    @Override
    public ResumeContentDto getResumeContent(UUID resumeId) {
        
        // Renamed variables to make sense for Resumes rather than Tokens
        String cacheKey = redisResumePrefix + "content:" + resumeId.toString();
        String cachedData = redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedData == null) {
            log.info("Cache miss for resume {}. Fetching from ResumeManager service.", resumeId);
            
            ResponseEntity<ResumeContentDto> response = resumeManager.getResumeContent(resumeId);
            ResumeContentDto resumeContentDto = response.getBody();
            
            if (resumeContentDto != null) {
                try {
                    // Convert the DTO to a JSON string to store in Redis
                    String jsonToCache = objectMapper.writeValueAsString(resumeContentDto);
                    redisTemplate.opsForValue().set(cacheKey, jsonToCache, Duration.ofMinutes(30));
                } catch (JsonProcessingException e) {
                    log.error("Failed to serialize ResumeContentDto for caching: {}", resumeId, e);
                }
            }
            
            return resumeContentDto != null ? resumeContentDto : new ResumeContentDto();
            
        } else {
            log.info("Cache hit for resume {}.", resumeId);
            try {
                return objectMapper.readValue(cachedData, ResumeContentDto.class);
            } catch (JsonProcessingException e) {
                log.error("Failed to deserialize cached ResumeContentDto for: {}", resumeId, e);
                return new ResumeContentDto(); 
            }
        }
    }
}