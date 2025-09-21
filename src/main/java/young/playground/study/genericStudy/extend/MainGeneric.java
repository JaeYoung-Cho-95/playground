package young.playground.study.genericStudy.extend;


public class MainGeneric {
  public static void main(String[] args){
    Cat smallCat = new Cat();
    Cat bigCat = new Cat();

    Cat[] catList = {
        smallCat,
        bigCat
    };

    OriginBucket<Cat> catOriginBucket = new OriginBucket<>(catList);
    new OriginBucket<>(catList);
  }
}
