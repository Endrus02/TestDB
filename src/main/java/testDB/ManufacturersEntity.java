package testDB;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "manufacturers", schema = "public", catalog = "for_omk")
public class ManufacturersEntity {
    private int id;
    private String manname;
    private String inn;

    private Set<MaterialsEntity> materialsSet = new HashSet<>();
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "mm",

            joinColumns = @JoinColumn(name = "manid"),

            inverseJoinColumns = @JoinColumn(name = "matid"))
    public Set<MaterialsEntity> getMaterialsSet() {
        return materialsSet;
    }

    public void setMaterialsSet(Set<MaterialsEntity> materials) {
        this.materialsSet = materials;
    }

    public void addMaterial(MaterialsEntity material) {
        materialsSet.add(material);
    }

    public void removeMaterial(MaterialsEntity material) {
        materialsSet.remove(material);
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
    @Column(name = "manname", nullable = true, length = -1)
    public String getManname() {
        return manname;
    }

    public void setManname(String manname) {
        this.manname = manname;
    }

    @Basic
    @Column(name = "inn", nullable = true, length = 12)
    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ManufacturersEntity that = (ManufacturersEntity) o;

        if (id != that.id) return false;
        if (manname != null ? !manname.equals(that.manname) : that.manname != null) return false;
        if (inn != null ? !inn.equals(that.inn) : that.inn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (manname != null ? manname.hashCode() : 0);
        result = 31 * result + (inn != null ? inn.hashCode() : 0);
        return result;
    }
}
