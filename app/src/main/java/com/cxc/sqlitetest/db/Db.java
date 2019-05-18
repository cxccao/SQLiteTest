package com.cxc.sqlitetest.db;

public class Db {
    public int Id;
    public String Name;
    public int Price;
    public String Country;

    public Db(){

    }

    public Db(int id, String name, int price, String country) {
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

    public void setPrice(int price) {
        this.Price = price;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        this.Country = country;
    }
}
