package wiki.laona.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author laona
 * @description 文件上传
 * @since 2022-05-13 11:34
 **/
@Data
@Component
@PropertySource("classpath:file-upload-prod.properties")
@ConfigurationProperties(prefix = "file")
public class FileUpload {

    /**
     * 头像上传路径
     */
    private String imageUserFaceLocation;
    /**
     * 服务器访问地址
     */
    private String imageServerUrl;
}
