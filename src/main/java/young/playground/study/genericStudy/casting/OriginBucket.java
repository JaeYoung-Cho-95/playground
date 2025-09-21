package young.playground.study.genericStudy.casting;


public class OriginBucket<T> {
  private T[] bucket;

  public OriginBucket(T[] bucket) {
    this.bucket = bucket;
  }

  public T getIndex(int index) {
    return bucket[index];
  }
}