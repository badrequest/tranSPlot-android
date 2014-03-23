package br.com.badrequest.transplot.integration.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by gmarques on 3/23/14.
 */
public @Getter @Setter class Region implements Serializable {

    private Point i;
    private Point f;
    private Integer scale;

    public Region() {
    }

    public Region(Point i, Point f, Integer scale) {
        this.i = i;
        this.f = f;
        this.scale = scale;
    }
}
