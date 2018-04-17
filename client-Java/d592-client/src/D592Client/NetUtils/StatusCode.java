package D592Client.NetUtils;

/**
 * Status codes to be used to indicate the state of a particular network operation
 */
public enum StatusCode {
    SUCCESS(0), ASK(1), ERROR(2);

    public int getCode() {
        return code;
    }

    private int code;

    StatusCode(int code) {
        this.code = code;
    }
}
