//TODO:
//  (1) Update this code to meet the style and JavaDoc requirements.
//			Why? So that you get experience with the code for a heap!
//			Also, this happens a lot in industry (updating old code
//			to meet your new standards). We've done this for you in
//			WeissCollection and WeissAbstractCollection.
//  (2) Implement getIndex() method and the related map integration
//			 -- see project description
//  (3) Implement update() method -- see project description

import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;

//You may uncomment this to use it, or use your HashTable class
//from Project 3.
import java.util.HashMap;

/**
 * PriorityQueue class implemented via the binary heap. From your textbook
 * (Weiss)
 * @param <AnyType> the data type
 */
public class WeissPriorityQueue<AnyType> extends WeissAbstractCollection<AnyType> {
    // --------------------------------------------------------
    // testing code goes here... edit this as much as you want!
    // --------------------------------------------------------

    /**
     * used to do some few tests make sure the methods inside works well.
     * @param args arg
     */
    public static void main(String[] args) {
        /**
         * Make the student class used to test the update and get index method.
         * @author 15409
         *
         */
        class Student {
            String gnum;
            String name;

            /**
             * Use to define the two values of the student.
             * @param gnum this as g number
             * @param name this as name
             */
            Student(String gnum, String name) {
                this.gnum = gnum;
                this.name = name;
            }

            /**The method is to used to compare to items.
             * @param o this item that need to be compare
             * @return true if equal false if not
             */
            public boolean equals(Object o) {
                if (o instanceof Student)
                    return this.gnum.equals(((Student) o).gnum);
                return false;
            }

            /**
             * print the name as a string.
             * @return the name as a string.
             */
            public String toString() {
                return name + "(" + gnum + ")";
            }

            /**
             * get the hash code.
             * @return the g number hash code.
             */
            public int hashCode() {
                return gnum.hashCode();
            }
        }

        Comparator<Student> comp = new Comparator<>() {
            public int compare(Student s1, Student s2) {
                return s1.name.compareTo(s2.name);
            }
        };

        // TESTS FOR INDEXING -- you'll need more testing...

        WeissPriorityQueue<Student> q = new WeissPriorityQueue<>(comp);

        q.add(new Student("G00000000", "Robert"));
        System.out.print(q.getIndex(new Student("G00000001", "Cindi")) + " "); // -1, no index
        System.out.print(q.getIndex(new Student("G00000000", "Robert")) + " "); // 1, at root
        System.out.println();

        q.add(new Student("G00000001", "Cindi"));
        System.out.print(q.getIndex(new Student("G00000001", "Cindi")) + " "); // 1, at root
        System.out.print(q.getIndex(new Student("G00000000", "Robert")) + " "); // 2, lower down
        System.out.println();

        q.remove(); // remove Cindi
        System.out.print(q.getIndex(new Student("G00000001", "Cindi")) + " "); // -1, no index
        System.out.print(q.getIndex(new Student("G00000000", "Robert")) + " "); // 1, at root
        System.out.println();
        System.out.println();

        // TESTS FOR UPDATING -- you'll need more testing...

        q = new WeissPriorityQueue<>(comp);
        q.add(new Student("G00000000", "Robert"));
        q.add(new Student("G00000001", "Cindi"));

        for (Student s : q)
            System.out.print(q.getIndex(s) + " "); // 1 2
        System.out.println();
        for (Student s : q)
            System.out.print(s.name + " "); // Cindi Robert
        System.out.println();

        Student bobby = new Student("G00000000", "Bobby");
        q.update(bobby);

        for (Student s : q)
            System.out.print(q.getIndex(s) + " "); // 1 2
        System.out.println();
        for (Student s : q)
            System.out.print(s.name + " "); // Bobby Cindi
        System.out.println();

        bobby.name = "Robert";
        q.update(bobby);

        for (Student s : q)
            System.out.print(q.getIndex(s) + " "); // 1 2
        System.out.println();
        for (Student s : q)
            System.out.print(s.name + " "); // Cindi Robert
        System.out.println();

        // you'll need more testing...
    }

    // Uncomment one of these lines depending on which hash table implementation you
    // are using
    /**
     * the hash map used to store the location and its value.
     */
    private HashMap<AnyType, Integer> indexMap; // if you're using the JCF hash
    // table
    // private HashTable<AnyType, Integer> indexMap; //if you're using project 3's
    // hash table

