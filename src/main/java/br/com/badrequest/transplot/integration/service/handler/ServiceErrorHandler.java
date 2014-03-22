package br.com.badrequest.transplot.integration.service.handler;

import org.androidannotations.annotations.EBean;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.web.client.RestClientException;

/**
 * Created by gmarques on 3/22/14.
 */
@EBean
public class ServiceErrorHandler implements RestErrorHandler {

    @Override
    public void onRestClientExceptionThrown(RestClientException e) {
        //TODO: Disparar broadcast informando o erro
    }

}
