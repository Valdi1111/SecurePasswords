package org.valdi.securepasswords.core.objects;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Iterator;

public class Password implements Iterable<Password.Element> {
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String link;
    @Expose
    private java.util.List<Element> elements;

    public Password() {
        this.elements = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    /*public List<Element> getElements() {
        return elements;
    }*/

    public void addElement(Element element) {
        this.elements.add(element);
    }

    public void removeElement(Element element) {
        this.elements.remove(element);
    }

    @Override
    public Iterator<Element> iterator() {
        return this.elements.iterator();
    }

    public int size() {
        return elements.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;

        Password password = (Password) o;

        return id == password.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Password{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", elements=" + elements +
                '}';
    }

    public static class Element {
        @Expose
        private String name;
        @Expose
        private String value;
        @Expose
        private boolean hide;

        public Element() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isHide() {
            return hide;
        }

        public void setHide(boolean hide) {
            this.hide = hide;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Element)) return false;

            Element element = (Element) o;

            return name != null ? name.equals(element.name) : element.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Element{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    ", hide=" + hide +
                    '}';
        }

    }

    public static class List extends ArrayList<Password> {

    }

}
