package br.com.badrequest.transplot.integration.service.mapper;

import br.com.badrequest.transplot.integration.bean.Auth;
import br.com.badrequest.transplot.integration.bean.Response;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

/**
 * Created by gmarques on 3/22/14.
 */

@Rest(rootUrl = "http://179.184.209.242/transplot-rest/rest", converters = {GsonHttpMessageConverter.class})
public interface TransplotServiceMapper extends RestClientErrorHandling {

    @Post("/registrar")
    Response registrar(Auth auth);

}
