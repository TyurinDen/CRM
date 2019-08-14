package com.ewp.crm.models;

import javax.persistence.*;

@Entity
@Table(name = "user_routes")
public class UserRoutes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_routes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "weight")
    private Integer weight;   // weight of the route in percent 0-100

    @Column(name = "userRouteType")
    @Enumerated(EnumType.STRING)
    private userRouteType userRouteType;

    public enum userRouteType {
        FROM_JM_EMAIL,     // канал с которого
        FROM_VK         //поступают заявки
    }

    public UserRoutes() {
    }

    public UserRoutes(UserRoutes userRoutes, User user, Integer weight, UserRoutes.userRouteType userRouteType) {
        this.user = user;
        this.weight = weight;
        this.userRouteType = userRouteType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public UserRoutes.userRouteType getUserRouteType() {
        return userRouteType;
    }

    public void setUserRouteType(UserRoutes.userRouteType userRouteType) {
        this.userRouteType = userRouteType;
    }

}
