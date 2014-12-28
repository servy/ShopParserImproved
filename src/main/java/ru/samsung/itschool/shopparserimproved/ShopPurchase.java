package ru.samsung.itschool.shopparserimproved;

import java.util.*;

/**
 * Неизменяемый (immutable) класс, который хранит данные об одной покупке в магазине - имя покупателя,
 * и набор пар "название товара" - стоимость.
 *
 * @author - Pavel K {@literal <theservy@gmail.com>}
 */
public class ShopPurchase {
    private String buyerName;
    private SortedMap<String, Integer> products;

    /**
     * Позволяет создать новый объект ShopPurchase
     *
     * @param buyerName имя покупателя
     * @param products  информация о списке покупок; подробности см. в джавадоке к методу {@link #getProducts()}
     */
    public ShopPurchase(String buyerName, Map<String, Integer> products) {
        this.buyerName = buyerName;
        // мы создаем немодифицируемую копию map'а, чтобы гарантировать, что после
        // вызова конструктора экземпляр нашего класса невозможно было (случайно или намеренно) изменить
        this.products = Collections.unmodifiableSortedMap(new TreeMap<>(products));
    }

    /**
     * Возвращаем имя покупателя, который совершил данную покупку
     *
     * @return имя покупателя
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     * Возвращает map, который каждому названию товара (ключу типа String)
     * сопоставляет количество денег (значение типа Integer), которые были
     * потрачены на этот товар в рамках этой покупки
     *
     * @return информацию о товарах в этой покупки
     */
    public Map<String, Integer> getProducts() {
        return products;
    }

    /**
     * Возвращает информацию о покупке в виде строки следующего вида
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
     * @return строковое представление данной покупки
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
