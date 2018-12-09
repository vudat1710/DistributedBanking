package com.project.server;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MessageQueue {

    private static HashMap<String, ArrayList<Identification>> mQueue = new HashMap<String, ArrayList<Identification>>();

    public static boolean addMQueue(Identification identification) {
        if (mQueue.containsKey(identification.getUser())) {
            mQueue.get(identification.getUser()).add(identification);
            for (Identification i : mQueue.get(identification.getUser())) {
                if (!i.isReadOnly() && i.compareTo(identification) == -1) {
                    identification.setReadOnly(true);
                }
            }
            return identification.isReadOnly();
        } else {
            ArrayList<Identification> queue = new ArrayList<Identification>();
            identification.setReadOnly(false);
            queue.add(identification);
            mQueue.put(identification.getUser(), queue);
        }
        System.out.println("Queue " + identification.getUser() + " has " + mQueue.get(identification.getUser()).size() + " access:" + identification.isReadOnly());
        return identification.isReadOnly();
    }

    public static void removeQueue(Identification identification) {
        boolean hasKey = false;
        System.out.println(identification.getUser());

        Iterator<Identification> iter = mQueue.get(identification.getUser()).iterator();
        while (iter.hasNext()) {
            Identification i = iter.next();
            if (i.compareTo(identification) == 1) iter.remove();
        }
        System.out.println("queue size: " + mQueue.get(identification.getUser()).size());
        if (mQueue.get(identification.getUser()).size() != 0) {
            for (Identification i : mQueue.get(identification.getUser())) {
                if (!i.isReadOnly()) hasKey = true;
            }
            if (!hasKey) mQueue.get(identification.getUser()).get(0).setReadOnly(false);
        }
    }

}
