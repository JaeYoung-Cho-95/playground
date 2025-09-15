package young.playground.genericStudy.GoodWileCard;


import java.util.Collection;

public class OriginBucket<T> {
  Object[] myList = new Object[5];
  int index = 0;

  public OriginBucket(Collection<? extends T> produce) {
    for (T elem : produce) {
      myList[index++] = elem;
    }
  }

  public void copy(Collection<? super T> consume) {
    for (Object elem: myList) {
      consume.add((T) elem);
    }
  }
}