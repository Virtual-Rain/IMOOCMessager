package net.zhouxu.web.italker.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/*群成员的Model*/
@Entity
@Table(name="TB_GROUP_MEMBER")
public class GroupMember {

    public static final int PERMISSION_TYPE_NONE=0;//
    public static final int PERMISSION_TYPE_ADMIN=1;//
    public static final int PERMISSION_TYPE_ADMIN_SU=100;//

    public static final int NOTIFY_LEVEL_INVALID=-1;//默认不接收消息
    public static final int NOTIFY_LEVEL_NONE=0;//默认通知级别
    public static final int NOTIFY_LEVEL_CLOSE=1;//接收消息不提示

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(updatable = false,nullable = false)
    private String id;

    //别名
    @Column
    private String alias;

    @Column(nullable = false)
    private int nitifyLevel=NOTIFY_LEVEL_NONE;

    @Column(nullable = false)
    private int permissionType=PERMISSION_TYPE_NONE;

    //定义为创建时间戳，创建就写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime creteAt=LocalDateTime.now();

    //定义为更新时间戳，更新就写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt=LocalDateTime.now();

    //成员对应的用户信息
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private User user;
    @Column(nullable = false,updatable = false,insertable = false)
    private String userId;

    //成员对应的群信息
    @JoinColumn(name = "groupId")
    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Group group;
    @Column(nullable = false,updatable = false,insertable = false)
    private String groupId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getNitifyLevel() {
        return nitifyLevel;
    }

    public void setNitifyLevel(int nitifyLevel) {
        this.nitifyLevel = nitifyLevel;
    }

    public int getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(int permissionType) {
        this.permissionType = permissionType;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
