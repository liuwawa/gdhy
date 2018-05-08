package io.renren.common.utils;

import java.util.Random;

/**
 * Created by hc on 2018/4/14.
 */
public class CalculationGold {

     Random rand = new Random();

     static int a = 500;
     static int  b = 3000;
     static int c = 8000;

    public  int calculation(Integer gold){

        if(gold <= a){

            //上限随机递减
            int one = (rand.nextInt((20 - (int)((float) gold/a*20))) + 30);
            System.out.print(one +"------1");
            return one;

        }else if(gold > a && gold <= b){

            int two = rand.nextInt((20 - (int)((float) (gold-a)/((b-a)*20)))) + 20;
            System.out.print(two +"------2");
            return  two;

        }
        else if(gold > b && gold <= c){


            int three = rand.nextInt((10 - (int)((float) (gold-b)/((c-b)*10)))) + 10;
            System.out.print(three +"------3");
            return  three;

        }else {
            int i = rand.nextInt(10) + 1;
            System.out.print(i +"------4");
            return i;
        }
    }
    static int gold = 0;
    public static void main(String[] args)  {

        CalculationGold c = new CalculationGold();

       for(int i= 0;i<2880;i++){
           try{
               Thread.sleep(1);
               gold = gold + c.calculation(gold);
               System.out.println("\t" +gold);
           }catch (Exception e){
               e.printStackTrace();

           }



       }
    }

}
