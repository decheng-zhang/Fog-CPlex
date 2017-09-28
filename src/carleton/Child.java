package carleton;

import java.io.IOException;

import carleton.Tes;

public class Child extends Tes {
    //int a;
    Child() {
        System.out.println("in child");
        a = 11;
    }
   int method(){
	   a =17;
		return a;
   }


 public static void main(String args[]) throws IOException {
        Child p1 = new Child();
        Tes p2 = p1;
       // p1.method();
        System.out.println(p1.method());
        System.out.println(p2.method());
        
    }
}