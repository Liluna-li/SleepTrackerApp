import java.util.List;
import java.util.function.Function;

public class CountSleepSessions implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult("Всего сессий сна", sessions.stream().count());
    }
}