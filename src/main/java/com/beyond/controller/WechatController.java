package com.beyond.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.beyond.pojo.Custom;
import com.beyond.pojo.User;
import com.beyond.service.ShopAndCustomService;
import com.beyond.service.UserService;
import com.beyond.utils.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author lyb
 * @create 10/28/21 3:10 PM
 */
@Slf4j
@Api(tags = "公众号模块")
@RestController
public class WechatController {
    @Autowired
    private WxMpService mpService;
    @Autowired
    private WxMaService maService;
    @Autowired
    private ShopAndCustomService scService;
    @Autowired
    public UserService userService;

    @ApiImplicitParam(name = "code", value = "用户code", required = true)
    @ApiOperation(value = "根据code向微信拉取用户信息，并返回给前端")
    @RequestMapping(value = "/getWxUserInfo", method = RequestMethod.GET)
    public ResponseBean<Custom> getWxUserInfo(String code) throws WxErrorException {
        WxOAuth2AccessToken wxOAuth2AccessToken = mpService.getOAuth2Service().getAccessToken(code);
        WxOAuth2UserInfo wxUser = mpService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, null);
//        String lang = "zh_CN"; //语言
//        WxMpUser user = mpService.getUserService().userInfo(openid,lang);
        Custom cs = scService.getCustomByWechat(wxUser);
        return ResponseBean.<Custom>builder()
                .code(20000)
                .msg("success")
                .data(cs)
                .build();
    }

    @ApiImplicitParam(name = "url", value = "当前网页的URL，不包含#及其后面部分", required = true)
    @ApiOperation(value = "根据url获取签名")
    @RequestMapping(value = "/getJsapiTiket", method = RequestMethod.GET)
    public ResponseBean<WxJsapiSignature> getJsapiTiket(String url) throws WxErrorException {
        final WxJsapiSignature jsSign = mpService.createJsapiSignature(url);
        return ResponseBean.<WxJsapiSignature>builder()
                .code(20000)
                .msg("success")
                .data(jsSign)
                .build();
    }


    @ApiIgnore
    @RequestMapping(value = "/getMpOpenId", method = RequestMethod.GET)
    public ResponseBean<String> getMpOpenId(String code) throws WxErrorException {
        WxMaJscode2SessionResult session = maService.getUserService().getSessionInfo(code);
        //String sessionKey = session.getSessionKey();
        String openId = session.getOpenid();
        return ResponseBean.<String>builder()
                .code(20000)
                .msg("success")
                .data(openId)
                .build();
    }

    @ApiIgnore
    @RequestMapping(value = "/mpLogin", method = RequestMethod.GET)
    public ResponseBean<User> mpLogin(String account, String pwd, String openId) {
        User user = userService.getUserByMp(account, pwd);
        if (null == user) {
            return ResponseBean.<User>builder()
                    .code(30000)
                    .msg("账号或密码不正确")
                    .build();
        } else if (null != user.getCredential() && !user.getCredential().equals(openId)) {
            return ResponseBean.<User>builder()
                    .code(30000)
                    .msg("账号已被其他微信绑定，请联系管理员")
                    .build();
        } else if (null != user.getCredential() && user.getCredential().equals(openId)) {
            return ResponseBean.<User>builder()
                    .code(20000)
                    .msg("登录成功")
                    .data(user)
                    .build();
        } else {
            user.setCredential(openId);
            userService.addUser(user);
            return ResponseBean.<User>builder()
                    .code(20000)
                    .msg("登录成功")
                    .data(user)
                    .build();
        }

    }

    @ApiIgnore
    @RequestMapping(value = "/mpLogout", method = RequestMethod.GET)
    public ResponseBean<T> mpLogin(Integer id) {
        User user = userService.getUserById(id);
        user.setCredential(null);
        userService.addUser(user);
        return ResponseBean.<T>builder()
                .code(20000)
                .msg("解除绑定成功！")
                .build();
    }
}
