package wiki.laona.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import wiki.laona.utils.JsonResult;

/**
 * @author laona
 * @description 通用异常处理
 * @since 2022-05-13 14:25
 **/
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JsonResult handlerMaxUploadFile(MaxUploadSizeExceededException exception) {
        return JsonResult.errorMsg("文件上传大小不能超出500k，请压缩图片或者降低图片质量再上传！");
    }
}
