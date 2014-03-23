package br.com.badrequest.transplot.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import br.com.badrequest.transplot.R;
import br.com.badrequest.transplot.integration.bean.Auth;
import br.com.badrequest.transplot.integration.bean.Response;
import br.com.badrequest.transplot.integration.service.handler.ServiceErrorHandler;
import br.com.badrequest.transplot.integration.service.mapper.TransplotServiceMapper;
import br.com.badrequest.transplot.preferences.LoginPrefs_;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by gmarques on 3/22/14.
 */
@EActivity
public class Start extends Activity {

    @SystemService
    TelephonyManager tManager;

    @Pref
    LoginPrefs_ loginPrefs;

    @RestService
    TransplotServiceMapper transplotServiceMapper;

    @Bean
    ServiceErrorHandler serviceErrorHandler;

    //TODO: Deixar externo a remocao das barras
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        int id = getResources().getIdentifier("config_enableTranslucentDecor", "bool", "android");
        if (id != 0) { //KitKat or higher
            boolean enabled = getResources().getBoolean(id);
            // enabled = are translucent bars supported on this device
            if (enabled) {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
        super.onCreate(savedInstanceState);
    }

    @AfterInject
    void checkLogin() {
        transplotServiceMapper.setRestErrorHandler(serviceErrorHandler);
        if (loginPrefs.registered().get()) {
            openMenu();
        } else {
            register();
        }
    }

    @UiThread
    void openMenu() {
        Intent menuIntent = new Intent(this, Menu_.class);
        menuIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(menuIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    @Background
    void register() {
        Response response = transplotServiceMapper.registrar(new Auth(tManager.getDeviceId()));
        if(response != null && response.success()) {
            loginPrefs.edit().registered().put(true);
            openMenu();
        } else {
            showDialogError();
        }
    }

    @UiThread
    void showDialogError() {
        new AlertDialog.Builder(this)
            .setMessage(R.string.dialog_error_register)
            .setTitle(R.string.dialog_error_title_no_connection)
            .setCancelable(false)
            .setNeutralButton(R.string.dialog_error_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    register();
                }
            })
            .create()
            .show();
    }
}
