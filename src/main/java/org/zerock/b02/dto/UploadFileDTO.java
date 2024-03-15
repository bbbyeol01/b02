package org.zerock.b02.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@ToString
public class UploadFileDTO {

    private List<MultipartFile> files;

}
