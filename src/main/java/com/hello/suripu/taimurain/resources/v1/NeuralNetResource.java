package com.hello.suripu.taimurain.resources.v1;

import com.hello.dropwizard.mikkusu.helpers.AdditionalMediaTypes;
import com.hello.suripu.core.resources.BaseResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

/**
 * Created by benjo on 3/17/16.
 */

@Path("/v1/neuralnet")
public class NeuralNetResource extends BaseResource  {
    @POST
    @Consumes(AdditionalMediaTypes.APPLICATION_PROTOBUF)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/evaluate")
    public byte[] evaluateNet(final byte[] body) {
        return new byte[0];
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getavailable")
    List<String> getAvailableNeuralNets() {
        return Collections.EMPTY_LIST;
    }
}
