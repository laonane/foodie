package wiki.laona.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wiki.laona.controller.BaseController;
import wiki.laona.pojo.Users;
import wiki.laona.pojo.bo.center.CenterUsersBO;
import wiki.laona.pojo.vo.UsersVO;
import wiki.laona.resource.FileUpload;
import wiki.laona.service.center.CenterUserService;
import wiki.laona.utils.CookieUtils;
import wiki.laona.utils.DateUtil;
import wiki.laona.utils.JsonResult;
import wiki.laona.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author laona
 * @description 用户信息
 * @since 2022-05-12 16:56
 **/
@Api(value = "用户中心-用户信息", tags = {"用户信息的相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CenterUserController.class);

    @Autowired
    private Sid sid;
    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "修改用户信息", tags = {"修改用户信息"}, httpMethod = "POST")
    @PostMapping("/update")
    public JsonResult userInfo(@ApiParam(name = "userId", value = "用户id", required = true)
                               @RequestParam String userId,
                               @RequestBody @Valid CenterUsersBO centerUsersBO,
                               BindingResult result,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return JsonResult.errorMap(errorMap);
        }

        Users userResult = centerUserService.updateUserInfo(userId, centerUsersBO);
        //  redis 实现用户会话
        UsersVO usersVO = conventUserVO(userResult);

        CookieUtils.setCookie(request, response, USER_INFO, JsonUtils.objectToJson(usersVO),true);

        // 增加令牌token，会整合进redis，分布式会话

        return JsonResult.ok();
    }

    /**
     * 获取验证信息的错误信息
     *
     * @param result {@link BindingResult} bandingResult
     * @return 错误信息
     */
    private Map<String, String> getErrors(BindingResult result) {

        List<FieldError> errorList = result.getFieldErrors();
        Map<String, String> map = new HashMap<>(1 << 4);
        for (FieldError error : errorList) {
            // 发生验证错误所对应的某个属性
            String errorField = error.getField();
            // 验证错误信息
            String errorMsg = error.getDefaultMessage();
            map.put(errorField, errorMsg);
        }

        return map;
    }

    /**
     * 去除隐私信息
     *
     * @param userResult 用户信息
     * @return 去除隐私信息后用户信息
     */
    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }


    @ApiOperation(value = "用户头像修改", tags = {"用户头像修改"}, httpMethod = "POST")
    @PostMapping("/uploadFace")
    public JsonResult uploadFace(@ApiParam(name = "userId", value = "用户id", required = true)
                                 @RequestParam String userId,
                                 @ApiParam(name = "file", value = "用户头像", required = true) MultipartFile file,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        // 头像保存的地址
        // String fileSpace = IMAGE_USER_FACE_LOCATION + File.separator;
        String fileSpace = fileUpload.getImageUserFaceLocation();
        // 在路径上为每一个用户增加一个userid，用于区分不同用户上传
        String uploadPathPrefix = userId;

        // 开始上传
        if (file != null) {
            FileOutputStream fileOutputStream = null;
            try {
                // 获取上传文件的文件名称
                String originalFilename = file.getOriginalFilename();

                if (StringUtils.isNotBlank(originalFilename)) {
                    // 文件重命名  laona-face.png -> ["laona-face", "png"]
                    String[] fileNameArr = originalFilename.split("\\.");

                    String suffix = fileNameArr[fileNameArr.length - 1];
                    // 格式判断
                    if (!StringUtils.equalsAnyIgnoreCase(suffix, "png", "jpg", "jpeg")) {
                        return JsonResult.errorMsg("图片格式不正确!");
                    }

                    // face-{userid}.png
                    // 文件名称重组 覆盖式上传，增量式：额外拼接当前时间
                    String newFileName = "face-" + userId + "." + suffix;

                    // 头像保存的最终位置
                    String finalFacePath = fileSpace  + File.separator + uploadPathPrefix + File.separator + newFileName;
                    // 用于提供给web服务访问的地址
                    uploadPathPrefix += ("/" + newFileName);
                    logger.info("图片地址:{}", uploadPathPrefix);

                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null) {
                        // 创建文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    // 文件输出保存到目录
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } catch (Exception e) {
                logger.error("文件不能为空", e);
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return JsonResult.errorMsg("文件不能为空!");
        }
        // 加上时间戳可以防止浏览器缓存
        String faceUrl = fileUpload.getImageServerUrl()
                + uploadPathPrefix
                + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        // 更新用户头像到数据库
        Users userResult = centerUserService.updateUserFace(userId, faceUrl);

        // 增加令牌token，会整合进redis，分布式会话
        UsersVO usersVO = conventUserVO(userResult);

        // 设置cookie
        CookieUtils.setCookie(request,response, USER_INFO, JsonUtils.objectToJson(usersVO), true);

        return JsonResult.ok(userResult);
    }

}
