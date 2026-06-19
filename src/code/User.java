package code;

public class User {
    String firstname;
    String lastname;
    int cnic;
    String password;
    int age;
    String province;
    int userId;
    boolean hasVoted;

    public User(int cnic, String password, int age, String city, int userId, String firstname, String lastname) {
        this.cnic = cnic;
        this.password = password;
        this.age = age;
        this.province = city;
        this.userId = userId;
        this.hasVoted = false;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
