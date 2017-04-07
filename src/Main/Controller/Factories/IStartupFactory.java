package Main.Controller.Factories;

import Main.Model.Logic.IGameNightValues;
import Main.Model.Storage.ICollectionBuilder;

/**
 * Created by Peter on 05/10/2016.
 */
public interface IStartupFactory {

  ICollectionBuilder getCollectionBuilder();

  IGameNightValues getGameNightValues();
}
