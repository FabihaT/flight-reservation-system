package src.account;

public class Address {
    // instance variables
    private int houseNum; // house number
    private String street; // street name
    private String city; // city name
    private String postalCode; // postal code
    private String province; // province name

    // constructor for Address
    Address(int houseNum, String street, String city, String postalCode, String province) {
        this.houseNum = houseNum;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.province = province;
    }

    // getters
    public int getHouseNum() {
        return houseNum;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode(){
        return postalCode;
    }

    public String getProvince(){
        return province;
    }

    // setters
    public void setHouseNum(int houseNum) {
        this.houseNum = houseNum;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }

    public void setProvince(String province){
        this.province = province;
    }
}
