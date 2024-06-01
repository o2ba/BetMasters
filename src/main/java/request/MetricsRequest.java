package request;


import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.TelemetryConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * This class is responsible for handling the metrics of the Azure services.
 * It uses a singleton pattern to manage the lifecycle of the telemetry client.
 */
public final class MetricsRequest {
    private static MetricsRequest instance;
    private TelemetryClient telemetryClient;

    private MetricsRequest() {
        initializeTelemetryClient();
    }

    /**
     * Initializes the TelemetryClient with the instrumentation key from environment.
     */
    private void initializeTelemetryClient() {
        String instrumentationKey = System.getenv("INSTRUMENTATION_KEY");
        if (instrumentationKey == null) {
            throw new RuntimeException("INSTRUMENTATION_KEY is not set in environment variables.");
        }

        TelemetryConfiguration config = TelemetryConfiguration.createDefault();
        config.setInstrumentationKey(instrumentationKey);
        telemetryClient = new TelemetryClient(config);

    }

    /**
     * Provides the global access point for the MetricsRequest instance.
     * @return the singleton instance of the MetricsRequest.
     */
    public static synchronized MetricsRequest getInstance() {
        if (instance == null) {
            instance = new MetricsRequest();
        }
        return instance;
    }

    /**
     * Returns the telemetry client.
     * @return the telemetry client
     */
    public @NotNull TelemetryClient getTelemetryClient() {
        return telemetryClient;
    }
}


