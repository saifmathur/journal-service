package com.journal.journal_service.services;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import com.itextpdf.layout.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.layout.element.Paragraph;

@Service
public class StorageService {
    private static final Logger log = LoggerFactory.getLogger(StorageService.class);


    @Autowired
    S3Client s3Client;

    @Value("${S3_BUCKET}")
    String bucketName;

    public StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public List<String> listFiles() {
        ListObjectsV2Request listObjects = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(listObjects);

        return response.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }


    public String uploadFile(MultipartFile file, String folderPath) throws IOException {
        CreateMultipartUploadRequest createRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(folderPath)
                .build();

        CreateMultipartUploadResponse createResponse = s3Client.createMultipartUpload(createRequest);
        String uploadId = createResponse.uploadId();

        try (InputStream inputStream = file.getInputStream()) {
            List<CompletedPart> completedParts = new ArrayList<>();
            int partNumber = 1;
            byte[] buffer = new byte[5 * 1024 * 1024]; // 5MB chunks
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                        .bucket(bucketName)
                        .key(folderPath)
                        .uploadId(uploadId)
                        .partNumber(partNumber)
                        .contentLength((long) bytesRead)
                        .build();

                UploadPartResponse uploadPartResponse = s3Client.uploadPart(uploadPartRequest,
                        software.amazon.awssdk.core.sync.RequestBody.fromBytes(buffer));

                completedParts.add(CompletedPart.builder()
                        .partNumber(partNumber++)
                        .eTag(uploadPartResponse.eTag())
                        .build());
            }

            CompleteMultipartUploadRequest completeRequest = CompleteMultipartUploadRequest.builder()
                    .bucket(bucketName)
                    .key(folderPath)
                    .uploadId(uploadId)
                    .multipartUpload(CompletedMultipartUpload.builder().parts(completedParts).build())
                    .build();

            s3Client.completeMultipartUpload(completeRequest);
            return folderPath;
        } catch (Exception e) {
            AbortMultipartUploadRequest abortRequest = AbortMultipartUploadRequest.builder()
                    .bucket(bucketName)
                    .key(folderPath)
                    .uploadId(uploadId)
                    .build();
            s3Client.abortMultipartUpload(abortRequest);
            throw new RuntimeException("Upload failed, multipart aborted.", e);
        }
    }


    public void generateAndUploadPdf(String content, String key) throws Exception {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);
            document.add(new Paragraph(content));
            document.close();

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key) // e.g., "resumes/user123/profile_resume.pdf"
                    .contentType("application/pdf")
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(outputStream.toByteArray()));

            System.out.println("✅ PDF uploaded to S3 at: " + key);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    public ResponseEntity<Resource> downloadFile(String key) throws Exception {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

            // 📄 Extract file name from the key
            String fileName = key.substring(key.lastIndexOf("/") + 1);

            // 📦 Prepare response with download headers
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(s3Object));

        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    public String extractTextFromS3PDF(String key) throws IOException {
        try (ResponseInputStream<?> s3Object = s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
             PDDocument document = PDDocument.load(s3Object)) {

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);

        } catch (S3Exception e) {
            System.err.println("Error downloading file from S3: " + e);
            throw e;
        }
    }

}
