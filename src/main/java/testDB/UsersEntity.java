package testDB;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "public", catalog = "for_omk")
public class UsersEntity {
    private int id;
    private String uslogin;
    private String uspassword;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "uslogin", nullable = true, length = -1)
    public String getUslogin() {
        return uslogin;
    }

    public void setUslogin(String uslogin) {
        this.uslogin = uslogin;
    }

    @Basic
    @Column(name = "uspassword", nullable = true, length = -1)
    public String getUspassword() {
        return uspassword;
    }

    public void setUspassword(String uspassword) {
        this.uspassword = uspassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersEntity that = (UsersEntity) o;

        if (id != that.id) return false;
        if (uslogin != null ? !uslogin.equals(that.uslogin) : that.uslogin != null) return false;
        if (uspassword != null ? !uspassword.equals(that.uspassword) : that.uspassword != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (uslogin != null ? uslogin.hashCode() : 0);
        result = 31 * result + (uspassword != null ? uspassword.hashCode() : 0);
        return result;
    }
}
