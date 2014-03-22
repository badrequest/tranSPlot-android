package br.com.badrequest.transplot.integration.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by gmarques on 3/22/14.
 */
public @Getter @Setter class Response implements Serializable {

    private String ans;

    public boolean success() {
        return "ok".equals(ans);
    }

}
