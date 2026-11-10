package org.example;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class Testiki {

    @Test
    public void testEqualsAndHashCode() {
        Product p1 = new Product("Периферия", "Logitech", "Мышь M330", 1890);
        Product p2 = new Product("Другая категория", "Logitech", "Мышь M330", 9999);
        Product p3 = new Product("Периферия", "Logitech", "Другая мышь", 1890);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, p3);
    }

    @Test
    public void testUniqueFilteringAndOrder() {
        List<Product> data = Main.getInitialData();
        Set<Product> set = new LinkedHashSet<>(data);
        List<Product> list = new ArrayList<>(set);

        assertEquals(8, list.size());
        assertEquals("Logitech", list.get(0).getVendor());
        assertEquals("Samsung", list.get(1).getVendor());
    }

    @Test
    public void testCategoryGrouping() {
        List<Product> data = new ArrayList<>(new LinkedHashSet<>(Main.getInitialData()));
        Map<String, List<Product>> map = new TreeMap<>();
        for (Product p : data) {
            map.computeIfAbsent(p.getCategory(), k -> new ArrayList<>()).add(p);
        }

        assertEquals(4, map.size());
        assertTrue(map.containsKey("Кабели"));
        assertEquals(2, map.get("Периферия").size());
    }

    @Test
    public void testCompositeSort() {
        Product p1 = new Product("A", "Logitech", "Б Мышь", 1890);
        Product p2 = new Product("A", "Logitech", "А Мышь", 1890);
        Product p3 = new Product("A", "Apple", "Мышь", 1890);
        Product p4 = new Product("A", "Apple", "Мышь", 500);

        List<Product> list = Arrays.asList(p1, p2, p3, p4);
        list.sort(Comparator.comparingInt(Product::getPrice)
                .thenComparing(Product::getVendor)
                .thenComparing(Product::getName));

        assertSame(p4, list.get(0));
        assertSame(p3, list.get(1));
        assertSame(p2, list.get(2));
        assertSame(p1, list.get(3));
    }

    @Test
    public void testMinMaxSearch() {
        List<Product> data = Main.getInitialData();
        Comparator<Product> productComparator = Comparator.comparingInt(Product::getPrice)
                .thenComparing(Product::getVendor)
                .thenComparing(Product::getName);

        Product min = Collections.min(data, productComparator);
        Product max = Collections.max(data, productComparator);

        assertEquals(490, min.getPrice());
        assertEquals("Ugreen", min.getVendor());
        assertEquals(24900, max.getPrice());
        assertEquals("LG", max.getVendor());
    }

    @Test
    public void testLogitechFrequency() {
        List<Product> data = new ArrayList<>(new LinkedHashSet<>(Main.getInitialData()));
        int count = 0;
        for (Product p : data) {
            if ("Logitech".equals(p.getVendor())) {
                count++;
            }
        }
        assertEquals(2, count);
    }

    @Test
    public void testUnmodifiableListProtection() {
        List<Product> list = new ArrayList<>(new LinkedHashSet<>(Main.getInitialData()));
        List<Product> unmodifiable = Collections.unmodifiableList(list);

        assertThrows(UnsupportedOperationException.class, () -> {
            unmodifiable.add(new Product("T", "T", "T", 100));
        });
    }

    @Test
    public void testRemoveIfAndMergeCount() {
        List<Product> list = new ArrayList<>(new LinkedHashSet<>(Main.getInitialData()));
        list.removeIf(p -> p.getPrice() < 1000);

        Map<String, Integer> map = new TreeMap<>();
        for (Product p : list) {
            map.merge(p.getVendor(), 1, Integer::sum);
        }

        assertFalse(map.containsKey("Kingston"));
        assertEquals(2, map.get("Logitech"));
        assertEquals(1, map.get("Ugreen"));
        assertEquals(6, list.size());
    }
}
