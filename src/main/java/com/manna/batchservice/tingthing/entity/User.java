package com.manna.batchservice.tingthing.entity;

import com.manna.batchservice.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`user`")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class User extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String birth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String nickname;

    private String imageUri;
    private String hatId;
    private String handId;
    private String skinId;
    private String topId;
    private String faceId;
    private String bottomId;
    private String background;

    @Override
    protected void onCreate() {
        super.onCreate();
        if (this.role == null) {
            this.role = Role.USER;
        }
        if (this.skinId == null) {
            this.skinId = "01";
        }
    }

    public enum Role {
        USER, ADMIN
    }

    public enum Gender {
        MALE, FEMALE
    }
}