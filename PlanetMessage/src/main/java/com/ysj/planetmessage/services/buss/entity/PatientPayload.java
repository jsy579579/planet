package com.ysj.planetmessage.services.buss.entity;



import com.ysj.planetmessage.services.buss.Payload;

import java.util.Date;

/**
 * @ClassName PatientPayload
 * @Description TODO
 * @Author ysj
 * @Date 2020/4/23 14:30
 * @Version 1.0
 * 消息体 此为测试方法 字段可根据业务变动而变动 实现Payload接口即可
 */
public class PatientPayload implements Payload {
    private final long id;
    private final String name;
    private final String gender;
    private final Date birthday;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    private PatientPayload(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.gender = builder.gender;
        this.birthday = builder.birthday;
    }

    public static class Builder {

        private long id;
        private String name;
        private String gender;
        private Date birthday;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder birthday(Date birthday) {
            this.birthday = birthday;
            return this;
        }

        public PatientPayload build() {
            return new PatientPayload(this);
        }
    }
}
