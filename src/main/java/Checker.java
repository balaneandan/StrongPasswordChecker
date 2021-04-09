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
        int start = 0;
        int i=0;
        while( i < password.length())
        {

            start = i;
            while (i < password.length() && password.charAt(i) == password.charAt(start))
                i++;

            if (i-start >=3)                        // verificam lungimea subsecventelor de char identice
            {
                nrReplaces += (i-start)/3;          // este nevoie de length/3 actiuni

                if(((i-start)-2) % 3  == 1)         // numaram subsecventele pt a calcula mai tarziu nr de inlocuiri pe care le putem economisi facand 1*one delete uri
                    one++;
                if(((i-start)-2) % 3  == 2)         // numaram subsecventele pt a calcula mai tarziu nr de inlocuiri pe care le putem economisi facand 2*two delete uri
                    two++;
                                                    // cazul de %3 == 0 va rezulta din nrReplaces-one-two

            }
        }
        requiredReplaceL1 = one;
        requiredReplaceL2 = two;

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

            int nrReplaces = duplicate(password);
            int nrDeletes = password.length()-20;
            int tempDelete=nrDeletes;
            int nrReplacesEcon=0;


            if(tempDelete > requiredReplaceL1)      // daca nr delete > nr secvente care necesita 1 delete pt a economisi un replace, cazul (L-2) % 3 = 1
            {
                nrReplacesEcon+=requiredReplaceL1;
                tempDelete-=requiredReplaceL1;
            }
            else
            {
                nrReplacesEcon+=tempDelete;                                     // economisim doar atatea replace uri cate delete uri trebuie sa facem, (le folosim pe toate)

                return nrDeletes+Math.max(nrReplaces-nrReplacesEcon,needsLUD);  // nu ne-au ramas delete uri, deci nu mai putem economisi alte replace uri
            }
            if(tempDelete > 0)                                                  // daca mai avem delete uri ramase, tratam cazul (L-2) % 3 = 2
            {
                if(tempDelete < 2*requiredReplaceL2)        // daca nr delete < nr secvente care necesita 2 delete pt a economisi un replace
                    nrReplacesEcon += tempDelete/2;         // salvam un nr de replace uri egal cu jumatate din numarul de delete
                else
                    nrReplacesEcon += requiredReplaceL2;    // actualizam numarul de replace uri salvate

                tempDelete -= 2*requiredReplaceL2;          // actualizam nr delete ramase
            }
            if(tempDelete > 0)                              // cazul  (L-2) % 3 = 0, daca ne-au ramas delete uri nefolosite, salvam atatea replace uri cat nr delete / 3 mai avem
                nrReplacesEcon += tempDelete / 3;


            MINIM += nrDeletes+Math.max(nrReplaces-nrReplacesEcon, needsLUD);
        }

        return MINIM;

    }

    public static void main(String[] args) {

        //"bbaaaaaaaaaaaaaaacccccc"
        //"aaaaAAAAAA000000123456"
        String password = "FFFFFFFFFFFFFFF11111111111111111111AAA";
        System.out.println(strongPasswordChecker(password));

    }
}
