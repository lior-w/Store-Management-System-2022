package ServiceLayer;

public class Response {

    private String error;

    public Response() {}

    public Response(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public boolean isError() {
        return error != null;
    }

}
