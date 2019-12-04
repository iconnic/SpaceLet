package com.artbycode.spacelet;

import com.artbycode.spacelet.Data.Section;
import com.artbycode.spacelet.Data.Space;
import com.artbycode.spacelet.Data.User;

import java.util.ArrayList;
import java.util.HashMap;

public class SingletonSession {
    private static SingletonSession instance;
    private static Section section = new Section();
    private static Space space = new Space();
    private static User user = new User();
    private static ArrayList<Space> combinedSpacesBackup = new ArrayList<>();

    private SingletonSession(){}

    public static SingletonSession Instance(){if (instance == null){instance = new SingletonSession();} return instance;}

    public static Section getSection() {
        return section;
    }

    public static void setSection(Section section) {
        SingletonSession.section = section;
    }

    public static Space getSpace() {
        return space;
    }

    public static void setSpace(Space space) {
        SingletonSession.space = space;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        SingletonSession.user = user;
    }

    public static ArrayList<Space> getCombinedSpacesBackup() {return combinedSpacesBackup;}

    public static void setCombinedSpacesBackup(ArrayList<Space> combinedSpacesBackup) {SingletonSession.combinedSpacesBackup = combinedSpacesBackup;}
}
