package com.leyoumall.test;

import com.leyoumall.LyItem;
import com.leyoumall.item.pojo.SpecGroup;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName Test
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-08
 * @Version V1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyItem.class)
public class Test {

    @org.junit.Test
    public void test1(){
        SpecGroup specParam = new SpecGroup(1L,1L,"434");
        SpecGroup specParam1 = new SpecGroup(1L,1L,"434");
        System.out.println(specParam==specParam1);
        System.out.println(specParam.equals(specParam1));
//        false
//        true
    }
    @org.junit.Test
    public void test99(){
      for (int i =1;i<=9;i++) {
          for (int j =1;j<=i;j++) {
              System.out.print(i+"x"+j+"="+i*j+" ");
          }
          System.out.println();
      }
    }
}
