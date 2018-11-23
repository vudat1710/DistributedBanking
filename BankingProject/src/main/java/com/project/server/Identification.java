package com.project.server;


public class Identification implements Comparable<Identification> {
    private String user;
    private boolean readOnly;
    private String identification; // port or url connect

    public String getUser() {
        return user;
    }

    public String getIdentification() {
        return identification;
    }

    public Identification(String user, String identification) {
        this.user = user;
        this.identification = identification;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public int compareTo(Identification identification) {
        if (identification.getUser().equals(this.user) && identification.getIdentification().equals(this.identification)) {
            return 1;
        }
        return -1;
    }

    @Override
    public String toString() {
        return getUser() + getIdentification();
    }
}
