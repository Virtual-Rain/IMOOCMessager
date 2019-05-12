package net.zhouxu.web.italker.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/*申请记录Model，对应数据库*/
@Entity
@Table(name="TB_APPLY")
public class Apply {

    public static final int TYPE_ADD_USER=1;//添加好友
    public static final int TYPE_ADD_GROUP=1;//添加群

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(updatable = false,nullable = false)
    private String id;

    //描述部分，对我们的申请做描述
    @Column(nullable = false)
    private String description;

    //附件,可空
    @Column(columnDefinition ="TEXT")
    private String attach;

    //当前申请的类型
    @Column(nullable = false)
    private int type;

    //目标Id；不进行强关联
    //type:User User.Id; type:Group Group.id;
    @Column(nullable = false)
    private String targetId;

    //定义为创建时间戳，创建就写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime creteAt=LocalDateTime.now();

    //定义为更新时间戳，更新就写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt=LocalDateTime.now();

    //申请人 可为空，系统管理员
    //可以有的很多个申请
    @ManyToOne
    @JoinColumn(name ="applicantId")
    private User applicant;
    @Column(updatable = false,insertable = false)
    private String applicantId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
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

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }
}
