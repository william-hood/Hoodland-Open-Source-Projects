package hoodland.opensource.memoir.java;

public interface HttpFieldProcessingFunction {
    /**
     * Implement this function to handle fields in objects such as HTTP messages. For example
     * you might want to base64-decode certain fields or JSON pretty-print the message body.
     * @param fieldName
     * @param fieldValue
     * @return
     */
    String processField(String fieldName, String fieldValue);
}
