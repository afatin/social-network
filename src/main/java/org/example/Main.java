package org.example;

public class Main {
    public static void main(String[] args) {

        DbFunctions db=new DbFunctions() ;
        db.connect_to_db( "mydatabase", "myuser",
        "mypassword");
    }
}