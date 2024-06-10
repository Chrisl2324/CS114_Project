package edu.njit.cs114;

/** Write Lab Part 2 Answers below  :
 *  (a) Time complexity will be O(n)
 *  (b) The copy cost is proportional to the number of items in an approximately linear fashion.
 *  (c) No, the copy cost does not change much because the copy cost will still be proportional to the number of items inserted.
 *  If this is over 5000, this cost will be the same as the values from the lab.
 */

/**
 * Author: Chris Lombardi
 * Date created: 2/14/2024
 */
public class DynamicDoubleArray {

    private static final int DEFAULT_INITIAL_CAPACITY = 1;

    private Double[] arr;
    private int size;
    // keeps track of number of element copies made during array expansion or contraction
    private int nCopies;

    public DynamicDoubleArray(int initialCapacity) {

        arr = new Double[initialCapacity];
    }

    public DynamicDoubleArray() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Add element at specified index position shifting to right elements at positions higher than
     * or equal to index
     *
     * @param index
     * @param elem
     * @throws IndexOutOfBoundsException if index < 0 or index > size()
     */
    public void add(int index, double elem) throws IndexOutOfBoundsException {
        // check edge cases
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        // create new array if needed
        if (size == arr.length) {
            Double[] newArr = expandArray();
            }
        for(int i = size-1; i>=index; i--){
            arr[i+1] = arr[i];

        }
        arr[index] = elem;
        size++;
        }


    public Double[] expandArray() {
        Double[] newArr = new Double[arr.length * 2];
        // copy array elements
        for (int i = 0; i < size; i++) {
            newArr[i] = arr[i];
            nCopies++;
        }
        arr = newArr;
        return arr;
    }
    /**
     * Append element to the end of the array
     * @param elem
     */
    public void add(double elem) {
        if (arr.length == size) {
            Double[] newArr = expandArray();
            size++;
            newArr[size - 1] = elem;

        }
        else {
            size++;
            arr[size - 1] = elem;
        }
    }
    /**
     * Set the element at specified index position replacing any previous value
     * @param index
     * @param elem
     * @return previous value in the index position
     * @throws IndexOutOfBoundsException if index < 0 or index >= size()
     */

    public double set(int index, double elem) throws IndexOutOfBoundsException {
        // check edge cases
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
        if (size == arr.length) {
            Double[] newArr = expandArray();
        }
        double old = arr[index];
        arr[index] = elem;

        return old;
    }

    /**
     * Get the element at the specified index position
     * @param index
     * @return
     * @throws IndexOutOfBoundsException if index < 0 or index >= size()
     */

