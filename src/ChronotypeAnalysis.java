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
        int fallAsleep = minutesFromNoon(s.getStart().toLocalTime());
        int wakeUp = minutesFromNoon(s.getEnd().toLocalTime());

        if (fallAsleep > minutesFromNoon(LocalTime.of(23, 0)) && wakeUp >= minutesFromNoon(LocalTime.of(9, 0))) {
            return Chronotype.OWL;
        }
        if (fallAsleep < minutesFromNoon(LocalTime.of(22, 0)) && wakeUp < minutesFromNoon(LocalTime.of(7, 0))) {
            return Chronotype.LARK;
        }
        return Chronotype.DOVE;
    }

    private static int minutesFromNoon(LocalTime t) {
        int minutesOfDay = t.getHour() * 60 + t.getMinute();
        int noonMinutes = 12 * 60;
        return minutesOfDay < noonMinutes ? minutesOfDay + 24 * 60 : minutesOfDay;
    }
}