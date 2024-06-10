package edu.njit.cs114;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Chris Lombardi
 * Date created: 2/26/2024
 */
public class ListPolynomial extends AbstractPolynomial {

    /**
     * To be completed for lab: Initialize the list
     */
    private List<PolynomialTerm> termList = new LinkedList<>(); // Initialize Linked List

    private class ListPolyIterator implements Iterator<PolynomialTerm> {

        private Iterator<PolynomialTerm> iter = termList.iterator();

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public PolynomialTerm next() {
            PolynomialTerm term = iter.next();
            return new PolynomialTerm(term.getCoefficient(), term.getPower());
        }
    }

    // Default constructor
    public ListPolynomial() {}

    /**
     * Create a single term polynomial
     * @param power
     * @param coefficient
     * @throws Exception
     */
    public ListPolynomial(int power, double coefficient) throws Exception { // Polynomial Constructor
        if (power < 0) {
            throw new Exception("Invalid power for the term");
        }
        addTerm(power, coefficient);
    }

    /**
     * Create a new polynomial that is a copy of "another".
     * NOTE : you should use only the interface methods of Polynomial
     *
     * @param another
     */
    public ListPolynomial(Polynomial another) {
        Iterator<PolynomialTerm> iterator = another.getIterator(); // Initialize iterator
        while(iterator.hasNext()){
            PolynomialTerm term = iterator.next();
            termList.add(new PolynomialTerm(term.getCoefficient(), term.getPower())); // add terms until iterator stops
        }
    }


    /**
     * Returns coefficient of power
     * @param power
     * @return
     */
    @Override
    public double coefficient(int power) {
        for(PolynomialTerm term: termList){
            if(term.getPower() == power){
                return term.getCoefficient();
            }
        }
        /**
         * Complete the code for lab
         */
        return 0;
    }

    /**
     * Returns degree of the polynomial
     * @return
     */
    @Override
    public int degree() {
        int maxPower = 0; // Initialize max power
        for(PolynomialTerm term: termList){ //iterate through list
            if(term.getPower() > maxPower){ // update max Power
                maxPower = term.getPower();
            }
        }
        return maxPower;
    }

    /**
     * Adds polynomial term; add to existing term if power already exists
     * @param power
     * @param coefficient
     * @throws Exception if power < 0
     */
    @Override
    public void addTerm(int power, double coefficient) throws Exception {
        if (power < 0) { // Check edge cases
            throw new Exception("Power cannot be negative!");
        }
        if (coefficient == 0) {
            return;
        }
        int index = 0; // Initialize index
        for (PolynomialTerm term : termList) { // Iterate through termList
            if (term.getPower() < power) { // Check different conditions for power
                termList.add(index, new PolynomialTerm(coefficient, power));
                return;
            } else if (term.getPower() == power) {
                double newCoeff = term.getCoefficient() + coefficient;
                if (newCoeff != 0) {
                    termList.set(index, new PolynomialTerm(newCoeff, power));
                } else {
                    termList.remove(index);
                }
                return;
            }
            index++; // Increment index
        }
        termList.add(new PolynomialTerm(coefficient, power)); // add term if all conditions passed

        /**
         * Complete the code for lab
         */
    }

    /**
     * Remove and return the term for the specified power,
     * @param power
     * @return removed term if it exists else zero term
     */
    @Override
    public PolynomialTerm removeTerm(int power) {
        Iterator<PolynomialTerm> iter = termList.iterator(); // Iterate through p
        while(iter.hasNext()){
            PolynomialTerm term = iter.next();
            if(term.getPower() == power){ // If power found remove term
                iter.remove();
                return term;
            }
        }
        /**for(PolynomialTerm term: termList){
            if(term.getPower() == power){
                PolynomialTerm removed = termList.get(termList.indexOf(term));
                termList.remove(term);
                return removed;
            }
        }
        /**
         * Complete the code for homework
         */
        return new PolynomialTerm(0, power);
    }

