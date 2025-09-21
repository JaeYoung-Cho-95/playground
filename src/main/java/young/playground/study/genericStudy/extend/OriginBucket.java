package young.playground.study.genericStudy.extend;


public class OriginBucket<T extends Animal> {
  private T[] bucket;

  public OriginBucket(T[] bucket) {
    this.bucket = bucket;
  }

  public T getIndex(int index) {
    return bucket[index];
  }
}