package net.zhouxu.web.italker.push.service;

import com.google.common.base.Strings;
import net.zhouxu.web.italker.push.bean.api.account.AccountRspModel;
import net.zhouxu.web.italker.push.bean.api.account.LoginModel;
import net.zhouxu.web.italker.push.bean.api.account.RegisterModel;
import net.zhouxu.web.italker.push.bean.api.base.ResponseModel;
import net.zhouxu.web.italker.push.bean.card.UserCard;
import net.zhouxu.web.italker.push.bean.db.User;
import net.zhouxu.web.italker.push.bean.db.UserFollow;
import net.zhouxu.web.italker.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


/*@author zhouxu*/
// 127.0.0.1/api/account/...
@Path("/account")
public class AccountService extends BaseService {

    //登录
    @POST
    //请求与返回格式的响应体为JSON
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model) {
        //校验参数
        if (!LoginModel.check(model)) {
            //返回参数异常
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.login(model.getAccount().trim(), model.getPassword().trim());
        if (user != null) {
            //如果请求携带了pushId,进行绑定
            if (!Strings.isNullOrEmpty(model.getPushId())) {
                return bind(user, model.getPushId());
            }
            //返回当前账户
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        } else {
            //登录失败
            return ResponseModel.buildLoginError();
        }
    }

    //注册
    @POST
    //请求与返回格式的响应体为JSON
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model) {

        //校验参数
        if (!RegisterModel.check(model)) {
            //返回参数异常
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (user != null) {
            //已有账号错误
            return ResponseModel.buildHaveAccountError();
        }
        user = UserFactory.findByName(model.getName().trim());
        if (user != null) {
            //已有名字
            return ResponseModel.buildHaveNameError();
        }
        //开始注册逻辑
        user = UserFactory.register(model.getAccount(),
                model.getPassword(), model.getName());
        if (user != null) {
            //如果请求携带了pushId 进行绑定
            if (!Strings.isNullOrEmpty(model.getPushId())) {
                return bind(user, model.getPushId());
            }
            //传递当前账户信息
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        } else {
            //注册错误
            return ResponseModel.buildRegisterError();
        }
    }

    //绑定设备Id
    @POST
    //请求与返回格式的响应体为JSON
    @Path("/bind/{pushId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //从请求头中获取token字段
    //pushId从url地址中获取
    public ResponseModel<AccountRspModel> bind(@PathParam("pushId") String pushId) {
        //校验参数
        if (Strings.isNullOrEmpty(pushId)) {
            //返回参数异常
            return ResponseModel.buildParameterError();
        }
        User self = getSelf();
        return bind(self, pushId);
    }

    /*绑定操作*/
    private static ResponseModel<AccountRspModel> bind(User user, String pushId) {
        //进行设备id绑定
        user = UserFactory.bindPushId(user, pushId);
        if (user == null) {
            //返回服务器异常
            return ResponseModel.buildServiceError();

        }
        //返回当前账户,并将绑定状态设置为true
        AccountRspModel rspModel = new AccountRspModel(user, true);
        return ResponseModel.buildOk(rspModel);
    }
}
