package zadaniaNieobiektowe.saper;

import java.util.Random;
import java.util.Scanner;

public class saper {
    private static int wysokoscPlanszy = 0;
    private static int szerokoscPlanszy = 0;
    private static char[][] planszaUzytkowa = new char[wysokoscPlanszy][szerokoscPlanszy];
    private static char[][] planszaGracza;
    private static int bomby;
    private static char znakPoziomy;
    private static char znakPionowy;
    private static char znakcznikPola;
    private static char znakDoWstawieniaWTabliceUzytkownika;

    public static void main(String[] args) {
        rozruchGry();
    }

    public static void rozruchGry() {
        ustawienieGry();
        planszaGracza();
        do {
            wyswietlaniePlanszy(planszaGracza);
            ruchGracza();
        } while (czyMoznaGrac());

    }

    public static void ustawienieGry() {
        System.out.println("Wybierz poziom trudność gdy;\n" +
                "1. amator\n" +
                "2. średniozaawansowany\n" +
                "3. zawodowiec\n" +
                "4. dowolne parametry");
        ustawienieParametrowPlanszy();
    }

    public static void ustawienieParametrowPlanszy() {
        Scanner scanner = new Scanner(System.in);
        int wybor = scanner.nextInt();
        if (wybor == 1) {
            wysokoscPlanszy = 7;
            szerokoscPlanszy = 7;
            bomby = 10;
        } else if (wybor == 2) {
            wysokoscPlanszy = 15;
            szerokoscPlanszy = 15;
            bomby = 40;
        } else if (wybor == 3) {
            wysokoscPlanszy = 20;
            szerokoscPlanszy = 15;
            bomby = 90;
        } else if (wybor == 4) {
            ustawDowolnaWielkosc();
        }
        planszaUzytkowa = new char[wysokoscPlanszy][szerokoscPlanszy];
        automatyczneUzupelnianiePlanszy();
    }

    public static void ustawDowolnaWielkosc() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj wysokość tablicy");
        wysokoscPlanszy = scanner.nextInt();
        System.out.println("Podaj szerokość tablicy");
        szerokoscPlanszy = scanner.nextInt();
        System.out.println("Podaj ilość bomb");
        bomby = scanner.nextInt();
    }

    public static void automatyczneUzupelnianiePlanszy() {
        int licznik = 0;
        Random random = new Random();
        do {
            int wysokosc = random.nextInt(wysokoscPlanszy);
            int szerokosc = random.nextInt(szerokoscPlanszy);
            if (planszaUzytkowa[wysokosc][szerokosc] != '*') {
                planszaUzytkowa[wysokosc][szerokosc] = '*';
                licznik++;
            }
        } while (licznik != bomby);
        ustawPomocniczeCyfry();
    }

    public static void ustawPomocniczeCyfry() {
        for (int i = 0; i < wysokoscPlanszy; i++) {
            for (int j = 0; j < szerokoscPlanszy; j++) {
                if (planszaUzytkowa[i][j] != '*') {
                    planszaUzytkowa[i][j] = IloscSasiadujacychMin(i, j);
                }
            }
        }
    }

    public static char IloscSasiadujacychMin(int wysokosc, int szerokosc) {
        char miny = '0';
        for (int i = wysokosc - 1; i <= wysokosc + 1; i++)
            if (i >= 0 && i < wysokoscPlanszy) {
                for (int j = szerokosc - 1; j <= szerokosc + 1; j++) {
                    if (j >= 0 && j < szerokoscPlanszy) {
                        if (i != wysokosc || j != szerokosc) {
                            if (planszaUzytkowa[i][j] == '*') {
                                miny++;
                            }
                        }
                    }
                }
            }
        return miny;
    }

    public static void wyswietlaniePlanszy(char[][] plansza) {
        int licznik = 0;
        char wskaznikPoziomy = 'a';
        for (int i = 1; i < szerokoscPlanszy + 1; i++) {
            if (i > 10) {
                System.out.print(" " + i);
            } else {
                System.out.print("  " + i);
            }
        }
        System.out.println("");
        System.out.print(wskaznikPoziomy + " ");
        wskaznikPoziomy++;
        for (char[] polaWysokosc : plansza) {
            for (char polaSzerokosc : polaWysokosc) {
                System.out.print(polaSzerokosc);
                System.out.print("  ");
                licznik++;
                if (licznik % szerokoscPlanszy == 0 && wskaznikPoziomy != 'a' + wysokoscPlanszy) {
                    System.out.println();
                    System.out.print(wskaznikPoziomy + " ");
                    wskaznikPoziomy++;
                }
            }
        }
    }

    public static void planszaGracza() {
        planszaGracza = new char[wysokoscPlanszy][szerokoscPlanszy];
        for (int i = 0; i < planszaGracza.length; i++) {
            for (int j = 0; j < planszaGracza[i].length; j++) {
                planszaGracza[i][j] = 3;
            }
        }
    }

    public static void ruchGracza() {
        wskazPole();
        zaznaczPole();
    }

    public static void wskazPole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPodaj pole do zaznaczenia");
        String pole = scanner.nextLine();
        znakPoziomy = pole.charAt(0);
        znakPionowy = pole.charAt(1);
        if (pole.length() == 3) {
            znakcznikPola = pole.charAt(2);
        }
    }

    public static void zaznaczPole() {
        znakPionowy -= 49;
        znakPoziomy -= 1;
        int index1 = Integer.parseInt(String.valueOf(znakPionowy));
        int index2 = Integer.parseInt(String.valueOf(znakPoziomy));
        if (znakcznikPola != 'z') {
            znakDoWstawieniaWTabliceUzytkownika = planszaUzytkowa[index1][index2];
            planszaGracza[index1][index2] = znakDoWstawieniaWTabliceUzytkownika;
            odkrywanieZer(index1, index2);
        } else {
            planszaGracza[index1][index2] = 33;
            znakcznikPola = ' ';
        }
    }

    public static void odkrywanieZer(int y, int x) {
        if (planszaUzytkowa[y][x] == '0') {
            for (int i = y - 1; i <= y + 1; i++) {
                if (i >= 0 && i < wysokoscPlanszy)
                    for (int j = x - 1; j <= x + 1; j++) {
                        if (j >= 0 && j < szerokoscPlanszy) {
                            if (i != y || j != x) {
                                boolean nieOdkryta = false;
                                if (planszaGracza[i][j] == 3) {
                                    nieOdkryta = true;
                                    planszaGracza[i][j] = planszaUzytkowa[i][j];
                                }
                                if (nieOdkryta && planszaUzytkowa[i][j] == '0') {
                                    odkrywanieZer(i, j);
                                }
                            }
                        }
                    }
            }
        }
    }


    public static boolean czyMoznaGrac() {
        if (!czyTrafilesBombe() && !czyWygrales()) {
            return true;
        }
        return false;
    }

    public static boolean czyTrafilesBombe() {
        if (znakDoWstawieniaWTabliceUzytkownika == '*') {
            System.out.println("\nKoniec gry, trafiłeś na bombę ");
            wyswietlaniePlanszy(planszaUzytkowa);
            return true;
        }
        return false;
    }

    public static boolean czyWygrales() {
        int licznik = 0;
        for (int i = 0; i < planszaGracza.length; i++) {
            for (int j = 0; j < planszaGracza[i].length; j++) {
                if (planszaGracza[i][j] == 3 || planszaGracza[i][j] == '!') {
                    licznik++;
                }
            }
        }
        if (licznik == bomby) {
            System.out.println("\nWygrałeś:)");
            wyswietlaniePlanszy(planszaUzytkowa);
            return true;
        }
        return false;
    }
}



