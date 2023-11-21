package com.group.libraryapp.dto.user.request;

public class UserCreateRequest {

  private String name;
  private Integer age;

  public  UserCreateRequest(String name, Integer age) {
    this.age = age;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Integer getAge() {
    return age;
  }

}
