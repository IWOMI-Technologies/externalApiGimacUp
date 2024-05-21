
package com.iwomi.External_api_cccNewUp.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ussdsession")
public class UserSession implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    public String phone;//phone without 237
    public String uuid;//unique identifier or subscription code of the user
    public String status;// 0 = acc attach, 1 = change pin, 2 = account block , 3= normal , 4 = invalidation to recieve PIN
    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String pos;//position or page
    public String preval;
    public String preval2;//previous value selected or entered
    public Boolean iscon;//if connected or not
    public String lang = "en";
    public String oldpin;
    public String newpin;
    public int nwpin = 3;//number of times pin entered wrongly
    public int nwotp = 3;//number of times otp entered wrongly
    public int inval = 2;//number of times an incorrect value is entered
    public String lib2;
    public Date crtd;
    public Date mdfi;
    public String nat;
    public String member;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String acc;//json array

    public String dele;
    public int max; // max trial test case
    public int iterator; // iterator for counts
    public int iteratorPIN;
    public int iteratorAMT;
    public String menulevel;
    public String provider;
    public String transtel;
    public String transacc;
    public String motif;
    public String top;
    public String region;
    public String Amount;
    public String billnum;
    public String billref;

    public String sublevel;
    public String sublevel2;
    public String pin;
    public String duration;
    public String language;

    @Override
    public String toString() {
        return "UserSession{" + "id=" + id + ", phone=" + phone + ", uuid=" + uuid + ", pos=" + pos + ", preval=" + preval + ", iscon=" + iscon + ", lang=" + lang + ", lib2=" + lib2 + ", crtd=" + crtd + ", mdfi=" + mdfi + ", dele=" + dele + '}';
    }


}

