package com.hello.suripu.taimurain.neuralnet;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.hello.suripu.taimurain.exceptions.NeuralNetConfigurationException;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.layers.FeedForwardLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * Created by benjo on 2/23/16.
 */
public class NeuralNetEvaluator {

    final static Logger LOGGER = LoggerFactory.getLogger(NeuralNetEvaluator.class);

    private final MultiLayerNetwork net;

    public static Optional<NeuralNetEvaluator> createFromBinDataAndConfig(final byte [] paramsBinData, final String confData) {
        final MultiLayerConfiguration confFromJson = MultiLayerConfiguration.fromJson(confData);
        final MultiLayerNetwork net = new MultiLayerNetwork(confFromJson);

        try {
            //final InputStream ios = Files.newInputStream(Paths.get("/Users/benjo/Downloads/foo.params"));
            final ByteArrayInputStream bis = new ByteArrayInputStream(paramsBinData);
            final DataInputStream dis = new DataInputStream(bis);

            final INDArray params = Nd4j.read(dis);
            net.init();
            net.setParameters(params);

            return Optional.of(new NeuralNetEvaluator(net));

        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return Optional.absent();
    }

    public static NeuralNetEvaluator createFromNet(final MultiLayerNetwork net) {
        return new NeuralNetEvaluator(net);
    }

    private NeuralNetEvaluator(final MultiLayerNetwork net) {
       this.net = net;
    }

    /*
    *  get output classes
    *  @param x has shape of NUM_STATES x LENGTH_OF_TIME_SERIES
    *
    * */

    public double [][] evaluate(final double [][] input) throws NeuralNetConfigurationException {
        //Data: has shape [miniBatchSize,nIn,timeSeriesLength];
        //so we are of size [1,nIn,timeSeriesLength]
        if (input.length == 0) {
            return new double[0][0];
        }

        //UGLY, but does the job
        final int expectedNumberOfInputs = ((FeedForwardLayer)net.getLayerWiseConfigurations().getConf(0).getLayer()).getNIn();

        final int dataInputVecSize = input.length;
        final int T = input[0].length;

        LOGGER.info("expected_num_inputs={}, actual_num_input{}, time_len={}",expectedNumberOfInputs,dataInputVecSize,T);

        if (dataInputVecSize < expectedNumberOfInputs) {
            throw new NeuralNetConfigurationException(String.format("excpected at least %d rows of data, and found %d instead.",expectedNumberOfInputs,dataInputVecSize));
        }

        final double [][] x = new double[expectedNumberOfInputs][0];

        for (int iInput = 0; iInput < expectedNumberOfInputs; iInput++) {
            x[iInput] = input[iInput];
        }


        final INDArray primitiveDataAsInput = Nd4j.create(x);
        final INDArray features = Nd4j.create(Lists.<INDArray>newArrayList(primitiveDataAsInput),new int[]{1,primitiveDataAsInput.size(0),primitiveDataAsInput.size(1)});
        final INDArray output = net.output(features).slice(0);

        final double [][] y = new double[output.shape()[0]][output.shape()[1]];


        for (int i = 0; i < y.length; i++) {
            for (int t = 0; t < y[0].length; t++) {
                y[i][t] = output.getRow(i).getDouble(t);
            }
        }

        return y;
    }
}
