package com.samsung.itschool.shopparserimproved;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс-builder, служащий для удобного создания экземпляров
 * {@link ShopPurchase}.
 *
 * @author - Pavel K {@literal <theservy@gmail.com>}
 */
public class ShopPurchaseBuilder {
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
     * Позволяет начать добавление товара, указав его стоимость. После этого метода
     * должен быть вызван метод {@link #finishAddingProduct(String)}, который укажет название
     * добавляемого товара и завершит добавление.
     *
     * @param cost стоимость добавляемого товара
     */
    public void startAddingProduct(int cost) {
        lastCost = cost;
    }

    /**
     * Позволяет закончить добавление товара. Этот метод должен быть вызван после метода
     * {@link #startAddingProduct(int)}, который бы указал стоимость добавляемого товара.
     *
     * @param name название добавляемого товара
     */
    public void finishAddingProduct(String name) {
        products.put(name, lastCost);
    }

    /**
     * Позволяет получить {@link ShopPurchase}
     * из данного "строителя"
     *
     * @return сформированная покупка
     */
    public ShopPurchase toShopPurchase() {
        return new ShopPurchase(buyerName, products);
    }
}
