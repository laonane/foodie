package wiki.laona.controller.chunk;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author laona
 * @description 分片实体
 * @since 2022-05-17 15:02
 **/
@Data
@ToString
@NoArgsConstructor
public class ChunkBO {
    @NotBlank(message = "文件名不能为空")
    private String filename;
    @NotNull(message = "文件总大小不能为空")
    private Long totalSize;
    @NotNull(message = "当前分片不能为空")
    private Long currentChunk;
    @NotNull(message = "当前分片大小不能为空")
    private Long chunkSize;
    @NotNull(message = "文件总数不能为空")
    private Long totalChunk;
    @NotBlank(message = "MD5不能为空")
    private String md5;
    private MultipartFile file;
}
