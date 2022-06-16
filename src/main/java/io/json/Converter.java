package io.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import simulation.environment.Epochs;

// todo
public class Converter {
  private final Gson gson;

  public Converter() {
    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();
    this.gson = builder.create();
  }

  public String toJSON(Epochs game) {
    return gson.toJson(game);
  }

  public Epochs fromJSON(String game) {
    return gson.fromJson(game, Epochs.class);
  }
}
