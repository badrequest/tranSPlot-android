package br.com.badrequest.transplot.integration.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by gmarques on 3/22/14.
 */
public @Getter @Setter class Auth implements Serializable {

    private String uid;
    private String os;

    public Auth(String uid) {
        this.uid = uid;
        this.os = "android";
    }

    public Auth(String uid, String os) {
        this.uid = uid;
        this.os = os;
    }

}
