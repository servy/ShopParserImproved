package com.samsung.itschool.shopparserimproved;

import java.util.*;

/**
 * Неизменяемый (immutable) класс, который хранит данные об одной транзакции магазина - имя покупателя,
 * и набор пар "имя продукта" - стоимость.
 *
 * @author - Pavel K {@literal <theservy@gmail.com>}
 */
public class ShopTransaction {
    private String buyerName;
    private SortedMap<String, Integer> products;

    /**
     * Позволяет создать новый объект ShopTransaction
     *
     * @param buyerName имя покупателя
     * @param products  информация о списке покупок; подробности см. в джавадоке к методу {@link #getProducts()}
     */
    public ShopTransaction(String buyerName, Map<String, Integer> products) {
        this.buyerName = buyerName;
        // мы создаем немодифицируемую копию map'а, чтобы гарантировать, что после
        // вызова конструктора экземпляр нашего класса невозможно было (случайно или намеренно) изменить
        this.products = Collections.unmodifiableSortedMap(new TreeMap<>(products));
    }

    /**
     * Возвращаем имя покупателя, который совершил данную транзакцию
     *
     * @return имя покупателя
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     * Возвращает map, который каждому названию продукта (ключу типа String)
     * сопоставляет количество денег (значение типа Integer), которые были
     * потрачены на этот продукт в рамках этой транзакции
     *
     * @return информацию о покупках в этой транзакции
     */
    public Map<String, Integer> getProducts() {
        return products;
    }

    /**
     * Возвращает информацию о транзакции в виде строки следующего вида
     *
     * <pre>
     * Buyer Name
     * Product #1 cost
     * Product #1 name
     * Product #2 cost
     * Product #2 name
     * </pre>
     *
     * и так далее для всех товаров. Товары выводятся в алфавитном порядке
     *
     * @return строковое представление данной транзакции
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(buyerName).append("\n");
        for (Map.Entry<String, Integer> entry: products.entrySet()) {
            result.append(entry.getValue()).append("\n");
            result.append(entry.getKey()).append("\n");
        }
        return result.toString();
    }
}
