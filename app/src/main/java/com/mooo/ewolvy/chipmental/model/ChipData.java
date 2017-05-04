package com.mooo.ewolvy.chipmental.model;

import java.util.ArrayList;
import java.util.List;

public class ChipData {
    private static final String[] limiting_thoughts = {"limit 1", "limit 2", "limit 3"};
    private static final String[] growing_thoughts = {"grow 1", "grow 2", "grow 3"};

    public static List<ListItem> getListData(){
        List<ListItem> data = new ArrayList<>();

        // Dummy data for testing purposes
        for (int x = 0; x < 4; x++){
            for (int i = 0; i < limiting_thoughts.length && i < growing_thoughts.length; i++){
                ListItem item = new ListItem();
                item.setLimiting(limiting_thoughts[i]);
                item.setGrowing(growing_thoughts[i]);
                data.add(item);
            }
        }

        return data;
    }
}