    /**
     * Evaluate polynomial at point
     * @param point
     * @return
     */
    @Override
    public double evaluate(double point) {
        double sum = 0; // Create counter
        for(PolynomialTerm term: termList){ // Iterate through termlist
            double value = Math.pow(point, term.getPower()) * term.getCoefficient();
            sum += value; // add each term to sum
        }
        /**
         * Complete the code for homework
         */
        return sum;
    }

    /**
     * Add polynomial p to this polynomial and return the result
     * @param p
     * @return
     */
    @Override
    public Polynomial add(Polynomial p) {
        Polynomial result = new ListPolynomial(this);

        Iterator<PolynomialTerm> iter = p.getIterator(); // Iterate through p
        while(iter.hasNext()){
            PolynomialTerm term = iter.next();
            try{ // use addTerm to create terms in result
                result.addTerm(term.getPower(), term.getCoefficient());
        }   catch (Exception e){
                e.printStackTrace();
            }
        }

        /**
         * Complete the code for homework
         */
        return result;
    }

    /**
     * Substract polynomial p from this polynomial and return the result
     * @param p
     * @return
     */
    @Override
    public Polynomial subtract(Polynomial p) {
        Polynomial result = new ListPolynomial(this);

        Iterator<PolynomialTerm> iter = p.getIterator(); // Iterate through p
        while(iter.hasNext()){
            PolynomialTerm term = iter.next();
            try{ // use addTerm to create result polynomial
                result.addTerm(term.getPower(), -term.getCoefficient());
            }
            catch (Exception e){
                e.getMessage();
            }
        }
        /**
         * Complete the code for homework
         */
        return result; // to be removed if necessary
    }

