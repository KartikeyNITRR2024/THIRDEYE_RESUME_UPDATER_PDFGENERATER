package com.thirdeye30.resumehelper.pdfgenerater.services;

import java.util.UUID;

import com.thirdeye30.resumehelper.pdfgenerater.dtos.CourseDto;

public interface CourseService {
	
	CourseDto getCourse(UUID courseId);

}
