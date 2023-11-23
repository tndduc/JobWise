package com.jobwise.spring.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/21/2023
 */

public interface FileUpload {
    String uploadFile(MultipartFile multipartFile) throws IOException;
}