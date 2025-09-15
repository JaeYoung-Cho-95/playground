package young.playground.genericStudy.GoodWildCard;


public class MainGeneric {
  public static void main(String[] args){
    Cat smallCat = new Cat();
    Cat bigCat = new Cat();

    Cat[] catList = {
        smallCat,
        bigCat
    };

    OriginBucket<? extends Animal> catBucket = new OriginBucket<Cat>(catList);

    // Required type: OriginBucket<? super Animal> / Provided: OriginBucket<Cat>
    // OriginBucket<? super Animal> objectBucket = new OriginBucket<Cat>(catList);

    // Required type: OriginBucket<? extends Cat> / Provided: OriginBucket<Animal>
    // OriginBucket<? extends Cat> catBucket2 = new OriginBucket<Animal>(catList);

    OriginBucket<? super Cat> objectBucket2 = new OriginBucket<Cat>(catList);
  }
}
