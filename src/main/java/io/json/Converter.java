package io.json;

import com.google.gson.*;
import com.google.gson.graph.*;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import io.arguments.Difficulty;
import simulation.computation.*;
import simulation.environment.Epochs;
import simulation.environment.VillageMap;
import simulation.player.PlayerState;
import simulation.player.PlayerStorage;
import simulation.strategy.AggressiveStrategy;
import simulation.strategy.BalancedStrategy;
import simulation.strategy.ConservativeStrategy;
import simulation.strategy.StrategyType;

/**
 * JSON Epochs class Serializer/Deserializer.
 */
public class Converter {
  private final Gson gson;

  /**
   * Set up Gson Object to interpret Epochs class with Traverse and StrategyType
   * inheritance and recursive dependencies.
   */
  public Converter() {
    RuntimeTypeAdapterFactory<TraverseBase> traverseTypeAdapter =
      RuntimeTypeAdapterFactory.of(TraverseBase.class)
        .registerSubtype(TraversePrices.class)
        .registerSubtype(TraverseDistance.class);

    RuntimeTypeAdapterFactory<StrategyType> strategyTypeAdapter =
      RuntimeTypeAdapterFactory.of(StrategyType.class)
        .registerSubtype(AggressiveStrategy.class)
        .registerSubtype(BalancedStrategy.class)
        .registerSubtype(ConservativeStrategy.class);

    GsonBuilder builder = new GsonBuilder();

    builder.setPrettyPrinting()
      .registerTypeAdapterFactory(strategyTypeAdapter)
      .registerTypeAdapterFactory(traverseTypeAdapter);

    new GraphAdapterBuilder()
      .addType(Epochs.class)
      .addType(BuyingAlgorithm.class)
      .addType(SellingAlgorithm.class)
      .addType(PlayerStorage.class)
      .addType(PlayerState.class)
      .addType(VillageMap.class)
      .addType(Difficulty.class)
      .registerOn(builder);

    this.gson = builder.create();
  }

  /**
   * Convert Epochs to JSON
   * @param game Epochs object.
   * @return JSON of Epochs.
   */
  public String toJSON(Epochs game) {
    return gson.toJson(game);
  }

  /**
   * Convert JSON of Epochs to Epochs Object.
   * @param game JSON of Epochs.
   * @return Epochs object.
   * @throws JsonSyntaxException bad json.
   */
  public Epochs fromJSON(String game) {
    return gson.fromJson(game, Epochs.class);
  }
}
