import java.util.Scanner;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int n) {
        // your solution here
        StringBuilder romanNumeral=new StringBuilder();
        while(n>=1000)
        {
            romanNumeral.append("M");
            n-=1000;
        }
        
        while(n>=100)
        {
            if(n>=900)
            {
                romanNumeral.append("CM");
                n-=900;
                break;
            }
            
            else if(n>=500)
            {
                romanNumeral.append("D");
                n-=500;
            }
            
            else if(n>=400)
            {
                romanNumeral.append("CD");
                n-=400;
            }
            
            else
            {
                romanNumeral.append("C");
                n-=100;
            }
        }
        
        while(n>=10)
        {
            if(n>=90)
            {
                romanNumeral.append("XC");
                n-=90;
            }
            
            else if(n>=50)
            {
                romanNumeral.append("L");
                n-=50;
            }
            
            else if(n>=40)
            {
                romanNumeral.append("XL");
                n-=40;
            }
            
            else
            {
                romanNumeral.append("X");
                n-=10;
            }
        }
        
        while(n>0)
        {
            if(n==9)
            {
                romanNumeral.append("IX");
                n-=9;
            }
            
            else if(n>=5)
            {
                romanNumeral.append("V");
                n-=5;
            }
            
            else if(n==4)
            {
                romanNumeral.append("IV");
                n-=4;
            }
            
            else
            {
                romanNumeral.append("I");
                n--;
            }
        }
        return romanNumeral.toString();
    }

}
