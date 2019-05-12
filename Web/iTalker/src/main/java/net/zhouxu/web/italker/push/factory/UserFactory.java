package net.zhouxu.web.italker.push.factory;

import com.google.common.base.Strings;
import net.zhouxu.web.italker.push.bean.db.User;
import net.zhouxu.web.italker.push.bean.db.UserFollow;
import net.zhouxu.web.italker.push.utils.Hib;
import net.zhouxu.web.italker.push.utils.TextUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserFactory {

    //通过Token查询用户信息
    public static User findByToken(String token) {
        return Hib.query(session -> (User) session
                .createQuery("from User where token=:token")
                .setParameter("token", token)
                .uniqueResult());
    }

    //通过手机查询用户信息
    public static User findByPhone(String phone) {
        return Hib.query(session -> (User) session
                .createQuery("from User where phone=:phone")
                .setParameter("phone", phone)
                .uniqueResult());
    }

    //通过用户姓名查询
    public static User findByName(String name) {
        return Hib.query(session -> (User) session
                .createQuery("from User where name=:name")
                .setParameter("name", name)
                .uniqueResult());
    }
    //通过用户ID查询
    public static User findById(String id) {
        return Hib.query(session -> session.get(User.class,id));
    }
    //更新用户信息到数据库
    public static User update(User user) {
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    /*给当前账户绑定设备id*/
    public static User bindPushId(User user, String pushId) {
        if (Strings.isNullOrEmpty(pushId))
            return null;
        //查询是否有其他设备绑定，如果有取消其绑定
        Hib.queryOnly(session -> {
            List<User> userList = (List<User>) session
                    .createQuery("from User where lower(pushId)=:pushId and id!=:userId")
                    .setParameter("pushId", pushId.toLowerCase())
                    .setParameter("userId", user.getId())
                    .list();
            for (User u : userList) {
                //更新为null
                u.setPushId(null);
                session.saveOrUpdate(u);
            }
        });
        if (pushId.equalsIgnoreCase(user.getPushId())) {
            //如果当前需要绑定的设备Id，之前已经绑定过，无需再绑定
            return user;
        } else {
            //如果账户绑定的设备id与当前需要绑定的设备id不同
            //需要跟之前的设备推送消息，强制下线
            if (!Strings.isNullOrEmpty(user.getPushId())) {
                //TODO 推送一个推出消息
            }
            //更新新的设备id
            user.setPushId(pushId);
            return update(user);
        }
    }

    /*用账户和密码进行登录*/
    public static User login(String account, String password) {
        final String accountStr = account.trim();
        //密码同样处理
        final String passwordStr = encodePassword(password);
        User user = Hib.query(session -> (User) session
                .createQuery("from User where phone=:phone and password=:password")
                .setParameter("phone", accountStr)
                .setParameter("password", passwordStr)
                .uniqueResult());
        if (user != null) {
            //用户进行登录操作，更新Token
            user = login(user);
        }
        return user;
    }

    /*用户注册写入数据库，并返回数据库中的User信息*/
    public static User register(String account, String password, String name) {
        //数据信息处理
        account = account.trim();
        password = encodePassword(password);

        User user = createUser(account, password, name);
        if (user != null) {
            user = login(user);
        }
        return user;
    }

    /*注册部分新建用户*/
    private static User createUser(String account, String password, String name) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setPhone(account);
        //数据库存储
        return Hib.query(session -> {
            session.save(user);
            return user;
        });
    }

    /*用户登录操作*/
    private static User login(User user) {
        //使用随机UUID值充当token
        String newToken = UUID.randomUUID().toString();
        //加密
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);
        //更新token
        return update(user);
    }

    //密码加密
    private static String encodePassword(String password) {
        //密码去除首位空格
        password = password.trim();
        //进行MD5非对称加密,加盐会更安全，盐需要存储
        password = TextUtil.getMd5(password);
        //Base64再加密,可加盐
        password = TextUtil.encodeBase64(password);
        return password;
    }
    //获取我的联系人的列表
    public static List<User>contacts(User self){
        return Hib.query(session -> {
            session.load(self,self.getId());
            //获取我关注的人
            Set<UserFollow> flows=self.getFollowing();
           //使用简写方式
            return flows.stream()
                    .map(UserFollow::getTarget).collect(Collectors.toList());
        });
    }
    /*关注人操作
    * @param origin 发起者
    * @param target 被关注的人
    * @param alias 备注名
    * @return 被关注人的信息*/
    public static User follow(final User origin,final User target,final String alias){
        UserFollow follow=getUserFollow(origin,target);
        if(follow!=null){
            //已关注直接返回
            return follow.getTarget();
        }
       return Hib.query(session -> {
            //想要操作懒加载的数据，需要重新load一次
            session.load(origin,origin.getId());
            session.load(target,target.getId());
            //我关注人的时候，同时他也关注我，需要添加两条数据
            UserFollow originFollow=new UserFollow();
            originFollow.setOrigin(origin);
            originFollow.setTarget(target);
            //备注只能是我对他的，他对我默认没有备注
            originFollow.setAlias(alias);

            UserFollow targetFollow=new UserFollow();
            targetFollow.setOrigin(target);
            targetFollow.setTarget(origin);

            //保存数据库
            session.save(originFollow);
            session.save(targetFollow);

            return target;

        });
    }
    /*查询是否关注
     * @param origin 发起者
     * @param target 被关注的人
     * @return 中间类 UserFollow*/
    public static UserFollow getUserFollow(final User origin,final User target){
        return Hib.query(session -> {
            return (UserFollow)session.createQuery("from UserFollow  where originId=:originId and targetId=:targetId")
                    .setParameter("originId",origin.getId())
                    .setParameter("targetId",target.getId())
                    .uniqueResult();//查询唯一数据
        });
    }

    /*搜索联系人*/
    @SuppressWarnings("unchecked")
    public static List<User> search(String name) {
        if(Strings.isNullOrEmpty(name))
            name="";//保证不能为null
        final String searchName="%"+name+"%";
        return Hib.query(session -> {
            //查询条件：name忽略大小写 like 头像、描述必须完善才能查询到
           return (List<User>) session.createQuery("from User where lower(name) like :name and portrait is not null and description is not null ")
            .setParameter("name",searchName)
                   .setMaxResults(20)
                   .list();
        });
    }
}
