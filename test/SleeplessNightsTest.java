import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleeplessNightsTest {

    private final CountSleeplessNights sleeplessNights = new CountSleeplessNights();

    @Test
    void exampleFromSpec_noSleeplessNights() {
        List<SleepingSession> sessions = TestData.sessions(List.of(
                "01.10.25 22:15;02.10.25 08:00;GOOD",
                "02.10.25 23:00;03.10.25 08:00;NORMAL",
                "03.10.25 14:30;03.10.25 15:20;NORMAL",
                "03.10.25 23:30;04.10.25 06:20;BAD"
        ));
        assertEquals(0L, sleeplessNights.apply(sessions).getValue());
    }

    @Test
    void sleepOnlyFromSevenToEleven_countsAsSleeplessNight() {
        List<SleepingSession> sessions = TestData.sessions(List.of("03.10.25 07:00;03.10.25 11:00;BAD"));
        assertEquals(1L, sleeplessNights.apply(sessions).getValue());
    }

    @Test
    void sleepFromTwentyThreeToThree_isNotSleeplessNight() {
        List<SleepingSession> sessions = TestData.sessions(List.of("02.10.25 23:00;03.10.25 03:00;NORMAL"));
        assertEquals(0L, sleeplessNights.apply(sessions).getValue());
    }

    @Test
    void sleepFromTwoToSeven_isNotSleeplessNight() {
        List<SleepingSession> sessions = TestData.sessions(List.of("03.10.25 02:00;03.10.25 07:00;NORMAL"));
        assertEquals(0L, sleeplessNights.apply(sessions).getValue());
    }

    @Test
    void intervalAcrossMonths_isCountedCorrectly() {
        List<SleepingSession> sessions = TestData.sessions(List.of(
                "30.10.25 22:15;31.10.25 08:00;GOOD",
                "31.10.25 23:00;01.11.25 08:00;NORMAL"
        ));
        assertEquals(0L, sleeplessNights.apply(sessions).getValue());
    }

    @Test
    void missedNightInTheMiddle_countsAsSleeplessNight() {
        List<SleepingSession> sessions = TestData.sessions(List.of(
                "01.10.25 22:00;02.10.25 06:00;NORMAL",
                "03.10.25 22:00;04.10.25 06:00;NORMAL"
        ));
        assertEquals(1L, sleeplessNights.apply(sessions).getValue());
    }

    @Test
    void firstSessionAfterNoon_nextNightIsSleeplessIfNotSlept() {
        List<SleepingSession> sessions = TestData.sessions(List.of(
                "01.10.25 14:00;01.10.25 15:00;NORMAL",
                "02.10.25 22:00;03.10.25 06:00;NORMAL"
        ));
        assertEquals(1L, sleeplessNights.apply(sessions).getValue());
    }
}