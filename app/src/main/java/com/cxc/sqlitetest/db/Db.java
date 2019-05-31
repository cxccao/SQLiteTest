package com.cxc.sqlitetest.db;

public class Db {
    int Id;
    String Name;
    int Price;
    String Country;

    Db() {

    }

    Db(int id, String name, int price, String country) {
        setId(id);
        setName(name);
        setPrice(price);
        setCountry(country);
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public int getPrice() {
        return Price;
    }

    void setPrice(int price) {
        this.Price = price;
    }

    public String getCountry() {
        return Country;
    }

    void setCountry(String country) {
        this.Country = country;
    }
}
