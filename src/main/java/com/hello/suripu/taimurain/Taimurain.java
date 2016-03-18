package com.hello.suripu.taimurain;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.hello.suripu.taimurain.configuration.TaimurainConfiguration;
import com.hello.suripu.taimurain.db.NeuralNetDAO;
import com.hello.suripu.taimurain.db.NeuralNetsFromS3;
import com.hello.suripu.taimurain.resources.v1.NeuralNetResource;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Taimurain extends Application<TaimurainConfiguration> {

    private final static Logger LOGGER = LoggerFactory.getLogger(Taimurain.class);

    public static void main(final String[] args) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        DateTimeZone.setDefault(DateTimeZone.UTC);
        new Taimurain().run(args);
    }

    @Override
    public void initialize(Bootstrap<TaimurainConfiguration> bootstrap) {
        bootstrap.addBundle(new DBIExceptionsBundle());
    }

    @Override
    public void run(final TaimurainConfiguration configuration, Environment environment) throws Exception {


        // Checks Environment first and then instance profile.
        final AWSCredentialsProvider awsCredentialsProvider = new DefaultAWSCredentialsProviderChain();

        final ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.withConnectionTimeout(200); // in ms
        clientConfiguration.withMaxErrorRetry(1);


        final AmazonS3 amazonS3 = new AmazonS3Client(awsCredentialsProvider, clientConfiguration);

        if(configuration.getMetricsEnabled()) {
          final String graphiteHostName = configuration.getGraphite().getHost();
          final String apiKey = configuration.getGraphite().getApiKey();
          final Integer interval = configuration.getGraphite().getReportingIntervalInSeconds();

          final String env = (configuration.getDebug()) ? "dev" : "prod";
          final String prefix = String.format("%s.%s.suripu-taimurain", apiKey, env);

          final Graphite graphite = new Graphite(new InetSocketAddress(graphiteHostName, 2003));

          final GraphiteReporter reporter = GraphiteReporter.forRegistry(environment.metrics())
              .prefixedWith(prefix)
              .convertRatesTo(TimeUnit.SECONDS)
              .convertDurationsTo(TimeUnit.MILLISECONDS)
              .filter(MetricFilter.ALL)
              .build(graphite);
          reporter.start(interval, TimeUnit.SECONDS);

          LOGGER.info("Metrics enabled.");
        } else {
          LOGGER.warn("Metrics not enabled.");
        }


        /* Neural net data DAOs */
        final NeuralNetDAO neuralNetDAO = NeuralNetsFromS3.createFromConfigBucket(amazonS3,configuration.getNeuralNetConfiguration().getBucket(),configuration.getNeuralNetConfiguration().getKey());
        final NeuralNetResource neuralNetResource = new NeuralNetResource(neuralNetDAO);

        environment.jersey().register(neuralNetResource);

    }


}
