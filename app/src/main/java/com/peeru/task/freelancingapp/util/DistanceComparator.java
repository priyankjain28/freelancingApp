package com.peeru.task.freelancingapp.util;

import com.peeru.task.freelancingapp.data.model.Task;

import java.util.Comparator;

/**
 * Created by Priyank Jain on 08-10-2018.
 */
public class DistanceComparator  implements Comparator<Task> {
    public int compare(Task item1, Task item2) {
        Double l = item1.getDistance() - item1.getDistance();
        return l.intValue();
    }
}
