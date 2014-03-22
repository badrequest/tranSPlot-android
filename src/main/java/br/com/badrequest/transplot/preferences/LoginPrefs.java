package br.com.badrequest.transplot.preferences;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by gmarques on 3/22/14.
 */
@SharedPref
public interface LoginPrefs {

    @DefaultBoolean(false)
    boolean registered();

}
