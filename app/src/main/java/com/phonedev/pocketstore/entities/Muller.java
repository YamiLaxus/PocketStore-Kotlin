package com.phonedev.pocketstore.entities;

public class Muller {

    public static void main(String[] args) {

        Muller();
    }

    static public void Muller() {
        double x0 = 0.5, x1 = 1, x2 = 1.5, x3;
        double h0, h1, d0, d1, A, B, C;
        double den, raiz;

        do {
            int interacion = 0;
            interacion = interacion + 1;

            h0 = x1 - x0;
            h1 = x2 - x1;
            d0 = (f(x1) - f(x0)) / h0;
            d1 = (f(x2) - f(x1)) / h1;

            A = (d1 - d0) / (h1 + h0);
            B = A * h1 + d1;
            C = f(x2);

            System.out.println("");
            System.out.println("A = " + A);
            System.out.println("B = " + B);
            System.out.println("C = " + C);
            System.out.println("");

            raiz = Math.sqrt(Math.pow(B, 2) - 4.0 * A * C);

            if (Math.abs(B + raiz) > Math.abs(B - raiz)) {
                den = B + raiz;
            } else {
                den = B - raiz;
            }

            x3 = x2 - 2 * C / den;

            System.out.println(" x" + interacion + " = " + x3 + " " + f(x3));

            x0 = x1;
            x1 = x2;
            x2 = x3;

        } while (Math.abs(f(x3)) > 0.000001);

    }

    static public double f(double x) {

        return (16 * Math.pow(x, 4) - 40 * Math.pow(x, 3) + 5 * Math.pow(x, 2) + 20 * x + 6);
    }

}
