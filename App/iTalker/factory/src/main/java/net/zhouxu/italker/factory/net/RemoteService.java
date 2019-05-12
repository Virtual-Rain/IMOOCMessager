package net.zhouxu.italker.factory.net;

import net.zhouxu.italker.factory.model.api.RspModel;
import net.zhouxu.italker.factory.model.api.account.AccountRspModel;
import net.zhouxu.italker.factory.model.api.account.LoginModel;
import net.zhouxu.italker.factory.model.api.account.RegisterModel;
import net.zhouxu.italker.factory.model.card.UserCard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by zx on 2018/5/10.
 * 网络请求的所有的接口
 */

public interface RemoteService {
    //网络请求注册一个注册接口,传入的是RegisterModel,返回 RspModel<AccountRspModel>
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /*登录接口*/
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /*绑定设备Id*/
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    /*用户搜索接口*/
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);

    /*用户关注接口*/
    @PUT("user/follow/{userId}")
    Call<RspModel<UserCard>> userFollow(@Path("userId") String userId);

    /*获取联系人列表*/
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();

    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);
}
