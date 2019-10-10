package testDB;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "materials", schema = "public", catalog = "for_omk")
public class MaterialsEntity {
    private int id;
    private String matname;

    private Set<ManufacturersEntity> manufacturersSet = new HashSet<>();
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "mm",

            joinColumns = @JoinColumn(name = "matid"),

            inverseJoinColumns = @JoinColumn(name = "manid"))
    public Set<ManufacturersEntity> getManufacturersSet() {
        return manufacturersSet;
    }

    public void setManufacturersSet(Set<ManufacturersEntity> manufacturers) {
        this.manufacturersSet = manufacturers;
    }

    public void addManufacturer(ManufacturersEntity manufacturer) {
        manufacturersSet.add(manufacturer);
    }
    public void removeManufacturer(ManufacturersEntity manufacturer) {
        manufacturersSet.remove(manufacturer);
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "matname", nullable = true, length = -1)
    public String getMatname() {
        return matname;
    }

    public void setMatname(String matname) {
        this.matname = matname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaterialsEntity that = (MaterialsEntity) o;

        if (id != that.id) return false;
        if (matname != null ? !matname.equals(that.matname) : that.matname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (matname != null ? matname.hashCode() : 0);
        return result;
    }
}
