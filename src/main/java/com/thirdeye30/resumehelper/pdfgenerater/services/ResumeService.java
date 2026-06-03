package com.thirdeye30.resumehelper.pdfgenerater.services;

import java.util.UUID;

import com.thirdeye30.resumehelper.pdfgenerater.dtos.ResumeContentDto;

public interface ResumeService {

	ResumeContentDto getResumeContent(UUID resumeId);

}
