package main.Controller.Factories;

import main.Model.Logic.IGameNightValues;
import main.Model.Storage.ICollectionBuilder;

/**
 * Created by Peter on 05/10/2016.
 */
public interface IStartupFactory {

  ICollectionBuilder getCollectionBuilder();

  IGameNightValues getGameNightValues();
}
