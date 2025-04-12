import com.squareup.moshi.*;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


public class Main {
    private static String MEM_CHACHED_PORT = "11211";
    private static String MEM_CHACHED_HOST = "localhost";

    private static String MEM_CHACHED_INST_NAME = "Lab4Alexandra";

    public static void main(String[] args) throws ParseException, IOException {
        System.out.print("\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n");
        createInstance();
        MemCachedClient client = getMemChachedClient();
        JsonAdapter<Object> serializer = getJsonSerializer(Object.class);

        //Демонстрация работы со строкой (класс String)
        System.out.println("Демонстрация работы со строкой (класс String):");
        String stringValue = "А.С. Пушкин Капитанская дочка";
        String strKey = "bookKey";
        client.add(strKey, serializer.toJson(stringValue));
        printValueFromMemChached(strKey, client.get(strKey));

        //Демонстрация работы с числом (тип int)
        System.out.println("Демонстрация работы с числом (int):");
        int intValue = 5101243;
        String intKey = "intKey";
        client.add(intKey, serializer.toJson(intValue));
        printValueFromMemChached(intKey, client.get(intKey));

        //Демонстрация работы с массивом байтов (тип byte[])
        System.out.println("Демонстрация работы с массивом байтов (тип byte[]):");
        byte[] byteArr = {73, 12, 11, 32, 126, 13, 11};
        String byteKey = "byteKey";
        client.add(byteKey, serializer.toJson(byteArr));
        printValueFromMemChached(byteKey,client.get(byteKey));

        //Демонстрация работы со списком (класс ArrayList)
        System.out.println("Демонстрация работы со списком (класс ArrayList):");
        List<Object> list = new ArrayList<>();
        list.add(1);
        list.add("Java");
        list.add(3.14);
        String listKey = "listKey";
        client.add(listKey, serializer.toJson(list));
       printValueFromMemChached(listKey,client.get(listKey));

        //Демонстрация работы с множеством (класс HashSet)
        System.out.println("Демонстрация работы с множеством (класс HashSet):");
        Set<Object> set = new HashSet<>();
        set.add(12);
        set.add(171.11);
        set.add("Java");
        String setKey = "setKey";
        client.add(setKey, serializer.toJson(set));
        printValueFromMemChached(setKey, client.get(setKey));

        //Демонстрация работы с словарем (хеш-таблицей) (класс HashMap)
        System.out.println("Демонстрация работы с словарем (хеш-таблицей) (класс HashMap):");
        Map<String, Object> map = new HashMap<>();
        map.put("key-1", "Java");
        map.put("key-2", 0.002);
        map.put("key-3", "Python");
        map.put("key-4", 111.23);
        String mapKey = "mapKey";
        client.add(mapKey, serializer.toJson(map));
        printValueFromMemChached(mapKey,client.get(mapKey));

        //Демонстрация работы с пользовательским классом (класс Account)
        System.out.println("Демонстрация работы с пользовательским классом (класс Account):");
        String accountKey = "alexandarAccount";
        Account account = new Account("Alexandra", "Ismailova", 20, new Date(2003, 1, 26), true);
        Moshi moshiWithDate = new Moshi.Builder()
                .add(Date.class, new Rfc3339DateJsonAdapter())
                .build();
        JsonAdapter<Account> jsonAdapter = moshiWithDate.adapter(Account.class);
        String json2 = jsonAdapter.toJson(account);
        client.add(accountKey, json2);
        Account accFromMemChached = jsonAdapter.fromJson(client.get(accountKey).toString());
        printValueFromMemChached(accountKey,accFromMemChached);

    }

    public static void printValueFromMemChached(String key, Object value) {
        System.out.println("Ключ: '" + key + "' + Значения: " + value);
        System.out.println();
    }

    public static void createInstance() {
        String[] servers = {MEM_CHACHED_HOST + ":" + MEM_CHACHED_PORT};
        SockIOPool crunchyPool = SockIOPool.getInstance(MEM_CHACHED_INST_NAME);
        crunchyPool.setServers(servers);
        crunchyPool.initialize();
    }

    public static MemCachedClient getMemChachedClient() {
        MemCachedClient client = new MemCachedClient(MEM_CHACHED_INST_NAME);
        return client;
    }

    public static JsonAdapter getJsonSerializer(Class className) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter serializer = moshi.adapter(className);
        return serializer;
    }


    public static class Account {
        private String name;
        private String surname;
        private Integer age;
        private Date birthday;
        private Boolean isActive;

        public Account(String name, String surname, Integer age, Date birthday, Boolean isActive) {
            this.name = name;
            this.surname = surname;
            this.age = age;
            this.birthday = birthday;
            this.isActive = isActive;
        }


        @Override
        public String toString() {
            return "Name = '" + name + '\'' +
                    ", surname = '" + surname + '\'' +
                    ", age = " + age +
                    ", birthday = " + birthday +
                    ", isActive = " + isActive +
                    '}';
        }
    }

}

