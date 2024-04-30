package com.iwomi.External_api_cccNewUp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Language")
public class Language implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name="rang", unique=true )
    private Integer rang;

    @Basic(optional = false)
    @Column(name = "valen")
    private String valen;
    @Basic(optional = false)
    @Column(name = "valfr")
    private String valfr;
    @Column(name = "active")
    private String active;//1 = active 0 = not active


    @Override
    public int hashCode() {
        int hash = 0;
        hash += ((this.id != null) ? this.id.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Language)) {
            return false;
        }
        final Language other = (Language)object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "entities.Language[ ids=" + this.id + " ]";
    }
}
