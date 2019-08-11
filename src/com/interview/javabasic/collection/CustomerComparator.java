package com.interview.javabasic.collection;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class CustomerComparator implements Comparator<Customer> {
    /**
     * 为何叫客户化排序，其实和自然排序的编写逻辑不会差太远，但是
     * 更为灵活，因为在TreeSet初始化中是可以带参传入的
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Customer o1, Customer o2) {
        if (o1.getName().compareTo(o2.getName()) > 0) {
            return -1;
        }

        if (o1.getName().compareTo(o2.getName()) < 0) {
            return 1;
        }

        return 0;
    }

    public static void main(String[] args) {
        Set<Customer> set = new TreeSet<Customer>(new CustomerComparator());

        Customer customer1 = new Customer("Tom", 5);
        Customer customer2 = new Customer("Tom", 9);
        Customer customer3 = new Customer("Tom", 2);
        set.add(customer1);
        set.add(customer2);
        set.add(customer3);
        // 下面的。类似，由于TreeSet特性，优先使用了比较构造器的compare，而直接忽视了compareTo
        // 因此Tom都一致，返回0, 去重了
        Iterator<Customer> it = set.iterator();
        while (it.hasNext()) {
            Customer customer = it.next();
            System.out.println(customer.getName() + " " + customer.getAge());
        }
    }
}
