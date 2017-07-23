package com.eisgroup.cb.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Address")
public class Address extends BaseObject {

    @JoinColumn(name = "country")
    @OneToOne(optional = false)
    private Country country;

    @NotNull
    @Size(min = 5, max = 5)
    @Column
    private String zip;

    @NotNull
    @Size(min = 1, max = 255)
    @Column
    private String state;

    @NotNull
    @Size(min = 1, max = 255)
    @Column
    private String city;

    @NotNull
    @Size(min = 1, max = 255)
    @Column
    private String address;


    public Address() {
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + getId() +
                ", country=" + country +
                ", zip='" + zip + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Address)) return false;

        Address address1 = (Address) o;

        if (country != null ? !country.equals(address1.getCountry()) : address1.getCountry() != null) return false;
        if (zip != null ? !zip.equals(address1.getZip()) : address1.getZip() != null) return false;
        if (state != null ? !state.equals(address1.getState()) : address1.getState() != null) return false;
        if (city != null ? !city.equals(address1.getCity()) : address1.getCity() != null) return false;
        return address != null ? address.equals(address1.getAddress()) : address1.getAddress() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}