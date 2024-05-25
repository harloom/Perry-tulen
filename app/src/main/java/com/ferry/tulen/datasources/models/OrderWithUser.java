package com.ferry.tulen.datasources.models;

public class OrderWithUser {
    private  Order order;
    private  UserWithIdDocument user;
    private  WorkMainWithIdDocument workMan;

    public OrderWithUser() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public UserWithIdDocument getUser() {
        return user;
    }

    public void setUser(UserWithIdDocument user) {
        this.user = user;
    }

    public WorkMainWithIdDocument getWorkMan() {
        return workMan;
    }

    public void setWorkMan(WorkMainWithIdDocument workMan) {
        this.workMan = workMan;
    }

    @Override
    public String toString() {
        return "OrderWithUser{" +
                "order=" + order +
                ", user=" + user.getUser().getFullName() +
                ", workMan=" + workMan.getWorkMan().getFullName() +
                '}';
    }
}
