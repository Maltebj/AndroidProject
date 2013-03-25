package com.example.AndroidProject;

/**
 * Created with IntelliJ IDEA.
 * User: tryggvim
 * Date: 24.3.2013
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
public class PuzzleNames {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PuzzleNames(String id, String name) {

        this.id = id;
        this.name = name;
    }

    private String id;
    private String name;
}
