package com.simpleproductapp.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String broj_tecajnice;
    private LocalDate datum_primjene;
    private String drzava;
    private String drzava_iso;
    private String sifra_valute;
    private String valuta;
    private int jedinica;
    private String kupovni_tecaj;
    private String srednji_tecaj;
    private String prodajni_tecaj;

}
