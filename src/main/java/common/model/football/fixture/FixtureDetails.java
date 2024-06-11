package common.model.football.fixture;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import common.annotation.RequiresInit;
import common.model.football.fixture.enums.FixtureStatus;
import io.swagger.models.auth.In;
import org.jetbrains.annotations.Nullable;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;


/**
 * This class represents the details of a fixture.
 * Without this method, certain fields will be null.
 */
public class FixtureDetails{

    // All getters / setters which require initialization must check if the object is initialized
    boolean init = false;

    // Fields with _ are not exposed to the client - also not with getters

    @SerializedName("timestamp")
    private int _timestamp;

    private record Status(
            @SerializedName("short") String shortStatus,
            @SerializedName("elapsed") Integer elapsed
    ) { }

    @SerializedName("status")
    private Status _status;

    private record Periods(
            @SerializedName("first") Integer firstPeriod,
            @SerializedName("second") Integer secondPeriod
    ) { }

    @SerializedName("periods")
    private Periods _periods;

    // Exposed fields with getters / setters

    /** The fixture id. */
    @SerializedName("id")
    private Integer id;

    /** The fixture date. */
    private transient LocalDateTime fixtureDate;

    /** The fixture status. */
    private transient FixtureStatus status;

    /** The fixture elapsed time. */
    private transient Integer elapsed;

    /** The fixture first period. */
    private transient Integer firstPeriod;

    /** The fixture second period. */
    private transient Integer secondPeriod;

    /**
     * This is called if a getter is called and the object is not initialized.
     */
    private void init() {
        Instant instant = Instant.ofEpochSecond(_timestamp);
        fixtureDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        status = FixtureStatus.valueOf(_status.shortStatus());
        elapsed = _status.elapsed();
        firstPeriod = _periods.firstPeriod();
        secondPeriod = _periods.secondPeriod();

        init = true;
    }

    /**
     * Returns the fixture id.
     * @return the fixture id
     */
    public Integer getId() {
        return id;
    }


    /**
     * Returns the fixture status.
     * @return the fixture status
     */
    public LocalDateTime getFixtureDate() {
        if (!init) init();
        return fixtureDate;
    }

    /**
     * Returns the fixture status.
     * @return the fixture status
     */
    public FixtureStatus getStatus() {
        if (!init) init();
        return FixtureStatus.valueOf(_status.shortStatus());
    }

    /**
     * Returns the fixture elapsed time.
     * @return the fixture elapsed time
     */
    public Integer getElapsed() {
        if (!init) init();
        return _status.elapsed();
    }

    /**
     * Returns the fixture first period.
     * @return the fixture first period
     */
    public Integer getFirstPeriod() {
        if (!init) init();
        return _periods.firstPeriod();
    }

    /**
     * Returns the fixture second period.
     * @return the fixture second period
     */
    public Integer getSecondPeriod() {
        if (!init) init();
        return _periods.secondPeriod();
    }

    @Override
    public String toString() {
        return "FixtureDetails{" +
                "id=" + id +
                ", fixtureDate=" + getFixtureDate() +
                ", status=" + getStatus() +
                ", elapsed=" + getElapsed() +
                ", firstPeriod=" + getFirstPeriod() +
                ", secondPeriod=" + getSecondPeriod() +
                '}';
    }

}
