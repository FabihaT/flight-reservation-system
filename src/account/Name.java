package src.account;
public class Name {
    private String firstName; // first name
    private String lastName;  // last name

    // constructor for Name
    Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
