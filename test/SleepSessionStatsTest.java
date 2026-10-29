import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepSessionStatsTest {

    private final CountSleepSessions countSessions = new CountSleepSessions();
    private final MinSleepDuration minDuration = new MinSleepDuration();
    private final MaxSleepDuration maxDuration = new MaxSleepDuration();
    private final AverageSleepDuration averageDuration = new AverageSleepDuration();
    private final CountBadQualitySessions badQuality = new CountBadQualitySessions();

    @Test
    void countSessions_emptyList_returnsZero() {
        List<SleepingSession> sessions = TestData.sessions(List.of());
        assertEquals(0L, countSessions.apply(sessions).getValue());
    }

    @Test
    void countSessions_threeSessions_returnsThree() {
        List<SleepingSession> sessions = TestData.sessions(List.of(
                "01.10.25 22:15;02.10.25 08:00;GOOD",
                "02.10.25 23:00;03.10.25 08:00;NORMAL",
                "03.10.25 14:30;03.10.25 15:20;NORMAL"
        ));
        assertEquals(3L, countSessions.apply(sessions).getValue());
    }

    @Test
    void minDuration_multipleSessions_returnsShortest() {
        List<SleepingSession> sessions = TestData.sessions(List.of(
                "01.10.25 22:00;02.10.25 04:00;GOOD",
                "01.10.25 13:00;01.10.25 23:00;NORMAL",
                "01.10.25 09:00;01.10.25 15:00;NORMAL"
        ));
        assertEquals(360L, minDuration.apply(sessions).getValue());
    }

    @Test
    void minDuration_singleSession_returnsItsDuration() {
        List<SleepingSession> sessions = TestData.sessions(List.of("01.10.25 10:00;01.10.25 11:30;GOOD"));
        assertEquals(90L, minDuration.apply(sessions).getValue());
    }

    @Test
    void maxDuration_multipleSessions_returnsLongest() {
        List<SleepingSession> sessions = TestData.sessions(List.of(
                "01.10.25 22:00;02.10.25 04:00;GOOD",
                "01.10.25 13:00;01.10.25 23:00;NORMAL",
                "01.10.25 09:00;01.10.25 15:00;NORMAL"
        ));
        assertEquals(600L, maxDuration.apply(sessions).getValue());
    }

    @Test
    void maxDuration_singleSession_returnsItsDuration() {
        List<SleepingSession> sessions = TestData.sessions(List.of("01.10.25 10:00;01.10.25 11:30;GOOD"));
        assertEquals(90L, maxDuration.apply(sessions).getValue());
    }

    @Test
    void averageDuration_multipleSessions_returnsAverage() {
        List<SleepingSession> sessions = TestData.sessions(List.of(
                "01.10.25 22:00;02.10.25 04:00;GOOD",
                "01.10.25 13:00;01.10.25 23:00;NORMAL",
                "01.10.25 09:00;01.10.25 15:00;NORMAL"
        ));
        Object value = averageDuration.apply(sessions).getValue();
        assertEquals(440.0, (Double) value, 0.001);
    }

    @Test
    void averageDuration_emptyList_returnsZero() {
        List<SleepingSession> sessions = TestData.sessions(List.of());
        Object value = averageDuration.apply(sessions).getValue();
        assertEquals(0.0, (Double) value, 0.001);
    }

    @Test
    void badQuality_twoBadSessions_returnsTwo() {
        List<SleepingSession> sessions = TestData.sessions(List.of(
                "01.10.25 22:15;02.10.25 08:00;GOOD",
                "02.10.25 23:00;03.10.25 08:00;BAD",
                "03.10.25 14:30;03.10.25 15:20;NORMAL",
                "03.10.25 23:30;04.10.25 06:20;BAD"
        ));
        assertEquals(2L, badQuality.apply(sessions).getValue());
    }

    @Test
    void badQuality_noBadSessions_returnsZero() {
        List<SleepingSession> sessions = TestData.sessions(List.of(
                "01.10.25 22:15;02.10.25 08:00;GOOD",
                "02.10.25 23:00;03.10.25 08:00;NORMAL"
        ));
        assertEquals(0L, badQuality.apply(sessions).getValue());
    }
}