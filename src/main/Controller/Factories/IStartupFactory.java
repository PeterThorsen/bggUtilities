package Controller.Factories;

import Model.Logic.IGameNightValues;
import Model.Storage.ICollectionBuilder;

/**
 * Created by Peter on 05/10/2016.
 */
public interface IStartupFactory {

  ICollectionBuilder getCollectionBuilder();

  IGameNightValues getGameNightValues();
}
