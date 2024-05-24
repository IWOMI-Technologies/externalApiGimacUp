package com.iwomi.External_api_cccNewUp.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author user
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@ToString
@Table(name = "ussdsession")
public class UserSession implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    public String phone;//phone without 237
    public String uuid;//unique identifier or subscription code of the user
    public String status;// 0 = acc attach, 1 = change pin, 2 = account block , 3= normal , 4 = invalidation to recieve PIN
    public String pos;//position or page
    public String prevLevel;//previous position or page
    public String oldPin;
    public String newPin;
    public int nwPin = 3;//number of times pin entered wrongly
    public int nwOtp = 3;//number of times otp entered wrongly
    public int inval = 2;//number of times an incorrect value is entered
    public Date crtd;
    public Date mdfi;
    public String nat;
    public String member;
    public String acc;
    public String wallet;
    public String dele;
    public int max;
    public int iterator;
    public int iteratorPIN;
    public int iteratorAMT;
    public String menuLevel;
    public String provider;
    public String motif;
    public String top;
    public String region;
    public String amount;
    public String billNum;
    public String ref;
    public String pin;
    public String duration;
    public String language = "en";
}