    /**
     * Multiply polynomial p with this polynomial and return the result
     * @param p
     * @return
     */
    @Override
    public Polynomial multiply(Polynomial p) {
        Polynomial result = new ListPolynomial(); // Initialize result

        Iterator<PolynomialTerm> iter1 = p.getIterator(); // iterate through p
        while(iter1.hasNext()) {
            PolynomialTerm term1 = iter1.next();
            Iterator<PolynomialTerm> iter2 = termList.iterator(); // iterate through thermList
            while(iter2.hasNext()) {
                PolynomialTerm term2 = iter2.next();
                try { // create new power and coefficient
                    double newCoefficient = term1.getCoefficient() * term2.getCoefficient();
                    int newPower = term1.getPower() + term2.getPower();
                    result.addTerm(newPower, newCoefficient);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    @Override
    public Polynomial divide(Polynomial p) throws Exception {
        Polynomial quotient = new ListPolynomial(); // Initialize the quotient polynomial

        if (p.degree() == 0 && p.coefficient(0) == 0) {
            throw new Exception("Cannot divide by zero!");
        }

        Polynomial residue = new ListPolynomial(this); // Initialize residue polynomial

        while (residue.degree() >= p.degree()) { //update current degree and coefficient
            int currentDegree = residue.degree() - p.degree();
            double currentCoeff = residue.coefficient(residue.degree()) / p.coefficient(p.degree());

            quotient.addTerm(currentDegree, currentCoeff);

            Iterator<PolynomialTerm> iter = p.getIterator(); // iterate through p
            while (iter.hasNext()) {
                PolynomialTerm term = iter.next();
                double subtractedCoeff = currentCoeff * p.coefficient(term.getPower());
                residue.addTerm(currentDegree + term.getPower(), -subtractedCoeff);
            }
        }

        return quotient;
    }

    @Override
    public Polynomial compose(Polynomial p) {
        Polynomial result = new ListPolynomial();

        Iterator<PolynomialTerm> iter1 = this.getIterator();
        while (iter1.hasNext()) {
            PolynomialTerm term1 = iter1.next();
            double term1Coeff = term1.getCoefficient();
            int term1Power = term1.getPower();

            Iterator<PolynomialTerm> iter2 = p.getIterator();
            while (iter2.hasNext()) {
                PolynomialTerm term2 = iter2.next();
                double term2Coeff = term2.getCoefficient();
                int term2Power = term2.getPower();

                try {
                    double newCoeff = term1Coeff * Math.pow(term2Coeff, term1Power);
                    int newPower = term1Power * term2Power;
                    result.addTerm(newPower, newCoeff);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }



    @Override
    public Iterator<PolynomialTerm> getIterator() {
        return new ListPolyIterator();
    }

    public static void main(String [] args) throws Exception {
        /** Uncomment after you have implemented all the functions */
        Polynomial p1 = new ListPolynomial();
        System.out.println("p1(x) = " + p1);
        assert p1.degree() == 0;
        assert p1.coefficient(0) == 0;
        assert p1.coefficient(2) == 0;
        assert p1.equals(new ListPolynomial());
        Polynomial p2 = new ListPolynomial(5, -1.6);
        p2.addTerm(0,3.1);
        p2.addTerm(4,2.5);
        p2.addTerm(2,-2.5);
        System.out.println("p2(x) = " + p2);
        assert p2.degree() == 5;
        assert p2.coefficient(4) == 2.5;
        assert p2.toString().equals("-1.600x^5 + 2.500x^4 - 2.500x^2 + 3.100");
        System.out.println("p2(1) = " + p2.evaluate(1));
        assert Math.abs(p2.evaluate(1)-1.5) <= 0.001;
        Polynomial p3 = new ListPolynomial(0, -4);
        p3.addTerm(5,3);
        p3.addTerm(5,-1);
        System.out.println("p3(x) = " + p3);
        assert p3.degree() == 5;
        assert p3.coefficient(5) == 2;
        assert p3.coefficient(0) == -4;
        System.out.println("p3(2) = " + p3.evaluate(2));
        assert p3.evaluate(2) == 60;
        Polynomial p21 = new ListPolynomial(p2);
        System.out.println("p21(x) = " + p21);
        assert p21.equals(p2);
        p21.addTerm(4, -3.1);
        System.out.println("p21(x) = " + p21);
        assert !p21.equals(p2);
        assert p21.coefficient(4) == p2.coefficient(4)-3.1;
        PolynomialTerm t1 = p21.removeTerm(4);
        System.out.println("p21(x) = " + p21);
        assert !p21.equals(p2);
        assert p21.coefficient(4) == 0;
        assert t1.getPower() == 4;
        assert Math.abs(t1.getCoefficient() - -0.6) <= 0.0001;
        System.out.println("p2(x) = " + p2);
        Polynomial p22 = new ListPolynomial(p21);
        t1 = p21.removeTerm(1);
        System.out.println("p21(x) = " + p21);
        assert p21.equals(p22);
        assert t1.getPower() == 1;
        assert t1.getCoefficient() == 0;
        try {
            Polynomial p5 = new ListPolynomial(-5, 4);
            assert false;
        } catch (Exception e) {
            // Exception expected
            assert true;
        }
        System.out.println("p2(x) + p3(x) = " + p2.add(p3));
        Polynomial result = p2.add(p3);
        assert result.degree() == 5;
        assert Math.abs(result.coefficient(5) - 0.4) <= 0.0001;;
        System.out.println("p2(x) - p3(x) = " +p2.subtract(p3));
        result = p2.subtract(p3);
        assert result.degree() == 5;
        assert Math.abs(result.coefficient(5) - -3.6) <= 0.0001;
        assert Math.abs(result.coefficient(0) - 7.1) <= 0.0001;
        System.out.println("p2(x) * p3(x) = " +p2.multiply(p3));
        result = p2.multiply(p3);
        assert result.degree() == 10;
        assert Math.abs(result.coefficient(10) - -3.2) <= 0.0001;
        assert Math.abs(result.coefficient(5) - 12.6) <= 0.0001;
        assert Math.abs(result.coefficient(0) - -12.4) <= 0.0001;
        assert Math.abs(p2.evaluate(1) * p3.evaluate(1) - result.evaluate(1)) <= 0.0001;
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
