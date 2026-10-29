import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ChronotypeTest {

    private final ChronotypeAnalysis chronotype = new ChronotypeAnalysis();

    @Test
    void lateFallAsleepAndLateWakeUp_isOwl() {
        List<SleepingSession> sessions = TestData.sessions(List.of("01.10.25 23:30;02.10.25 10:00;GOOD"));
        assertSame(Chronotype.OWL, chronotype.apply(sessions).getValue());
    }

    @Test
    void fallAsleepAfterMidnightAndWakeUpAtNine_isOwl() {
        List<SleepingSession> sessions = TestData.sessions(List.of("01.10.25 01:00;01.10.25 09:00;GOOD"));
        assertSame(Chronotype.OWL, chronotype.apply(sessions).getValue());
    }

    @Test
    void earlyFallAsleepAndEarlyWakeUp_isLark() {
        List<SleepingSession> sessions = TestData.sessions(List.of("01.10.25 21:30;02.10.25 06:00;GOOD"));
        assertSame(Chronotype.LARK, chronotype.apply(sessions).getValue());
    }

    @Test
    void averageFallAsleepAndWakeUp_isDove() {
        List<SleepingSession> sessions = TestData.sessions(List.of("01.10.25 22:30;02.10.25 08:00;GOOD"));
        assertSame(Chronotype.DOVE, chronotype.apply(sessions).getValue());
    }

    @Test
    void equalNumberOfOwlAndLarkNights_isDove() {
        List<SleepingSession> sessions = TestData.sessions(List.of(
                "01.10.25 23:30;02.10.25 10:00;GOOD",
                "03.10.25 21:30;04.10.25 06:00;GOOD"
        ));
        assertSame(Chronotype.DOVE, chronotype.apply(sessions).getValue());
    }

    @Test
    void mostFrequentType_wins() {
        List<SleepingSession> sessions = TestData.sessions(List.of(
                "01.10.25 23:30;02.10.25 10:00;GOOD",
                "03.10.25 23:30;04.10.25 10:00;GOOD",
                "05.10.25 22:30;06.10.25 08:00;GOOD"
        ));
        assertSame(Chronotype.OWL, chronotype.apply(sessions).getValue());
    }

    @Test
    void dayNapIsIgnored() {
        List<SleepingSession> sessions = TestData.sessions(List.of("01.10.25 14:30;01.10.25 15:20;NORMAL"));
        assertSame(Chronotype.DOVE, chronotype.apply(sessions).getValue());
    }

    @Test
    void emptyLog_returnsDove() {
        List<SleepingSession> sessions = TestData.sessions(List.of());
        assertSame(Chronotype.DOVE, chronotype.apply(sessions).getValue());
    }

    @Test
    void toString_returnsRussianName() {
        assertEquals("Сова", Chronotype.OWL.toString());
        assertEquals("Жаворонок", Chronotype.LARK.toString());
        assertEquals("Голубь", Chronotype.DOVE.toString());
    }
}