package com.iwomi.External_api_cccNewUp.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "sanm")
public class Nomenclature {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @GenericGenerator(name = "seq", strategy = "increment")
    private Long id;
    private String tabcd;
    private String acscd;
    private String dele;
    private String lib1;
    private String lib2;
    private String lib3;
    private String lib4;
    private String lib5;
    private String lib6;
    private String lib7;
    private String lib8;
    private String lib9;
    private String lib10;
    private String mdp1;
    private String mdp2;
    private String mdp3;
    private String mdp4;
    private String mdp5;
    private Double taux1;
    private Double taux2;
    private Double taux3;
    private Double taux4;
    private Double taux5;
    private Long mnt1;
    private Long mnt2;
    private Long mnt3;
    private Long mnt4;
    private Long mnt5;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Africa/Douala")
    @Temporal(TemporalType.DATE)
    private Date dat1;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Africa/Douala")
    @Temporal(TemporalType.DATE)
    private Date dat2;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Africa/Douala")
    @Temporal(TemporalType.DATE)
    private Date dat3;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Africa/Douala")
    @Temporal(TemporalType.DATE)
    private Date dat4;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Africa/Douala")
    @Temporal(TemporalType.DATE)
    private Date dat5;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Africa/Douala")
    @Temporal(TemporalType.DATE)
    private Date crtd;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Africa/Douala")
    @Temporal(TemporalType.DATE)
    private Date mdfi;
    private String muser;
    private String cuser;
    private String cetab;
}
