package com.peeru.task.freelancingapp.util;

import com.peeru.task.freelancingapp.data.model.Task;

import java.util.Comparator;

public class DateComparator implements Comparator<Task> {
    public int compare(Task item1, Task item2) {
        Long l = Long.valueOf(item2.getCreatedDate())-Long.valueOf(item1.getCreatedDate());
        return l.intValue();
    }
}