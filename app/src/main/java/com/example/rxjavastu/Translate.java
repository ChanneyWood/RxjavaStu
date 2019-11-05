package com.example.rxjavastu;

public class Translate {
    private int status;
    private Content content;

    @Override
    public String toString() {
        return "Translate{" +
                "status=" + status +
                ", content=" + content +
                '}';
    }

    static class Content{
        private String out;

        @Override
        public String toString() {
            return "Content{" +
                    "out='" + out + '\'' +
                    '}';
        }
    }
}
