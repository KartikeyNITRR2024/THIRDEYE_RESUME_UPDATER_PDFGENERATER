package com.thirdeye30.resumehelper.pdfgenerater.services;

import java.util.UUID;

import com.thirdeye30.resumehelper.pdfgenerater.enums.PdfType;

public interface PdfGeneraterService {
	byte[] generatePdf(UUID resumeId, PdfType pdfType);

}
