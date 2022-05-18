package com.example.fayed_final_project.Student;

public class Student {

    private String id;
    private String name;
    private String surname;
    private String fathers_name;
    private String national_id;
    private String date_of_birth;
    private String gender;

    public Student(){

    }
    
    public Student(String id, String name, String fathers_name, String surname, String national_id, String gender, String date_of_birth) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.fathers_name = fathers_name;
        this.national_id = national_id;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFathersName() {
        return fathers_name;
    }

    public void setFathers_name(String fathers_name) {
        this.fathers_name = fathers_name;
    }

    public String getNationalId() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getDateOfBirth() {
        return date_of_birth;
    }

    public void setDateOfBirth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    // toString
    public String toString(){
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", fathers_name='" + fathers_name + '\'' +
                ", surname='" + surname + '\'' +
                ", national_id='" + national_id + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", gender='" + gender + '\'' +
                "}";
    }
}
