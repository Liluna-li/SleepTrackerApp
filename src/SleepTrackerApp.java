import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {
    private static final String SLEEP_LOG_PATH = "sleep_log.txt";

    private static final List<Function<List<SleepingSession>, SleepAnalysisResult>> ANALYSES = List.of(
            new CountSleepSessions(),
            new MinSleepDuration(),
            new MaxSleepDuration(),
            new AverageSleepDuration(),
            new CountBadQualitySessions(),
            new CountSleeplessNights(),
            new ChronotypeAnalysis()
    );

    public static void main(String[] args) {
        List<SleepingSession> sessions;
        try {
            sessions = SleepLogReader.read(SLEEP_LOG_PATH);
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл: " + SLEEP_LOG_PATH);
            return;
        }

        ANALYSES.stream()
                .map(f -> f.apply(sessions))
                .forEach(result -> System.out.println(result.getDescription() + ": " + result.getValue()));
    }
}