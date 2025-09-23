package com.project.objectes;

import java.io.Serializable;
import java.util.HashMap;

public class PR121hashmap implements Serializable {
    private static final long serialVersionUID = 1L;  // És necessari per garantir la compatibilitat en serialització.
    private HashMap<String, Integer> persones;

    public PR121hashmap() {
        persones = new HashMap<>();
    }

    public HashMap<String, Integer> getPersones() {
        return persones;
    }

    public void setPersones(HashMap<String, Integer> persones) {
        this.persones = persones;
    }
}
