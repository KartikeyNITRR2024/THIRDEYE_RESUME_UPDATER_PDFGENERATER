package com.thirdeye30.resumehelper.pdfgenerater.controllers;

import java.util.UUID;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thirdeye30.resumehelper.pdfgenerater.enums.PdfType;
import com.thirdeye30.resumehelper.pdfgenerater.services.PdfGeneraterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/pdfgenerater/v1")
@RequiredArgsConstructor
@Slf4j
public class PdfGeneraterController {
	
	private final PdfGeneraterService pdfGeneraterService;
	
    @GetMapping("/resumes/{id}/{type}/download")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable UUID id,
            @PathVariable PdfType type) {
            byte[] pdfBytes = pdfGeneraterService.generatePdf(id, type);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            
            // 2. Use Spring's builder to format the 'inline' header perfectly
            ContentDisposition contentDisposition = ContentDisposition.inline()
                    .filename("Resume_" + type.name() + ".pdf")
                    .build();
            headers.setContentDisposition(contentDisposition);
            
            // 3. Optional but helpful: prevent the browser from caching a broken view
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    // 4. Explicitly set content length so the browser knows the exact file size to render
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);
    }
    
    @GetMapping("/course/{id}/download")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable UUID id) {
            byte[] pdfBytes = pdfGeneraterService.generatePdf(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            
            // 2. Use Spring's builder to format the 'inline' header perfectly
            ContentDisposition contentDisposition = ContentDisposition.inline()
                    .filename("Interview_prep.pdf")
                    .build();
            headers.setContentDisposition(contentDisposition);
            
            // 3. Optional but helpful: prevent the browser from caching a broken view
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    // 4. Explicitly set content length so the browser knows the exact file size to render
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);
    }
}
