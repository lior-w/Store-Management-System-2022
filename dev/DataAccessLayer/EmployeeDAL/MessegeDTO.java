package DataAccessLayer.EmployeeDAL;

public class MessegeDTO {
    public String id;
    public String messege;


    public MessegeDTO(String id, String messege) {
        this.id = id;
        this.messege = messege;

    }
    public String fieldsToString() {
        return String.format("(\"%s\",\"%s\")", this.id, this.messege);
    }
}
