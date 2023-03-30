package ServiceLayer;

public class ResponseT<T> extends Response {

    private T data;

    private ResponseT(T data, String msg) {
        super(msg);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public static <T> ResponseT<T> fromValue(T data) {
        return new ResponseT<>(data, null);
    }

    public static <T> ResponseT<T> fromValue(T data, String msg) {
        return new ResponseT<>(data, msg);
    }

    public static <T> ResponseT<T> fromError(String msg) {
        return new ResponseT<>(null, msg);
    }

}
