package com.thirdeye30.resumehelper.pdfgenerater.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseDto {
	private UUID id;
    private String company;
    private String jobDescription;
    private String jobUrl;
    private Map<String, List<String>> result;
    private Map<String, List<List<String>>> courseResult;
    private LocalDateTime createTime;
}
