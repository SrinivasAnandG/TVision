package com.srinivasanand.tvision;

/**
 * Created by srinivasanand on 23/09/17.
 */

public class ContactsListValues {
    String name,number;
    public ContactsListValues(String name, String number)
    {
        this.name=name;
        this.number=number;
    }
    public ContactsListValues()
    {

    }
    public String getName()
    {
        return name;
    }
    public String getNumber()
    {
        return number;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public void setNumber(String number)
    {
        this.number=number;
    }


}
