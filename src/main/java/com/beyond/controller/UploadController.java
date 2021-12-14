package com.beyond.controller;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.beyond.pojo.Activity;
import com.beyond.pojo.Music;
import com.beyond.service.ActivityService;
import com.beyond.utils.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

@Slf4j
@RestController
@ApiIgnore
public class UploadController {


    @Autowired
    public ActivityService acService;

    public static String getAddress(String path) {
        if (System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0) {
            //FIXME 修复linux路径
            return "/home/activity_file/" + path;
        } else if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
            return "e:/activity_file/" + path;
        } else {
            return "/Users/lyb/Desktop/activity_file/" + path;
        }
    }

    // 上传图片
    @RequestMapping("/mediaUpload")
    public ResponseBean uploadMedia(HttpServletRequest request, HttpServletResponse response) {
        try {
            MultipartHttpServletRequest imageRequest = (MultipartHttpServletRequest) request;
            MultipartFile uploadFile = imageRequest.getFile("file");
            Integer id = Integer.valueOf(request.getHeader("id"));
            //  1产品主图图 2产品详情图 3活动主图 4活动详情图 5音乐
            Integer uploadType = Integer.valueOf(request.getHeader("uploadType"));
            String originalFilename = uploadFile.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filePath;
            if (uploadType != 5) {
                BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
                // 原始图像的宽度和高度
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();


                if ((int) uploadFile.getSize() > 3 * 1024 * 1024) {
                    return new ResponseBean(60000, "单张图片不能大于3MB", null);
                }
                //产品图得是正方形
//                if (uploadType == 1 && height != width) {
//                    return new ResponseBean(60002, "产品图片必须为正方形", null);
//                }
                filePath = getAddress("image") + "/" + id + "/";
            } else {
                if ((int) uploadFile.getSize() > 5 * 1024 * 1024) {
                    return new ResponseBean(60000, "音乐不能大于5MB", null);
                }
                filePath = getAddress("music") + "/" + id + "/";
            }

            //delAllFile(filePath);
            //参数1为终端ID
            //参数2为数据中心ID
            Snowflake snowflake = IdUtil.getSnowflake(1, 1);
            String snowId = String.valueOf(snowflake.nextId());
            //fuck off!!!
            File file2 = new File(filePath);
            if (!file2.exists()) {
                file2.mkdirs();
            }

            //如果上传详情图
            if (uploadType == 2) {
                String mediaName = snowId + "prodMain" + ext;
                File file = new File(filePath + mediaName);
                uploadFile.transferTo(file);
                Activity ac = acService.getActivityById(id);
                ac.setProdDetailImage("/image/" + id + "/" + mediaName);
                acService.addActivity(ac);
                return new ResponseBean(20000, "上传成功", mediaName);
            } else if (uploadType == 3) {
                String mediaName = snowId + "activityMain" + ext;
                File file = new File(filePath + mediaName);
                uploadFile.transferTo(file);
                Activity ac = acService.getActivityById(id);
                ac.setMainImage("/image/" + id + "/" + mediaName);
                acService.addActivity(ac);
                return new ResponseBean(20000, "上传成功", "/image/" + id + "/" + mediaName);
            } else if (uploadType == 4) {
                String mediaName = snowId + "activityDetail" + ext;
                File file = new File(filePath + mediaName);
                uploadFile.transferTo(file);
                return new ResponseBean(20000, "上传成功", "/image/" + id + "/" + mediaName);
            } else if (uploadType == 5) {
                String mediaName = snowId + "music" + ext;
                File file = new File(filePath + mediaName);
                uploadFile.transferTo(file);
                Music mu = new Music();
                mu.setUrl("/music/" + id + "/" + mediaName);
                mu.setName(originalFilename.substring(0, originalFilename.lastIndexOf(".")));
                mu.setUserId(id);
                mu.setSize(String.valueOf(uploadFile.getSize()));
                mu.setGmtCreate(new Date());
                acService.addMusic(mu);
                return new ResponseBean(20000, "上传成功", "/music/" + id + "/" + originalFilename);
            }

            // 压缩系数
            float resizeTimes = 1f;
            // 调整后的图片的宽度和高度
            int toWidth = (int) (500 * resizeTimes);
            int toHeight = (int) (500 * resizeTimes);
            // 生成缩略图
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            BufferedImage smallImage = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);
            smallImage.getGraphics().drawImage(
                    bufferedImage.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            String formatName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String thumbnailName = snowId + "prodMain" + ext;
            ImageIO.write(smallImage, formatName /* format desired */,
                    new File(filePath + thumbnailName) /* target */);
            Activity ac = acService.getActivityById(id);
            ac.setProdImage("/image/" + id + "/" + thumbnailName);
            acService.addActivity(ac);
            return new ResponseBean(20000, "上传成功", "/image/" + id + "/" + thumbnailName);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean(60002, "上传失败", null);
        }

    }


    // 删除文件夹下所有文件
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            temp = new File(path + tempList[i]);
            if (temp.isFile()) {
                temp.delete();
            }
        }
        return flag;
    }
}
