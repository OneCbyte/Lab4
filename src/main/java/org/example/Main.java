package org.example;

import java.util.*;

public class Main {

    public static List<Product> getInitialData() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("Периферия", "Logitech", "Мышь M330", 1890));
        list.add(new Product("Накопители", "Samsung", "SSD 980 500GB", 4590));
        list.add(new Product("Периферия", "Logitech", "Клавиатура K380", 1890));
        list.add(new Product("Кабели", "Ugreen", "Кабель USB-C 1 м", 490));
        list.add(new Product("Накопители", "Samsung", "SSD 980 500GB", 4590));
        list.add(new Product("Мониторы", "LG", "Монитор 24MP60", 11900));
        list.add(new Product("Кабели", "Ugreen", "Хаб USB 4 порта", 1890));
        list.add(new Product("Периферия", "Logitech", "Мышь M330", 1890));
        list.add(new Product("Накопители", "Kingston", "Флешка DT50 64GB", 790));
        list.add(new Product("Мониторы", "LG", "Монитор 27QN880", 24900));
        return list;
    }

    public static void main(String[] args) {
        System.out.println("Karuchkin prpp2");

        /*
         Обоснование выбора коллекций:
         Для хранения исходной накладной выбран ArrayList, так как он сохраняет порядок добавления.
         Для отсечения дубликатов с сохранением порядка первого упоминания применен LinkedHashSet
         Для группировки товаров по категориям и финального подсчета остатков использован
         TreeMap, поскольку его структура автоматически сортирует ключи в алфавитном порядке,
         что полностью удовлетворяет условиям задачи.
        */

        List<Product> invoice = getInitialData();
        System.out.println("1) Строк в накладной: " + invoice.size());

        Set<Product> uniqueSet = new LinkedHashSet<>(invoice);
        List<Product> uniqueList = new ArrayList<>(uniqueSet);
        System.out.println("2) Уникальных позиций: " + uniqueList.size() + "; повторов отброшено: " + (invoice.size()
                - uniqueSet.size()));

        Map<String, List<Product>> categoryMap = new TreeMap<>();
        for (Product p : uniqueList) {
            categoryMap.computeIfAbsent(p.getCategory(), k -> new ArrayList<>()).add(p);
        }
        System.out.println("3) Категорий: " + categoryMap.size());
        for (Map.Entry<String, List<Product>> entry : categoryMap.entrySet()) {
            System.out.print("   " + entry.getKey() + " (" + entry.getValue().size() + "): ");
            StringJoiner sj = new StringJoiner(", ");
            for (Product p : entry.getValue()) {
                sj.add(p.getName());
            }
            System.out.println(sj);
        }

        Comparator<Product> productComparator = Comparator.comparingInt(Product::getPrice)
                .thenComparing(Product::getVendor)
                .thenComparing(Product::getName);

        uniqueList.sort(productComparator);

        System.out.println("4) Прайс-лист:");
        for (Product p : uniqueList) {
            System.out.println("   " + p.getPrice() + "  " + p.getVendor() + " — " + p.getName());
        }

        Product minPriceProduct = Collections.min(uniqueList, productComparator);
        Product maxPriceProduct = Collections.max(uniqueList, productComparator);
        System.out.println("5) Самая дешёвая: " + minPriceProduct.getVendor() + " — " +
                minPriceProduct.getName() + " (" + minPriceProduct.getPrice() + ")");
        System.out.println("   Самая дорогая:  " + maxPriceProduct.getVendor() + " — " +
                maxPriceProduct.getName() + " (" + maxPriceProduct.getPrice() + ")");

        Product logitechTarget = new Product("", "Logitech", "Мышь M330", 0);
        int logitechCount = 0;
        for (Product p : uniqueList) {
            if ("Logitech".equals(p.getVendor())) {
                logitechCount++;
            }
        }
        System.out.println("6) Позиций Logitech: " + logitechCount);


        System.out.print("7) Попытка добавить в защищённый список: ");
        List<Product> unmodifiable = Collections.unmodifiableList(uniqueList);
        try {
            unmodifiable.add(new Product("Тест", "Тест", "Тест", 0));
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getClass().getSimpleName());
        }

        int sizeBefore = uniqueList.size();
        uniqueList.removeIf(p -> p.getPrice() < 1000);
        int removedCount = sizeBefore - uniqueList.size();
        System.out.println("8) Дешевле 1000 руб. — убрано: " + removedCount + "; осталось: " + uniqueList.size());

        Map<String, Integer> vendorCountMap = new TreeMap<>();
        for (Product p : uniqueList) {
            vendorCountMap.merge(p.getVendor(), 1, Integer::sum);
        }
        System.out.println("9) Осталось по поставщикам: " + vendorCountMap);
    }
}
