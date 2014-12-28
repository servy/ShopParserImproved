package ru.samsung.itschool.shopparserimproved;

import java.io.*;

/**
 * Данный enum представляет собой список всех состояний конечного автомата для разбора строк вида
 *
 * Ivan Ivanov | 359 "apples", 90 "coffee", 30 "legumes (peas, beans, peanuts)".
 *
 * и вывода частей таких строк в виде отдельных строк, разделенных переносом строки. Каждое
 * состояние имеет свою реализацию метода process для генерации выхода и вычисления нового
 * состояния.
 *
 * Также, данный enum содержит метод main, который содержит демонстрацию использования
 * данного конечного автомата на основе чтения входного потока и генерации выходного потока.
 *
 * @author - Pavel K {@literal <theservy@gmail.com>}
 */
public enum State {

    /**
     * Состояние, соответствующее чтению имени покупателя - начальное состояние
     */
    READING_NAME {
        @Override
        public State process(Reader input, ShopPurchaseBuilder output) throws IOException {
            // пропустим возможные стартовые пробелы
            int currentChar = input.read();
            while (currentChar == ' ')
                currentChar = input.read();

            // здесь мы будем собирать имя покупателя
            StringBuilder name = new StringBuilder();
            while (true) {
                switch (currentChar) {
                    case '|':   // мы нашли разделитель - вертикальную черту
                        output.setBuyerName(name.toString().trim()); // мы закончили собирать имя покупателя
                        return READING_COST;
                    case -1:   // у нас закончилась строка
                        throw new IllegalStateException("Ожидал разделитель '|', но нашел конец строки. Строка неправильного формата.");
                    default: // для всех остальных символов - добавляем их в имя
                        name.append((char) currentChar);
                }
                currentChar = input.read(); // читаем следующий символ
            }
        }
    },

    /**
     * Состояние, соответствующее чтению стоимости покупки
     */
    READING_COST {
        @Override
        public State process(Reader input, ShopPurchaseBuilder output) throws IOException {
            // пропустим возможные стартовые пробелы
            int currentChar = input.read();
            while (currentChar == ' ')
                currentChar = input.read();

            // здесь мы будем собирать число
            StringBuilder costStr = new StringBuilder();
            while (true) {
                switch (currentChar) {
                    case '"':  // если мы нашли разделитель (кавычку)
                        int cost = Integer.valueOf(costStr.toString().trim()); // преобразуем строковое представление числа в int
                        output.startAddingProduct(cost); // и запоминаем это число
                        return READING_PRODUCT;
                    case -1:  // у нас закончилась строка
                        throw new IllegalStateException("Ожидал разделитель '\"', но нашел конец строки. Строка неправильного формата.");
                    default:  // для всех остальных символов - добавляем их в стоимость
                        costStr.append((char)currentChar);
                }
                currentChar = input.read(); // читаем следующий символ
            }
        }
    },

    /**
     * Состояние, соответствующее чтению названия товара
     */
    READING_PRODUCT {
        @Override
        public State process(Reader input, ShopPurchaseBuilder output) throws IOException {
            // мы не пропускаем возможные стартовые пробелы, так как внутри кавычек их не должно быть (или они являются
            // частью названия товара)
            StringBuilder productName = new StringBuilder();
            while (true) {
                int currentChar = input.read(); // читаем очередной символ
                switch (currentChar) {
                    case '"': // если мы нашли разделитель (кавычку)
                        output.finishAddingProduct(productName.toString()); // мы закончили собирать название товара
                        return READING_DELIMITER;
                    case -1: // у нас закончилась строка
                        throw new IllegalStateException("Ожидал разделитель '\"', но нашел конец строки. Строка неправильного формата.");
                    default: // для всех остальных символов - добавляем их в имя товара
                        productName.append((char)currentChar);
                }
            }
        }
    },

    /**
     * Состояние, соответствующее чтению разделителя между элементами списка (запятой) или
     * признака окончания списка (точкой).
     */
    READING_DELIMITER {
        @Override
        public State process(Reader input, ShopPurchaseBuilder output) throws IOException {
            while (true) {
                int currentChar = input.read(); // читаем очередной символ
                switch (currentChar) {
                    case '.':              // если мы нашли разделитель: точку
                        return FINISHED;     // наш конечный автомат закончил свою работу - переходим
                                             // в состояние завершения
                    case ',':              // если же мы нашли разделитель: запятую
                        return READING_COST; // значит у нас есть еще один элемент в списке - переходим в
                                             // состояние чтения стоимости
                    case -1: // у нас закончилась строка
                        throw new IllegalStateException("Ожидал разделитель ',' или '.', но нашел конец строки. Строка неправильного формата.");
                    case ' ':
                        break; // пробелы мы просто пропускаем, ничего не делая
                    default: // любой другой символ в текущем состоянии - ошибка
                        throw new IllegalStateException(String.format("Ожидаю разделитель - точку, запятую или пробел, но нашел символ %c. " +
                                "Строка не соответствует формату.", (char)currentChar));
                }
            }
        }
    },

    /**
     * Состояние, соответствующее окончанию работы конечного автомата
     */
    FINISHED {
        @Override
        public State process(Reader input, ShopPurchaseBuilder output) throws IOException {
            while (true) {
                int currentChar = input.read(); // читаем очередной символ
                switch (currentChar) {
                    case ' ':  // пробелы и символы конца строки мы просто пропускаем
                    case '\n':
                    case '\r':
                        break;
                    case -1:   // мы дошли до конца строки
                        return  FINISHED;
                    default:   // любой другой символ в текущем состоянии - ошибка
                        throw new IllegalStateException(String.format("После конечной точки допустимы только пробелы " +
                                "и символ конца строки, но найден символ %c. Строка не соответствует формату.",
                                (char)currentChar));
                }
            }
        }
    };

    /**
     * Читает некоторое количество символов из input и возвращает новое
     * состояние конечного автомата на основании прочитанных символов; пишет
     * необходимые выходные данные в output.
     *
     * @param input  источник символьных данных
     * @param output объект для хранения промежуточной генерируемой конечным автоматом информации
     * @return новое состояние конечного автомата
     */
    public abstract State process(Reader input, ShopPurchaseBuilder output) throws IOException;

    /**
     * Реализация основного метода программы для демонстрации работы конечного автомата
     * @param args параметры командной строки; данная реализация их игнорирует
     */
    public static void main(String[] args) throws IOException {
        BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));

        String inputLine = stdinReader.readLine(); // читаем только первую введенную строку
        StringReader inputStringReader = new StringReader(inputLine);

        // сюда мы будем собирать результат работы конечного автомата
        ShopPurchaseBuilder output = new ShopPurchaseBuilder();

        // текущее состояние конечного автомата: изначально это чтение имени покупателя
        State currentState = State.READING_NAME;
        while (currentState != State.FINISHED) {
            currentState = currentState.process(inputStringReader, output);
        }

        ShopPurchase purchase = output.toShopPurchase();
        // выведем результат; здесь мы могли бы написать какую-нибудь логику работы с purchase
        // если бы нам это было нужно
        System.out.print(purchase);
    }
}
