package com.hello.suripu.taimurain.resources.v1;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.protobuf.InvalidProtocolBufferException;
import com.hello.dropwizard.mikkusu.helpers.AdditionalMediaTypes;
import com.hello.suripu.api.datascience.NeuralNetMessages;
import com.hello.suripu.api.datascience.NeuralNetProtos;
import com.hello.suripu.core.util.JsonError;
import com.hello.suripu.taimurain.neuralnet.NeuralNetEvaluator;
import com.hello.suripu.taimurain.db.NeuralNetDAO;
import com.hello.suripu.taimurain.utils.InputOutputConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by benjo on 3/17/16.
 */

@Path("/v1/neuralnet")
public class NeuralNetResource  {

    final Logger LOGGER = LoggerFactory.getLogger(NeuralNetResource.class);

    final Map<String,NeuralNetEvaluator> nets;

    public NeuralNetResource(final NeuralNetDAO neuralNetDAO) {

        final List<String> ids = neuralNetDAO.getAvailableIds();
        nets = Maps.newHashMap();

        for (final String id : ids) {
            final Optional<NeuralNetProtos.NeuralNetMessage> serializedNet = neuralNetDAO.getNetDataById(id);

            if (!serializedNet.isPresent()) {
                LOGGER.error("net_id={}, error=does_not_exist");
                continue;
            }

            final Optional<NeuralNetEvaluator> neuralNetEvaluator = NeuralNetEvaluator.createFromBinDataAndConfig(serializedNet.get().getParams().toByteArray(),serializedNet.get().getConfiguration());

            if (!neuralNetEvaluator.isPresent()) {
                LOGGER.error("net_id={}, error=failed_to_deserialize");
                continue;
            }

            nets.put(id,neuralNetEvaluator.get());
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/evaluate")
    public byte[] evaluateNet(final byte[] body) {
        try {
            final NeuralNetMessages.NeuralNetInput input = NeuralNetMessages.NeuralNetInput.parseFrom(body);

            if (!input.hasNetId()) {
                LOGGER.warn("action=eval_net, error=no_net_specified");
                throw new InvalidProtocolBufferException("requested net did not have an ID!");
            }

            if (!nets.containsKey(input.getNetId())) {
                LOGGER.warn("action=eval_net, error=net_not_found");
                throw new InvalidProtocolBufferException(String.format("requested net %s was not found",input.getNetId()));
            }

            final NeuralNetEvaluator evaluator = nets.get(input.getNetId());

            final double [][] x =  InputOutputConversion.matFromProtobuf(input);

            final double [][] y = evaluator.evaluate(x);

            final byte[] bytes = InputOutputConversion.protobufFromMat(y).toByteArray();

            int nrows = 0;
            int ncols = 0;
            if (y.length > 0) {
                nrows = y.length;
                ncols = y[0].length;
            }
            
            LOGGER.info("output was size {} x {} and {} bytes", nrows, ncols, bytes.length);


            return bytes;


        }
        catch (final InvalidProtocolBufferException e) {
            LOGGER.error(e.getMessage());
            throw new WebApplicationException(Response.status(Response.Status.NO_CONTENT)
                    .entity(new JsonError(204, e.getMessage())).build());
        }
        catch (final Exception e) {
            LOGGER.error(e.getMessage());
            throw new WebApplicationException(Response.status(Response.Status.NO_CONTENT)
                    .entity(new JsonError(204, e.getMessage())).build());
        }
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getavailable")
    public Set<String> getAvailableNeuralNets() {
        return nets.keySet();
    }
}
