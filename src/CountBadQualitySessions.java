import java.util.List;
import java.util.function.Function;

public class CountBadQualitySessions implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long bad = sessions.stream()
                .filter(s -> s.getQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult("Сессий с плохим качеством сна", bad);
    }
}