import java.util.Map;

public class TestStruct {
    public String name = "Hi";
    public int value = 7;
    public double otherValue = 42.9;
    public TestStruct child = null;
    private String troll = "nya-nya!";

    public Map<String, String> test1 = Map.of(
            "LOTR", "Sauron",
            "Star Wars", "Darth Vader",
            "It", "Pennywise"
            );
}
