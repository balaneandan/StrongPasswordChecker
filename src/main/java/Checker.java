/*
*       Strong password checker
*
*       Constrangeri:
*       1. It has at least 6 characters and at most 20 characters.
        2. It must contain at least one lowercase letter, at least one uppercase letter, and at least one digit.
        3. It must NOT contain three repeating characters in a row ("...aaa..." is weak, but "...aa...a..." is strong, assuming other conditions are met).
*
*       Operatii posibile : insert, delete si replace
*       Din cateva exemple putem trage concluzia ca REPLACE este cea mai puternica operatie pentru a trata constrangerea 3.
*       Ordine eficienta : replace, insert, delete.
*
*       1. luand in considerare lungimea, o sa avem 3 cazuri posibile
*
*               1. password.length() < 6 -- trebuie sa crestem lungimea la 6 folosind insert si rezolvam alte constrangeri cu replace
*               2. 6 < password.length() < 20 -- nu avem nevoie de delete sau insert, ci doar de replace
*               3. password.length() > 20 -- trebuie sa scadem lungimea la 20 folosind delete si rezolvam alte constrangeri cu replace
*
*       2. conditia 2 se poate satisface folosind insert sau edit, trebuie sa tinem cont si de constrangerea 3 cand facem asta
*
*       3. conditia 3 se poate rezolva in mai multe moduri, dar tinand cont si de conditia 1 si de nr de miscari necesare, cel mai potrivit ar fi sa facem replace
*
*        edit - este nevoie de length/3 actiuni     (length este lungimea subsirului care contine caractere identice)
*        insert - este nevoie de (length-1)/2
*        delete -este nevoie de length - 2
*
*
* */

public class Checker {


    public static int requiredReplaceL0=0;
    public static int requiredReplaceL1=0;
    public static int requiredReplaceL2=0;

    public static boolean hasLowerCase(String password)
    {
        return password.chars().anyMatch(c-> Character.isLetter(c) && Character.isLowerCase(c));
    }
    public static boolean hasUpperCase(String password)
    {
        return password.chars().anyMatch(c-> Character.isLetter(c) && Character.isUpperCase(c));
    }
    public static boolean hasDigit(String password)
    {
        return password.chars().anyMatch(Character::isDigit);
    }


    public static int duplicate(String password)
    {
        int nrReplaces = 0;
        int one =0;
        int two=0;
        int three =0;
        int start = 0;
        int i=0;
        while( i < password.length())
        {

            start = i;
            while (i < password.length() && password.charAt(i) == password.charAt(start))
                i++;

            if (i-start >=3)
            {
                nrReplaces += (i-start)/3;          // este nevoie de length/3 actiuni

                if(((i-start)-2) % 3  == 1)
                    one++;
                if(((i-start)-2) % 3  == 2)
                    two++;
                if(((i-start)-2) % 3  == 2)
                    three++;

            }
        }
        requiredReplaceL1 = one;
        requiredReplaceL2 = two;
        requiredReplaceL0 = three;
        return  nrReplaces;
    }


    public static int strongPasswordChecker(String password)
    {
        int MINIM=0;
        int needsLUD =0;  // needs lower, upper or digit
        if(!hasLowerCase(password))
            needsLUD++;
        if(!hasUpperCase(password))
            needsLUD++;
        if(!hasDigit(password))
            needsLUD++;

        if(password.length() < 6 )                                                  // case 1
        {
            MINIM += needsLUD + Math.max(0, 6 - (password.length() + needsLUD));
        }

        if(password.length() >=6 && password.length() <=20)                          // case 2
        {
            // aici nu mai exista constrangerea de lungime, iar celelalte 2 constrangeri se pot rezolva prin replace  = > max intre nr miscari ale cazurilor
            int nrReplaces = 0;
            nrReplaces = duplicate(password);
            MINIM+=Math.max(needsLUD,nrReplaces);

        }
                                                                                    // case 3
        if(password.length() > 20)         // putem satisface constrangerea de length folosind operatii de delete, iar apoi vom folosi replace pentru a satisface celelalte 2 constrangeri
        {

            int nrReplaces = 0;
            nrReplaces = duplicate(password);
            int nrDeletes = password.length()-20;
            int tempDelete=nrDeletes;
            int sum=0;



            if(tempDelete > requiredReplaceL1)
            {
                sum+=requiredReplaceL1;
                tempDelete-=requiredReplaceL1;
            }
            else
            {
                sum+=tempDelete;  // se poate face return
                tempDelete = 0;
            }
            if(tempDelete > requiredReplaceL2*2)
            {
                sum+=requiredReplaceL2*2;
                tempDelete-=requiredReplaceL2*2;
            }
            else
            {
                sum+=Math.max(tempDelete,0);
                tempDelete=0;
            }
            if(tempDelete > requiredReplaceL0*3)
            {
                sum+=requiredReplaceL0*3;
            }
            else
            {
                sum+=Math.max(tempDelete,0);
            }

            MINIM+=nrDeletes+Math.max(nrReplaces-sum,needsLUD);

        }

        return MINIM;

    }

    public static void main(String[] args) {

        //"bbaaaaaaaaaaaaaaacccccc"
        //"aaaaAAAAAA000000123456"
        String password = "bbaaaaaaaaaaaaaaacccccc";
        System.out.println(strongPasswordChecker(password));

    }
}
