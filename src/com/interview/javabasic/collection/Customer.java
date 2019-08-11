package com.interview.javabasic.collection;

import java.util.Set;
import java.util.TreeSet;

public class Customer implements Comparable {
    private String name;

    private int age;

    public Customer(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int compareTo(Object o) {
        Customer other = (Customer) o;

        // 先按照name进行排序，注意里面比较属性也只能再调用属性的compareTo进行比较了，
        // 属性如果字符串，则是按照自然排序来了，因为String里自己重写实现的compareTo就是按照自然排序来
        // 所以compareTo才是一般叫做自然排序
        if (this.name.compareTo(other.getName()) > 0) {
            return 1;
        }
        if (this.name.compareTo(other.getName()) < 0) {
            return -1;
        }

        // 再按照age属性排序
        if (this.age > other.getAge()) {
            return 1;
        }
        if (this.age < other.getAge()) {
            return -1;
        }
        return 0;
    }

    /**
     * 虽然TreeMap中equals和hashCode并无用，但对于HashMap是非常有用，因此对于key要放对象，就最好保持良好习惯实现
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Customer)) {
            return false;
        }
        final Customer other = (Customer) obj;

        if (this.name.equals(other.getName()) && this.age == other.getAge()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 配合equals必须重写实现，规则虽然可以自定义，但也是要根据name和age来
     * @return
     */
    @Override
    public int hashCode() {
        int result;
        result = (name == null ? 0 : name.hashCode());
        result = 29 * result + age;
        return result;
    }

    public static void main(String[] args) {
        // 注意以下的set，如果age都为15，则用compareTo比较的时候，name和age都为相等了是重复元素
        // 因此第二次add的时候，Customer的compareTo直接返回0，表示相同重复元素，并无用到equals和hashCode
        // set的规范和特性是一定会保证唯一，hashSet的唯一去重实现是依赖于equals和hashCode
        // 而TreeSet的唯一去重实现是依赖于compare或者compareTo，0表示相同重复
        // Set<Customer> set = new TreeSet<Customer>();
        // Customer customer1 = new Customer("Tom", 15);
        // Customer customer2 = new Customer("Tom", 15);
        // set.add(customer1);
        // set.add(customer2);
        // // System.out.println(set);
        // for (Customer c : set) {
        //     System.out.println(c.name + " " + c.age);
        // }

        // TreeSet相比于Set去重唯一的另外一个特点是排序，也是根据compare或CompareTo的返回正数或负数决定
        Set<Customer> set = new TreeSet<Customer>();
        Customer customer1 = new Customer("Tom", 16);
        Customer customer2 = new Customer("Tom", 15);
        set.add(customer1);
        set.add(customer2);
        // System.out.println(set);
        for (Customer c : set) {
            System.out.println(c.name + " " + c.age);
        }
    }
}
