package net.zhouxu.web.italker.push.bean.db;



import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/*用户关系的Model*/
@Entity
@Table(name="TB_USER_Follow")
public class UserFollow {
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(updatable = false,nullable = false)
    private String id;

    //定义一个发起人，你关注某人
    //多对1 你可以关注很多人，每一次关注都是一条记录
    //你可以创建很多个关注信息，所以是多对1
    //这里多对1指的是一个User对多个UserFollow
    //optional 不可选，必须存储，一条关注记录一定要有一个“你”
    @ManyToOne(optional = false)
    //定义关联的表字段名id，对应User表中的id字段
    //定义的是数据库中的存储字段
    @JoinColumn(name = "originId")
    private User origin;
    //这个列提取到我们的Model中，不允许为空，不允许更新，插入
    @Column(nullable = false,updatable = false,insertable = false)
    private String originId;

    //定义关注的目标，你关注的人
    //也是多对1，你可以被很多人关注，每一条关注都是一条记录
    //多个UserFollow对应一个User
    @ManyToOne(optional = false)
    //定义关联的表字段名id，对应User表中的id字段
    //定义的是数据库中的存储字段
    @JoinColumn(name = "targetId")
    private User target;
    //这个列提取到我们的Model中，不允许为空，不允许更新，插入
    @Column(nullable = false,updatable = false,insertable = false)
    private String targetId;

    //别名，target的备注 可为空
    @Column
    private String alias;

    //定义为创建时间戳，创建就写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime creteAt=LocalDateTime.now();

    //定义为更新时间戳，更新就写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt=LocalDateTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOrigin() {
        return origin;
    }

    public void setOrigin(User origin) {
        this.origin = origin;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public LocalDateTime getCreteAt() {
        return creteAt;
    }

    public void setCreteAt(LocalDateTime creteAt) {
        this.creteAt = creteAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }


}
