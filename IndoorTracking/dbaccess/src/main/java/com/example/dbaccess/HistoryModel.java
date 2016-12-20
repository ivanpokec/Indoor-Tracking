package com.example.dbaccess;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Paula on 19.12.2016..
 */

public class HistoryModel {

    public static List<HistoryModel> HistoryModelList = new List<HistoryModel>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<HistoryModel> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(HistoryModel historyModel) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends HistoryModel> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends HistoryModel> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public HistoryModel get(int index) {
            return null;
        }

        @Override
        public HistoryModel set(int index, HistoryModel element) {
            return null;
        }

        @Override
        public void add(int index, HistoryModel element) {

        }

        @Override
        public HistoryModel remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<HistoryModel> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<HistoryModel> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<HistoryModel> subList(int fromIndex, int toIndex) {
            return null;
        }
    };

    String roomName;
    String time;
    String description;
    String userNameLastName;


    public HistoryModel(String description, String roomName, String time, String userNameLastName) {
        this.roomName = roomName;
        this.time = time;
        this.description = description;
        this.userNameLastName = userNameLastName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserNameLastName() {
        return userNameLastName;
    }

    public void setUserNameLastName(String userNameLastName) {
        this.userNameLastName = userNameLastName;
    }
}
