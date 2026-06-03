package com.thirdeye30.resumehelper.pdfgenerater.dtos;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeContentDto {
	private UUID id;
    private String name;
    private String email;
    private String content;
    private String contentUrl;
}
