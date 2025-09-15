package young.playground.genericStudy.GoodWileCard;

import java.util.ArrayList;
import java.util.List;

public class Bucket<T, U, A> {
  List<T> TBucket = new ArrayList<>();
  List<U> UBucket = new ArrayList<>();
  List<A> ABucket = new ArrayList<>();

  public void addT(T value) {
    TBucket.add(value);
  }

  public void addU(U value) {
    UBucket.add(value);
  }

  public void addA(A value) {
    ABucket.add(value);
  }

  public String toString() {
    return "TBucket: "
        + TBucket.toString() + "\n"
        + "UBucket: "
        + UBucket.toString() + "\n"
        + "ABucket: "
        + ABucket.toString();
  }
}
