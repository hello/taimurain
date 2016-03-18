package com.hello.suripu.taimurain.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hello.suripu.coredw8.configuration.GraphiteConfiguration;
import com.hello.suripu.core.configuration.NewDynamoDBConfiguration;
import com.hello.suripu.coredw8.configuration.S3BucketConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TaimurainConfiguration extends Configuration {



    @Valid
    @NotNull
    @JsonProperty("metrics_enabled")
    private Boolean metricsEnabled;
    public Boolean getMetricsEnabled() {
        return metricsEnabled;
    }

    @Valid
    @JsonProperty("debug")
    private Boolean debug = Boolean.FALSE;
    public Boolean getDebug() {
        return debug;
    }

    @Valid
    @NotNull
    @JsonProperty("graphite")
    private GraphiteConfiguration graphite;
    public GraphiteConfiguration getGraphite() {
        return graphite;
    }


    @Valid
    @NotNull
    @JsonProperty("neural_net_config")
    private S3BucketConfiguration neuralNetConfiguration;
    public S3BucketConfiguration getNeuralNetConfiguration() { return neuralNetConfiguration; }

}
