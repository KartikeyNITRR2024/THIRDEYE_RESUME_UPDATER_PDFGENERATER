package com.thirdeye30.resumehelper.pdfgenerater.externalcontrollers;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.thirdeye30.resumehelper.pdfgenerater.dtos.ResumeContentDto;

@FeignClient(
		name = "RESUMEMANAGER"
)
public interface ResumeManagerClient {

	@GetMapping("/resumemanager/v1/resumes/{id}/content")
	ResponseEntity<ResumeContentDto> getResumeContent(
        @PathVariable("id") UUID uuid
    );
}
