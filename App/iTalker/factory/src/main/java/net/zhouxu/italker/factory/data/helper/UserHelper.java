package net.zhouxu.italker.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.zhouxu.italker.factory.Factory;
import net.zhouxu.italker.factory.R;
import net.zhouxu.italker.factory.data.DataSource;
import net.zhouxu.italker.factory.model.api.RspModel;
import net.zhouxu.italker.factory.model.card.UserCard;
import net.zhouxu.italker.factory.model.db.User;
import net.zhouxu.italker.factory.model.db.User_Table;
import net.zhouxu.italker.factory.net.NetWork;
import net.zhouxu.italker.factory.net.RemoteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户信息网络请求
 * Created by zx on 2018/5/31.
 */

public class UserHelper {
    //更新用户信息操作，异步
  /*  public static void update(UserUpdateModel model){

    }*/
    //搜索的方法
    public static Call search(String name, final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);

        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    //返回数据
                    callback.onDataLoaded(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        //返回当前的调度者
        return call;
    }

    //关注的网络请求
    public static void follow(String id, final DataSource.Callback<UserCard> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<UserCard>> call = service.userFollow(id);

        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    //保存到本地数据库
                    User user = userCard.build();
                    user.save();
                    //TODO 通知联系人刷新

                    //返回数据
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    //刷新联系人的操作
    public static void refreshContacts(final DataSource.Callback<List<UserCard>>callback){
        RemoteService service=NetWork.remote();
        service.userContacts()
                .enqueue(new Callback<RspModel<List<UserCard>>>() {
                    @Override
                    public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                        RspModel<List<UserCard>> rspModel=response.body();
                        if(rspModel.success()){
                            //返回数据
                            callback.onDataLoaded(rspModel.getResult());
                        }else{
                            Factory.decodeRspCode(rspModel,callback);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                        callback.onDataNotAvailable(R.string.data_network_error);
                    }
                });
    }

    //从本地查询一个用的信息
    public static User findFromLocal(String id){
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }
    public static User findFromNet(String id){
        RemoteService remoteService=NetWork.remote();
        try{
            Response<RspModel<UserCard>>response=remoteService.userFind(id).execute();
            UserCard card=response.body().getResult();
            if(card!=null){
                //TODO 数据库的存储但是没有通知
                User user=card.build();
                user.save();

                return user;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /*搜索一个用户，优先本地缓存
    * 本地没有从网络拉取*/
    public static User search(String id){
        User user=findFromLocal(id);
        if(user==null){
            return findFromNet(id);
        }
        return user;
    }

    /*搜索一个用户，优先网络查询
    * 没有从本地缓存拉取*/
    public static User searchFirstOfNet(String id){
        User user=findFromNet(id);
        if(user==null){
            return findFromLocal(id);
        }
        return user;
    }
}
