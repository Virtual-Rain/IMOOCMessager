package net.zhouxu.web.italker.push.service;

import com.google.common.base.Strings;
import net.zhouxu.web.italker.push.bean.api.account.AccountRspModel;
import net.zhouxu.web.italker.push.bean.api.base.ResponseModel;
import net.zhouxu.web.italker.push.bean.api.user.UpdateInfoModel;
import net.zhouxu.web.italker.push.bean.card.UserCard;
import net.zhouxu.web.italker.push.bean.db.User;
import net.zhouxu.web.italker.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/*用户信息处理*/
//127.0.0.1/api/user/
@Path("/user")
public class UserService extends BaseService {

    //用户信息修改，返回修改后的信息
    @PUT
    //@Path("aa") 不写path就是当前目录
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(@HeaderParam("token") String token,
                                          UpdateInfoModel model) {
        if (Strings.isNullOrEmpty(token) || !UpdateInfoModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();
        //更新用户信息
        self = model.updateToUser(self);
        UserFactory.update(self);
        UserCard card = new UserCard(self, true);
        return ResponseModel.buildOk(card);
    }

    //获取联系人
    @GET
    @Path("/contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact() {
        User self = getSelf();
        //拿到我的联系人
        List<User> users = UserFactory.contacts(self);
        //转换为UserCard
        List<UserCard> userCards = users.stream()
                .map(user -> {
                    return new UserCard(user, true);
                })
                .collect(Collectors.toList());//map操作，相当于转置操作，User-》UserCard
        return ResponseModel.buildOk(userCards);
    }

    //关注人 其实是双方同时关注
    @PUT //修改类使用put
    @Path("/follow/{followId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
        User self = getSelf();
        //自己不能关注自己
        if (self.getId().equals(followId)
                || Strings.isNullOrEmpty(followId)) {
            return ResponseModel.buildParameterError();
        }
        //找到我要关注的人
        User followUser = UserFactory.findById(followId);
        if (followUser == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }
        //备注默认没有，以后扩展
        followUser = UserFactory.follow(self, followUser, null);
        if (followUser == null) {
            //返回服务器异常
            return ResponseModel.buildServiceError();
        }
        //TODO 通知我关注的人我关注了他

        //返回我关注的人的信息
        return ResponseModel.buildOk(new UserCard(followUser, true));
    }

    //获取某人的信息
    @GET //修改类使用put
    @Path("{id}")//http://127.0.0.1/api/user/{id}
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> getUser(@PathParam("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            //返回参数异常
            return ResponseModel.buildParameterError();
        }
        User self = getSelf();
        if (self.getId().equalsIgnoreCase(id)) {
            //返回自己，不需查数据库
            return ResponseModel.buildOk(new UserCard(self, true));
        }
        User user = UserFactory.findById(id);
        if (user == null) {
            //没找到，返回没找到用户
            return ResponseModel.buildNotFoundUserError(null);
        }
        //如果我们之间有关注的记录，则我已关注需要查询信息的用户
        boolean isFollow = UserFactory.getUserFollow(self, user) != null;
        return ResponseModel.buildOk(new UserCard(user, isFollow));
    }

    //搜索人的接口实现
    //为了简化分页，每次20条
    @GET
    @Path("/search/{name:(.*)?}")//name为任意字符，可以为空
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(@DefaultValue("") @PathParam("name") String name) {
        User self = getSelf();
        //先查询数据
        List<User> searchUsers = UserFactory.search(name);
        //把查询的人封装为UserCard
        //判断是否有我关注的人，如果有设置关注状态
        List<User> contacts=UserFactory.contacts(self);

        List<UserCard>userCards=searchUsers.stream()
                .map(user -> {
                    //判断这个人是否在我的联系人中的人 或者我自己
                    boolean isFollow=user.getId().equalsIgnoreCase(self.getId())
                            || contacts.stream().anyMatch(
                                    contactUser->contactUser.getId()
                            .equalsIgnoreCase(user.getId())
                    );
                    return  new UserCard(user,isFollow);
                }).collect(Collectors.toList());
        //返回
        return ResponseModel.buildOk(userCards);
    }

}
