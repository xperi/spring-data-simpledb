package org.springframework.data.simpledb.sample.simpledb.repository.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.simpledb.sample.simpledb.domain.SimpleDbUser;
import org.springframework.data.simpledb.sample.simpledb.domain.SimpleDbUser.NestedBClass;
import org.springframework.data.simpledb.sample.simpledb.domain.SimpleDbUser.NestedBClass.NestedCClass;

public class SimpleDbUserBuilder {

    public static SimpleDbUser createUserWithSampleAttributes(String itemName) {
        SimpleDbUser user = new SimpleDbUser();
        {
            user.setItemName(itemName);
            
            user.setIntField(11);
            user.setFloatField(0.01f);
            user.setDoubleField(-0.003d);
            user.setLongField(Long.MAX_VALUE);
            user.setShortField((short) -2);
            user.setByteField((byte) 1);
            user.setStringField("tes_string$");
            user.setBooleanField(Boolean.TRUE);
            
            final NestedCClass cClass = new NestedCClass();
            {
            	cClass.setDoubleField(0.001d);
            }
            final NestedBClass bClass = new NestedBClass();
            {
            	bClass.setIntField(11);
            	bClass.setNestedNestedC(cClass);
            }
            user.setNestedB(bClass);
        }
        return user;
    }

    public static List<SimpleDbUser> createListOfItems(int length) {
        List<SimpleDbUser> list = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            String itemName = "Item_" + i;
            SimpleDbUser user = createUserWithSampleAttributes(itemName);
            list.add(user);
        }
        return list;
    }
}
