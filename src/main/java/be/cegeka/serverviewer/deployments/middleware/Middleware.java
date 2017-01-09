package be.cegeka.serverviewer.deployments.middleware;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "middleware")
public class Middleware implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    public Middleware(){}

    public Middleware(String name){
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @Override
    @Transient
    public boolean isNew() {
        return id == null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Middleware middleware = (Middleware) o;
        return Objects.equals(name, middleware.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
