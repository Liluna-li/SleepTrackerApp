import java.util.List;
import java.util.stream.Collectors;

public class TestData {
    public static List<SleepingSession> sessions(List<String> lines) {
        return lines.stream().map(SleepLogReader::parse).collect(Collectors.toList());
    }
}