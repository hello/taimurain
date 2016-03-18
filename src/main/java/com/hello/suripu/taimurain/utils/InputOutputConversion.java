package com.hello.suripu.taimurain.utils;

import com.hello.suripu.api.datascience.NeuralNetMessages;

/**
 * Created by benjo on 3/18/16.
 */
public class InputOutputConversion {

    public final static double [] vecFromProtobuf(final NeuralNetMessages.DataVector datavec) {
        final double [] out = new double[datavec.getVecCount()];
        for (int i = 0; i < datavec.getVecCount(); i++) {
            out[i] = datavec.getVec(i);
        }

        return out;
    }

    public final static double [][] matFromProtobuf(final NeuralNetMessages.NeuralNetInput input) {

        final double[][] mat = new double[input.getMatCount()][0];
        for (int i = 0; i < input.getMatCount(); i++) {

            mat[i] = vecFromProtobuf(input.getMat(i));
        }

        return mat;
    }

    public final static NeuralNetMessages.DataVector protobufFromVec(final double [] vec) {
        final NeuralNetMessages.DataVector.Builder builder =  NeuralNetMessages.DataVector.newBuilder();

        for (int i = 0; i < vec.length; i++) {
            builder.addVec(vec[i]);
        }

        return builder.build();
    }

    public final static NeuralNetMessages.NeuralNetOutput protobufFromMat(final double [][] mat) {

        final NeuralNetMessages.NeuralNetOutput.Builder builder = NeuralNetMessages.NeuralNetOutput.newBuilder();
        for (int i = 0; i < mat.length; i++) {
            builder.addMat(protobufFromVec(mat[i]));
        }

        return builder.build();
    }
}
