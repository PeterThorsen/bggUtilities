package Main.Sorting;

import Main.Models.Structure.Reason;

import java.util.ArrayList;

/**
 * Created by Peter on 18/12/2016.
 */
public class InsertionSortReasons {

  public static Reason[] sort(ArrayList<Reason> arrList) {
    Reason[] arr = new Reason[arrList.size()];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = arrList.get(i);
    }

    int j;
    Reason key;
    int i;

    for (j = 1; j < arr.length; j++)
    {
      key = arr[j];
      for (i = j - 1; (i >= 0) && (arr[i].value < key.value); i--)
      {
        arr[i + 1] = arr[i];
      }
      arr[i + 1] = key;
    }
    return arr;
  }
}
