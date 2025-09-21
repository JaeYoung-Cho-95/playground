package young.playground.study.genericStudy.wildCard;


public class OriginBucket<T> {
  private T[] bucket;

  public OriginBucket(T[] bucket) {
    this.bucket = bucket;
  }

  public T getIndex(int index) {
    return bucket[index];
  }
}