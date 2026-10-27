import java.util.List;
import java.util.function.Function;

public class AverageSleepDuration implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        double average = sessions.stream()
                .mapToLong(SleepingSession::durationMinutes)
                .average()
                .orElse(0);
        return new SleepAnalysisResult("Средняя продолжительность сессии (минут)", average);
    }
}