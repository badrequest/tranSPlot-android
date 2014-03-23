package br.com.badrequest.transplot.integration.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by gmarques on 3/23/14.
 */
public @Getter @Setter class Point implements Serializable {

    private int x;
    private int y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
