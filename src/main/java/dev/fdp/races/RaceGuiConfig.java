package dev.fdp.races;

public class RaceGuiConfig {
  private String name = "";

  // Геттеры и сеттеры
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "RaceGuiConfig{" +
        "name=" + name +
        '}';
  }
}
