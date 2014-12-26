package com.samsung.itschool.shopparserimproved;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс-builder, служащий для удобного создания экземпляров
 * {@link com.samsung.itschool.shopparserimproved.ShopTransaction}.
 *
 * @author - Pavel K {@literal <theservy@gmail.com>}
 */
public class ShopTransactionBuilder {
    private String buyerName;
    private Map<String, Integer> products = new HashMap<>();
    private int lastCost;

    /**
     * Позволяет установить имя покупателя
     *
     * @param name имя покупателя
     */
    public void setBuyerName(String name) {
        buyerName = name;
    }

    /**
     * Позволяет начать добавление продукта, указав его стоимость. После этого метода
     * должен быть вызван метод {@link #finishAddingProduct(String)}, который укажет имя
     * добавляемого продукта и завершит добавление.
     *
     * @param cost стоимость добавляемого продукта
     */
    public void startAddingProduct(int cost) {
        lastCost = cost;
    }

    /**
     * Позволяет закончить добавление продукта. Этот метод должен быть вызван после метода
     * {@link #startAddingProduct(int)}, который бы указал стоимость добавляемого продукта.
     *
     * @param name имя добавляемого продукта
     */
    public void finishAddingProduct(String name) {
        products.put(name, lastCost);
    }

    /**
     * Позволяет получить {@link com.samsung.itschool.shopparserimproved.ShopTransaction}
     * из данного "строителя"
     *
     * @return результирующая транзакция
     */
    public ShopTransaction toShopTransaction() {
        return new ShopTransaction(buyerName, products);
    }
}
