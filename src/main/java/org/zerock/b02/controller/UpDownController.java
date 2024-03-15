package org.zerock.b02.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.b02.dto.UploadFileDTO;
import org.zerock.b02.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Slf4j
public class UpDownController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @Operation(summary = "upload POST", description = "POST 방식으로 업로드")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(@Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))UploadFileDTO uploadFileDTO){
        log.info("upload file " + uploadFileDTO);

        final List<UploadResultDTO> list = new ArrayList<>();

//        이미지가 있으면
        if(uploadFileDTO.getFiles() != null){
            uploadFileDTO.getFiles().forEach( multipartFile -> {

                String originalName = multipartFile.getOriginalFilename();
                log.info("originalName " + originalName);

                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);

                boolean image = false;

//                이미지 저장
                try {
                    multipartFile.transferTo(savePath);

//                    이미지라면
                    if(Files.probeContentType(savePath).startsWith("image")){

//                        파일 이름 생성
                        File thumbnailFile = new File(uploadPath, "s_" + uuid + "_" + originalName);

//                        썸네일 만들기
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 200, 200);

                        image = true;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originalName)
                        .image(image)
                        .build());
            });

            return list;

        }// if

        return null;
    }


    @Operation(summary = "view file", description = "GET 방식으로 첨부 파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);

    }

    @Operation(summary = "delete file", description = "DELETE 방식으로 파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();

        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

//            썸네일이 존재한다면(이미지 파일이라면)
            if(contentType.startsWith("image")){
                File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                thumbnailFile.delete();
            }
        } catch (IOException e) {

            log.error(e.getMessage());
        }

        resultMap.put("result", removed);

        return resultMap;
    }


}