    public double get(int index) throws IndexOutOfBoundsException {
        // check edge cases
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        } else {
            return arr[index];
        }
    }

    /**
     * Remove and return the element at the specified index position. The elements with positions
     *  higher than index are shifted to left
     * @param index
     * @return
     * @throws IndexOutOfBoundsException if index < 0 or index >= size()
     */

    public double remove(int index) throws IndexOutOfBoundsException {
        // check edge cases
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of Bounds!");
        }
        double value = arr[index];
        size--;
        // shift array indexes
        for (int i = index; i < size; i++) {
            arr[i] = arr[i + 1];
        }
        contractIfNeccasarry();
        return value;
        }

    /**
     * Remove and return the element at the end of the array
     * @return
     * @throws IndexOutOfBoundsException if size() == 0
     */

    public double remove() throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Array is empty");
        }
        double value = arr[size-1];
        size--;
        contractIfNeccasarry();
        return value;
    }

    /**
     * Removes from this list all of the elements whose index is between fromIndex,
     * inclusive, and toIndex, exclusive.
     * Shifts any succeeding elements to the left (reduces their index).
     * This call shortens the list by (toIndex - fromIndex) elements.
     * @return
     * @throws IndexOutOfBoundsException if fromIndex or toIndex is out of range
     * i.e. (fromIndex < 0 || fromIndex >= size() || toIndex > size() || toIndex < fromIndex)
     */
    public void removeRange(int fromIndex, int toIndex) throws IndexOutOfBoundsException {
        //check edge cases
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= size || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
        int numToRemove = toIndex - fromIndex;
        // shift array indexes
        for (int i = fromIndex; i < size - numToRemove; i++) {
            arr[i] = arr[i + numToRemove];
        }
        size -= numToRemove;
    }



    /**
     * Increase the capacity of the array, if necessary, to ensure that it holds
     * at least minCapacity elements
     * @param minCapacity
     */
    public void ensureCapacity(int minCapacity) {
        if(arr.length < minCapacity){
            Double[] newArr = new Double[minCapacity];
            for(int i = 0; i < size; i++){
                newArr[i] = arr[i];
                nCopies++;
            }
            arr = newArr;
        }
    }

    /**
     * Trim the capacity of the array to hold just the number of elements
     */
    public void trimToSize() {
        if (arr.length > size) {
            Double[] newArr = new Double[size];
            for (int i = 0; i < size; i++) {
                newArr[i] = arr[i];
                nCopies++;
            }
            arr = newArr;

        }
    }
    void contractIfNeccasarry() {
        if (size <= (arr.length) / 4) {
            Double[] arr2 = new Double[arr.length / 2];
            for (int i = 0; i < size; i++) {
                arr2[i] = arr[i];
                nCopies++;
            }
            arr = arr2;
        }
    }

            /**
             * Returns the number of elements in the array
             * @return
             */
            public int size() {
                return size;
            }

            /**
             * Returns the total number of copy operations done due to expansion of array
             * @return
             */
            public int nCopies() {
                return nCopies;
            }


            public String toString() {
                StringBuilder builder = new StringBuilder();
                builder.append("(" + arr.length + ")");
                builder.append("[");
                for (int i=0; i < size; i++) {
                    if (i > 0) {
                        builder.append(",");
                    }
                    builder.append(arr[i] == null ? "" : arr[i]);
                }
                builder.append("]");
                return builder.toString();
            }

            public static void main(String [] args) throws Exception {
                DynamicDoubleArray arr = new DynamicDoubleArray();
                arr.add(8.5);
                arr.add(12.1);
                arr.add(-5.7);
                System.out.println("array of size " + arr.size() + " : " + arr);
                assert arr.size() == 3;
                arr.add(1, 4.9);
                arr.add(2, 20.2);
                System.out.println("array of size " + arr.size() + " : " + arr);
                assert arr.size() == 5;
                double oldVal = arr.set(2, 25);
                System.out.println("old value at index 2 after replacing it with 25 = " + oldVal);
                assert oldVal == 20.2;
                System.out.println("Element at position 2 = " + arr.get(2));
                assert arr.get(2) == 25;
                System.out.println("array of size " + arr.size() + " : " + arr);
                assert arr.size() == 5;
                double removedVal = arr.remove(0);
                System.out.println("Removed element at position 0 = " + removedVal);
                System.out.println("array of size " + arr.size()+ " : " + arr);
                assert removedVal == 8.5;
                assert arr.size() == 4;
                removedVal = arr.remove(2);
                System.out.println("Removed element at position 2 = " + removedVal);
                System.out.println("array of size " + arr.size()+ " : " + arr);
                assert removedVal == 12.1;
                assert arr.size() == 3;
                removedVal = arr.remove(2);
                System.out.println("Removed element at position 2 = " + removedVal);
                System.out.println("array of size " + arr.size()+ " : " + arr);
                assert removedVal == -5.7;
                assert arr.size() == 2;
                removedVal = arr.remove();
                System.out.println("Removed element at end = " + removedVal);
                System.out.println("array of size " + arr.size()+ " : " + arr);
                assert removedVal == 25;
                assert arr.size() == 1;
                removedVal = arr.remove();
                System.out.println("Removed element at end = " + removedVal);
                System.out.println("array of size " + arr.size()+ " : " + arr);
                assert removedVal == 4.9;
                assert arr.size() == 0;
                arr.add(67);
                arr.add(-14);
                arr.add(15);
                System.out.println("array of size " + arr.size()+ " : " + arr);
                assert arr.size == 3;
                arr.add(9.5);
                arr.add(-14);
                arr.add(22);
                assert arr.size == 6;
                arr.removeRange(2,5);
                System.out.println("array of size " + arr.size()+ " : " + arr);
                assert arr.size == 3;
                int[] nItemsArr = new int[]{0, 100000, 200000, 400000, 800000, 1600000, 3200000};
                DynamicDoubleArray arr1 = new DynamicDoubleArray();
                System.out.println("Using initial array capacity of 1...");
                long totalTime = 0;
                for (int k = 1; k < nItemsArr.length; k++) {
                    for (int i = 0; i < nItemsArr[k] - nItemsArr[k - 1]; i++) {
                        long startTime = System.currentTimeMillis();
                        arr1.add(i + 1);
                        long stopTime = System.currentTimeMillis();
                        totalTime += (stopTime - startTime);
                    }
                    System.out.println("copy cost for inserting " + nItemsArr[k] + " items = " +
                            +arr1.nCopies());
                    System.out.println("total time(ms) for inserting " + nItemsArr[k] + " items = " +
                            +totalTime);
                }
                DynamicDoubleArray arr2 = new DynamicDoubleArray(5000);
                System.out.println("Using initial array capacity of 5000...");
                totalTime = 0;
                for (int k = 1; k < nItemsArr.length; k++) {
                    for (int i = 0; i < nItemsArr[k] - nItemsArr[k - 1]; i++) {
                        long startTime = System.currentTimeMillis();
                        arr2.add(i + 1);
                        long stopTime = System.currentTimeMillis();
                        totalTime += (stopTime - startTime);
                    }
                    System.out.println("copy cost for inserting " + nItemsArr[k] + " items = " +
                            +arr2.nCopies());
                    System.out.println("total time(ms) for inserting " + nItemsArr[k] + " items = " +
                            +totalTime);
                }

                totalTime = 0;
                for (int k=1; k < nItemsArr.length; k++) {
                    for (int i = 0; i < nItemsArr[k]-nItemsArr[k-1]; i++) {
                        long startTime = System.currentTimeMillis();
                        arr1.remove();
                        long stopTime = System.currentTimeMillis();
                        totalTime += (stopTime - startTime);
                    }
                    System.out.println("total time(ms) for deleting " + nItemsArr[k] + " items = " +
                            + totalTime);
                }
            }

        }