    // you implement this
    /**
     * This method is to use hash map to store the 
     * index of the array and its value.
     * @param x the item that need to find the index
     * @return the location of the item x.
     */
    public int getIndex(AnyType x) {
        // average case O(1)

        // returns the index of the item in the heap,
        // or -1 if it isn't in the heap

        int capacity = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] != null) {
                indexMap.put(array[i], i);
                capacity++;
                if (capacity == currentSize) {
                    break;
                }
            }
        }

        if (indexMap.get(x) == null) {
            return -1;
        } else {
            return indexMap.get(x);
        }

    }

    // you implement this
    /**
     * This method is used to update the locations of a heap. It 
     * need to check the parent and children index relationship, and
     * try to percolate down or percolate up.
     * @param x beginning from this thing
     * @return true if practice success or false if not
     */
    public boolean update(AnyType x) {
        // O(lg n) average case
        // or O(lg n) worst case if getIndex() is guarenteed O(1)

        if (array[getIndex(x)] == null) {
            return false;
        }

        else {

            int parentIndex = getIndex(x) / 2;
            int leftChild = getIndex(x) * 2;
            int rightChild = getIndex(x) * 2 + 1;

            if (compare(array[parentIndex], x) > 0) {
                while (array[parentIndex] != null && parentIndex > 0 && compare(array[parentIndex], x) > 0) {
                    AnyType current = array[parentIndex];
                    array[parentIndex] = x;
                    array[getIndex(x)] = current;
                    parentIndex = parentIndex / 2;
                }

            }

            else if (compare(array[parentIndex], x) < 0) {
                if (leftChild > currentSize && rightChild > currentSize) {
                    array[getIndex(x)] = x;
                } else if (rightChild > currentSize) {
                    if (compare(array[leftChild], x) > 0 && leftChild < currentSize) {
                        array[getIndex(x)] = x;
                    } else {
                        percolateDown(getIndex(x));
                    }
                }
            }
            return true;
        }
        // dummy return, make sure to replace this!
    }

    /**
     * Construct an empty PriorityQueue.
     */
    @SuppressWarnings("unchecked")
    public WeissPriorityQueue() {
        indexMap = new HashMap<AnyType, Integer>();

        currentSize = 0;
        cmp = null;
        array = (AnyType[]) new Object[DEFAULT_CAPACITY + 1];
    }

    /**
     * Construct an empty PriorityQueue with a specified comparator.
     * @param c the comparator super c.
     */
    @SuppressWarnings("unchecked")
    public WeissPriorityQueue(Comparator<? super AnyType> c) {
        indexMap = new HashMap<AnyType, Integer>();

        currentSize = 0;
        cmp = c;
        array = (AnyType[]) new Object[DEFAULT_CAPACITY + 1];
    }

    /**
     * Construct a PriorityQueue from another Collection.
     * @param coll the extended collection coll.
     */
    @SuppressWarnings("unchecked")
    public WeissPriorityQueue(WeissCollection<? extends AnyType> coll) {
        indexMap = new HashMap<AnyType, Integer>();

        cmp = null;
        currentSize = coll.size();
        array = (AnyType[]) new Object[(currentSize + 2) * 11 / 10];

        int i = 1;
        for (AnyType item : coll)
            array[i++] = item;
        buildHeap();
    }

    /**
     * Compares lhs and rhs using comparator if provided by cmp, or the default
     * comparator.
     * @param lhs the first element that needed to be compare
     * @param rhs the second element that needed to be compare
     * @return compare result
     */
    @SuppressWarnings("unchecked")
    private int compare(AnyType lhs, AnyType rhs) {
        if (cmp == null)
            return ((Comparable) lhs).compareTo(rhs);
        else
            return cmp.compare(lhs, rhs);
    }

    /**
     * Adds an item to this PriorityQueue.
     * 
     * @param x any object.
     * @return true.
     */
    public boolean add(AnyType x) {
        if (currentSize + 1 == array.length)
            doubleArray();

        // Percolate up
        int hole = ++currentSize;
        array[0] = x;

        for (; compare(x, array[hole / 2]) < 0; hole /= 2) {
            array[hole] = array[hole / 2];
        }

        array[hole] = x;

        return true;
    }

    /**
     * Returns the number of items in this PriorityQueue.
     * 
     * @return the number of items in this PriorityQueue.
     */
    public int size() {
        return currentSize;
    }

    /**
     * Make this PriorityQueue empty.
     */
    public void clear() {
        currentSize = 0;
    }

    /**
     * Returns an iterator over the elements in this PriorityQueue. The iterator
     * does not view the elements in any particular order.
     * @return new iterator.
     */
    public Iterator<AnyType> iterator() {
        return new Iterator<AnyType>() {
            int current = 0;

            public boolean hasNext() {
                return current != size();
            }

            @SuppressWarnings("unchecked")
            public AnyType next() {
                if (hasNext())
                    return array[++current];
                else
                    throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Returns the smallest item in the priority queue.
     * 
     * @return the smallest item.
     * @throws NoSuchElementException if empty.
     */
    public AnyType element() {
        if (isEmpty())
            throw new NoSuchElementException();
        return array[1];
    }

    /**
     * Removes the smallest item in the priority queue.
     * 
     * @return the smallest item.
     * @throws NoSuchElementException if empty.
     */
    public AnyType remove() {
        AnyType minItem = element();
        array[1] = array[currentSize--];
        percolateDown(1);

        indexMap.remove(minItem);

        return minItem;
    }

    /**
     * Establish heap order property from an arbitrary arrangement of items. Runs in
     * linear time.
     */
    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--)
            percolateDown(i);
    }

    /**
     * the default of the size is this number.
     */
    private static final int DEFAULT_CAPACITY = 100;

    /**
     * the number of the size.
     */
    private int currentSize; // Number of elements in heap
    /**
     * the array of data type anytype.
     */
    private AnyType[] array; // The heap array
    /**
     * comparator cmp.
     */
    private Comparator<? super AnyType> cmp;

    /**
     * Internal method to percolate down in the heap.
     * 
     * @param hole the index at which the percolate begins.
     */
    private void percolateDown(int hole) {
        int child;
        AnyType tmp = array[hole];

        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            if (child != currentSize && compare(array[child + 1], array[child]) < 0)
                child++;
            if (compare(array[child], tmp) < 0) {
                array[hole] = array[child];
            } else
                break;
        }
        array[hole] = tmp;
    }

    /**
     * Internal method to extend array.
     */
    @SuppressWarnings("unchecked")
    private void doubleArray() {
        AnyType[] newArray;

        newArray = (AnyType[]) new Object[array.length * 2];
        for (int i = 0; i < array.length; i++)
            newArray[i] = array[i];
        array = newArray;
    }
}
