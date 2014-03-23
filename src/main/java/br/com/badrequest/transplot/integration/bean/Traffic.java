package br.com.badrequest.transplot.integration.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gmarques on 3/23/14.
 */
public @Getter @Setter class Traffic implements Serializable {

    private String status;
    private String time;
    private List<Velocity> data;

    public @Getter @Setter class Velocity implements Serializable {

        private Point position;
        private Integer meanVelocity;

    }
}
