import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class CountSleeplessNights implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Бессонных ночей", 0L);
        }

        List<SleepingSession> sorted = sessions.stream()
                .sorted(Comparator.comparing(SleepingSession::getStart))
                .toList();

        LocalDateTime firstStart = sorted.getFirst().getStart();
        LocalDate firstNight = firstNightDate(firstStart);
        LocalDate lastNight = sorted.getLast().getEnd().toLocalDate();

        long totalNights = ChronoUnit.DAYS.between(firstNight, lastNight) + 1;
        if (totalNights < 0) {
            totalNights = 0;
        }

        long sleepless = LongStream.range(0, totalNights)
                .mapToObj(i -> firstNight.plusDays(i))
                .filter(nightDate -> hasNoNightlySleep(nightDate, sorted))
                .count();

        return new SleepAnalysisResult("Бессонных ночей", sleepless);
    }

    private static LocalDate firstNightDate(LocalDateTime firstStart) {
        LocalDate date = firstStart.toLocalDate();
        if (firstStart.toLocalTime().isAfter(LocalTime.NOON)) {
            return date.plusDays(1);
        }
        return date;
    }

    private static boolean hasNoNightlySleep(LocalDate nightDate, List<SleepingSession> sessions) {
        LocalDateTime windowStart = nightDate.atStartOfDay();
        LocalDateTime windowEnd = nightDate.atTime(LocalTime.of(6, 0));
        return sessions.stream().noneMatch(s -> overlaps(s, windowStart, windowEnd));
    }

    private static boolean overlaps(SleepingSession s, LocalDateTime windowStart, LocalDateTime windowEnd) {
        return s.getStart().isBefore(windowEnd) && s.getEnd().isAfter(windowStart);
    }
}