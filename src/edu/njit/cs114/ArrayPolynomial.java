package edu.njit.cs114;

import java.util.Arrays;
import java.util.Iterator;

/**
 *Author: Chris Lombardi
* Date created: 1/25/2024
 */
public class ArrayPolynomial extends AbstractPolynomial {
    // coefficient array
    private double[] coefficients = new double[1];
    // Default constructor
    public ArrayPolynomial() {
    }
    public ArrayPolynomial(int power, double coefficient) throws Exception {
        if(power <0){
            throw new Exception("Power cannot be negative!");
        }
        addTerm(power, coefficient);
    }
    public ArrayPolynomial(Polynomial another) {
        int other_degree = another.degree();
        for (int i = other_degree; i >= 0; i--) {
            try {
                addTerm(i, another.coefficient(i));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    @Override
    public void addTerm(int power, double coefficient) throws Exception {
        if(power <0){
            throw new Exception("Power cannot be negative!");
        }
        if(coefficient == 0){
            return;
        }
        if(power > coefficients.length -1){
            coefficients = Arrays.copyOf(coefficients, power + 1);
        }
        coefficients[power] += coefficient;
    }

    @Override
    public PolynomialTerm removeTerm(int power) {
        if(power <0 || power > coefficients.length){
            return new PolynomialTerm(0, 0);
        }
        double removedCoefficient = coefficients[power];
        coefficients[power] = 0;  // Set the coefficient to zero
        return new PolynomialTerm(removedCoefficient, power);
    }

    @Override
    public double coefficient(int power) {
        if (power < 0 || power >= coefficients.length) {
            return 0;
        }
        return coefficients[power];
    }
    @Override
    public int degree() {
        for (int power = coefficients.length - 1; power >= 0; power--){
            if (coefficients[power] != 0) {
                return power;
            }
        }
        return 0;
    }
    @Override
    public double evaluate(double point) {
        double value = 0.0;
        for(int i = coefficients.length-1 ; i >=0; i--){
            value += Math.pow(point, i) * coefficients[i];
        }
        return value;
    }

    @Override
    public Polynomial add(Polynomial p) {
        Polynomial result = new ArrayPolynomial();
        int maxDegree = Math.max(degree(), p.degree());
        for(int i = maxDegree; i >=0; i--) {
            double thisCoefficient = coefficient(i);
            double pCoefficient = p.coefficient(i);
            try {
                result.addTerm(i, thisCoefficient + pCoefficient);

            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
                return result;
    }
    @Override
    public Polynomial subtract(Polynomial p) {
        Polynomial result = new ArrayPolynomial();
        int maxDegree = Math.max(degree(), p.degree());
        for(int i = maxDegree; i >=0; i--) {
            double thisCoefficient = coefficient(i);
            double pCoefficient = p.coefficient(i);
            try {
                result.addTerm(i, thisCoefficient - pCoefficient);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public Polynomial multiply(Polynomial p) {
        Polynomial result = new ArrayPolynomial();
        for (int i = this.degree(); i >= 0; i--) {
            for (int j = p.degree(); j >= 0; j--) {
                try {
                    result.addTerm(i + j, this.coefficient(i) * p.coefficient(j));
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return result;
    }
    @Override
    public Polynomial divide(Polynomial p) throws Exception {
        if (p.degree() == 0 && p.coefficient(0) == 0) {
            throw new Exception("Division by zero");
        }

        Polynomial quotient = new ArrayPolynomial();
        Polynomial residue = new ArrayPolynomial(this);

        while (residue.degree() >= p.degree()) {
            int currentDegree = residue.degree() - p.degree();
            double currentCoefficient = residue.coefficient(residue.degree()) / p.coefficient(p.degree());

            quotient.addTerm(currentDegree, currentCoefficient);

            for (int i = 0; i <= p.degree(); i++) {
                double subtractCoefficient = currentCoefficient * p.coefficient(i);
                residue.addTerm(currentDegree + i, -subtractCoefficient);
            }
        }
        return quotient;
    }

    @Override
    public Polynomial compose(Polynomial p) {
        Polynomial result = new ArrayPolynomial();
        return result;
    }

    @Override
    public Iterator<PolynomialTerm> getIterator() {
        return null;
    }
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int degree = degree();
        if (degree == 0) {
            return "" + String.format("%.3f", coefficients[0]);
        }
        for (int i = degree; i >= 0; i--) {
            if (coefficients[i] == 0) {
                continue;
            }
            if (i < degree) {
                builder.append(coefficients[i] > 0 ? " + " : " - ");
                builder.append(String.format("%.3f", Math.abs(coefficients[i])));
            } else {
                builder.append(String.format("%.3f", coefficients[i]));
            }
            if (i > 0) {
                builder.append("x");
                if (i > 1) {
                    builder.append("^" + i);
                }
            }
        }
        return builder.toString();
    }

    /**
     * Returns true if the object obj is a Polynomial and its coefficients are the
     * same
     * for all the terms in the polynomial
     *
     * @param obj
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Polynomial)) {
            return false;
        }
        Polynomial other = (Polynomial) obj;
        if (degree() != other.degree()) {
            return false;
        }
        int degree = degree();
        for (int i = 0; i <= degree; i++) {
            if (coefficients[i] != other.coefficient(i)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        Polynomial p1 = new ArrayPolynomial();
        System.out.println("p1(x) = " + p1);
        assert p1.degree() == 0;
        assert p1.coefficient(0) == 0;
        assert p1.coefficient(2) == 0;
        assert p1.equals(new ArrayPolynomial());
        Polynomial p2 = new ArrayPolynomial(4, 3.6);
        p2.addTerm(0, 3.6);
        p2.addTerm(3, 2.5);
        p2.addTerm(2, -2.5);
        System.out.println("p2(x) = " + p2);
        assert p2.degree() == 4;
        assert p2.coefficient(2) == -2.5;
        assert p2.toString().equals("3.600x^4 + 2.500x^3 - 2.500x^2 + 3.600");
        System.out.println("p2(1) = " + p2.evaluate(1));
        assert Math.abs(p2.evaluate(1)-7.2) <= 0.001;
        Polynomial p3 = new ArrayPolynomial(0, -4);
        p3.addTerm(5, 3);
        p3.addTerm(5, -1);
        System.out.println("p3(x) = " + p3);
        assert p3.degree() == 5;
        assert p3.coefficient(5) == 2;
        assert p3.coefficient(0) == -4;
        System.out.println("p3(2) = " + p3.evaluate(2));
        assert Math.abs(p3.evaluate(2)-60) <= 0.001;
        Polynomial p21 = new ArrayPolynomial(p2);
        System.out.println("p21(x) = " + p21);
        assert p21.equals(p2);
        PolynomialTerm t1 = p21.removeTerm(4);
        assert t1.getCoefficient()== 3.6;
        assert t1.getPower() == 4;
        System.out.println("p21(x) = " + p21);
        assert !p21.equals(p2);
        assert p21.coefficient(4) == 0;
        assert p21.degree() == 3;
        try {
            Polynomial p5 = new ArrayPolynomial(-5, 4);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        System.out.println("p2(x) + p3(x) = " + p2.add(p3));
        Polynomial result = p2.add(p3);
        assert result.degree() == 5;
        assert Math.abs(result.coefficient(5) - 2) <= 0.0001;
        System.out.println("p2(x) - p3(x) = " +p2.subtract(p3));
        result = p2.subtract(p3);
        assert result.degree() == 5;
        assert Math.abs(result.coefficient(5) - -2) <= 0.0001;
        assert Math.abs(result.coefficient(0) - 7.6) <= 0.0001;
        System.out.println("p2(x) * p3(x) = " +p2.multiply(p3));
        result = p2.multiply(p3);
        assert result.degree() == 9;
        assert Math.abs(result.coefficient(9) - 7.2) <= 0.0001;
        assert Math.abs(result.coefficient(5) - 7.2) <= 0.0001;
        assert Math.abs(result.coefficient(0) - -14.4) <= 0.0001;
        assert Math.abs(p2.evaluate(1) * p3.evaluate(1) - result.evaluate(1)) <=0.0001;
        result = result.divide(p3);
        System.out.println("p2(x) * p3(x) / p3(x) = " + result);
        assert result.degree() == p2.degree();
        assert Math.abs(result.coefficient(4) - p2.coefficient(4)) <= 0.0001;
        assert Math.abs(result.coefficient(3) - p2.coefficient(3)) <= 0.0001;
        assert Math.abs(result.coefficient(2) - p2.coefficient(2)) <= 0.0001;
        assert Math.abs(result.coefficient(1) - p2.coefficient(1)) <= 0.0001;
        assert Math.abs(result.coefficient(0) - p2.coefficient(0)) <= 0.0001;

    }
}