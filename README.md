
       Strong password checker

       Constrangeri:
       1. It has at least 6 characters and at most 20 characters.
       2. It must contain at least one lowercase letter, at least one uppercase letter, and at least one digit.
       3. It must NOT contain three repeating characters in a row ("...aaa..." is weak, but "...aa...a..." is strong, assuming other conditions are met).

       Operatii posibile : insert, delete si replace
       Din cateva exemple putem trage concluzia ca REPLACE este cea mai puternica operatie pentru a trata constrangerea 3.
       Ordine eficienta : replace, insert, delete.

       1. luand in considerare lungimea, o sa avem 3 cazuri posibile

               1. password.length() < 6 -- trebuie sa crestem lungimea la 6 folosind insert si rezolvam alte constrangeri cu replace
               2. 6 < password.length() < 20 -- nu avem nevoie de delete sau insert, ci doar de replace
               3. password.length() > 20 -- trebuie sa scadem lungimea la 20 folosind delete si rezolvam alte constrangeri cu replace

       2. conditia 2 se poate satisface folosind insert sau Replace, trebuie sa tinem cont si de constrangerea 3 cand facem asta

       3. conditia 3 se poate rezolva in mai multe moduri, dar tinand cont si de conditia 1 si de nr de miscari necesare, cel mai potrivit ar fi sa facem replace

        edit - este nevoie de length/3 actiuni     (length este lungimea subsirului care contine caractere identice)
        insert - este nevoie de (length-1)/2
        delete -este nevoie de length - 2


    Pentru inceput am calculat numarul de actiuni necesare pentru a indeplini conditia 2 (needsLUD), deoarece acest lucru se poate face 
    foarte usor. Am ales sa verific separat Lower case, Upper case si digit in metodele (hasLower, hasUpper, hasDigit)
    pentru a face codul mai usor de inteles, desi se puteau verifica intr-o singura parcurgere a stringului alaturi de 
    constrangerea 3. 
    
    Caz 1 

        Atunci cand parola are lungimea < 6, numarul minim de actiuni necesar trebuie sa indeplineasca conditia 1 si 2 
        Dupa ce adunam actiunile necesare pentru conditia 2 , daca lungimea parolei este inca < 6, vom adauga caractere pana cand
        lungimea va fi 6.

    Caz 2 

        Atunci cand lungimea este cuprinsa in intervalul [6,20], parola trebuie sa respecte conditiile 2 si 3 in acelasi timp,
        contitia 1 fiind indeplinita.
        In metoda duplicate parcurgem String ul si calculam numarul necesar de replace uri pentru fiecare substring de caractere identice intalnit
        si il adaugam la variabila nrReplaces. 
        Calculam nrReplaces deoarece este cea mai eficienta miscare, rezolvand ambele conditii cu aceleasi miscari,
        adica atunci cand inlocuim un caracter dintr-un sir de caractere identice, il putem inlocui a.i. sa indeplineasca si conditia 2.
        De aceea, vom compara numarul de actiuni pt conditia 2, si nr de actiuni pt conditia 3 si vom alege maximul dintre ele.

    Caz 3 

        Lungimea > 20  
        Putem satisface constrangerea de length folosind operatii de delete, iar apoi vom folosi replace pentru a satisface celelalte 2 constrangeri
        Daca analizam exemplul de subsecventa "aaa" ne dam seama ca facand un delete, economisim un Replace pentru conditia 2.
        In functie de lungimile subsecventelor, avem nevoie de 1,2 sau 3 ori nr de delete uri pentru a economisi un replace .
        
            L -- lungime subsecventa. Luam in calcul L-2 deoarece primul si ultimul caracter nu vor fi modificate sau sterse sau inserate
            
            caz 3.1 (L-2) % 3 == 0   ex: (7-2)%3==0 "aaaaaaa" - avem nevoie de 6 delete uri pentru a nu face 2 replace uri 
            caz 3.2 (L-2) % 3 == 1   requiredReplace1 ++
            caz 3.3 (L-2) % 3 == 2   requiredReplace2 ++ 
                                     requiredReplace3 = nrReplaces - requiredReplace1 - requiredReplace2
            In metoda duplicate  vom aduna numarul de miscari necesare pt fiecare caz iar apoi vom calcula cate replace uri putem economisi facand delete.

            Rezultatul va fi suma dintre nrDeletes + maximul dintre (needsLUD si nrReplaces - nr replace economisite).
