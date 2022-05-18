package wiki.laona.controller.chunk;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wiki.laona.utils.JsonResult;

import javax.validation.Valid;
import java.io.*;

/**
 * @author laona
 * @description 分片接口
 *                  用于测试
 * @since 2022-05-17 14:59
 **/
@Ignore
@RestController
@RequestMapping("chunk")
public class ChunkController {

    private static final Logger logger = LoggerFactory.getLogger(ChunkController.class);
    private static final String basePath = "E:\\foodieUpload\\bigfiles\\";

    @RequestMapping("upload")
    public JsonResult chunkUpload(@Valid ChunkBO chunkBO) throws IOException {
        logger.info("chunkBO ---->>>>  {}", chunkBO);
        File file = new File(basePath + chunkBO.getFilename());
        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        long chunkSize = chunkBO.getChunkSize();
        byte[] buffer = new byte[(int) chunkSize];
        long startIdx = (chunkBO.getCurrentChunk() - 1) * chunkBO.getChunkSize();
        // long endIdx = Math.min((chunkBO.getCurrentChunk() + 1) * chunkSize, chunkBO.getTotalSize());
        accessFile.seek(startIdx);
        InputStream inputStream = chunkBO.getFile().getInputStream();
        int read = inputStream.read(buffer);
        if (read > 0) {
            accessFile.write(buffer, 0, read);
        }
        accessFile.close();
        return JsonResult.ok();
    }
}
