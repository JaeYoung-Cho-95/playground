package young.playground.genericStudy.casting;


public class MainGeneric {
  public static void main(String[] args){
    Cat smallCat = new Cat();
    Cat bigCat = new Cat();

    Cat[] catList = {
        smallCat,
        bigCat
    };
//    OriginBucket<Animal> catBucket = new OriginBucket<Cat>(catList);
//    OriginBucket<Object> objectBucket = new OriginBucket<Cat>(catList);
  }
}
