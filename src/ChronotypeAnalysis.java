import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeAnalysis implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream()
                .filter(ChronotypeAnalysis::isNightSession)
                .map(ChronotypeAnalysis::classify)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long owl = counts.getOrDefault(Chronotype.OWL, 0L);
        long lark = counts.getOrDefault(Chronotype.LARK, 0L);
        long dove = counts.getOrDefault(Chronotype.DOVE, 0L);

        Chronotype result = Chronotype.DOVE;
        if (owl > lark && owl > dove) {
            result = Chronotype.OWL;
        } else if (lark > owl && lark > dove) {
            result = Chronotype.LARK;
        }

        return new SleepAnalysisResult("Тип пользователя", result);
    }

    private static boolean isNightSession(SleepingSession s) {
        return s.getStart().getHour() < 6 || !s.getStart().toLocalDate().equals(s.getEnd().toLocalDate());
    }

    private static Chronotype classify(SleepingSession s) {
        LocalTime fallAsleep = s.getStart().toLocalTime();
        LocalTime wakeUp = s.getEnd().toLocalTime();

        if (fallAsleep.isAfter(LocalTime.of(23, 0)) && wakeUp.isAfter(LocalTime.of(9, 0))) {
            return Chronotype.OWL;
        }
        if (fallAsleep.isBefore(LocalTime.of(22, 0)) && wakeUp.isBefore(LocalTime.of(7, 0))) {
            return Chronotype.LARK;
        }
        return Chronotype.DOVE;
    }
}