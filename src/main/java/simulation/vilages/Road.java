package simulation.vilages;

import io.arguments.Difficulty;
import simulation.environment.Position;
import simulation.strategy.StrategyType;

public class Road {
  private final Position start;
  private final Position finish;
  private final StrategyType strategyType;

  public Road(Position start, Position finish, StrategyType strategyType) {
    this.start = start;
    this.finish = finish;
    this.strategyType = strategyType;
  }

  public float calculateDistance() {
    return (float)Math.sqrt(start.getX() * finish.getX() + start.getY() * finish.getY());
  }

  //todo ...
}
